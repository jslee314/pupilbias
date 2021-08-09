package com.jslee.pupilbias.util

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.jslee.pupilbias.R
import com.jslee.pupilbias.data.constant.ViewStatus
import com.jslee.pupilbias.data.vo.IrisImage
import com.jslee.pupilbias.images.PhotoGridAdapter

/**
 * Glide 라이브러리를 사용해서 이미지를 로드하는 기능
 *  URL 스트링을 -> [ImageView]에 로드함  */
@BindingAdapter("BA_imageUrl")
fun bindImage(imgView: ImageView, drawable: Drawable) {

    // imgUrl이 null 이 아닐경우 수행 >>>>>  let() + ?.
    drawable?.let {
        Glide.with(imgView.context)         // Uri 개체에서 ImageView로 이미지를 로드함 with().load(imgUri).into(imgView)
            .load(drawable)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)  // 이미지 로드를 하는 도중에 나오는 이미지
                    .error(R.drawable.ic_broken_image))         // 이미지 로드를 실패했을 때 나오는 이미지
            .into(imgView)
    }
}

/** [GroundProperty] 데이터가없는 경우 (데이터가 null) [RecyclerView]를 숨기고
 * 그렇지 않으면 표시 하는 기능
 */
@BindingAdapter("BA_listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: MutableList<IrisImage>?) {
    val adapter = recyclerView.adapter as PhotoGridAdapter
    adapter.submitList(data)
}


@BindingAdapter("BA_status")
fun bindStatus(view: View, status: ViewStatus) {
    when (status) {
        ViewStatus.VISIBLE -> {
            view.visibility = View.VISIBLE
            // textView.setText(R.string.activity_version_update_require_install_txt)
        }
        ViewStatus.GONE -> {
            view.visibility = View.GONE
        }
    }
}
@BindingAdapter("BA_opposition_status")
fun bindOppositionStatus(view: View, status: ViewStatus) {
    when (status) {
        ViewStatus.VISIBLE -> {
            view.visibility = View.GONE
            // textView.setText(R.string.activity_version_update_require_install_txt)
        }
        ViewStatus.GONE -> {
            view.visibility = View.VISIBLE
        }
    }
}