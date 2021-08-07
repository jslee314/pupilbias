package com.jslee.pupilbias.data.vo

import android.graphics.drawable.Drawable
import android.os.Parcelable
import com.jslee.pupilbias.data.constant.ViewStatus
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class IrisImage (
    val id: Int?,
    val fileName: String?,
    val imgSrcUrl: @RawValue Drawable?,
    var viewSeg: ViewStatus = ViewStatus.VISIBLE

) : Parcelable {

    var bitmapMaskOnly: Drawable? = null
    var segmentationLog: String = " "

    var imgWidth: Int = 640
    var imgHeight:Int = 480

    var pupilCenterX:Int = 0
    var pupilCenterY:Int = 0

    var pupilRadius:Int = 0

}