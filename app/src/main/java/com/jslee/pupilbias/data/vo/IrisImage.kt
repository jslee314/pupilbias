package com.jslee.pupilbias.data.vo

import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class IrisImage (val id: Int,
                      val imgSrcUrl: @RawValue Drawable) : Parcelable {


}