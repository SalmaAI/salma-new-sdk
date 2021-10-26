package ai.mawdoo3.salma.binding

import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.utils.AppUtils
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation
import java.util.*

class MyBindingAdapters {
    companion object {
        @JvmStatic
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

        @JvmStatic
        @BindingAdapter("app:enableCollapsingScroll")
        fun setCollapsingToolbarLayoutScrollEnabled(
            collapsingToolbarLayout: CollapsingToolbarLayout,
            enabled: Boolean?
        ) {
            val lp = collapsingToolbarLayout.layoutParams as AppBarLayout.LayoutParams
            if (enabled != null && enabled == true) {
                lp.scrollFlags =
                    AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
            } else {
                lp.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
            }
            collapsingToolbarLayout.layoutParams = lp
        }

        @JvmStatic
        @BindingAdapter("showTime")
        fun AppCompatTextView.setDayAndTime(
            showDay: Boolean
        ) {
            if (showDay) {
                this.text = AppUtils.getCurrentTimeWithDay()
            } else {
                this.text = AppUtils.getCurrentTime()
            }
        }

        @JvmStatic
        @BindingAdapter("showWelcomingMessage", "showMessageBasedOnTime")
        fun AppCompatTextView.setWelcomingMessage(
            name: String?,
            showMessageBasedOnTime: Boolean
        ) {
            var welcomeMessage = ""
            if (showMessageBasedOnTime) {
                val hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                welcomeMessage = when (hours) {
                    in 5..11 -> {
                        this.context.getString(R.string.good_morning)
                    }
                    in 12..17 -> {
                        this.context.getString(R.string.good_evening)
                    }
                    else -> {
                        this.context.getString(R.string.hello)
                    }
                }
                if (!name.isNullOrEmpty()) {
                    welcomeMessage += "{ $name}"
                }
            } else {
                var newName = ""
                if (!name.isNullOrEmpty()) {
                    newName = "$name!"
                }
                welcomeMessage =
                    String.format(this.context.getString(R.string.hello_message), newName)
            }
            this.text = welcomeMessage
        }
    }


}