package com.jslee.pupilbias.pupilSeg

import android.graphics.Bitmap
import android.util.Log
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Moments
import org.opencv.utils.Converters
import java.lang.Math.*
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
        Imgproc.findContours(grayMat, contours, contourMat, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE)

        // step2: 영역이 가장 큰 contour 찾기
        val maxValIdx = findLargestContour(contours)
        val largestContour: MatOfPoint = contours[maxValIdx]

        var contourPointList: List<Point> = ArrayList<Point>()
        Converters.Mat_to_vector_Point(largestContour, contourPointList)

        return contourPointList
    }

    fun getMinEnclosingCircle(bitmap: Bitmap, largestContour: MatOfPoint){

        var imageMat: Mat = Mat()
        // step 0: 이미지 resize
        Utils.bitmapToMat(bitmap, imageMat)

        val radius = FloatArray(1)
        val center = Point()

        val currentContour2f = MatOfPoint2f()
        largestContour.convertTo(currentContour2f, CvType.CV_32FC2)

        Imgproc.minEnclosingCircle(currentContour2f, center, radius)
        Imgproc.circle(imageMat, center, radius[0].toInt(), Scalar(255.0, 0.0, 0.0))

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

    /**
     * @작성자 : 이재선
     * @내용 : 예측다각형의 가로 세로 구하기
     * 도형의 contour의 좌표들의
     * 최왼쪽 및 최오른쪽의 x좌표,
     * 최상단 및 최하단의 y 좌표 */
    fun getRectParm(contourPointList: List<Point>): Pair<Point, Point> {

        var rectStart: Point = Point(640.0, 480.0)
        var rectEnd: Point = Point(0.0, 0.0)

        for (i in contourPointList.indices) {

            var Px: Double = contourPointList[i].x
            var Py: Double = contourPointList[i].y

            if (rectStart.x >= Px) {
                rectStart.x = Px
            }
            if (rectStart.y >= Py) {
                rectStart.y = Py
            }

            if (rectEnd.x <= Px) {
                rectEnd.x = Px
            }
            if (rectEnd.y <= Py) {
                rectEnd.y = Py
            }
        }

        return Pair(rectStart, rectEnd)
    }

    fun getDistance(point1: Point, point2: Point): Double {

        return Math.sqrt(pow(abs(point2.x-point1.x), 2.0) + pow(abs(point2.y-point1.y), 2.0));
    }

    fun getAngle(point1: Point, point2: Point): Double {
        val dx = point2.x - point1.x
        val dy = point2.y - point1.y

        val rad = atan2(dx, dy)
        val degree = (rad*180) / PI

        return degree
    }

    fun getArcCount(pupilMaskBitmap: Bitmap, centerPoint: Point, startAngle: Double, endAngle: Double): Pair<Bitmap, Int>{

        var pupilMat: Mat = Mat()
        Utils.bitmapToMat(pupilMaskBitmap, pupilMat) // Android Bitmap are RGB But in opencv Mat, the channels are BGR by default.

        val zeroMat: Mat = Mat.zeros(pupilMat.size(), CvType.CV_8UC4)
        Imgproc.ellipse(zeroMat, centerPoint, Size(pupilMaskBitmap.height.toDouble(), pupilMaskBitmap.height.toDouble()),
            0.0, startAngle, endAngle, Scalar(255.0, 255.0 , 255.0), -1)

        var dstMat: Mat = Mat()

        Core.bitwise_and(zeroMat, pupilMat, dstMat)

        Imgproc.cvtColor(dstMat, dstMat, Imgproc.COLOR_RGB2GRAY)
        val count = Core.countNonZero(dstMat)
        Log.d("jjslee", "count : " + count)
        Imgproc.cvtColor(dstMat, dstMat, Imgproc.COLOR_RGB2BGR)

        val drawBitmap: Bitmap = pupilMaskBitmap.copy(pupilMaskBitmap.config, pupilMaskBitmap.isMutable)
        Utils.matToBitmap(dstMat, drawBitmap)

        return Pair(drawBitmap, count)
    }

//    fun addImage(img1:Mat, img2:Mat, out:Mat) {
//        for (var y:Int = 0; y < out.size().height; y++){
//            for (int x = 0; x < out.size().width; x++)
//            {
//                if (img1.at<uchar>(y, x) + img2.at<uchar>(y, x) > 255)
//                    out.at<uchar>(y, x) = 255;
//                else
//                    out.at<uchar>(y, x) = img1.at<uchar>(y, x) + img2.at<uchar>(y, x);
//            }
//        }
//    }


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

    fun drawRadius(pupilMaskBitmap: Bitmap, rectStart:Point, rectEnd:Point, scalar: Scalar): Bitmap {

        var colorMat: Mat = Mat()
        Utils.bitmapToMat(pupilMaskBitmap, colorMat) // Android Bitmap are RGB But in opencv Mat, the channels are BGR by default.
        Imgproc.cvtColor(colorMat, colorMat, Imgproc.COLOR_BGR2RGB)

        Imgproc.rectangle(
            colorMat, rectStart, rectEnd,
            scalar, 1)
        Imgproc.cvtColor(colorMat, colorMat, Imgproc.COLOR_RGB2BGR)

        val drawBitmap: Bitmap = pupilMaskBitmap.copy(pupilMaskBitmap.config, pupilMaskBitmap.isMutable)
        Utils.matToBitmap(colorMat, drawBitmap)
        return drawBitmap
    }

    fun drawArc(centerPoint: Point, bitmap: Bitmap, radius:Int, scalar: Scalar, startAngle: Double, endAngle:Double): Bitmap {
        var colorMat: Mat = Mat()
        Utils.bitmapToMat(bitmap, colorMat) // Android Bitmap are RGB But in opencv Mat, the channels are BGR by default.
        Imgproc.cvtColor(colorMat, colorMat, Imgproc.COLOR_BGR2RGB)

        Imgproc.ellipse(colorMat, centerPoint, Size(radius.toDouble(), radius.toDouble()), 0.0, startAngle, endAngle, scalar, -1)

        Imgproc.cvtColor(colorMat, colorMat, Imgproc.COLOR_RGB2BGR)

        val drawBitmap: Bitmap = bitmap.copy(bitmap.config, bitmap.isMutable)
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