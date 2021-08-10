package com.jslee.pupilbias.pupilSeg

import android.R.attr.x
import android.R.attr.y
import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Moments
import org.opencv.utils.Converters
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.sqrt

class AutoSetPupilAndIris {

    fun getLargestContour(resizedBitmap: Bitmap?) :List<Point> {

        var grayMat: Mat = Mat()
        // step 0: 이미지 resize
        Utils.bitmapToMat(resizedBitmap, grayMat)
        Imgproc.cvtColor(grayMat, grayMat, Imgproc.COLOR_RGB2GRAY)

        // step1: 마스크의 contours 찾기
        var contourMat: Mat = Mat()

        var contours = ArrayList<MatOfPoint>()
        Imgproc.findContours( grayMat, contours, contourMat, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE )

        // step2: 영역이 가장 큰 contour 찾기
        val maxValIdx = findLargestContour(contours)
        val largestContour: MatOfPoint = contours[maxValIdx]

        var contourPointList: List<Point> = ArrayList<Point>()
        Converters.Mat_to_vector_Point(largestContour, contourPointList)

        return contourPointList
    }

    /**
     * pupil 의 중심 구하기
     * -> 2차원 분포의 1차 모멘트값 산정하기 (분포의 무게중심)
     * -> 산정된 값을 예측원의 중심 으로 함 */
    fun getPupilCenter(resizedBitmap: Bitmap?): Point {

        var grayMat: Mat = Mat()
        // step 0: 이미지 resize
        Utils.bitmapToMat(resizedBitmap, grayMat)
        Imgproc.cvtColor(grayMat, grayMat, Imgproc.COLOR_RGB2GRAY)

        // step1: 마스크의 contours 찾기
        var contourMat: Mat = Mat()

        var contours = ArrayList<MatOfPoint>()
        Imgproc.findContours( grayMat, contours, contourMat, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE )

        // step2: 영역이 가장 큰 contour 찾기
        val maxValIdx = findLargestContour(contours)
        val largestContour: MatOfPoint = contours[maxValIdx]

        // step3: contour의 1차 모멘트 구하기기
        val moments: Moments = Imgproc.moments(largestContour)
        val centroid = Point()
        centroid.x = (moments._m10 / moments._m00)
        centroid.y = (moments._m01 / moments._m00)
        return centroid
    }

    /**
     * @작성자 : 이재선
     * @최초작성일 : 2020-09-02 오후 06:42
     * @내용 : 예측원의 반지름 구하기
     * 도형의 contour의 좌표들과 도심간의 거리의 평균 */
    fun getRadius(pupilCenter: Point, contourPointList: List<Point>): Int {

        // step 3: 원의 반지름 : 도형의 contour의 좌표들과 도심간의 거리의 평균
        var sumRadius = 0
        val cX: Double = pupilCenter.x
        val cY: Double = pupilCenter.y
        for (i in contourPointList.indices) {
            val x: Double = contourPointList[i].x
            val y: Double = contourPointList[i].y
            sumRadius = (sumRadius + sqrt(((x - cX) * (x - cX) + (y - cY) * (y - cY)))).toInt()
        }
        return (sumRadius.toFloat() / contourPointList.size).roundToInt()
    }


    fun drawCircle(point: Point, pupilMaskBitmap: Bitmap, radius:Int, scalar: Scalar): Bitmap {
        var colorMat: Mat = Mat()
        Utils.bitmapToMat(pupilMaskBitmap, colorMat) // Android Bitmap are RGB But in opencv Mat, the channels are BGR by default.
        Imgproc.cvtColor(colorMat, colorMat, Imgproc.COLOR_BGR2RGB)

        Imgproc.circle(colorMat, point, radius, scalar, 1)
        Imgproc.cvtColor(colorMat, colorMat, Imgproc.COLOR_RGB2BGR)

        val drawBitmap: Bitmap = pupilMaskBitmap.copy(pupilMaskBitmap.config, pupilMaskBitmap.isMutable)
        Utils.matToBitmap(colorMat, drawBitmap)
        return drawBitmap
    }

    fun drawRadius(point: Point, pupilMaskBitmap: Bitmap, width:Int, height:Int, scalar: Scalar): Bitmap {
        var grayMat: Mat = Mat()
        Utils.bitmapToMat(pupilMaskBitmap, grayMat)
        Imgproc.cvtColor(grayMat, grayMat, Imgproc.COLOR_RGB2GRAY)

        Imgproc.rectangle(
            grayMat,
            Point((x - 5).toDouble(), (y - 5).toDouble()),
            Point((x + 5).toDouble(), (y + 5).toDouble()),
            scalar, -1)

        val drawBitmap: Bitmap = pupilMaskBitmap.copy(pupilMaskBitmap.config, pupilMaskBitmap.isMutable)
        Utils.matToBitmap(grayMat, drawBitmap)
        return drawBitmap
    }

    fun drawArc(point: Point, pupilMaskBitmap: Bitmap, radius:Int, scalar: Scalar, startAngle: Double, endAngle:Double): Bitmap {
        var colorMat: Mat = Mat()
        Utils.bitmapToMat(pupilMaskBitmap, colorMat) // Android Bitmap are RGB But in opencv Mat, the channels are BGR by default.
        Imgproc.cvtColor(colorMat, colorMat, Imgproc.COLOR_BGR2RGB)

        Imgproc.ellipse(colorMat, point, Size(radius.toDouble(), radius.toDouble()), 0.0, startAngle, endAngle, scalar, -1)

        val drawBitmap: Bitmap = pupilMaskBitmap.copy(pupilMaskBitmap.config, pupilMaskBitmap.isMutable)
        Utils.matToBitmap(colorMat, drawBitmap)
        return drawBitmap

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


}