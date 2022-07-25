package com.mobiroller.core.coreui.extensions

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(url: String?) {
    Glide.with(context)
        .load(url)
        .into(this)
}

fun ImageView.loadImage(url: Uri?) {
    Glide.with(context)
        .load(url)
        .into(this)
}

fun ImageView.loadImage(byteArray: ByteArray?) {
    Glide.with(context)
        .load(byteArray)
        .into(this)
}