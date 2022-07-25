package com.mobiroller.core.coreui.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener

fun ImageView.loadImage(imageUrl: String) {
    Glide.with(this)
            .load(imageUrl)
            .into(this)
}

fun ImageView.loadImage(drawableInt: Int) {
    Glide.with(this)
            .load(drawableInt)
            .into(this)
}

fun ImageView.loadImage(imageUrl: String, placeholder: Int, listener: CoreImageLister, failedImage: Int) {
    Glide.with(this)
            .load(imageUrl)
            .placeholder(placeholder)
            .addListener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any, target: com.bumptech.glide.request.target.Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                    post {
                        setImageResource(failedImage)
                    }
                    listener.onFailed()
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any, target: com.bumptech.glide.request.target.Target<Drawable?>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                    listener.onSuccess()
                    return false
                }
            })
            .into(this)


}

interface CoreImageLister {
    fun onFailed()
    fun onSuccess()
}
