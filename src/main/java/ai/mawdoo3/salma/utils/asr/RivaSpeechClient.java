package ai.mawdoo3.salma.utils.asr;

import java.io.IOException;

import com.google.api.gax.grpc.GrpcCallContext;
import com.google.api.gax.grpc.GrpcCallSettings;
import com.google.api.gax.grpc.GrpcCallableFactory;
import com.google.api.gax.rpc.BidiStreamingCallable;
import com.google.api.gax.rpc.ClientContext;

public class RivaSpeechClient {
    private final RivaSpeechRecognitionGrpc.RivaSpeechRecognitionStub stub;
    private final ClientContext clientContext;
    private final BidiStreamingCallable<RivaAsr.StreamingRecognizeRequest, RivaAsr.StreamingRecognizeResponse>streamingRecognizeCallable;

    public static RivaSpeechClient create(RivaSpeechRecognitionGrpc.RivaSpeechRecognitionStub stub) throws IOException {
        return new RivaSpeechClient(stub);
    }


    protected RivaSpeechClient(RivaSpeechRecognitionGrpc.RivaSpeechRecognitionStub stub) {
        this.stub = stub;
        GrpcCallContext grpcCallContext=  GrpcCallContext.createDefault();
        grpcCallContext=grpcCallContext.withChannel(stub.getChannel());
        this.clientContext= ClientContext.newBuilder().setDefaultCallContext(grpcCallContext).build();



        GrpcCallSettings<RivaAsr.StreamingRecognizeRequest, RivaAsr.StreamingRecognizeResponse> streamingRecognizeTransportSettings = GrpcCallSettings.<RivaAsr.StreamingRecognizeRequest, RivaAsr.StreamingRecognizeResponse>newBuilder().setMethodDescriptor(RivaSpeechRecognitionGrpc.getStreamingRecognizeMethod()).build();
        this.streamingRecognizeCallable = GrpcCallableFactory.<RivaAsr.StreamingRecognizeRequest, RivaAsr.StreamingRecognizeResponse>createBidiStreamingCallable(streamingRecognizeTransportSettings, null, clientContext);

    }


    public RivaSpeechRecognitionGrpc.RivaSpeechRecognitionStub getStub() {
        return stub;
    }
    public final BidiStreamingCallable<RivaAsr.StreamingRecognizeRequest, RivaAsr.StreamingRecognizeResponse>
    streamingRecognizeCallable() {
        return streamingRecognizeCallable;
    }
}
