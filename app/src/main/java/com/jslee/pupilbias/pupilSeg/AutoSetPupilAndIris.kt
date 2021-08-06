package com.jslee.pupilbias.pupilSeg

import android.graphics.Bitmap
import android.graphics.Point
import java.util.*

class AutoSetPupilAndIris {
//    var largestContour: MatOfPoint? = null
//    var contourMat: Mat? = null
//    var contours: List<MatOfPoint>? = null
//    var grayMat: Mat? = null
//    var resizedBitmap: Bitmap? = null
//    var contourPointList: List<Point>? = null
//
//    /**
//     * @작성자 : 이재선
//     * @최초작성일 : 2020-09-02 오후 06:42
//     * @내용 : pupil 의 중심 구하기
//     * -> 2차원 분포의 1차 모멘트값 산정하기 (분포의 무게중심)
//     * -> 산정된 값을 예측원의 중심 으로 함
//     * @수정 :
//     * @버젼 :
//     * */
//    fun getPupilCenter(grayMaskBitmap: Bitmap?, maskWidth: Int, maskHeight: Int): Point? {

//        // step 0: 이미지 resize
//        var resizedBitmap = Bitmap.createScaledBitmap(grayMaskBitmap!!, maskWidth, maskHeight, true)
//        var grayMat = Mat()
//        Utils.bitmapToMat(resizedBitmap, grayMat)
//        Imgproc.cvtColor(grayMat, grayMat, Imgproc.COLOR_RGB2GRAY)
//
//        // step1: 마스크의 contours 찾기
//        contours = ArrayList<MatOfPoint>()
//        contourMat = Mat()
//        Imgproc.findContours(
//            grayMat,
//            contours,
//            contourMat,
//            Imgproc.RETR_TREE,
//            Imgproc.CHAIN_APPROX_SIMPLE
//        )
//
//        // step2: 영역이 가장 큰 contour 찾기
//        val maxValIdx = findLargestContour(contours)
//        largestContour = contours.get(maxValIdx)
//
//        // step3: contour의 1차 모멘트 구하기기
//        val moments: Moments = Imgproc.moments(largestContour)
//        val centroid = Point()
//        centroid.x = moments.get_m10() / moments.get_m00()
//        centroid.y = moments.get_m01() / moments.get_m00()
//        return centroid
//    }
//
//    private fun findLargestContour(contours: List<MatOfPoint>): Int {
//        var maxVal = 0.0
//        var maxValIdx = 0
//        for (contourIdx in contours.indices) {
//            val contourArea: Double = Imgproc.contourArea(contours[contourIdx])
//            if (maxVal < contourArea) {
//                maxVal = contourArea
//                maxValIdx = contourIdx
//            }
//        }
//        return maxValIdx
//    }
//
//    /**
//     * @작성자 : 이재선
//     * @최초작성일 : 2020-09-02 오후 06:42
//     * @내용 : 예측원의 반지름 구하기
//     * 도형의 contour의 좌표들과 도심간의 거리의 평균
//     * @수정 :
//     * @버젼 :
//     */
//    fun getRadius(
//        irisImgInfo: IrisImgInfo,
//        grayMaskBitmap: Bitmap?,
//        maskWidth: Int,
//        maskHeight: Int
//    ): Int {
//
//        // step 0: 이미지 resize
//        resizedBitmap = Bitmap.createScaledBitmap(grayMaskBitmap!!, maskWidth, maskHeight, true)
//        grayMat = Mat()
//        Utils.bitmapToMat(resizedBitmap, grayMat)
//        Imgproc.cvtColor(grayMat, grayMat, Imgproc.COLOR_RGB2GRAY)
//
//        // step 1: 마스크의 contours 찾기
//        contours = ArrayList<MatOfPoint>()
//        contourMat = Mat()
//        Imgproc.findContours(
//            grayMat,
//            contours,
//            contourMat,
//            Imgproc.RETR_TREE,
//            Imgproc.CHAIN_APPROX_SIMPLE
//        )
//
//        // step2: 영역이 가장 큰 contour 찾기
//        val maxValIdx = findLargestContour(contours)
//        largestContour = contours.get(maxValIdx)
//
//        // step 2: 원의 반지름 : 도형의 contour의 좌표들과 도심간의 거리의 평균
//        contourPointList = ArrayList<Point>()
//        Converters.Mat_to_vector_Point(largestContour, contourPointList)
//        var sumRadius = 0
//        val cX: Int = irisImgInfo.getPpCntrX()
//        val cY: Int = irisImgInfo.getPpCntrY()
//        for (i in contourPointList.indices) {
//            val x: Double = contourPointList.get(i).x
//            val y: Double = contourPointList.get(i).y
//            sumRadius = (sumRadius + Math.sqrt((x - cX) * (x - cX) + (y - cY) * (y - cY))).toInt()
//        }
//        return Math.round(sumRadius.toFloat() / contourPointList.size).toInt()
//    }

}