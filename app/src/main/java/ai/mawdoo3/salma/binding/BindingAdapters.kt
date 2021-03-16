package com.banking.common.binding

import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView


interface BindingAdapters {


    /***
     * @param model - Any object supported by Glide (Uri, File, Bitmap, String, resource id as Int, ByteArray, and Drawable)
     */
    @BindingAdapter(
        "loadImage",
        "corners",
        "strokeWidth",
        "strokeColor",
        "isCircle",
        "placeHolder",
        requireAll = false
    )
    fun <T> AppCompatImageView.loadImage(
        model: T,
        corners: Float,
        strokeWidth: Float,
        strokeColor: Int,
        isCircle: Boolean,
        placeHolder: Int
    )

    @BindingAdapter("loadImageWithBlur")
    fun <T> AppCompatImageView.blur(
        model: T
    )

    @BindingAdapter("loadImageResource")
    fun setImageResource(imageView: AppCompatImageView, resource: Int)

    @BindingAdapter("cardBackgroundColorCode")
    fun setCardBackgroundColorCode(view: MaterialCardView, color: String)

    /**
     * @param loadWithTopCornersAndBlur is a url
     */
    @BindingAdapter("loadImageWithTopCornersAndBlur")
    fun loadImageWithTopCornersAndBlur(view: AppCompatImageView, loadWithTopCornersAndBlur: String?)

    @BindingAdapter("formatDate")
    fun AppCompatTextView.formatDate(strDate: String)

    @BindingAdapter("fromHTML")
    fun AppCompatTextView.setHTMLText(text: String?)

    @BindingAdapter(value = ["setStyle"])
    fun AppCompatTextView.setStyle(@StyleRes style: Int)
}
