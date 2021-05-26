package ai.mawdoo3.salma.binding

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation

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
    }

}