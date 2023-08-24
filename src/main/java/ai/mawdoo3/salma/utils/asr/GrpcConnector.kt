package ai.mawdoo3.salma.utils.asr

import ai.mawdoo3.salma.utils.asr.riva.RivaAsr
import ai.mawdoo3.salma.utils.asr.riva.RivaSpeechRecognitionGrpc
import android.content.Context
import android.provider.Settings
import android.util.Log
//import asr_service.Asr
//import asr_service.asr_serviceGrpc
import com.google.protobuf.BoolValue
import com.google.protobuf.BytesValue
import com.google.protobuf.Int32Value
import com.google.protobuf.StringValue
import io.grpc.ManagedChannel
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream

const val CERT_NAME = "riva_stg.crt"

object GrpcConnector {

    private lateinit var reco: RecoBuilder
    private val stub: RivaSpeechRecognitionGrpc.RivaSpeechRecognitionStub? = null

    //    private val stub: asr_serviceGrpc.asr_serviceStub? = null
    var streamObserverSpeakChunk: StreamObserver<RivaAsr.StreamingRecognizeRequest>? = null

    fun connect(context: Context): ManagedChannel {
        val channel: ManagedChannel?
        val input: InputStream?
        try {
            input = context.resources.assets.open(CERT_NAME)
            channel = ChannelBuilder.buildTls(
                "asr-riva-stg.salma-app.com", 443, input
            )
            reco = RecoBuilder(channel)
            input.close()

        } catch (e: Throwable) {
            Log.d("GRPC", "Connection failed")
            throw FailedChannelConnectionException()
        }
        return channel
    }

    private fun getASRStub(channel: ManagedChannel): RivaSpeechRecognitionGrpc.RivaSpeechRecognitionStub {
        stub?.let {
            return it
        } ?: run {
            return RivaSpeechRecognitionGrpc.newStub(channel)
        }
    }

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