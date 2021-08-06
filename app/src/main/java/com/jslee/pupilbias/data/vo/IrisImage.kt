package com.jslee.pupilbias.data.vo

import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class IrisImage (
    val id: Int,
    val fileName: String,
    val imgSrcUrl: @RawValue Drawable

) : Parcelable {

    lateinit var bitmapMaskOnly: Drawable

    var imgWidth: Int = 640
    var imgHeight:Int = 480

    var pupilCenterX:Int = 0
    var pupilCenterY:Int = 0


}