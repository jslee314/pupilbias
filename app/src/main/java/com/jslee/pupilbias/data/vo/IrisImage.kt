package com.jslee.pupilbias.data.vo

import android.graphics.drawable.Drawable
import android.os.Parcelable
import com.jslee.pupilbias.data.constant.ViewStatus
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import org.opencv.core.MatOfPoint
import org.opencv.core.Point

@Parcelize
data class IrisImage (
    val id: Int?,
    val fileName: String?,
    val originalImg: @RawValue Drawable? = null,
    var viewSeg: ViewStatus = ViewStatus.GONE

) : Parcelable {

    var maskImg: Drawable? = null
    var segmentationLog: String = " "

    // 이미지 크기
    var imgWidth: Int = 640
    var imgHeight: Int = 480

    // 컨투어
    var contourPointList: List<Point>? = null

    // 예측원의 반지름
    var circleRadius: Int = 0

    // 예측원의 중심
    var circleCenter: Point = Point(320.0, 240.0)

    // 예측 사각형 시작점, 끝점
    var rectStart: Point = Point(0.0, 0.0)
    var rectEnd: Point = Point(640.0, 480.0)

    // 예측 사각형 가로, 세로
    val rectWidth: Int
        get() = (rectEnd.x - rectStart.x).toInt()

    val rectHeight: Int
        get() = (rectEnd.y - rectStart.y).toInt()

    // 예측 사각형 중심
    val rectCenter: Point
        get() = Point(rectEnd.x - (rectWidth / 2), rectEnd.y - (rectHeight / 2))

}