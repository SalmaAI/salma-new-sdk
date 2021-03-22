package ai.mawdoo3.salma.ui

import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.databinding.ActivityMainBinding
import ai.mawdoo3.salma.utils.asr.GrpcConnector
import ai.mawdoo3.salma.utils.asr.VoiceRecorder
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.banking.common.base.BaseActivity
import com.google.protobuf.ByteString
import io.grpc.ManagedChannel

class MainActivity : BaseActivity() , GrpcConnector.ITranscriptionStream{
    lateinit var binding: ActivityMainBinding
    private var sessionId: String = ""
    private var mVoiceRecorder: VoiceRecorder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        this.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_close);
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true);



        val channel = GrpcConnector.connect(this)
        GrpcConnector.registerVoiceRecognitionListener(this)
        GrpcConnector.startVoiceRecognition(channel)


        val mVoiceCallback: VoiceRecorder.Callback = getVoiceRecorderCallbacks(channel)
        mVoiceRecorder = VoiceRecorder(mVoiceCallback)




    }

    private fun getVoiceRecorderCallbacks(channel: ManagedChannel): VoiceRecorder.Callback {
        return object : VoiceRecorder.Callback() {
            override fun onVoiceStart() {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_analytics -> {

            }
            R.id.action_help -> {

            }
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onTranscriptionReceived(text: String) {
        // show the bubble and wait to final message set text on the message bubble
        // Maybe you need to run your code inside runOnUIThread {} Block
    }

    override fun onFinalTranscriptionReceived(text:String) {
        // end mic animation and return the normal state then send the message to server
        // Maybe you need to run your code inside runOnUIThread {} Block
    }

    override fun onSessionIdReceived(sessionId: String) {
        // start voice recorder
        this.sessionId = sessionId
        mVoiceRecorder?.start()
        // change button behavior
        /* ...... */
        // Maybe you need to run your code inside runOnUIThread {} Block
    }


}