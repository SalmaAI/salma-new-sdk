package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.RateAnswerDialogListener
import ai.mawdoo3.salma.data.dataModel.TextMessageUiModel
import ai.mawdoo3.salma.data.enums.MessageSender
import ai.mawdoo3.salma.databinding.FragmentChatBotBinding
import ai.mawdoo3.salma.ui.GpsUtils
import ai.mawdoo3.salma.utils.views.ChatBarView
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.banking.common.base.BaseFragment
import com.banking.common.base.BaseViewModel
import com.banking.common.utils.AppUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class ChatBotFragment : BaseFragment(), ChatBarView.ChatBarListener,
    RateAnswerDialogListener {

    private val viewModel: ChatBotViewModel by viewModel()
    private val adapter: MessagesAdapter by inject { parametersOf(viewModel) }
    private lateinit var binding: FragmentChatBotBinding

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext().applicationContext)
    }

    private var cancellationTokenSource = CancellationTokenSource()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentChatBotBinding.inflate(inflater, container, false)
        binding.chatActionsView.setActionsListener(this)
        adapter.clear()
        adapter.addItem(TextMessageUiModel("كيف يمكنني مساعدتك؟", MessageSender.Masa))
        binding.recyclerView.adapter = adapter
        return attachView(binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.messageResponseList.observe(viewLifecycleOwner, {
            adapter.addItems(it)
            binding.recyclerView.postDelayed(Runnable {
                binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
            }, 500)
        })
        viewModel.showLoader.observe(viewLifecycleOwner, {
            binding.appBar.setExpanded(false)
            adapter.loading(it)
        })
        viewModel.rateAnswer.observe(viewLifecycleOwner, {

            findNavController().navigate(
                ChatBotFragmentDirections.actionChatBotFragmentToRateAnswerDialogFragment(it, this)
            )

        })
        viewModel.getUserLocation.observe(viewLifecycleOwner, {
            checkGPSPermission()
        })

    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    private fun checkGPSPermission() {
        // Check Fine permission
        Permissions.check(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
            null,
            object : PermissionHandler() {
                override fun onGranted() {
                    // Main code
                    GpsUtils(requireContext()).turnGPSOn(object : GpsUtils.onGpsListener {
                        override fun gpsStatus(isGPSEnable: Boolean) {
                            if (isGPSEnable) {
                                requestCurrentLocation()
                            }
                        }
                    })
                }

                override fun onDenied(
                    context: Context?,
                    deniedPermissions: ArrayList<String>?
                ) {
                    Snackbar.make(
                        binding.root,
                        context!!.getString(R.string.location_permission_denied_message),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                override fun onBlocked(
                    context: Context?,
                    blockedList: ArrayList<String>?
                ): Boolean {
                    AppUtils.showSettingsDialog(
                        requireContext(),
                        R.string.location_permission_denied_message
                    )
                    return true
                }
            })

    }

    @SuppressLint("MissingPermission")
    fun requestCurrentLocation() {
        adapter.loading(true)
        val currentLocationTask: Task<Location> =
            fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            )

        currentLocationTask.addOnCompleteListener { task: Task<Location> ->
            if (task.isSuccessful) {
                val result: Location = task.result
                "Location (success): ${result.latitude}, ${result.longitude}"
                showSnackbarMessage("Location (success): ${result.latitude}, ${result.longitude}")
                adapter.loading(false)
            } else {
                adapter.loading(false)
                showSnackbarMessage(getString(R.string.get_location_failed_message))
            }
        }
    }

    /**
     * this method will be called when user click send button
     */
    override fun sendMessage(messageText: String) {
        viewModel.sendMessage(messageText)
    }


    /**
     * this method will be called when user click on record button to start speaking
     */
    override fun onStartListening() {

    }


    /**
     * this method will be called when user click on end button while speaking
     */
    override fun onEndListening() {

    }

    override fun rateAnswer(answerId: String, isGood: Boolean) {

    }
}