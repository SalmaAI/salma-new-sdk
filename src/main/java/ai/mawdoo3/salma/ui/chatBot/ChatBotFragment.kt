package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.BuildConfig
import ai.mawdoo3.salma.MasaSdkInstance
import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.RateAnswerDialogListener
import ai.mawdoo3.salma.base.BaseFragment
import ai.mawdoo3.salma.base.BaseViewModel
import ai.mawdoo3.salma.data.dataModel.PermissionMessageUiModel
import ai.mawdoo3.salma.data.dataModel.TextMessageUiModel
import ai.mawdoo3.salma.data.enums.ChatBarType
import ai.mawdoo3.salma.data.enums.MessageSender
import ai.mawdoo3.salma.databinding.FragmentChatBotBinding
import ai.mawdoo3.salma.ui.GpsUtils
import ai.mawdoo3.salma.utils.AppUtils
import ai.mawdoo3.salma.utils.makeGone
import ai.mawdoo3.salma.utils.views.ChatBarView
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.assent.GrantResult
import com.afollestad.assent.Permission
import com.afollestad.assent.askForPermissions
import com.afollestad.assent.isAllGranted
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class ChatBotFragment : BaseFragment(), ChatBarView.ChatBarListener,
    RateAnswerDialogListener {

    private var phone = ""
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

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.itemAnimator = SlideInUpAnimator()
        binding.recyclerView.itemAnimator?.apply {
            addDuration = 400
            removeDuration = 0
            moveDuration = 0
            changeDuration = 0
        }
        var welcomeImage: Int
        val hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (hours) {
            in 5..11 -> {
                welcomeImage = R.drawable.header_morning
            }
            in 12..17 -> {
                welcomeImage = R.drawable.header_evening
            }
            else -> {
                welcomeImage = R.drawable.header_night
            }
        }
        binding.imgHeader.setImageResource(welcomeImage)

        if (BuildConfig.FLAVOR == "jkb") {
            adapter.addItem(
                TextMessageUiModel(
                    getString(R.string.masa_welcoming_message),
                    MessageSender.Masa,
                    time = AppUtils.getCurrentTime()
                )
            )
        }
        if (MasaSdkInstance.username.isNullOrEmpty()) {
            binding.name = ""
        } else {
            binding.name = MasaSdkInstance.username
        }
        if (MasaSdkInstance.chatBarType == ChatBarType.NONE) {
            binding.chatBarView.makeGone()
        } else {
            binding.chatBarView.setChatBarType(MasaSdkInstance.chatBarType)
        }
        return attachView(binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.messageResponseList.observe(viewLifecycleOwner, {
            Log.d("SendMessage", "Add Masa message")
            adapter.addItems(it)
        })
        viewModel.openLink.observe(viewLifecycleOwner, {
            AppUtils.openLinkInTheBrowser(it, requireContext())
        })
        viewModel.openDialUp.observe(viewLifecycleOwner, {
            AppUtils.makePhoneCall(it, requireContext())
        })
        viewModel.messageSent.observe(viewLifecycleOwner, {
            Log.d("SendMessage", "Add user message")
            adapter.clear()
            binding.enableCollapse = true
            binding.appBar.setExpanded(false)
//            adapter.addItem(it)

            binding.recyclerView.postDelayed({
                adapter.addItem(it)
            }, 500)
//            scrollToBottom()
        })
        viewModel.ttsAudioList.observe(viewLifecycleOwner, {
//            binding.chatBarView.playAudioList(it)
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
                    MessageSender.User,
                    time = AppUtils.getCurrentTime()
                )
            )
            scrollToBottom()
            checkLocationPermission()
        })
        viewModel.requestPermission.observe(viewLifecycleOwner, {
            if (it == Permission.ACCESS_FINE_LOCATION) {
                if (!isAllGranted(Permission.ACCESS_FINE_LOCATION)) {
                    requestPermission(Permission.ACCESS_FINE_LOCATION)
                }
            } else if (it == Permission.RECORD_AUDIO) {
                if (!isAllGranted(Permission.RECORD_AUDIO)) {
                    requestPermission(Permission.RECORD_AUDIO)
                }
            } else if (it == Permission.CALL_PHONE) {
                if (!isAllGranted(Permission.CALL_PHONE)) {
                    requestPermission(Permission.CALL_PHONE)
                }
            }
        })
        viewModel.makeCall.observe(viewLifecycleOwner, {
            phone = it
            val permissionsGranted: Boolean = isAllGranted(Permission.CALL_PHONE)
            if (permissionsGranted) {
                makeCall()
            } else {
                adapter.addItem(
                    PermissionMessageUiModel(
                        getString(R.string.call_permission_message),
                        getString(R.string.call_permission_button_title),
                        Permission.CALL_PHONE
                    )
                )
                scrollToBottom()
            }
        })
        viewModel.goToLocation.observe(viewLifecycleOwner, {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=$it")
            )
            startActivity(intent)
        })

    }

    private fun makeCall() {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:" + phone)
        startActivity(intent)
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
                            viewModel.sendMessage(
                                text = "",
                                payload = result.latitude.toString() + "," + result.longitude.toString(),
                                showMessage = false
                            )
                        } else {
                            adapter.addItem(
                                TextMessageUiModel(
                                    getString(R.string.get_location_failed_message),
                                    MessageSender.Masa,
                                    time = AppUtils.getCurrentTime()
                                )
                            )
                        }
                    }
                }
            }
        })
    }

    /**
     * this function will show native permission dialog to request permission
     * if permission permanently denied custom settings dialog will be shown
     */
    private fun requestPermission(permission: Permission) {
        // Requests one or more permissions, sending the result to a callback
        askForPermissions(permission) { result ->
            // Check the result, see the Using Results section
            // Returns GRANTED, DENIED, or PERMANENTLY_DENIED
            when (result[permission]) {
                GrantResult.GRANTED -> {
                    when (permission) {
                        Permission.RECORD_AUDIO -> {
                            binding.chatBarView.startListening()
                        }
                        Permission.CALL_PHONE -> {
                            makeCall()
                        }
                        Permission.ACCESS_FINE_LOCATION -> {
                            requestCurrentLocation()
                        }
                    }
                }
                GrantResult.PERMANENTLY_DENIED -> {
                    val message = when (permission) {
                        Permission.RECORD_AUDIO -> {
                            R.string.record_audio_permission_message
                        }
                        Permission.CALL_PHONE -> {
                            R.string.call_permission_message
                        }
                        Permission.ACCESS_FINE_LOCATION -> {
                            R.string.location_permission_message
                        }
                        else -> {
                            R.string.permission_denied_message
                        }
                    }
                    AppUtils.showSettingsDialog(
                        requireContext(),
                        message
                    )
                }
            }
        }
    }

    /**
     * this method will be called when user click send button
     */
    override fun sendMessage(messageText: String) {
        AppUtils.hideKeyboard(activity, binding.chatBarView)
        viewModel.sendMessage(text = messageText, payload = messageText)
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

    override fun showError(error: Int) {
        showSnackbarMessage(error)
    }

    private fun scrollToBottom() {
//        binding.appBar.setExpanded(false)
//        binding.recyclerView.postDelayed(Runnable {
//            binding.recyclerView.layoutManager?.smoothScrollToPosition(
//                binding.recyclerView,
//                RecyclerView.State(), adapter.getListCount() - 1
//            );
//        }, 500)

    }

    override fun rateAnswer(answerId: String, isGood: Boolean) {

    }
}