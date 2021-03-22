package ai.mawdoo3.salma.utils.views

import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.databinding.ChatBarLayoutBinding
import ai.mawdoo3.salma.utils.asr.GrpcConnector
import ai.mawdoo3.salma.utils.asr.VoiceRecorder
import ai.mawdoo3.salma.utils.makeGone
import ai.mawdoo3.salma.utils.makeVisible
import android.Manifest
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.protobuf.ByteString
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import io.grpc.ManagedChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


/**
 * created by Omar Qadomi on 3/17/21
 */
class ChatBarView : FrameLayout, GrpcConnector.ITranscriptionStream {
    private lateinit var channel: ManagedChannel
    lateinit var binding: ChatBarLayoutBinding
    private var actionStatus = ChatBarStatus.Nothing
    private var listener: ChatBarListener? = null
    private var sessionId: String = ""
    private var mVoiceRecorder: VoiceRecorder? = null


    interface ChatBarListener {
        fun sendMessage(messageText: String)
        fun onStartListening()
        fun onEndListening()
    }


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context?) {
        context?.let {
            channel = GrpcConnector.connect(it)
            GrpcConnector.registerVoiceRecognitionListener(this)
            val mVoiceCallback: VoiceRecorder.Callback = getVoiceRecorderCallbacks(channel)
            mVoiceRecorder = VoiceRecorder(mVoiceCallback)
        }
        val inflater = LayoutInflater.from(context)
        binding = ChatBarLayoutBinding.inflate(inflater)
        this.addView(binding.root)
        binding.imgSend.setOnClickListener {
            if (actionStatus == ChatBarStatus.Listening
                || actionStatus == ChatBarStatus.Speaking
            ) {
                listener?.onEndListening()
                stopListening()
            } else if (binding.etMessage.text.isNullOrEmpty()) {
                listener?.onStartListening()
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
                    binding.imgSend.setImageResource(R.drawable.ic_microphone)
                } else {
                    binding.imgSend.setImageResource(R.drawable.ic_send)
                }
            }

        })
    }

    fun setActionsListener(listener: ChatBarListener) {
        this.listener = listener
    }

    fun setActionsStatus(status: ChatBarStatus) {
        when (status) {
            ChatBarStatus.Nothing -> stopListening()
            ChatBarStatus.Listening -> checkPermissionAndStartListening()
            ChatBarStatus.Speaking -> startSpeaking()
        }

    }

    private fun sendMessage() {
        listener?.sendMessage(binding.etMessage.text.toString())
        binding.etMessage.text?.clear()
    }

    private fun sendGRPCMessage(text: String) {
        listener?.sendMessage(text)
        binding.tvGrpcText.text = ""
        binding.tvGrpcText.makeGone()
    }

    private fun checkPermissionAndStartListening() {
        Permissions.check(
            context,
            Manifest.permission.RECORD_AUDIO,
            null,
            object : PermissionHandler() {
                override fun onGranted() {
                    startListening()
                }

                override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
                    Snackbar.make(
                        this@ChatBarView,
                        context!!.getString(R.string.audio_permission_denied_message),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                override fun onBlocked(
                    context: Context?,
                    blockedList: ArrayList<String>?
                ): Boolean {
                    showSettingsDialog()
                    return true
                }
            })

    }

    private fun showSettingsDialog() {
        val alertDialogBuilder = MaterialAlertDialogBuilder(
            context,
            R.style.Theme_MobileBanking_JKB_MaterialAlertDialog
        )
            .setTitle(context.getString(R.string.permission_denied_title))
            .setMessage(context!!.getString(R.string.audio_permission_denied_message))
            .setPositiveButton(R.string.go_to_settings) { dialog, which ->
                Intent(
                    ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:${context!!.packageName}")
                ).apply {
                    addCategory(Intent.CATEGORY_DEFAULT)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(this)
                }
                dialog.dismiss()
            }.setNegativeButton(R.string.cancel) { dialog, which ->
                dialog.dismiss()
            }
        alertDialogBuilder.show()
    }

    private fun startSpeaking() {
        actionStatus = ChatBarStatus.Speaking
        binding.aviListening.makeGone()
        binding.aviSpeaking.makeVisible()
        binding.imgSend.makeVisible()
        binding.imgSend.setImageDrawable(null)
        binding.etMessage.makeGone()
        binding.tvSpeak.makeGone()
        binding.tvGrpcText.makeVisible()
    }

    private fun startListening() {
        actionStatus = ChatBarStatus.Listening
        var mediaPlayer = MediaPlayer.create(context, R.raw.ring)
        mediaPlayer.start() // no need to call prepare(); create() does that for you
        binding.aviListening.makeVisible()
        binding.aviSpeaking.makeGone()
        binding.imgSend.makeGone()
        binding.etMessage.makeGone()
        binding.tvSpeak.makeVisible()
        binding.tvGrpcText.makeGone()
        GrpcConnector.startVoiceRecognition(channel)
    }

    private fun stopListening() {
        actionStatus = ChatBarStatus.Nothing
        binding.aviListening.makeGone()
        binding.aviSpeaking.makeGone()
        binding.tvSpeak.makeGone()
        binding.imgSend.setImageResource(R.drawable.ic_microphone)
        binding.imgSend.makeVisible()
        binding.etMessage.makeVisible()
        binding.tvGrpcText.makeGone()
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
                CoroutineScope(Dispatchers.Main).launch {
                    setActionsStatus(ChatBarStatus.Speaking)
                }
            }

            override fun onVoice(data: ByteArray?, size: Int) {
                data?.apply {
                    val stringByte =
                        GrpcConnector.getByteBuilder().setValue(ByteString.copyFrom(data))?.build()
                    GrpcConnector.sendVoice(channel, sessionId, stringByte)
                }
            }

            override fun onVoiceEnd() {
            }
        }
    }

    override fun onTranscriptionReceived(text: String) {
        binding.tvGrpcText.text = text
        Log.d("stremoe", text)
    }

    override fun onFinalTranscriptionReceived(text: String) {
        sendGRPCMessage(text)
        setActionsStatus(ChatBarStatus.Nothing)
        mVoiceRecorder?.stop()
    }

    override fun onSessionIdReceived(sessionId: String) {
        Log.d("sessionId", sessionId)

        // start voice recorder

        this.sessionId = sessionId
        mVoiceRecorder?.start()
    }
}
