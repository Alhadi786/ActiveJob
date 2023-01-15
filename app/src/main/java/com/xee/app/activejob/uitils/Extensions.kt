package com.xee.app.activejob.uitils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadImage(
    url: Any?,
    placeholder: Int = 0,
    error: Int = 0,
    onlyFitCenter: Boolean = false,
) {

    val requestOptions = if (onlyFitCenter)
        RequestOptions().fitCenter().placeholder(placeholder).error(error)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
    else
        RequestOptions().fitCenter().centerCrop().placeholder(placeholder).error(error)
            .diskCacheStrategy(DiskCacheStrategy.ALL)

    Glide.with(context).asBitmap().load(url).apply(requestOptions).into(this)
}

fun View.show() {
    visibility = View.VISIBLE
}