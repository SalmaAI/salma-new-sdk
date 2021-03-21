package ai.mawdoo3.salma.utils.views

import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.databinding.ChatBarLayoutBinding
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
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import java.util.*


/**
 * created by Omar Qadomi on 3/17/21
 */
class ChatBarView : FrameLayout {
    lateinit var binding: ChatBarLayoutBinding
    private var actionStatus = ChatBarStatus.Nothing
    private var listener: ChatBarListener? = null

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context?) {
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
    }

    private fun stopListening() {
        actionStatus = ChatBarStatus.Nothing
        binding.aviListening.makeGone()
        binding.aviSpeaking.makeGone()
        binding.tvSpeak.makeGone()
        binding.imgSend.setImageResource(R.drawable.ic_microphone)
        binding.imgSend.makeVisible()
        binding.etMessage.makeVisible()
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        binding.root.layout(
            0,
            0,
            binding.root.getMeasuredWidth(),
            binding.root.getMeasuredHeight()
        );

    }

    interface ChatBarListener {
        fun sendMessage(messageText: String)
        fun onStartListening()
        fun onEndListening()
    }
}
