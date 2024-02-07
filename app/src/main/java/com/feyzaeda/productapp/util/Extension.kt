package com.feyzaeda.productapp.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.feyzaeda.productapp.R

fun String.removeWhitespaces() = replace(" ", "_")

fun ImageView.glide(context: Context, photoUrl: String) {
    Glide.with(context)
        .load(photoUrl)
        .override(1024, 768)
        .placeholder(R.drawable.loading)
        .into(this)
}