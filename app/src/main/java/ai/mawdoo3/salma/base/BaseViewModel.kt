package com.banking.common.base

import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.utils.UnauthorizedException
import android.app.Application
import androidx.lifecycle.AndroidViewModel

import com.hadilq.liveevent.LiveEvent
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    val applicationContext = application
    val showErrorMessage = LiveEvent<String>()
    val showErrorMessageRid = LiveEvent<Int>()

    val showProgress = LiveEvent<Boolean>()
    val showLoadMoreProgress = LiveEvent<Boolean>()

    val screenState = LiveEvent<BaseFragment.ScreenState>()

    val unauthorized = LiveEvent<Boolean>()

    open fun onStartLoading(isLoadMore: Boolean = false) {
        if (isLoadMore) showLoadMoreProgress.value = true
        else
            showProgress.value = true
    }

    open fun onLoadFailure(error: Throwable, asToast: Boolean) {
        error.printStackTrace()
        showLoadMoreProgress.value = false
        showProgress.value = false

        when (error) {
            is UnauthorizedException -> {
                unauthorized.value = true
            }

            is SocketTimeoutException,
            is UnknownHostException,
            is ConnectException -> {
                if (asToast) {
                    showErrorMessageRid.postValue(R.string.no_connection_message)
                } else {
                    screenState.value = BaseFragment.ScreenState.NoInternet
                }
            }

            else -> {
                if (asToast) {
                    showErrorMessage.postValue(
                        error.message
                            ?: applicationContext.getString(R.string.general_error_message)
                    )
                } else {
                    screenState.value = BaseFragment.ScreenState.General(
                        error.message
                            ?: applicationContext.getString(R.string.general_error_message)
                    )
                }
            }
        }

    }

    open fun onLoadSuccess() {
        showLoadMoreProgress.value = false
        screenState.value = BaseFragment.ScreenState.Nothing

    }

    open fun onEmptyScreen() {
        screenState.value = BaseFragment.ScreenState.Empty

    }
}