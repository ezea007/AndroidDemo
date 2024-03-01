package com.test.application

import android.graphics.Color
import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

class ImageViewBindingAdapter {

    companion object {

        // app:image="@{networkImage}" 和对应的app:image的image相互对应
        @BindingAdapter("networkImage")
        @JvmStatic
        fun setImage(imageView: ImageView, url: String?) {
            if (!TextUtils.isEmpty(url)) {
                Glide.with(imageView.context).load(url).into(imageView)
                return
            }
            imageView.setBackgroundColor(Color.GRAY)
        }

        // app:defaultImage="@{localImage}" 和对应的localImage的image相互对应
        @BindingAdapter("localImage")
        @JvmStatic
        fun setImage(imageView: ImageView, id: Int) {
            imageView.setImageResource(id)
        }
    }
}
