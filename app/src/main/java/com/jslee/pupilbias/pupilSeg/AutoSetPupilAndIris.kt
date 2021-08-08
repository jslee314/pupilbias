package com.jslee.pupilbias.pupilSeg

import android.R.attr.x
import android.R.attr.y
import android.graphics.Bitmap
import com.jslee.pupilbias.data.vo.IrisImage
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Moments
import org.opencv.utils.Converters
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.sqrt


class AutoSetPupilAndIris {
    lateinit var largestContour: MatOfPoint
    lateinit var contours: List<MatOfPoint>
    lateinit var contourPointList: List<Point>

    /**
     * pupil 의 중심 구하기
     * -> 2차원 분포의 1차 모멘트값 산정하기 (분포의 무게중심)
     * -> 산정된 값을 예측원의 중심 으로 함
     * */
    fun getPupilCenter(resizedBitmap: Bitmap?): Point {
        var grayMat: Mat = Mat()

        // step 0: 이미지 resize
//        var resizedBitmap = Bitmap.createScaledBitmap(grayMaskBitmap!!, maskWidth, maskHeight, true)
        Utils.bitmapToMat(resizedBitmap, grayMat)
        Imgproc.cvtColor(grayMat, grayMat, Imgproc.COLOR_RGB2GRAY)

        // step1: 마스크의 contours 찾기
        var contourMat: Mat = Mat()

        contours = ArrayList<MatOfPoint>()
        Imgproc.findContours( grayMat, contours, contourMat, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE )

        // step2: 영역이 가장 큰 contour 찾기
        val maxValIdx = findLargestContour(contours)
        largestContour = contours[maxValIdx]

        // step3: contour의 1차 모멘트 구하기기
        val moments: Moments = Imgproc.moments(largestContour)
        val centroid = Point()
        centroid.x = (moments._m10 / moments._m00)
        centroid.y = (moments._m01 / moments._m00)
        return centroid
    }

    private fun findLargestContour(contours: List<MatOfPoint>): Int {
        var maxVal = 0.0
        var maxValIdx = 0
        for (contourIdx in contours.indices) {
            val contourArea: Double = Imgproc.contourArea(contours[contourIdx])
            if (maxVal < contourArea) {
                maxVal = contourArea
                maxValIdx = contourIdx
            }
        }
        return maxValIdx
    }

    /**
     * @작성자 : 이재선
     * @최초작성일 : 2020-09-02 오후 06:42
     * @내용 : 예측원의 반지름 구하기
     * 도형의 contour의 좌표들과 도심간의 거리의 평균
     */
    fun getRadius(irisImg: IrisImage, resizedBitmap: Bitmap?): Int {
        var grayMat: Mat = Mat()
        // step 0: 이미지 resize
//        resizedBitmap = Bitmap.createScaledBitmap(grayMaskBitmap!!, maskWidth, maskHeight, true)
        Utils.bitmapToMat(resizedBitmap, grayMat)
        Imgproc.cvtColor(grayMat, grayMat, Imgproc.COLOR_RGB2GRAY)

        // step 1: 마스크의 contours 찾기
        var contourMat: Mat = Mat()
        contours = ArrayList<MatOfPoint>()
        Imgproc.findContours( grayMat, contours, contourMat, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE )

        // step2: 영역이 가장 큰 contour 찾기
        val maxValIdx = findLargestContour(contours)
        largestContour = contours[maxValIdx]

        // step 2: 원의 반지름 : 도형의 contour의 좌표들과 도심간의 거리의 평균
        contourPointList = ArrayList<Point>()
        Converters.Mat_to_vector_Point(largestContour, contourPointList)
        var sumRadius = 0
        val cX: Double = irisImg.pupilCenter.x
        val cY: Double = irisImg.pupilCenter.y
        for (i in contourPointList.indices) {
            val x: Double = contourPointList[i].x
            val y: Double = contourPointList[i].y
            sumRadius = (sumRadius + sqrt(((x - cX) * (x - cX) + (y - cY) * (y - cY)))).toInt()
        }
        return (sumRadius.toFloat() / contourPointList.size).roundToInt()
    }

    fun drawCircle(point: Point, resizedBitmap: Bitmap, radius:Int, scalar: Scalar): Bitmap {
        var colorMat: Mat = Mat()
        Utils.bitmapToMat(resizedBitmap, colorMat) // Android Bitmap are RGB But in opencv Mat, the channels are BGR by default.
        Imgproc.cvtColor(colorMat, colorMat, Imgproc.COLOR_BGR2RGB)

        Imgproc.circle(colorMat, point, radius, scalar, 2)

        Utils.matToBitmap(colorMat, resizedBitmap)
        return resizedBitmap
    }

    fun drawArc(point: Point, resizedBitmap: Bitmap, radius:Int, scalar: Scalar, startAngle: Double, endAngle:Double): Bitmap {
        var colorMat: Mat = Mat()
        Utils.bitmapToMat(resizedBitmap, colorMat) // Android Bitmap are RGB But in opencv Mat, the channels are BGR by default.
        Imgproc.cvtColor(colorMat, colorMat, Imgproc.COLOR_BGR2RGB)

        Imgproc.ellipse(colorMat, point, Size(radius.toDouble(), radius.toDouble()), 0.0,0.0, 90.0, scalar, -1)

        Utils.matToBitmap(colorMat, resizedBitmap)
        return resizedBitmap

    }

    fun drawRadius(point: Point, resizedBitmap: Bitmap, width:Int, height:Int): Bitmap{
        var grayMat: Mat = Mat()
        Utils.bitmapToMat(resizedBitmap, grayMat)
        Imgproc.cvtColor(grayMat, grayMat, Imgproc.COLOR_RGB2GRAY)

        Imgproc.rectangle(
            grayMat,
            Point((x - 5).toDouble(), (y - 5).toDouble()),
            Point((x + 5).toDouble(), (y + 5).toDouble()),
            Scalar(0.0, 128.0, 255.0), -1)

        Utils.matToBitmap(grayMat, resizedBitmap)
        return resizedBitmap
    }
}