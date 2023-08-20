package ai.mawdoo3.salma.utils.asr

import ai.mawdoo3.salma.BuildConfig
import android.content.Context
import android.provider.Settings
import android.util.Log
import com.google.protobuf.BytesValue
import io.grpc.ManagedChannel
import java.io.InputStream

const val CERT_NAME = "riva_stg.crt"

object GrpcConnector {

    private lateinit var reco: RecoBuilder
//    private val stub: RivaSpeechRecognitionGrpc.RivaSpeechRecognitionStub? = null
//    var streamObserverSpeakChunk: StreamObserver<RivaAsr.StreamingRecognizeRequest>? = null
//    private val stub: asr_serviceGrpc.asr_serviceStub? = null
//    var streamObserverSpeakChunk: StreamObserver<Asr.speak>? = null

    fun connect(context: Context): ManagedChannel {
        val channel: ManagedChannel?
        val input: InputStream?
        try {
            input = context.resources.assets.open(CERT_NAME)
            channel = ChannelBuilder.buildTls(
                BuildConfig.ASR_URL, 443, input
            )
            reco = RecoBuilder(channel)
            input.close()
            return channel
        } catch (e: Throwable) {
            Log.d("GRPC", "Connection failed")
            throw FailedChannelConnectionException()
        }
    }

//    private fun getASRStub(channel: ManagedChannel): asr_serviceGrpc.asr_serviceStub {
//        stub?.let {
//            return it
//        } ?: run {
//            return asr_serviceGrpc.newStub(channel)
//        }
//    }

    fun getByteBuilder(): BytesValue.Builder =
        BytesValue.newBuilder()

    fun sendVoice(voice: ByteArray?) {
        reco.setListener()
        reco.setVoiceListener(voiceRecognition)
        reco.doTranscribe(reco.buildRequest(voice))
    }

    private var voiceRecognition: ITranscriptionStream? = null

    interface IVoiceRecognition {
        //fun onSessionIdReceived(sessionId: String)
    }

    interface ITranscriptionStream : IVoiceRecognition {
        fun onTranscriptionReceived(text: String)
        fun onFinalTranscriptionReceived(text: String)
    }

    fun registerVoiceRecognitionListener(voiceRecognition: ITranscriptionStream) {
        this.voiceRecognition = voiceRecognition
    }


    fun getID(context: Context?): String {
        val sID = Settings.Secure.getString(
            context?.contentResolver,
            Settings.Secure.ANDROID_ID
        );
        return sID
    }

    class FailedChannelConnectionException : Exception()
}