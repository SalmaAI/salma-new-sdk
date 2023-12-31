@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")

package ai.mawdoo3.salma.utils

import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.base.BaseFragment
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat


object AppUtils {

    private var snackbar: Snackbar? = null


    fun showSnackbarMessage(msg: String, activity: Activity, view: View?) {
        hideKeyboard(activity, view)
        view?.let {
            snackbar = Snackbar.make(it, msg, Snackbar.LENGTH_INDEFINITE)
                .setAction(activity.getString(R.string.general_error_message)) {
                    Log.d("", "")
                }
            snackbar?.setActionTextColor(
                getColorFromAttr(
                    activity,
                    R.attr.actionMenuTextColor
                )
            )
            snackbar?.show()
        }
    }

    fun showSettingsDialog(context: Context, @StringRes message: Int) {
        val alertDialogBuilder = MaterialAlertDialogBuilder(
            context,
            R.style.Theme_Salma_MaterialAlertDialog
        )
            .setTitle(context.getString(R.string.permission_denied_title))
            .setMessage(message)
            .setPositiveButton(R.string.go_to_settings) { dialog, _ ->
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:${context.packageName}")
                ).apply {
                    addCategory(Intent.CATEGORY_DEFAULT)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(this)
                }
                dialog.dismiss()
            }.setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
        alertDialogBuilder.show()
    }


    fun sessionEndedDialog(
        context: Context,
        @StringRes title: Int,
        @StringRes message: Int,
        listener: SessionListener?
    ) {
        val alertDialogBuilder =
            MaterialAlertDialogBuilder(context, R.style.Theme_Salma_MaterialAlertDialog)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.ok) { dialog, _ ->
                    listener?.onSessionEnded()
                }
        alertDialogBuilder.show()
    }

    interface SessionListener {
        fun onSessionEnded()
    }

    fun getColorFromAttr(context: Context, colorAttr: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(colorAttr, typedValue, true)
        return typedValue.data
    }

//    fun addBorderToBitmap(
//        srcBitmap: Bitmap,
//        borderWidth: Int,
//        borderColor: Int,
//        radius: Int
//    ): Bitmap {
//        val dstBitmap = Bitmap.createBitmap(
//            srcBitmap.width + borderWidth * 2,  // Width
//            srcBitmap.height + borderWidth * 2,  // Height
//            Bitmap.Config.ARGB_8888 // Config
//        )
//
//        val canvas = Canvas(dstBitmap)
//
//        val paint = Paint()
//        paint.color = borderColor
//        paint.style = Paint.Style.STROKE
//        paint.strokeWidth = borderWidth.toFloat()
//        paint.isAntiAlias = true
//
//        val rect = RectF(
//            borderWidth / 2f,
//            borderWidth / 2f,
//            canvas.width - borderWidth * 3f,
//            canvas.height - borderWidth * 3f
//        )
//
//        val rect2 = RectF(
//            borderWidth / 2f,
//            borderWidth / 2f,
//            canvas.width - canvas.width * 0.25f,
//            canvas.height - canvas.width * 0.25f
//        )
//
//        canvas.drawBitmap(srcBitmap, null, rect, null)
//
//        canvas.drawRoundRect(
//            rect,
//            radius.toFloat(),
//            radius.toFloat(),
//            paint
//        )
//
//        srcBitmap.recycle()
//
//        return dstBitmap
//    }

    fun makePhoneCall(phoneNumber: String, context: Context) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        context.startActivity(intent)
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    // px to db
//    fun convertPixelsToDp(px: Float, context: Context): Float {
//        return px / (context.resources
//            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
//    }
//
//    fun tryToAddPrefixToUrl(url: String): String {
//        var result = url
//
//        if (url.isNullOrBlank() || !Patterns.WEB_URL.matcher(url).matches()) {
//            return result
//        }
//
//        if (!result.startsWith("www.") && !result.startsWith("http://") && !result.startsWith("https://")) {
//            result = "www.$result"
//        }
//
//        if (!result.startsWith("http://") && !result.startsWith("https://")) {
//            result = "http://$result"
//        }
//        return result
//    }
//
//    fun drawableToBitmap(drawable: Drawable): Bitmap? {
//        var bitmap: Bitmap? = null
//        if (drawable is BitmapDrawable) {
//            val bitmapDrawable = drawable
//            if (bitmapDrawable.bitmap != null) {
//                return bitmapDrawable.bitmap
//            }
//        }
//        bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
//            Bitmap.createBitmap(
//                1,
//                1,
//                Bitmap.Config.ARGB_8888
//            ) // Single color bitmap will be created of 1x1 pixel
//        } else {
//            Bitmap.createBitmap(
//                drawable.intrinsicWidth,
//                drawable.intrinsicHeight,
//                Bitmap.Config.ARGB_8888
//            )
//        }
//        val canvas = Canvas(bitmap)
//        drawable.setBounds(0, 0, canvas.width, canvas.height)
//        drawable.draw(canvas)
//        return bitmap
//    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentTime(): String {
        val date = System.currentTimeMillis()
        val spf = SimpleDateFormat("hh:mm a")
        val time = spf.format(date)
//            .replace("ص", "صباحاً").replace("م", "مساءا")
        return replaceIndianNoWithArabicNo(time)
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentTimeWithDay(): String {
        val date = System.currentTimeMillis()
        val spf = SimpleDateFormat("hh:mm a")
        val time = spf.format(date)
            .replace("ص", "صباحاً").replace("م", "مساءً")
        val dayFormat = SimpleDateFormat("EEEE")
        val day = dayFormat.format(date)
        return "$day ${replaceIndianNoWithArabicNo(time)}"
    }

//    @SuppressLint("SimpleDateFormat")
//    fun convertStringToDate(dateString: String): Date {
//        try {
//            var spf = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss")
//            var newDate: Date? = null
//            newDate = spf.parse(dateString)
//            return newDate
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//        return Date()
//    }

//    @SuppressLint("SimpleDateFormat")
//    fun formatDateToView(date: String?, showTime: Boolean = true): String? {
//        try {
//            var spf = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss")
//            var newDate: Date? = null
//            newDate = spf.parse(date?.let { date } ?: run { "2020-10-10 10:10:10" })
//            spf = SimpleDateFormat("yyyy/MM/dd")
//            val date = spf.format(newDate) // date in String
//            if (showTime) {
//                spf = SimpleDateFormat("hh:mm a")
//                val time = spf.format(newDate).replace("ص", "صباحاً").replace("م", "مساءا")
//                return "$date ,الساعة $time"
//            } else {
//                return date
//            }
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//        return ""
//    }

    private fun replaceIndianNoWithArabicNo(indianNo: String): String {
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

//    fun isHaveIndianNO(text: String): Boolean {
//        if (text.contains("٠")) return true
//        if (text.contains("١")) return true
//        if (text.contains("٢")) return true
//        if (text.contains("٣")) return true
//        if (text.contains("٤")) return true
//        if (text.contains("٥")) return true
//        if (text.contains("٦")) return true
//        if (text.contains("٧")) return true
//        if (text.contains("٨")) return true
//        if (text.contains("٩")) return true
//        return false
//    }

    fun openLinkInTheBrowser(url: String, context: Context) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }

    fun copyToClipboard(text: String, context: Context) {
        val clipboard: ClipboardManager? =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText(text, text)
        Toast.makeText(context, context.getString(R.string.text_copied), Toast.LENGTH_SHORT).show()
        clipboard?.setPrimaryClip(clip)
    }

    fun openShareIntent(text: String, context: Context) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "")
        intent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(
            Intent.createChooser(
                intent,
                context.getString(R.string.choose_share_method)
            )
        )
    }

    fun hideKeyboard(activity: Activity?, view: View?) {
        val imm: InputMethodManager? =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

//    fun getFileSizeInMB(path: String): Double {
//        val file = File(path)
//        // Get length of file in bytes
//        val fileSizeInBytes = file.length()
//        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
//        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
//        val fileSizeInKB = fileSizeInBytes.toDouble() / 1024
//        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
//        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
//        return fileSizeInKB / 1024
//    }


//    fun isInternetAvailable(context: Context): Boolean {
//        var result = false
//        val connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val networkCapabilities = connectivityManager.activeNetwork ?: return false
//            val capabilities =
//                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
//            result = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//                    && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
//        } else {
//            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
//            result = activeNetwork?.isConnected?:false
//        }
//
//        return result
//    }

    fun navigateToFragment(context: BaseFragment, @IdRes actionID: Int) {
        context.findNavController().navigate(actionID)
    }

    fun navigateToFragment(context: BaseFragment, navDirection: NavDirections) {
        context.findNavController().navigate(navDirection)
    }

//    fun navigateToActivity(context: FragmentActivity, @IdRes viewId: Int, @IdRes actionID: Int) {
//        context.findNavController(viewId).navigate(actionID)
//    }
//
//    fun setAppBarHeight(context: Context, appBarLayout: AppBarLayout) {
//        val appBarLayout: AppBarLayout = appBarLayout
//        appBarLayout.layoutParams =
//            CoordinatorLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                getStatusBarHeight(context) + dpToPx(context, 48)
//            )
//    }

//    fun getStatusBarHeight(context: Context): Int {
//        var result = 0
//        val resourceId: Int =
//            context.resources.getIdentifier("status_bar_height", "dimen", "android")
//        if (resourceId > 0) {
//            result = context.resources.getDimensionPixelSize(resourceId)
//        }
//        return result
//    }
//
//    fun dpToPx(context: Context, dp: Int): Int {
//        val density: Float = context.resources
//            .displayMetrics.density
//        return (dp.toFloat() * density).roundToInt()
//    }

    fun requestFocus(context: Context, view: View) {
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        view.requestFocus()
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }


}