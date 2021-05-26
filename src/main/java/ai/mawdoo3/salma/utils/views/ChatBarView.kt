package ai.mawdoo3.salma.utils.views

import ai.mawdoo3.salma.BuildConfig
import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.data.enums.ChatBarType
import ai.mawdoo3.salma.databinding.ChatBarLayoutBinding
import ai.mawdoo3.salma.utils.TTSStreamHelper
import ai.mawdoo3.salma.utils.asr.GrpcConnector
import ai.mawdoo3.salma.utils.asr.VoiceRecorder
import ai.mawdoo3.salma.utils.makeGone
import ai.mawdoo3.salma.utils.makeVisible
import android.content.Context
import android.media.MediaPlayer
import android.text.Editable
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
    private var audioList: ArrayList<String>? = null
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
                    stopListening()
                }

            }
        }

        binding.imgAction.setOnClickListener {
            if (actionStatus == ChatBarStatus.Listening
                || actionStatus == ChatBarStatus.Speaking
            ) {
                cancelCurrentRecord = true
                stopListening()
            } else if (actionStatus == ChatBarStatus.PlayingAudio) {
                TTSStreamHelper.getInstance(this.context).stopStream()
                audioList?.clear()
                stopListening()
            } else if (binding.etMessage.text.isNullOrEmpty()) {
                checkPermissionAndStartListening()
            } else {
                sendMessage()
            }
        }
        binding.aviListening.setOnClickListener {
            stopListening()
        }
        binding.etMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    binding.imgAction.setImageResource(R.drawable.ic_microphone)
                } else {
                    binding.imgAction.setImageResource(R.drawable.ic_send)
                }
            }

        })
    }

    fun setChatBarType(chatBarType: ChatBarType) {
        this.chatBarType = chatBarType
        if (chatBarType == ChatBarType.AUDIO) {
            binding.etMessage.makeGone()
        }

    }

    fun playAudioList(list: List<String>) {
        audioList = ArrayList(list)
        playAudio(audioList!![0])
        this.audioList!!.removeAt(0)
    }

    fun setActionsListener(listener: ChatBarListener) {
        this.listener = listener
    }

    private fun sendMessage() {
        if (binding.etMessage.text.toString().trim().isNotEmpty()) {
            listener?.sendMessage(binding.etMessage.text.toString())
            binding.etMessage.text?.clear()
        }
    }

    private fun sendGRPCMessage(text: String) {
        Log.d("GRPC", "Stop record")
        stopListening()
        CoroutineScope(Dispatchers.Main).launch {
            listener?.sendMessage(text)
            Log.d("GRPC", "Send message ->" + text)
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
        Log.d("GRPC", "begin of start speaking")
        actionStatus = ChatBarStatus.Speaking
        CoroutineScope(Dispatchers.Main).launch {
            binding.aviListening.makeGone()
            binding.aviSpeaking.makeVisible()
            binding.imgAction.makeGone()
//            binding.imgAction.makeVisible()
//            binding.imgAction.setImageResource(R.drawable.ic_delete)
            binding.etMessage.makeGone()
            binding.tvSpeak.makeGone()
            binding.tvGrpcText.text = ""
            binding.tvGrpcText.makeVisible()
        }
    }

    fun startListening() {
        if (channel != null) {
            Log.d("GRPC", "begin of start Listening")
            CoroutineScope(Dispatchers.Main).launch {
                actionStatus = ChatBarStatus.Listening
                var mediaPlayer = MediaPlayer.create(context, R.raw.ring)
                mediaPlayer.start() // no need to call prepare(); create() does that for you
                binding.aviListening.makeVisible()
                binding.aviSpeaking.makeGone()
                binding.imgAction.makeGone()
                binding.etMessage.makeGone()
                binding.tvSpeak.makeVisible()
                binding.tvGrpcText.makeGone()
            }
            GrpcConnector.startVoiceRecognition(channel!!)
        } else {
            Log.d("GRPC", "channel not initialized")
            listener?.showError(R.string.connection_failed_error)
        }
    }

    private fun stopListening() {
        Log.d("GRPC", "begin of Stop Listening")
        CoroutineScope(Dispatchers.Main).launch {
            actionStatus = ChatBarStatus.Nothing
            binding.aviListening.makeGone()
            binding.aviSpeaking.makeGone()
            binding.tvSpeak.makeGone()
            binding.imgAction.setImageResource(R.drawable.ic_microphone)
            binding.imgAction.makeVisible()
            if (chatBarType == ChatBarType.TEXT_AND_AUDIO) {
                binding.etMessage.makeVisible()
            }
            binding.tvGrpcText.setText("")
            binding.tvGrpcText.makeGone()
            Log.d("GRPC", "end of Stop Listening")
        }
        mVoiceRecorder?.stop()
    }

    private fun playAudio(ttsID: String) {
        mVoiceRecorder?.stop()
        val url = String.format(BuildConfig.TTS_URL, ttsID)
        TTSStreamHelper.getInstance(this.context).startStreaming(url)
        CoroutineScope(Dispatchers.Main).launch {
            actionStatus = ChatBarStatus.PlayingAudio
            binding.aviListening.makeGone()
            binding.aviSpeaking.makeVisible()
            binding.imgAction.makeVisible()
            binding.imgAction.setImageResource(R.drawable.ic_volume_mute)
            binding.etMessage.makeGone()
            binding.tvSpeak.makeGone()
            binding.tvGrpcText.makeGone()
        }
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
                startSpeaking()

            }

            override fun onVoice(data: ByteArray?, size: Int) {
                Log.d("GRPC", "onVoice")
                data?.apply {
                    val stringByte =
                        GrpcConnector.getByteBuilder().setValue(ByteString.copyFrom(data))?.build()
                    GrpcConnector.sendVoice(channel, sessionId, stringByte)
                }
            }

            override fun onVoiceEnd() {
                Log.d("GRPC", "onVoiceEnd")
                stopListening()
            }
        }
    }

    override fun onTranscriptionReceived(text: String) {
        binding.tvGrpcText.text = text
        Log.d("GRPC", "Received text ->" + text)
    }

    override fun onFinalTranscriptionReceived(text: String) {
        Log.d("GRPC", "Final text ->" + text)
        if (text.isNotEmpty() && !cancelCurrentRecord) {
            sendGRPCMessage(text)
        }
    }

    override fun onSessionIdReceived(sessionId: String) {
        Log.d("sessionId", sessionId)

        // start voice recorder

        this.sessionId = sessionId
        mVoiceRecorder?.start()
    }
}
