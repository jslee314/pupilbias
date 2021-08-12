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


    // 컨투어 분석
    var contourPointList: List<Point>? = null

    // 
    var imgWidth: Int = 640
    var imgHeight: Int = 480
    var pupilRadius: Int = 0

    var pupilCenter: Point = Point(0.0, 0.0)
    var rectCenter: Point = Point(0.0, 0.0)


}