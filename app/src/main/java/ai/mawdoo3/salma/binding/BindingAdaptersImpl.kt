package com.banking.common.binding

import android.graphics.Color
import android.text.Spannable
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.HtmlCompat
import androidx.core.widget.TextViewCompat
import com.banking.common.utils.AppUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.text.SimpleDateFormat

class BindingAdaptersImpl : BindingAdapters {

    override fun <T> AppCompatImageView.loadImage(
        model: T,
        corners: Float,
        strokeWidth: Float,
        strokeColor: Int,
        isCircle: Boolean,
        placeHolder: Int
    ) {

        model?.let {
            Glide.with(context)
                .load(model)
                .placeholder(placeHolder)
                .error(placeHolder)
                .fallback(placeHolder)
                .transform(
                    if (isCircle && strokeWidth > 0) {
                        CropCircleWithBorderTransformation(strokeWidth.toInt(), strokeColor)
                    } else if (isCircle) {
                        CircleCrop()
                    } else {
                        MultiTransformation(
                            FitCenter()
                        )
                    }

                )
                .into(this)
        }

    }


    override fun <T> AppCompatImageView.blur(url: T) {
        url?.let {
            val multi = MultiTransformation(
                BlurTransformation(30),
                CenterCrop()
            )
            Glide.with(context)
                .load(url)
//                .error(R.drawable.worm_dot_background)
                .apply(RequestOptions.bitmapTransform(multi))
                .into(this)
        }
    }

    override fun setImageResource(imageView: AppCompatImageView, resource: Int) {
        imageView.setImageResource(resource)
    }

    override fun setCardBackgroundColorCode(view: MaterialCardView, color: String) {
        val color = Color.parseColor(color)
        view.setCardBackgroundColor(color)
    }

    override fun loadImageWithTopCornersAndBlur(view: AppCompatImageView, url: String?) {
        if (url == null) return
        Glide.with(view.context)
            .load(url)
            .transform(
                MultiTransformation(
                    CenterCrop(),
                    BlurTransformation(25),
                    RoundedCornersTransformation(
                        AppUtils.convertDpToPixel(5f, view.context).toInt(),
                        0,
                        RoundedCornersTransformation.CornerType.TOP
                    )
                )
            ).into(view)
    }

    private val inDateFormat = SimpleDateFormat("yyyy-MM-dd")
    private val outDateFormat = SimpleDateFormat("E dd MMM")
    override fun AppCompatTextView.formatDate(
        strDate: String
    ) {
        try {
            this.text = outDateFormat.format(inDateFormat.parse(strDate))
        } catch (ex: Exception) {
        }
    }

    override fun AppCompatTextView.setHTMLText(text: String?) {
        text?.let {
            //convert html to spannable
            val spannable =
                HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT) as Spannable
            //remove underline from href url
//            for (u in spannable.getSpans(0, spannable.length, URLSpan::class.java)) {
//                spannable.setSpan(object : UnderlineSpan() {
//                    override fun updateDrawState(tp: TextPaint) {
//                        tp.isUnderlineText = false
//                    }
//                }, spannable.getSpanStart(u), spannable.getSpanEnd(u), 0)
//            }
            this.text = spannable.trim()
        }
    }


    override fun AppCompatTextView.setStyle(style: Int) {
        TextViewCompat.setTextAppearance(this, style)
    }
}