package ai.mawdoo3.salma.utils.views

import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.data.TtsItem
import ai.mawdoo3.salma.data.enums.ChatBarType
import ai.mawdoo3.salma.databinding.ChatBarLayoutBinding
import ai.mawdoo3.salma.utils.AppUtils
import ai.mawdoo3.salma.utils.TTSStreamHelper
import ai.mawdoo3.salma.utils.asr.GrpcConnector
import ai.mawdoo3.salma.utils.asr.VoiceRecorder
import ai.mawdoo3.salma.utils.makeInvisible
import ai.mawdoo3.salma.utils.makeVisible
import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.assent.Permission
import com.afollestad.assent.isAllGranted
import com.google.protobuf.ByteString
import io.grpc.ManagedChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * created by Omar Qadomi on 3/17/21
 */
class ChatBarView : FrameLayout, GrpcConnector.ITranscriptionStream {
    private var channel: ManagedChannel? = null
    lateinit var binding: ChatBarLayoutBinding
    private var actionStatus = ChatBarStatus.Nothing
    private var listener: ChatBarListener? = null
    private var sessionId: String = ""
    private var mVoiceRecorder: VoiceRecorder? = null
    private var cancelCurrentRecord: Boolean = false
    private var audioList: ArrayList<TtsItem>? = null
    private var chatBarType: ChatBarType = ChatBarType.TEXT_AND_AUDIO

    interface ChatBarListener {
        fun sendMessage(messageText: String)
        fun requestMicPermission()
        fun showError(connectionFailedError: Int)
    }


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context?) {
        context?.let {
            try {
                channel = GrpcConnector.connect(it)
                GrpcConnector.registerVoiceRecognitionListener(this)
                channel?.let { channel ->
                    val mVoiceCallback: VoiceRecorder.Callback = getVoiceRecorderCallbacks(channel)
                    mVoiceRecorder = VoiceRecorder(mVoiceCallback)
                    GrpcConnector.startVoiceRecognition(channel)
                }
            } catch (e: GrpcConnector.FailedChannelConnectionException) {
            }

        }
        val inflater = LayoutInflater.from(context)
        binding = ChatBarLayoutBinding.inflate(inflater)
        this.addView(binding.root)
        TTSStreamHelper.getInstance(this.context).setTtsStreamCompletionListener {
            audioList?.let {
                if (it.size > 0) {
                    playAudio(it[0])
                    it.removeAt(0)
                } else {
                    resetLayoutState()
                }

            }
        }

        binding.speakLayout.aviSpeaking.setOnClickListener {
            mVoiceRecorder?.stop()
            resetLayoutState()
            startNewGrpcSession()
        }

        binding.audioLayout.imgMute.setOnClickListener {
            resetLayoutState()
        }
        binding.inputLayout.imgAction.setOnClickListener {
            if (binding.inputLayout.etMessage.text.isNullOrEmpty()) {
                checkPermissionAndStartListening()
            } else {
                sendMessage()
            }
        }
        binding.listenLayout.root.setOnClickListener {
            resetLayoutState()
            mVoiceRecorder?.stop()
        }
        binding.inputLayout.etMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    binding.inputLayout.imgAction.setImageResource(R.drawable.ic_microphone)
                } else {
                    binding.inputLayout.imgAction.setImageResource(R.drawable.ic_send)
                }
            }

        })

    }

    fun setChatBarType(chatBarType: ChatBarType) {
        this.chatBarType = chatBarType
        if (chatBarType == ChatBarType.AUDIO) {
            binding.inputLayout.etMessage.makeInvisible()
        }

    }

    fun playAudioList(list: List<TtsItem>) {
        audioList = ArrayList(list)
        if (!audioList.isNullOrEmpty()) {
            playAudio(audioList!![0])
            this.audioList!!.removeAt(0)
        }
    }

    fun setActionsListener(listener: ChatBarListener) {
        this.listener = listener
    }

    private fun sendMessage() {
        listener?.sendMessage(binding.inputLayout.etMessage.text.toString())
        binding.inputLayout.etMessage.text?.clear()

    }


    private fun sendGRPCMessage(text: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("GRPC", "Send message ->" + text)
            listener?.sendMessage(text)
        }
    }

    private fun checkPermissionAndStartListening() {
        val permissionsGranted: Boolean =
            (this.context as AppCompatActivity).isAllGranted(Permission.RECORD_AUDIO)
        if (permissionsGranted) {
            startListening()
        } else {
            listener?.requestMicPermission()
        }
//        // Requests one or more permissions, sending the result to a callback
//        (this.context as AppCompatActivity).askForPermissions(Permission.RECORD_AUDIO) { result ->
//            // Check the result, see the Using Results section
//            // Returns GRANTED, DENIED, or PERMANENTLY_DENIED
//            when (result[Permission.RECORD_AUDIO]) {
//                GrantResult.GRANTED -> {
//                    startListening()
//                }
//                GrantResult.DENIED -> {
//                    Snackbar.make(
//                        this@ChatBarView,
//                        context!!.getString(R.string.audio_permission_denied_message),
//                        Snackbar.LENGTH_SHORT
//                    ).show()
//                }
//                GrantResult.PERMANENTLY_DENIED -> {
//                    AppUtils.showSettingsDialog(
//                        this@ChatBarView.context,
//                        R.string.audio_permission_denied_message
//                    )
//                }
//            }
//        }
    }

    private fun startSpeaking() {
        actionStatus = ChatBarStatus.Speaking
        CoroutineScope(Dispatchers.Main).launch {
            binding.inputLayout.root.makeInvisible()
            binding.listenLayout.root.makeInvisible()
            binding.audioLayout.root.makeInvisible()
            binding.speakLayout.root.makeVisible()
        }
    }

    fun startListening() {
        if (channel != null) {
            CoroutineScope(Dispatchers.Main).launch {
                mVoiceRecorder?.updateHearingStatus(true)
                actionStatus = ChatBarStatus.Listening
                binding.inputLayout.root.makeInvisible()
                binding.listenLayout.root.makeVisible()
                binding.audioLayout.root.makeInvisible()
                binding.speakLayout.root.makeInvisible()
                mVoiceRecorder?.start()
            }
        } else {
            Log.d("GRPC", "channel not initialized")
            listener?.showError(R.string.connection_failed_error)
        }
    }

    private fun playAudio(ttsItem: TtsItem) {
        TTSStreamHelper.getInstance(this.context).startStreaming(ttsItem.ttsId, ttsItem.isDynamic)
        CoroutineScope(Dispatchers.Main).launch {
            mVoiceRecorder?.stop()
            actionStatus = ChatBarStatus.PlayingAudio
            binding.inputLayout.root.makeInvisible()
            binding.listenLayout.root.makeInvisible()
            binding.audioLayout.root.makeVisible()
            binding.speakLayout.root.makeInvisible()
        }
    }

    fun resetLayoutState() {
        CoroutineScope(Dispatchers.Main).launch {
            actionStatus = ChatBarStatus.Nothing
            binding.inputLayout.root.makeVisible()
            binding.listenLayout.root.makeInvisible()
            binding.audioLayout.root.makeInvisible()
            binding.speakLayout.root.makeInvisible()
            binding.speakLayout.tvGrpcText.text = ""
        }
        audioList?.clear()
        TTSStreamHelper.getInstance(this.context).stopStream()
    }

    private fun startNewGrpcSession() {
        Log.d("GRPC", "Start initialize new session")
        GrpcConnector.startVoiceRecognition(channel!!)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        binding.root.layout(
            0,
            0,
            binding.root.getMeasuredWidth(),
            binding.root.getMeasuredHeight()
        );

    }

    private fun getVoiceRecorderCallbacks(channel: ManagedChannel): VoiceRecorder.Callback {
        return object : VoiceRecorder.Callback() {
            override fun onVoiceStart() {
                Log.d("GRPC", "onVoiceStart")
                cancelCurrentRecord = false
            }

            override fun onVoice(data: ByteArray?, size: Int) {
                    Log.d("GRPC", "onVoice " + data)
                    data?.apply {
                        val stringByte =
                            GrpcConnector.getByteBuilder().setValue(ByteString.copyFrom(data))
                                ?.build()
                        GrpcConnector.sendVoice(channel, sessionId, stringByte)
                    }

            }

            override fun onVoiceEnd() {
                Log.d("GRPC", "onVoiceEnd")
            }
        }
    }

    override fun onTranscriptionReceived(text: String) {
        startSpeaking()
        binding.speakLayout.tvGrpcText.text = text
        Log.d("GRPC", "Received text ->" + text)
    }

    override fun onFinalTranscriptionReceived(text: String) {
        Log.d("GRPC", "Final text ->" + text)
        mVoiceRecorder?.updateHearingStatus(false)
        CoroutineScope(Dispatchers.IO).launch {
            mVoiceRecorder?.stop()
        }
        if (text.isNotEmpty() && !cancelCurrentRecord) {
            sendGRPCMessage(text)
        }
        resetLayoutState()
        startNewGrpcSession()
    }

    override fun onSessionIdReceived(sessionId: String) {
        Log.d("GRPC", "sessionId ->" + sessionId)
        // start voice recorder

        this.sessionId = sessionId
//        mVoiceRecorder?.start()
    }

    fun showNumberKeyPad() {
        binding.inputLayout.root.makeVisible()
        binding.inputLayout.etMessage.inputType = InputType.TYPE_CLASS_NUMBER;
        AppUtils.requestFocus(context, binding.inputLayout.etMessage)
    }

    fun setInputType(inputType: Int) {
        binding.inputLayout.etMessage.inputType = inputType
    }

    fun destroyView() {
        mVoiceRecorder?.stop()
        audioList?.clear()
        TTSStreamHelper.getInstance(this.context).stopStream()
    }
}
