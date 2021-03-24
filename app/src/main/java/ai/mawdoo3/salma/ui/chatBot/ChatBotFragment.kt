package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.RateAnswerDialogListener
import ai.mawdoo3.salma.data.dataModel.PermissionMessageUiModel
import ai.mawdoo3.salma.data.dataModel.TextMessageUiModel
import ai.mawdoo3.salma.data.enums.MessageSender
import ai.mawdoo3.salma.databinding.FragmentChatBotBinding
import ai.mawdoo3.salma.ui.GpsUtils
import ai.mawdoo3.salma.utils.views.ChatBarView
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.afollestad.assent.GrantResult
import com.afollestad.assent.Permission
import com.afollestad.assent.askForPermissions
import com.afollestad.assent.isAllGranted
import com.banking.common.base.BaseFragment
import com.banking.common.base.BaseViewModel
import com.banking.common.utils.AppUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

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
        binding.chatBarView.setActionsListener(this)
        adapter.clear()
        adapter.addItem(TextMessageUiModel("كيف يمكنني مساعدتك؟", MessageSender.Masa))
        binding.recyclerView.adapter = adapter
        return attachView(binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.messageResponseList.observe(viewLifecycleOwner, {
            adapter.addItems(it)
            scrollToBottom()
        })
        viewModel.ttsAudioList.observe(viewLifecycleOwner, {
            binding.chatBarView.playAudioList(it)
        })
        viewModel.showLoader.observe(viewLifecycleOwner, {
            adapter.loading(it)
        })
        viewModel.rateAnswer.observe(viewLifecycleOwner, {

            findNavController().navigate(
                ChatBotFragmentDirections.actionChatBotFragmentToRateAnswerDialogFragment(it, this)
            )

        })
        viewModel.getUserLocation.observe(viewLifecycleOwner, {
            adapter.addItem(
                TextMessageUiModel(
                    getString(R.string.use_my_location),
                    MessageSender.User
                )
            )
            scrollToBottom()
            checkLocationPermission()
        })
        viewModel.requestPermission.observe(viewLifecycleOwner, {
            if (it == Permission.ACCESS_FINE_LOCATION) {
                if (!isAllGranted(Permission.ACCESS_FINE_LOCATION)) {
                    requestLocationPermission()
                }
            } else if (it == Permission.RECORD_AUDIO) {
                if (!isAllGranted(Permission.RECORD_AUDIO)) {
                    requestRecordAudioPermission()
                }
            }
        })

    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    /**
     * this fun will check has location permission then will proceed and request current location
     * otherwise will show message for user to request location permission
     */
    private fun checkLocationPermission() {
        val permissionsGranted: Boolean = isAllGranted(Permission.ACCESS_FINE_LOCATION)
        if (permissionsGranted) {
            requestCurrentLocation()
        } else {//show location permission card
            adapter.loading(true)
            binding.recyclerView.postDelayed({
                adapter.addItem(
                    PermissionMessageUiModel(
                        getString(R.string.location_permission_message),
                        getString(R.string.location_permission_button_title),
                        Permission.ACCESS_FINE_LOCATION
                    )
                )
                adapter.loading(false)
                scrollToBottom()
            }, 1000)
        }

    }

    /**
     * this function will show native permission dialog to request location permission
     */
    private fun requestLocationPermission() {
        // Requests one or more permissions, sending the result to a callback
        askForPermissions(Permission.ACCESS_FINE_LOCATION) { result ->
            // Check the result, see the Using Results section
            // Returns GRANTED, DENIED, or PERMANENTLY_DENIED
            when (result[Permission.ACCESS_FINE_LOCATION]) {
                GrantResult.GRANTED -> {
                    requestCurrentLocation()
                }
                GrantResult.DENIED -> {
                }
                GrantResult.PERMANENTLY_DENIED -> {
                    AppUtils.showSettingsDialog(
                        requireContext(),
                        R.string.location_permission_denied_message
                    )
                }
            }
        }
    }

    /**
     * this function will check if GPS is enabled or not and then get current location
     */
    @SuppressLint("MissingPermission")
    fun requestCurrentLocation() {
        GpsUtils(requireContext()).turnGPSOn(object : GpsUtils.onGpsListener {
            override fun gpsStatus(isGPSEnable: Boolean) {
                if (isGPSEnable) {
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
            }
        })
    }

    /**
     * this function will show native permission dialog to request location permission
     */
    private fun requestRecordAudioPermission() {
        // Requests one or more permissions, sending the result to a callback
        askForPermissions(Permission.RECORD_AUDIO) { result ->
            // Check the result, see the Using Results section
            // Returns GRANTED, DENIED, or PERMANENTLY_DENIED
            when (result[Permission.RECORD_AUDIO]) {
                GrantResult.GRANTED -> {
                    binding.chatBarView.startListening()
                }
                GrantResult.PERMANENTLY_DENIED -> {
                    AppUtils.showSettingsDialog(
                        requireContext(),
                        R.string.record_audio_permission_message
                    )
                }
            }
        }
    }

    /**
     * this method will be called when user click send button
     */
    override fun sendMessage(messageText: String) {
        viewModel.sendMessage(messageText)
    }

    override fun requestMicPermission() {
        adapter.addItem(
            PermissionMessageUiModel(
                getString(R.string.record_audio_permission_message),
                getString(R.string.record_audio_permission_button_title),
                Permission.RECORD_AUDIO
            )
        )
        scrollToBottom()
    }

    private fun scrollToBottom() {
        binding.appBar.setExpanded(false)
        binding.recyclerView.postDelayed(Runnable {
            binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
        }, 500)
    }

    override fun rateAnswer(answerId: String, isGood: Boolean) {

    }
}