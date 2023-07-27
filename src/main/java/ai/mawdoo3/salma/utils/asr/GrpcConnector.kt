package ai.mawdoo3.salma.utils.asr

import ai.mawdoo3.salma.BuildConfig
import android.content.Context
import android.provider.Settings
import android.util.Log
import asr_service.Asr
import asr_service.asr_serviceGrpc
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

const val CERT_NAME = "asr-latest.crt"

object GrpcConnector {

    private val stub: asr_serviceGrpc.asr_serviceStub? = null
    var streamObserverSpeakChunk: StreamObserver<Asr.speak>? = null

    fun connect(context: Context): ManagedChannel {
        val channel: ManagedChannel?
        val input: InputStream?
        try {
            input = context.resources.assets.open(CERT_NAME)
            channel = ChannelBuilder.buildTls(
                BuildConfig.ASR_URL, 443, input
            )
            input.close()
            return channel
        } catch (e: Throwable) {
            Log.d("GRPC", "Connection failed")
            throw FailedChannelConnectionException()
        }
    }

    private fun getASRStub(channel: ManagedChannel): asr_serviceGrpc.asr_serviceStub {
        stub?.let {
            return it
        } ?: run {
            return asr_serviceGrpc.newStub(channel)
        }
    }

    fun getByteBuilder(): BytesValue.Builder =
        BytesValue.newBuilder()

    fun sendVoice(channel: ManagedChannel, sessionId: String, voice: BytesValue?, first: Boolean) {
        val stub = getASRStub(channel)
        streamObserverSpeakChunk =
            stub.transcribeStream(object : StreamObserver<Asr.transcription_stream> {
                override fun onNext(value: Asr.transcription_stream?) {
                    value?.let {
                        voiceRecognition?.let { ref ->
                            it.text.value.let { value ->
                                ref.onTranscriptionReceived(value)
                            }
                            CoroutineScope(Dispatchers.Main).launch {
                                if (value.final.value) {
                                    try {
                                        streamObserverSpeakChunk?.onCompleted()
                                        ref.onFinalTranscriptionReceived(it.text.value)
                                    } catch (e: Exception) {

                                    }
                                }
                            }
                        }
                    }
                }

                override fun onError(t: Throwable?) {
                    Log.d("GRPC", "Error -> " + t?.message.toString())
                }

                override fun onCompleted() {
                    Log.d("", "")
                }
            })
        val asrSpearBuilder: Asr.speak.Builder = Asr.speak.newBuilder()
        asrSpearBuilder.sampleRate = Int32Value.of(VoiceRecorder.SAMPLE_RATE)
        asrSpearBuilder.audioBytes = voice
        asrSpearBuilder.id = StringValue.of(sessionId)
        Log.d("userId", asrSpearBuilder.id.value)
        asrSpearBuilder.first = BoolValue.of(first)
        streamObserverSpeakChunk?.onNext(asrSpearBuilder.build())
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