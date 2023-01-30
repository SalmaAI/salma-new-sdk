package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.MasaSdkInstance
import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.base.BaseFragment
import ai.mawdoo3.salma.base.BaseViewModel
import ai.mawdoo3.salma.data.dataModel.*
import ai.mawdoo3.salma.data.enums.ChatBarType
import ai.mawdoo3.salma.data.enums.MessageSender
import ai.mawdoo3.salma.databinding.FragmentChatBotBinding
import ai.mawdoo3.salma.ui.GpsUtils
import ai.mawdoo3.salma.utils.AppUtils
import ai.mawdoo3.salma.utils.getNavigationResult
import ai.mawdoo3.salma.utils.makeGone
import ai.mawdoo3.salma.utils.views.ChatBarView
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.View.OnTouchListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.assent.GrantResult
import com.afollestad.assent.Permission
import com.afollestad.assent.askForPermissions
import com.afollestad.assent.isAllGranted
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ChatBotFragment : BaseFragment(), ChatBarView.ChatBarListener {

    private var scrollingUp: Boolean = false
    private var phone = ""
    private val viewModel: ChatBotViewModel by viewModel()
    private val adapter: MessagesAdapter by inject { parametersOf(viewModel) }
    private lateinit var binding: FragmentChatBotBinding
    private var homeMenu: MenuItem? = null

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext().applicationContext)
    }

    private var cancellationTokenSource = CancellationTokenSource()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentChatBotBinding.inflate(inflater, container, false)
        binding.chatBarView.setActionsListener(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
        addRecyclerItemAnimator()
        if (adapter.isEmpty()) {
            adapter.addItem(
                HeaderUiModel(
                    sender = MessageSender.Masa,
                    name = MasaSdkInstance.username
                )
            )
            viewModel.sendMessage("", "القائمة الرئيسية", showMessage = false, newSession = true)
        }
        if (MasaSdkInstance.chatBarType == ChatBarType.NONE) {
            binding.chatBarView.makeGone()
        } else {
            binding.chatBarView.setChatBarType(MasaSdkInstance.chatBarType)
        }
        binding.loadPrevious.setOnClickListener {
            loadMore()
        }

//        binding.recyclerView.setOnTouchListener(OnTouchListener { v, event ->
//            if (event.action == MotionEvent.ACTION_SCROLL) {
//                Toast.makeText(requireContext(), "Up", Toast.LENGTH_SHORT).show()
//                // Do what you want
//                true
//            } else false
//        })
//        binding.recyclerView.setOnTouchListener(OnTouchListener { view, motionEvent ->
//            detector.onTouchEvent(motionEvent)
//            return@OnTouchListener false
//        })

//        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
////            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
////                scrollingUp = dy < 0
//////                if (dy > 0) {
//////                    binding.swipeRefreshLayout.isEnabled = false
//////                }
////                super.onScrolled(recyclerView, dx, dy)
////            }
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if (newState == SCROLL_STATE_IDLE && scrollingUp
//                    && !recyclerView.canScrollVertically(-1)
//                ) {
//                    CoroutineScope(Dispatchers.Main).launch {
//                        loadMore()
//                    }
//
//                }
//            }
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if (dy<0 && !recyclerView.canScrollVertically(-1))
//                {
//                    CoroutineScope(Dispatchers.Main).launch {
//                        loadMore()
//                    }
//                }
//            }
//        })
        return attachView(binding.root)
    }

    open class OnSwipeTouchListener(ctx: Context?, val callback: SwipeCallback) : OnTouchListener {
        private val gestureDetector: GestureDetector

        init {
            gestureDetector = GestureDetector(ctx, GestureListener())
        }

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            return gestureDetector.onTouchEvent(event)
        }

        private inner class GestureListener : SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                var result = false
                try {
                    val diffY = e2.y - e1.y
                    val diffX = e2.x - e1.x
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                callback.onSwipeRight()
                            } else {
                                callback.onSwipeLeft()
                            }
                            result = true
                        }
                    } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            callback.onSwipeBottom()
                        } else {
                            callback.onSwipeTop()
                        }
                        result = true
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
                return result
            }

            private val SWIPE_THRESHOLD = 300
            private val SWIPE_VELOCITY_THRESHOLD = 300

        }

        interface SwipeCallback {
            fun onSwipeRight()
            fun onSwipeLeft()
            fun onSwipeTop()
            fun onSwipeBottom()
        }
    }


    private fun loadMore() {
        if (adapter.itemCount < viewModel.historyList.size) {

//            adapter.clear()
            val visibleMessageIndex = viewModel.historyList.indexOf(adapter.getItem(0))
            val sublist = viewModel.historyList.subList(0, visibleMessageIndex)
            sublist.map {
                when (it) {
                    is QuickReplyMessageUiModel -> {
                        it.isHistory = true
                    }
                    is BillsMessageUiModel -> {
                        it.isHistory = true
                    }
                    is InformationalMessageUiModel -> {
                        it.isHistory = true
                    }

                }
            }
            binding.recyclerView.itemAnimator = null
            adapter.addItems(sublist, 0)
            binding.recyclerView.postDelayed({
//                binding.recyclerView.layoutManager?.scrollToPosition(
//                    visibleMessageIndex
//                )
                addRecyclerItemAnimator()
            }, 500)
        }
        binding.loadPrevious.makeGone()
//        if (adapter.getItem(0) is HeaderUiModel) {
//            adapter.removeItem(0)
//        }

//        viewModel.getMessagesHistory()
////        binding.loadMoreProgress.makeVisible()
//        delay(1000)
//        if (adapter.getItem(0) is HeaderUiModel) {
//            adapter.removeItem(0)
//        }
//        val items = ArrayList<TextMessageUiModel>()
//        items.add(TextMessageUiModel("مرحبا", MessageSender.User, false, "", false))
//        items.add(TextMessageUiModel("اهلا بك", MessageSender.Masa, false, "", false))
//        items.add(TextMessageUiModel("وين أقرب فرع", MessageSender.User, false, "", false))
//        items.add(
//            TextMessageUiModel(
//                "الرجاء ادخال ايم المنطقة",
//                MessageSender.Masa,
//                false,
//                "",
//                false
//            )
//        )
//        adapter.addItems(items, 0)
////        binding.loadMoreProgress.makeGone()
//        binding.swipeRefreshLayout.isRefreshing = false


    }

    private fun addRecyclerItemAnimator() {
        binding.recyclerView.itemAnimator = SlideInUpAnimator()
        binding.recyclerView.itemAnimator?.apply {
            addDuration = 400
            removeDuration = 0
            moveDuration = 0
            changeDuration = 0
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getNavigationResult("Message")?.observe(viewLifecycleOwner) {
            it as String
            if (it.isNotEmpty()) {
                viewModel.sendMessage(it, it, true)
            }
        }
        viewModel.messageResponseList.observe(viewLifecycleOwner) {
            Log.d("SendMessage", "Add Masa message")
            Log.d("GRPC", "Message response")
            viewModel.historyList.addAll(it)
            adapter.addItems(it)
            scrollToBottom()
            homeMenu?.isEnabled = true
        }

        viewModel.openLink.observe(viewLifecycleOwner) {
            AppUtils.openLinkInTheBrowser(it, requireContext())
        }
        viewModel.openDialUp.observe(viewLifecycleOwner) {
            AppUtils.makePhoneCall(it, requireContext())
        }
        viewModel.stopTTS.observe(viewLifecycleOwner) {
            binding.chatBarView.resetLayoutState()
        }
        viewModel.messageSent.observe(viewLifecycleOwner) {
            Log.d("SendMessage", "Add user message")
            Log.d("GRPC", "Message sent")
            viewModel.historyList.add(it)
            binding.chatBarView.setInputType(InputType.TYPE_CLASS_TEXT)
            adapter.clear()
            binding.recyclerView.postDelayed({
                adapter.addItem(it)
            }, 300)
            // TODO: remove comment on below line of code to show load history icon when send new message
//            binding.loadPrevious.makeVisible()

        }
        viewModel.ttsAudioList.observe(viewLifecycleOwner) {
            binding.chatBarView.playAudioList(it)
        }
        viewModel.showLoader.observe(viewLifecycleOwner) {
            adapter.loading(it)
        }
        viewModel.rateAnswer.observe(viewLifecycleOwner) {

            findNavController().navigate(
                ChatBotFragmentDirections.actionChatBotFragmentToRateAnswerDialogFragment(it)
            )

        }
        viewModel.getUserLocation.observe(viewLifecycleOwner) {
            scrollToBottom()
            checkLocationPermission()
        }
        viewModel.requestPermission.observe(viewLifecycleOwner) {
            if (it == Permission.ACCESS_FINE_LOCATION) {
                if (!isAllGranted(Permission.ACCESS_FINE_LOCATION)) {
                    requestPermission(Permission.ACCESS_FINE_LOCATION)
                } else {
                    Log.d("", "")
                }
            } else if (it == Permission.RECORD_AUDIO) {
                if (!isAllGranted(Permission.RECORD_AUDIO)) {
                    requestPermission(Permission.RECORD_AUDIO)
                } else {
                    Log.d("", "")
                }
            } else if (it == Permission.CALL_PHONE) {
                if (!isAllGranted(Permission.CALL_PHONE)) {
                    requestPermission(Permission.CALL_PHONE)
                } else {
                    Log.d("", "")
                }
            }
        }
        viewModel.makeCall.observe(viewLifecycleOwner) {
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
        }

        viewModel.goToLocation.observe(viewLifecycleOwner) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=$it")
            )
            startActivity(intent)
        }

        viewModel.openNumberKeyPad.observe(this) {
            binding.chatBarView.showNumberKeyPad()
        }

        viewModel.openTextKeyPad.observe(this) {
            binding.chatBarView.showTextKeyPad()
        }

    }

    override fun onPause() {
        binding.chatBarView.destroyView()
        super.onPause()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        homeMenu = menu.findItem(R.id.action_home)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_analytics -> {

            }
            R.id.action_help -> {
                AppUtils.navigateToFragment(this, R.id.action_chatBotFragment_to_helpFragment)
            }
            R.id.action_home -> {

                viewModel.sendMessage(
                    "القائمة الرئيسية",
                    "القائمة الرئيسية",
                    showMessage = true,
                    newSession = false
                )
                item.isEnabled = false
            }
            else -> {
                Log.d("", "")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun makeCall() {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phone")
        startActivity(intent)
    }

    override fun getViewModel(): BaseViewModel = viewModel

    /**
     * this fun will check has location permission then will proceed and request current location
     * otherwise will show message for user to request location permission
     */
    private fun checkLocationPermission() {
        val permissionsGranted: Boolean =
            isAllGranted(Permission.ACCESS_FINE_LOCATION) || isAllGranted(Permission.ACCESS_COARSE_LOCATION)
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
            }, 500)
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
                    val currentLocationTask: Task<Location> = fusedLocationClient.lastLocation
//                        fusedLocationClient.getCurrentLocation(
//                            LocationRequest.PRIORITY_HIGH_ACCURACY,
//                            cancellationTokenSource.token
//                        )

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
                } else {
                    Log.d("", "")
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
                        Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION -> {
                            requestCurrentLocation()
                        }
                        else -> {
                            Log.d("", "")
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
                        Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION -> {
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
                else -> {
                    Log.d("", "")
                }
            }
        }
    }

    /**
     * this method will be called when user click send button
     */
    override fun sendMessage(messageText: String) {

        binding.chatBarView.setInputType(InputType.TYPE_CLASS_TEXT)
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

    override fun showError(connectionFailedError: Int) =
        showSnackbarMessage(connectionFailedError)

    override fun checkAsrEnabled(): Boolean {
        return viewModel.asrEnabled
    }

    override fun getAsrDisabledMessage(): String {
        return viewModel.asrDisabledMessage
    }


    private fun scrollToBottom() =
        binding.recyclerView.postDelayed({
            val recyclerItems = binding.recyclerView.layoutManager?.itemCount ?: 0

            binding.recyclerView.layoutManager?.smoothScrollToPosition(
                binding.recyclerView,
                RecyclerView.State(), recyclerItems - 1
            )
        }, 500)


}


