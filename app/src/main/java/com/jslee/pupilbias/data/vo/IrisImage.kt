package com.jslee.pupilbias.data.vo

import android.graphics.drawable.Drawable
import android.os.Parcelable
import com.jslee.pupilbias.data.constant.ViewStatus
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import org.opencv.core.Point

@Parcelize
data class IrisImage (
    val id: Int?,
    val fileName: String?,
    val imgDrawableInt: @RawValue Drawable? = null,
    var viewSeg: ViewStatus = ViewStatus.VISIBLE

) : Parcelable {

    var bitmapMaskOnly: Drawable? = null

    var imgWidth: Int = 640
    var imgHeight:Int = 480

    var segmentationLog: String = " "

    var pupilCenter: Point = Point(0.0, 0.0)

    var pupilRadius:Int = 0

}