@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")

package com.banking.common.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.util.Patterns
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.banking.common.base.BaseFragment
import com.google.android.material.appbar.AppBarLayout
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


object AppUtils {
    fun getColorFromAttr(context: Context, colorAttr: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(colorAttr, typedValue, true)
        @ColorInt val color = typedValue.data
        return color
    }

    fun addBorderToBitmap(
        srcBitmap: Bitmap,
        borderWidth: Int,
        borderColor: Int,
        radius: Int
    ): Bitmap {
        val dstBitmap = Bitmap.createBitmap(
            srcBitmap.width + borderWidth * 2,  // Width
            srcBitmap.height + borderWidth * 2,  // Height
            Bitmap.Config.ARGB_8888 // Config
        )

        val canvas = Canvas(dstBitmap)

        val paint = Paint()
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth.toFloat()
        paint.isAntiAlias = true

        val rect = RectF(
            borderWidth / 2f,
            borderWidth / 2f,
            canvas.width - borderWidth * 3f,
            canvas.height - borderWidth * 3f
        )

        val rect2 = RectF(
            borderWidth / 2f,
            borderWidth / 2f,
            canvas.width - canvas.width * 0.25f,
            canvas.height - canvas.width * 0.25f
        )

        canvas.drawBitmap(srcBitmap, null, rect, null)

        canvas.drawRoundRect(
            rect,
            radius.toFloat(),
            radius.toFloat(),
            paint
        )

        srcBitmap.recycle()

        return dstBitmap
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    // px to db
    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun tryToAddPrefixToUrl(url: String): String {
        var result = url

        if (url.isNullOrBlank() || !Patterns.WEB_URL.matcher(url).matches()) {
            return result
        }

        if (!result.startsWith("www.") && !result.startsWith("http://") && !result.startsWith("https://")) {
            result = "www.$result"
        }

        if (!result.startsWith("http://") && !result.startsWith("https://")) {
            result = "http://$result"
        }
        return result
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        var bitmap: Bitmap? = null
        if (drawable is BitmapDrawable) {
            val bitmapDrawable = drawable
            if (bitmapDrawable.bitmap != null) {
                return bitmapDrawable.bitmap
            }
        }
        bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(
                1,
                1,
                Bitmap.Config.ARGB_8888
            ) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    @SuppressLint("SimpleDateFormat")
    fun convertStringToDate(dateString: String): Date {
        try {
            var spf = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss")
            var newDate: Date? = null
            newDate = spf.parse(dateString)
            return newDate
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return Date()
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDateToView(date: String?, showTime: Boolean = true): String? {
        try {
            var spf = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss")
            var newDate: Date? = null
            newDate = spf.parse(date?.let { date } ?: run { "2020-10-10 10:10:10" })
            spf = SimpleDateFormat("yyyy/MM/dd")
            val date = spf.format(newDate) // date in String
            if (showTime) {
                spf = SimpleDateFormat("hh:mm a")
                val time = spf.format(newDate).replace("ص", "صباحاً").replace("م", "مساءا")
                return "$date ,الساعة $time"
            } else {
                return date
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }

    fun replaceIndianNoWithArabicNo(indianNo: String): String {
        var text = indianNo
        text = text.replace("٠", "0")
        text = text.replace("١", "1")
        text = text.replace("٢", "2")
        text = text.replace("٣", "3")
        text = text.replace("٤", "4")
        text = text.replace("٥", "5")
        text = text.replace("٦", "6")
        text = text.replace("٧", "7")
        text = text.replace("٨", "8")
        text = text.replace("٩", "9")

        return text
    }

    fun isHaveIndianNO(text: String): Boolean {
        if (text.contains("٠")) return true
        if (text.contains("١")) return true
        if (text.contains("٢")) return true
        if (text.contains("٣")) return true
        if (text.contains("٤")) return true
        if (text.contains("٥")) return true
        if (text.contains("٦")) return true
        if (text.contains("٧")) return true
        if (text.contains("٨")) return true
        if (text.contains("٩")) return true
        return false
    }

    fun openLinkInTheBrowser(url: String, context: Context) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }

    fun hideKeyboard(activity: Activity?, view: View?) {
        val imm: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun getFileSizeInMB(path: String): Double {
        val file = File(path)
        // Get length of file in bytes
        val fileSizeInBytes = file.length()
        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        val fileSizeInKB = fileSizeInBytes.toDouble() / 1024
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        return fileSizeInKB / 1024
    }


    fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        } else {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo = cm.activeNetworkInfo
            result = activeNetwork.isConnected
        }

        return result
    }

    fun navigateToFragment(context: BaseFragment, @IdRes actionID: Int) {
        context.findNavController().navigate(actionID)
    }

    fun navigateToFragment(context: BaseFragment, navDirection: NavDirections) {
        context.findNavController().navigate(navDirection)
    }

    fun navigateToActivity(context: FragmentActivity, @IdRes viewId: Int, @IdRes actionID: Int) {
        context.findNavController(viewId).navigate(actionID)
    }


    fun setAppBarHeight(context: Context, appBarLayout: AppBarLayout) {
        val appBarLayout: AppBarLayout = appBarLayout
        appBarLayout.layoutParams =
            CoordinatorLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(context) + dpToPx(context, 48)
            )
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId: Int =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun dpToPx(context: Context, dp: Int): Int {
        val density: Float = context.resources
            .displayMetrics.density
        return (dp.toFloat() * density).roundToInt()
    }

    fun requestFocus(context: Context, view: View) {
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        view.requestFocus()
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }


}