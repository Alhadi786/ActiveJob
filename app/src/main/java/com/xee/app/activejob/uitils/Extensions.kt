package com.xee.app.activejob.uitils

import android.location.Location
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.util.*

fun ImageView.loadImage(
    url: Any?,
    placeholder: Int = 0,
    error: Int = 0,
    onlyFitCenter: Boolean = false,
) {

    val requestOptions = if (onlyFitCenter)
        RequestOptions().fitCenter().placeholder(placeholder).error(error)
            .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
    else
        RequestOptions().fitCenter().centerCrop().placeholder(placeholder).error(error)
            .diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop()

    Glide.with(context).asBitmap().load(url).apply(requestOptions).into(this)
}

fun CoroutineScope.launchPeriodicAsync(repeatMillis: Long, action: () -> Unit) = this.async {
    while (isActive) {
        action()
        delay(repeatMillis)
    }
}
fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun Location.distanceBetween(latitude: Double, longitude: Double): Float {

    val location = Location("")
    location.latitude = latitude
    location.longitude = longitude
    return this.distanceTo(location) / 1000
}

