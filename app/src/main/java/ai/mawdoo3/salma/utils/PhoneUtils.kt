package com.banking.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.provider.Settings.Secure
import org.apache.commons.lang3.StringUtils
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


object PhoneUtils {
    fun getDeviceId(context: Context?): String {
        val androidId = getAndroidId(context)
//        val serial = Build.SERIAL
        val info: String = getDeviceInfo()
        val udid: String = StringUtils.join(Arrays.asList(androidId, info), "-")
        val hash: String = encrypt_MD5(udid)
        val result = hash ?: udid
        return result
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidId(context: Context?): String {
        return if (context != null) {
            Secure.getString(context.contentResolver, "android_id")
        } else {
            ""
        }
    }

    private fun getDeviceInfo(): String {
        return ("35" + Build.BOARD.length % 10
                + Build.BRAND.length % 10
                + Build.DEVICE.length % 10
                + Build.DISPLAY.length % 10
                + Build.HOST.length % 10
                + Build.ID.length % 10
                + Build.MANUFACTURER.length % 10
                + Build.MODEL.length % 10
                + Build.PRODUCT.length % 10
                + Build.TAGS.length % 10
                + Build.TYPE.length % 10
                + Build.USER.length % 10)
    }

    private fun encrypt_MD5(s: String): String {
        var m: MessageDigest? = null
        try {
            m = MessageDigest.getInstance("MD5")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        m!!.update(s.toByteArray(), 0, s.length)
        val hash = BigInteger(1, m.digest())
        return String.format("%032x", hash)
    }

    fun getPhoneTypeByScreenSize(context: Context): String {
        if (context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            return "TABLET"
        } else {
            return "PHONE"
        }
    }
}

