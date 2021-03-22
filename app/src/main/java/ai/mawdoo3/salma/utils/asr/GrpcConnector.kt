package ai.mawdoo3.salma.utils.asr

import ai.mawdoo3.salma.BuildConfig
import android.content.Context
import android.util.Log
import asr_service.Asr
import asr_service.asr_serviceGrpc
import com.google.protobuf.BytesValue
import com.google.protobuf.Empty
import com.google.protobuf.Int32Value
import com.google.protobuf.StringValue
import io.grpc.ManagedChannel
import io.grpc.stub.StreamObserver
import java.io.InputStream
import java.lang.Exception

/**
 * Created by iSaleem on 3/22/21
 */

const val CERT_NAME = "asr_nemo.crt"
object GrpcConnector {

    private val stub : asr_serviceGrpc.asr_serviceStub? = null
    private var sessionId: String = ""
    var streamObserverSpeakChunk: StreamObserver<Asr.speak>? = null

    fun connect(context: Context) : ManagedChannel {
        var channel: ManagedChannel?
        var input: InputStream?
        try {
            input = context.resources.assets.open(CERT_NAME)
            channel = ChannelBuilder.buildTls(
                BuildConfig.ASR_URL, 443, input
            )
            input.close()
            return channel
        } catch (e: Throwable) {
            throw FailedChannelConnectionException("Cannot Connect to Server " + e.message)
        }
    }

    fun getASRStub(channel: ManagedChannel) : asr_serviceGrpc.asr_serviceStub{
        stub?.let {
            return it
        } ?: run {
            return asr_serviceGrpc.newStub(channel)
        }
    }

    fun getByteBuilder() : BytesValue.Builder{
        return BytesValue.newBuilder()
    }

    fun startVoiceRecognition(channel: ManagedChannel){
        val stub = getASRStub(channel)
        stub.getSid(Empty.getDefaultInstance(), object : StreamObserver<Asr.session_id> {
            override fun onNext(value: Asr.session_id?) {
                sessionId = value?.sid?.value.toString()
                voiceRecognition?.let {
                    it.onSessionIdReceived(sessionId)
                }
            }

            override fun onError(t: Throwable?) {
            }

            override fun onCompleted() {
            }
        })
    }

    fun sendVoice(channel: ManagedChannel,sessionId: String,voice: BytesValue?) {
        val stub = getASRStub(channel)
        streamObserverSpeakChunk =
            stub.transcribeStream(object : StreamObserver<Asr.transcription_stream> {
                override fun onNext(value: Asr.transcription_stream?) {
                    value?.let {

                        voiceRecognition?.let { ref ->
                            it.text.value.let { value ->
                                ref.onTranscriptionReceived(value)
                            }
                            if (value.final.value) {
                                streamObserverSpeakChunk?.onCompleted()
                                ref.onFinalTranscriptionReceived(it.text.value)
                            }
                        }
                    }

                }

                override fun onError(t: Throwable?) {
                    Log.d("transcribe", t?.message.toString())
                }

                override fun onCompleted() {

                }
            })
        val asrSpearBuilder: Asr.speak.Builder = Asr.speak.newBuilder()
        asrSpearBuilder.sampleRate = Int32Value.of(VoiceRecorder.SAMPLE_RATE)
        asrSpearBuilder.audioBytes = voice
        asrSpearBuilder.sid = StringValue.of(sessionId)
        streamObserverSpeakChunk?.onNext(asrSpearBuilder.build())
    }

    private var voiceRecognition: ITranscriptionStream? = null

    interface IVoiceRecognition{
        fun onSessionIdReceived(sessionId:String)
    }

    interface ITranscriptionStream : IVoiceRecognition {
        fun onTranscriptionReceived(text:String)
        fun onFinalTranscriptionReceived(text:String)
    }

    fun registerVoiceRecognitionListener(voiceRecognition:ITranscriptionStream){
        this.voiceRecognition = voiceRecognition
    }


    class FailedChannelConnectionException(message:String) : Exception()
}