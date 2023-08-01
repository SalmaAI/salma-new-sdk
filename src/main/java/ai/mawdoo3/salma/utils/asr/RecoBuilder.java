package ai.mawdoo3.salma.utils.asr;


import android.util.Log;

import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.protobuf.ByteString;

import java.io.IOException;

import ai.mawdoo3.salma.utils.asr.riva.RivaAsr;
import ai.mawdoo3.salma.utils.asr.riva.RivaAudio;
import ai.mawdoo3.salma.utils.asr.riva.RivaSpeechClient;
import ai.mawdoo3.salma.utils.asr.riva.RivaSpeechRecognitionGrpc;
import io.grpc.ManagedChannel;


public class RecoBuilder {

    String cachedName = "";
    private ClientStream<RivaAsr.StreamingRecognizeRequest> clientStream;
    private ResponseObserver<RivaAsr.StreamingRecognizeResponse> responseObserver;
    private final RivaSpeechClient client;
    GrpcConnector.ITranscriptionStream listener;
    RecoBuilder(ManagedChannel channel) throws IOException {
        RivaSpeechRecognitionGrpc.RivaSpeechRecognitionStub  stub = RivaSpeechRecognitionGrpc.newStub(channel).withWaitForReady();
        client=RivaSpeechClient.create(stub);
        responseObserver= getStreamResponseObserver();
        clientStream =client.streamingRecognizeCallable().splitCall(responseObserver);
    }

    public void setVoiceListener(GrpcConnector.ITranscriptionStream listener){
        if(listener != null) {
            this.listener = listener;
        }
    }

    public void setListener(){
       if(responseObserver == null) {
            responseObserver = getStreamResponseObserver();
       }
    }
    public RivaAsr.StreamingRecognizeRequest buildRequest(byte[] voice) {
        RivaAsr.StreamingRecognizeRequest.Builder reqeustStreamBuilder = RivaAsr.StreamingRecognizeRequest.newBuilder();
        if(voice!=null)
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
                    if (response.getResultsList().get(0).getIsFinal()) {
                        listener.onFinalTranscriptionReceived(response.getResultsList().get(0).getAlternativesList().get(0).getTranscript());
                    }
                    else {
                        String text = response.getResultsList().get(0).getAlternativesList().get(0).getTranscript();
                        if (!text.equals(cachedName)) {
                            listener.onTranscriptionReceived(response.getResultsList().get(0).getAlternativesList().get(0).getTranscript());
                        }
                        cachedName = text;
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


}
