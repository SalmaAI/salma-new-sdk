package ai.mawdoo3.salma.utils.asr;

import android.util.Log;
import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.protobuf.ByteString;

import ai.mawdoo3.salma.utils.asr.riva.RivaAsr;
import ai.mawdoo3.salma.utils.asr.riva.RivaAudio;
import ai.mawdoo3.salma.utils.asr.riva.RivaSpeechClient;
import ai.mawdoo3.salma.utils.asr.riva.RivaSpeechRecognitionGrpc;
import io.grpc.ManagedChannel;

import java.io.IOException;


public class RecoBuilder {

    private final RivaSpeechClient client;
    String cachedName = "";
    GrpcConnector.ITranscriptionStream listener;
    private ClientStream<RivaAsr.StreamingRecognizeRequest> clientStream;
    private ResponseObserver<RivaAsr.StreamingRecognizeResponse> responseObserver;

    RecoBuilder(ManagedChannel channel) throws IOException {
        RivaSpeechRecognitionGrpc.RivaSpeechRecognitionStub stub = RivaSpeechRecognitionGrpc.newStub(channel).withWaitForReady();
        client = RivaSpeechClient.create(stub);
        responseObserver = getStreamResponseObserver();
        clientStream = client.streamingRecognizeCallable().splitCall(responseObserver);
        cachedName = "";
    }

    public void setVoiceListener(GrpcConnector.ITranscriptionStream listener) {
        if (listener != null) {
            this.listener = listener;
        }
    }

    public void setListener() {
        if (responseObserver == null) {
            responseObserver = getStreamResponseObserver();
        }
    }

    public RivaAsr.StreamingRecognizeRequest buildRequest(byte[] voice) {
        RivaAsr.StreamingRecognizeRequest.Builder reqeustStreamBuilder = RivaAsr.StreamingRecognizeRequest.newBuilder();
        if (voice != null)
            reqeustStreamBuilder.setAudioContent(ByteString.copyFrom(voice));
        else {
            reqeustStreamBuilder.setStreamingConfig(getConfig());
        }
        return reqeustStreamBuilder.build();
    }


    public RivaAsr.StreamingRecognitionConfig getConfig() {
        RivaAsr.RecognitionConfig.Builder recognitionConfigBuilder = RivaAsr.RecognitionConfig.newBuilder();
        RivaAsr.StreamingRecognitionConfig.Builder recognitionStreamConfigBuilder = RivaAsr.StreamingRecognitionConfig.newBuilder();
        recognitionConfigBuilder.setEncoding(RivaAudio.AudioEncoding.LINEAR_PCM);
        recognitionConfigBuilder.setSampleRateHertz(16000);
        recognitionConfigBuilder.setLanguageCode("ar-AR");
        recognitionConfigBuilder.setMaxAlternatives(1);
        recognitionConfigBuilder.setEnableAutomaticPunctuation(true);
        recognitionConfigBuilder.setVerbatimTranscripts(true);
        recognitionStreamConfigBuilder.setInterimResults(true);
        recognitionStreamConfigBuilder.setConfig(recognitionConfigBuilder.build());
        return recognitionStreamConfigBuilder.build();
    }

    public void doTranscribe(RivaAsr.StreamingRecognizeRequest streamingRecognizeRequest) {
        clientStream.send(streamingRecognizeRequest);
    }

    private ResponseObserver<RivaAsr.StreamingRecognizeResponse> getStreamResponseObserver() {
        return new ResponseObserver<RivaAsr.StreamingRecognizeResponse>() {
            @Override
            public void onStart(StreamController streamController) {
            }

            @Override
            public void onResponse(RivaAsr.StreamingRecognizeResponse response) {
                if (response.getResultsList().size() > 0) {
                    String text = response.getResultsList().get(0).getAlternativesList().get(0).getTranscript();
                    if (isNotBlank(text)){
                        int textLength=length(text);
                        int cachedLength=length(cachedName);
                        if (response.getResultsList().get(0).getIsFinal()) {
                            listener.onFinalTranscriptionReceived(text);
                            cachedName = text;
                        } else {

                            if (!text.equals(cachedName) && !cachedName.contains(text) &&textLength>=cachedLength) {
                                listener.onTranscriptionReceived(text);
                                cachedName = text;

                            }
                        }
                    }
                }

            }

            @Override
            public void onError(Throwable throwable) {
                Log.d("GRPC", throwable.getMessage());
            }

            @Override
            public void onComplete() {


            }
        };
    }

    private boolean isNotBlank(String text) {
        return !isBlank(text);
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen = length(cs);
        if (strLen == 0) {
            return true;
        } else {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }
    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }


}