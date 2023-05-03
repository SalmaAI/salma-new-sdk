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
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.afollestad.assent.Permission
import com.afollestad.assent.isAllGranted
import com.google.protobuf.ByteString
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import io.grpc.ManagedChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ChatBarView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs),
    GrpcConnector.ITranscriptionStream {

    private var channel: ManagedChannel? = null
    lateinit var binding: ChatBarLayoutBinding
    private var actionStatus = ChatBarStatus.Nothing
    private var listener: ChatBarListener? = null
    private var mVoiceRecorder: VoiceRecorder? = null
    private var cancelCurrentRecord: Boolean = false
    private var audioList: ArrayList<TtsItem>? = null
    private var chatBarType: ChatBarType = ChatBarType.TEXT_AND_AUDIO

    var resultListener: ((result: Boolean?) -> Unit)? = null

    interface ChatBarListener {
        fun sendMessage(messageText: String)
        fun requestMicPermission()
        fun showError(connectionFailedError: Int)
        fun checkAsrEnabled(): Boolean
        fun getAsrDisabledMessage(): String
    }

    init {
        init(context)
    }

    private fun init(context: Context?) {
        context?.let {
            try {
                channel = GrpcConnector.connect(it)
                GrpcConnector.registerVoiceRecognitionListener(this)
                CoroutineScope(Dispatchers.IO).launch {
                    channel?.let { channel ->
                        val mVoiceCallback: VoiceRecorder.Callback =
                            getVoiceRecorderCallbacks(channel)
                        mVoiceRecorder = VoiceRecorder(mVoiceCallback)
                    }
                }
            } catch (e: GrpcConnector.FailedChannelConnectionException) {
                Log.d("failed", "connection failed")
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
        }

        binding.audioLayout.imgMute.setOnClickListener {
            resetLayoutState()
        }
        binding.inputLayout.imgAction.setOnClickListener {

            if (binding.inputLayout.etMessage.text.isNullOrEmpty()) {
                if (listener?.checkAsrEnabled() == true) {
                    checkPermissionAndStartListening()
                } else {
                    // show tooltip
                    val balloon = Balloon.Builder(context!!)
                        .setWidthRatio(0.9f)
                        .setHeight(BalloonSizeSpec.WRAP)
                        .setText(listener?.getAsrDisabledMessage() ?: "")
                        .setTextColorResource(R.color.masaPrimaryColor)
                        .setTextSize(15f)
                        .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                        .setArrowSize(10)
                        .setArrowPosition(0.5f)
                        .setPadding(8)
                        .setCornerRadius(8f)
                        .setBackgroundColorResource(R.color.masaSecondaryColor)
                        .setBalloonAnimation(BalloonAnimation.OVERSHOOT)
                        .build()
                    balloon.showAlignTop(binding.inputLayout.imgAction)


                    balloon.dismissWithDelay(2000L)
                }
            } else {
                sendMessage()
            }


        }
        binding.listenLayout.root.setOnClickListener {
            resetLayoutState()
            mVoiceRecorder?.stop()
        }
        binding.inputLayout.etMessage.doAfterTextChanged {
            if (it.isNullOrEmpty()) {
                binding.inputLayout.imgAction.setImageResource(R.drawable.ic_chatbot_microphone)
            } else {
                binding.inputLayout.imgAction.setImageResource(R.drawable.ic_chatbot_send)
            }
        }
    }

    fun setChatBarType(chatBarType: ChatBarType) {
        this.chatBarType = chatBarType
        if (chatBarType == ChatBarType.AUDIO) {
            binding.inputLayout.etMessage.makeInvisible()
        } else {
            Log.d("", "")
        }
    }

    fun playAudioList(list: List<TtsItem>) {
        audioList = ArrayList(list)
        if (!audioList.isNullOrEmpty()) {
            playAudio(audioList!![0])
            this.audioList!!.removeAt(0)
        } else {
            Log.d("", "")
        }
    }

    fun setActionsListener(listener: ChatBarListener) {
        this.listener = listener
    }

    private fun sendMessage() {
        if (binding.inputLayout.etMessage.text?.trim()?.isNotEmpty() == true) {
            listener?.sendMessage(binding.inputLayout.etMessage.text.toString())
            binding.inputLayout.etMessage.text?.clear()
        } else {
            Log.d("", "")
        }
    }

    private fun sendGRPCMessage(text: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("GRPC", "Send message ->$text")
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
        TTSStreamHelper.getInstance(this.context)
            .startStreaming(ttsItem.ttsId, ttsItem.isDynamic, ttsItem.ttsText)
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
        resultListener?.invoke(true)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        binding.root.layout(
            0,
            0,
            binding.root.measuredWidth,
            binding.root.measuredHeight
        )

    }

    var firstTime = true

    private fun getVoiceRecorderCallbacks(channel: ManagedChannel): VoiceRecorder.Callback {
        return object : VoiceRecorder.Callback() {
            override fun onVoiceStart() {
                Log.d("GRPC", "onVoiceStart")
                cancelCurrentRecord = false
                firstTime = true
            }

            override fun onVoice(data: ByteArray?, size: Int) {
                Log.d("GRPC", "onVoice $data")
                data?.apply {
                    val stringByte =
                        GrpcConnector.getByteBuilder().setValue(ByteString.copyFrom(data))
                            ?.build()
                    GrpcConnector.sendVoice(
                        channel,
                        GrpcConnector.getID(context),
                        stringByte,
                        firstTime
                    )
                    firstTime = false
                }
            }
        }
    }

    override fun onTranscriptionReceived(text: String) {
        startSpeaking()
        binding.speakLayout.tvGrpcText.text = text
        Log.d("GRPC", "Received text ->$text")
    }

    override fun onFinalTranscriptionReceived(text: String) {
        Log.d("GRPC", "Final text ->$text")
        mVoiceRecorder?.updateHearingStatus(false)
        CoroutineScope(Dispatchers.IO).launch {
            mVoiceRecorder?.stop()
        }
        if (text.isNotEmpty() && !cancelCurrentRecord) {
            sendGRPCMessage(text)
        }
        resetLayoutState()
    }

    fun showNumberKeyPad() {
        binding.inputLayout.root.makeVisible()
        binding.inputLayout.etMessage.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        AppUtils.requestFocus(context, binding.inputLayout.etMessage)
    }

    fun showTextKeyPad() {
        binding.inputLayout.root.makeVisible()
        binding.inputLayout.etMessage.inputType =
            InputType.TYPE_CLASS_TEXT
        AppUtils.requestFocus(context, binding.inputLayout.etMessage)
    }

    fun requestEditTextFocus() {
        AppUtils.requestFocus(context, binding.inputLayout.etMessage)
    }

    fun setInputType(inputType: Int) {
        binding.inputLayout.etMessage.inputType = inputType
    }

    fun destroyView() {
        resetLayoutState()
        mVoiceRecorder?.stop()
    }
}
