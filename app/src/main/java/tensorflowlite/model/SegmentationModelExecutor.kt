package tensorflowlite.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.SystemClock
import android.util.Log
import androidx.core.graphics.ColorUtils
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.GpuDelegate
import org.tensorflow.lite.support.common.FileUtil
import tensorflowlite.data.ModelExecutionResultVO
import tensorflowlite.utils.ImageUtils
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class SegmentationModelExecutor(context: Context, mode: Int, private var useGPU: Boolean = false) {
    private var gpuDelegate: GpuDelegate? = null

    private val segmentationMasks: ByteBuffer
    private val interpreter: Interpreter

    private var fullTimeExecutionTime = 0L
    private var preprocessTime = 0L
    private var imageSegmentationTime = 0L
    private var maskFlatteningTime = 0L

    private var numberThreads = 2

    // 신경망 관련 변수들
    private var imageSegmentationModel: String = ""
    private var IMAGE_WIDTH = 640
    private var IMAGE_HEIGHT = 480
    private var MASK_WIDTH = 640
    private var MASK_HEIGHT = 480
    private var NUM_CLASSES = 1
    private var IMAGE_MEAN = 127.5f
    private var IMAGE_STD = 127.5f
    val segmentColors = IntArray(2)

    companion object {
        private const val TAG = "ImageSegmentationMExec"

//        const val STRESSRING_MODEL = 1
//        const val CHOLESTEROLRING_MODEL = 2
//        const val ABSORPTIONRING_MODEL = 3
        const val AUTONERVOUS_MODEL = 4
        const val PUPIL_MODEL = 5
        const val PUPIL_MODEL_128 = 6
    }

    init {
        when (mode) {
//            STRESSRING_MODEL -> imageSegmentationModel = "stressring_model_v1.tflite"
//            CHOLESTEROLRING_MODEL -> imageSegmentationModel = "cholesterolring_model_v1.tflite"
//            ABSORPTIONRING_MODEL -> imageSegmentationModel = "absorptionring_model_v2.tflite"
//            AUTONERVOUS_MODEL -> imageSegmentationModel = "autonervous_model_sota.tflite"

            PUPIL_MODEL -> {
                IMAGE_WIDTH = 512
                IMAGE_HEIGHT = 384
                MASK_WIDTH = 128
                MASK_HEIGHT = 96
                NUM_CLASSES = 3
                imageSegmentationModel = "iris_pupil_v1_model.tflite"
            }
            PUPIL_MODEL_128 -> {
                IMAGE_WIDTH = 128
                IMAGE_HEIGHT = 128
                MASK_WIDTH = 128
                MASK_HEIGHT = 128
                IMAGE_MEAN = 0.0f
                IMAGE_STD = 255.0f
                imageSegmentationModel = "pupil_model_v1.tflite"
            }
        }
        if(mode == PUPIL_MODEL) {
            segmentColors[0] = Color.TRANSPARENT
            segmentColors[1] = Color.BLUE
        }
        if(mode == PUPIL_MODEL_128) {
            segmentColors[0] = Color.BLACK
            segmentColors[1] = Color.WHITE
        }

        interpreter = getInterpreter(context, imageSegmentationModel, useGPU)
        segmentationMasks = ByteBuffer.allocateDirect(1 * MASK_WIDTH * MASK_HEIGHT * NUM_CLASSES * 4)
        segmentationMasks.order(ByteOrder.nativeOrder())
    }

    fun execute(data: Bitmap, mode: Int): ModelExecutionResultVO {
        try {
            fullTimeExecutionTime = SystemClock.uptimeMillis()

            preprocessTime = SystemClock.uptimeMillis()
            val scaledBitmap =
                ImageUtils.scaleBitmapAndKeepRatio(
                    data,
                    IMAGE_HEIGHT, IMAGE_WIDTH
                )
            val contentArray =
                ImageUtils.bitmapToByteBuffer(
                    scaledBitmap,
                    IMAGE_WIDTH,
                    IMAGE_HEIGHT,
                    IMAGE_MEAN,
                    IMAGE_STD
                )
            preprocessTime = SystemClock.uptimeMillis() - preprocessTime

            imageSegmentationTime = SystemClock.uptimeMillis()
            interpreter.run(contentArray, segmentationMasks)
            contentArray.clear()
            imageSegmentationTime = SystemClock.uptimeMillis() - imageSegmentationTime
            Log.d(TAG, "Time to run the model $imageSegmentationTime")

            maskFlatteningTime = SystemClock.uptimeMillis()

            if (mode == PUPIL_MODEL) {
                val (pupilIrisMask, pupilMask, itemsFound) =
                    convertBytebufferMaskToBitmap(
                        segmentationMasks, MASK_WIDTH, MASK_HEIGHT
                    )
                maskFlatteningTime = SystemClock.uptimeMillis() - maskFlatteningTime
                Log.d(TAG, "Time to flatten the mask result $maskFlatteningTime")

                fullTimeExecutionTime = SystemClock.uptimeMillis() - fullTimeExecutionTime
                Log.d(TAG, "Total time execution $fullTimeExecutionTime")

                return ModelExecutionResultVO(
                    pupilIrisMask,
                    scaledBitmap,
                    pupilMask,
                    formatExecutionLog(),
                    itemsFound
                )
            } else if (mode == PUPIL_MODEL_128) {
                val (pupilIrisMask, pupilMask, itemsFound) =
                    convertBytebufferMaskToBitmap(
                        segmentationMasks, MASK_WIDTH, MASK_HEIGHT
                    )
                maskFlatteningTime = SystemClock.uptimeMillis() - maskFlatteningTime
                Log.d(TAG, "Time to flatten the mask result $maskFlatteningTime")

                fullTimeExecutionTime = SystemClock.uptimeMillis() - fullTimeExecutionTime
                Log.d(TAG, "Total time execution $fullTimeExecutionTime")

                return ModelExecutionResultVO(
                    pupilIrisMask,
                    scaledBitmap,
                    pupilMask,
                    formatExecutionLog(),
                    itemsFound
                )
            } else {
                val (maskImageApplied, maskOnly, itemsFound) =
                    convertBytebufferMaskToBitmap(
                        segmentationMasks, MASK_WIDTH, MASK_HEIGHT, scaledBitmap,
                        segmentColors
                    )
                maskFlatteningTime = SystemClock.uptimeMillis() - maskFlatteningTime
                Log.d(TAG, "Time to flatten the mask result $maskFlatteningTime")

                fullTimeExecutionTime = SystemClock.uptimeMillis() - fullTimeExecutionTime
                Log.d(TAG, "Total time execution $fullTimeExecutionTime")

                return ModelExecutionResultVO(
                    maskImageApplied,
                    scaledBitmap,
                    maskOnly,
                    formatExecutionLog(),
                    itemsFound
                )
            }
        } catch (e: Exception) {
            val exceptionLog = "something went wrong: ${e.message}"
            Log.d(TAG, exceptionLog)

            val emptyBitmap =
                ImageUtils.createEmptyBitmap(
                    IMAGE_WIDTH,
                    IMAGE_HEIGHT
                )
            return ModelExecutionResultVO(
                emptyBitmap,
                emptyBitmap,
                emptyBitmap,
                exceptionLog,
                HashSet(0)
            )
        }
    }

    @Throws(IOException::class)
    private fun getInterpreter(
        context: Context,
        modelName: String,
        useGpu: Boolean = false
    ): Interpreter {
        val tfliteOptions = Interpreter.Options()
        tfliteOptions.setNumThreads(numberThreads)

        gpuDelegate = null
        if (useGpu) {
            gpuDelegate = GpuDelegate()
            tfliteOptions.addDelegate(gpuDelegate)
        }

        return Interpreter(FileUtil.loadMappedFile(context, modelName), tfliteOptions)
    }

    private fun formatExecutionLog(): String {
        val sb = StringBuilder()
        sb.append("Input Image Size: ${IMAGE_WIDTH}x $IMAGE_HEIGHT\n")
        sb.append("Output Mask Size: ${MASK_WIDTH}x $MASK_HEIGHT\n")
        sb.append("GPU enabled: $useGPU\n")
        sb.append("Number of threads: $numberThreads\n")
        sb.append("Pre-process execution time: $preprocessTime ms\n")
        sb.append("Model execution time: $imageSegmentationTime ms\n")
        sb.append("Mask flatten time: $maskFlatteningTime ms\n")
        sb.append("Full execution time: $fullTimeExecutionTime ms\n")
        return sb.toString()
    }

    fun close() {
        interpreter.close()
        if (gpuDelegate != null) {
            gpuDelegate!!.close()
        }
        segmentationMasks.clear()
    }

    private fun convertBytebufferMaskToBitmap(
        inputBuffer: ByteBuffer,
        imageWidth: Int,
        imageHeight: Int,
        backgroundImage: Bitmap,
        colors: IntArray
    ): Triple<Bitmap, Bitmap, Set<Int>> {
        val conf = Bitmap.Config.ARGB_8888
        val maskBitmap = Bitmap.createBitmap(imageWidth, imageHeight, conf)
        val resultBitmap = Bitmap.createBitmap(imageWidth, imageHeight, conf)
        val scaledBackgroundImage =
            ImageUtils.scaleBitmapAndKeepRatio(
                backgroundImage,
                imageHeight,
                imageWidth
            )
        val mSegmentBits = Array(imageWidth) { IntArray(imageHeight) }
        val itemsFound = HashSet<Int>()
        inputBuffer.rewind()

        for (y in 0 until imageHeight) {
            for (x in 0 until imageWidth) {
                mSegmentBits[x][y] = 0
                val value = inputBuffer.getFloat((y * imageWidth + x) * 4)

                if (value >= 0.5) {
                    mSegmentBits[x][y] = 1
                } else {
                    mSegmentBits[x][y] = 0
                }

                val newPixelColor = ColorUtils.compositeColors(
                    colors[mSegmentBits[x][y]],
                    scaledBackgroundImage.getPixel(x, y)
                )
                resultBitmap.setPixel(x, y, newPixelColor)
                maskBitmap.setPixel(x, y, colors[mSegmentBits[x][y]])
            }
        }

        return Triple(resultBitmap, maskBitmap, itemsFound)
    }
    /* Pupil & Iris 검출 모델용 함수 */
    private fun convertBytebufferMaskToBitmap(
        inputBuffer: ByteBuffer,
        imageWidth: Int,
        imageHeight: Int
    ): Triple<Bitmap, Bitmap, Set<Int>> {

        val colors = IntArray(NUM_CLASSES)
        colors[0] = Color.BLACK
        colors[1] = Color.WHITE
        colors[2] = Color.GRAY
        val conf = Bitmap.Config.ARGB_8888
        val pupilBitmap = Bitmap.createBitmap(imageWidth, imageHeight, conf)
        val pupilIrisBitmap = Bitmap.createBitmap(imageWidth, imageHeight, conf)

        val mSegmentPupilBits = Array(imageWidth) { IntArray(imageHeight) }
        val mSegmentIrisBits = Array(imageWidth) { IntArray(imageHeight) }
        val itemsFound = HashSet<Int>()
        inputBuffer.rewind()

        for (y in 0 until imageHeight) {
            for (x in 0 until imageWidth) {
                var maxVal = 0f
                mSegmentPupilBits[x][y] = 0
                mSegmentIrisBits[x][y] = 0

                for (c in 0 until NUM_CLASSES) {
                    val value = inputBuffer.getFloat((y * imageWidth * NUM_CLASSES + x * NUM_CLASSES + c) * 4)
                    if (c == 0 || value > maxVal) {
                        maxVal = value
                        mSegmentIrisBits[x][y] = c
                        if (c == 2) mSegmentPupilBits[x][y] = c
                    }
                }

                pupilIrisBitmap.setPixel(x, y, colors[mSegmentIrisBits[x][y]])
                pupilBitmap.setPixel(x, y, colors[mSegmentPupilBits[x][y]])
            }
        }

        return Triple(pupilIrisBitmap, pupilBitmap, itemsFound)
    }
}
