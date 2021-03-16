package com.banking.common.base

import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.databinding.FragmentBaseBinding
import ai.mawdoo3.salma.utils.setVisible
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

import com.banking.common.utils.AppUtils
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {

    private var toast: Toast? = null
    var baseFragmentBinding: FragmentBaseBinding? = null


    //must override in fragment
    abstract fun getViewModel(): BaseViewModel?

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.hideKeyboard(activity, this.view)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        baseFragmentBinding = FragmentBaseBinding.inflate(inflater)
        baseFragmentBinding?.stateLayout?.button?.setOnClickListener { onTryToReload() }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getViewModel()?.unauthorized?.observe(viewLifecycleOwner, Observer {
//            listener.unauthorized()
        })

        getViewModel()?.showProgress?.observe(viewLifecycleOwner, Observer {
            showProgress(it)
        })

        getViewModel()?.showLoadMoreProgress?.observe(viewLifecycleOwner, Observer {
            showLoadMoreProgress(it)
        })

        getViewModel()?.showErrorMessageRid?.observe(viewLifecycleOwner, Observer {
            showSnackbarMessage(it)
        })

        getViewModel()?.showErrorMessage?.observe(viewLifecycleOwner, Observer {
            showSnackbarMessage(it)
        })

        getViewModel()?.screenState?.observe(viewLifecycleOwner, Observer {
            setScreenState(it)
        })


        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    fun attachView(root: View): View? {
        baseFragmentBinding?.container?.addView(root)
        return baseFragmentBinding?.root
    }

    protected open fun showProgress(show: Boolean) {
        if (show) {
            setScreenState(ScreenState.Loading)
        } else {
            setScreenState(ScreenState.Nothing)
        }
    }

    fun showSnackbarMessage(msg: String) {
        this.view?.let {
            AppUtils.hideKeyboard(activity, this.view)
            Snackbar.make(it, msg, Snackbar.LENGTH_SHORT).show()
        }
    }

    fun showSnackbarMessage(@StringRes msg: Int) {
        this.view?.let {
            AppUtils.hideKeyboard(activity, this.view)
            Snackbar.make(it, msg, Snackbar.LENGTH_SHORT).show()
        }
    }

    protected fun cancelToast() {
        toast?.cancel()
    }

    private fun setScreenState(state: ScreenState) {
        when (state) {
            is ScreenState.Nothing -> showNothingState()
            is ScreenState.NoInternet -> showNoInternetState()
            is ScreenState.Empty -> showEmptyState()
            is ScreenState.Loading -> showLoadingState()
            is ScreenState.General -> showGeneralState(state.message)
        }
    }

    private fun showLoadingState() {
        baseFragmentBinding?.stateLayout?.loaderLayout?.setVisible(true)
        baseFragmentBinding?.stateLayout?.errorLayout?.setVisible(false)
        baseFragmentBinding?.stateLayout?.root?.bringToFront()
    }

    open fun showNothingState() {
        baseFragmentBinding?.stateLayout?.errorLayout?.setVisible(false)
        baseFragmentBinding?.stateLayout?.loaderLayout?.setVisible(false)
    }

    open fun showNoInternetState() {
        baseFragmentBinding?.stateLayout?.loaderLayout?.setVisible(false)
        baseFragmentBinding?.stateLayout?.errorLayout?.setVisible(true)
        baseFragmentBinding?.stateLayout?.text?.setText(R.string.no_connection_message)
        baseFragmentBinding?.stateLayout?.root?.bringToFront()
    }

    open fun showEmptyState() {
        baseFragmentBinding?.stateLayout?.loaderLayout?.setVisible(false)
        baseFragmentBinding?.stateLayout?.errorLayout?.setVisible(true)
        baseFragmentBinding?.stateLayout?.button?.setVisible(false)
        baseFragmentBinding?.stateLayout?.text?.text = getString(R.string.no_data_available)
        baseFragmentBinding?.stateLayout?.root?.bringToFront()
    }


    open fun showGeneralState(message: String) {
        baseFragmentBinding?.stateLayout?.loaderLayout?.setVisible(false)
        baseFragmentBinding?.stateLayout?.errorLayout?.setVisible(true)
        baseFragmentBinding?.stateLayout?.text?.text = message
        baseFragmentBinding?.stateLayout?.root?.bringToFront()
    }

    /**
     * override this fun in the child classes to implement try to reload from empty state layout
     */
    open fun onTryToReload() {
        setScreenState(ScreenState.Nothing)
    }

    //override this method in your fragment for call adapter show loadmore
    open fun showLoadMoreProgress(it: Boolean) {}

    sealed class ScreenState {
        object NoInternet : ScreenState()
        object Empty : ScreenState()
        object Nothing : ScreenState()
        object Loading : ScreenState()
        data class General(val message: String) : ScreenState()

    }


    override fun onDestroy() {
        super.onDestroy()
        showProgress(false)
    }

    protected fun setFullScreenFlags() {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected fun clearFullScreenFlags() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


}