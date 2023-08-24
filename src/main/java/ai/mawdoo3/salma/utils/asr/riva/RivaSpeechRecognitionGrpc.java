package ai.mawdoo3.salma.utils.asr.riva;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * The RivaSpeechRecognition service provides two mechanisms for converting speech to text.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.55.1)",
    comments = "Source: riva/riva_asr.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class RivaSpeechRecognitionGrpc {

  private RivaSpeechRecognitionGrpc() {}

  public static final String SERVICE_NAME = "nvidia.riva.asr.RivaSpeechRecognition";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<RivaAsr.RecognizeRequest,
      RivaAsr.RecognizeResponse> getRecognizeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Recognize",
      requestType = RivaAsr.RecognizeRequest.class,
      responseType = RivaAsr.RecognizeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<RivaAsr.RecognizeRequest,
      RivaAsr.RecognizeResponse> getRecognizeMethod() {
    io.grpc.MethodDescriptor<RivaAsr.RecognizeRequest, RivaAsr.RecognizeResponse> getRecognizeMethod;
    if ((getRecognizeMethod = RivaSpeechRecognitionGrpc.getRecognizeMethod) == null) {
      synchronized (RivaSpeechRecognitionGrpc.class) {
        if ((getRecognizeMethod = RivaSpeechRecognitionGrpc.getRecognizeMethod) == null) {
          RivaSpeechRecognitionGrpc.getRecognizeMethod = getRecognizeMethod =
              io.grpc.MethodDescriptor.<RivaAsr.RecognizeRequest, RivaAsr.RecognizeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Recognize"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RivaAsr.RecognizeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RivaAsr.RecognizeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RivaSpeechRecognitionMethodDescriptorSupplier("Recognize"))
              .build();
        }
      }
    }
    return getRecognizeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<RivaAsr.StreamingRecognizeRequest,
      RivaAsr.StreamingRecognizeResponse> getStreamingRecognizeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "StreamingRecognize",
      requestType = RivaAsr.StreamingRecognizeRequest.class,
      responseType = RivaAsr.StreamingRecognizeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<RivaAsr.StreamingRecognizeRequest,
      RivaAsr.StreamingRecognizeResponse> getStreamingRecognizeMethod() {
    io.grpc.MethodDescriptor<RivaAsr.StreamingRecognizeRequest, RivaAsr.StreamingRecognizeResponse> getStreamingRecognizeMethod;
    if ((getStreamingRecognizeMethod = RivaSpeechRecognitionGrpc.getStreamingRecognizeMethod) == null) {
      synchronized (RivaSpeechRecognitionGrpc.class) {
        if ((getStreamingRecognizeMethod = RivaSpeechRecognitionGrpc.getStreamingRecognizeMethod) == null) {
          RivaSpeechRecognitionGrpc.getStreamingRecognizeMethod = getStreamingRecognizeMethod =
              io.grpc.MethodDescriptor.<RivaAsr.StreamingRecognizeRequest, RivaAsr.StreamingRecognizeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "StreamingRecognize"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RivaAsr.StreamingRecognizeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RivaAsr.StreamingRecognizeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RivaSpeechRecognitionMethodDescriptorSupplier("StreamingRecognize"))
              .build();
        }
      }
    }
    return getStreamingRecognizeMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RivaSpeechRecognitionStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RivaSpeechRecognitionStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RivaSpeechRecognitionStub>() {
        @Override
        public RivaSpeechRecognitionStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RivaSpeechRecognitionStub(channel, callOptions);
        }
      };
    return RivaSpeechRecognitionStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RivaSpeechRecognitionBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RivaSpeechRecognitionBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RivaSpeechRecognitionBlockingStub>() {
        @Override
        public RivaSpeechRecognitionBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RivaSpeechRecognitionBlockingStub(channel, callOptions);
        }
      };
    return RivaSpeechRecognitionBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RivaSpeechRecognitionFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RivaSpeechRecognitionFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RivaSpeechRecognitionFutureStub>() {
        @Override
        public RivaSpeechRecognitionFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RivaSpeechRecognitionFutureStub(channel, callOptions);
        }
      };
    return RivaSpeechRecognitionFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * The RivaSpeechRecognition service provides two mechanisms for converting speech to text.
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * Recognize expects a RecognizeRequest and returns a RecognizeResponse. This request will block
     * until the audio is uploaded, processed, and a transcript is returned.
     * </pre>
     */
    default void recognize(RivaAsr.RecognizeRequest request,
                           io.grpc.stub.StreamObserver<RivaAsr.RecognizeResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRecognizeMethod(), responseObserver);
    }

    /**
     * <pre>
     * StreamingRecognize is a non-blocking API call that allows audio data to be fed to the server in
     * chunks as it becomes available. Depending on the configuration in the StreamingRecognizeRequest,
     * intermediate results can be sent back to the client. Recognition ends when the stream is closed
     * by the client.
     * </pre>
     */
    default io.grpc.stub.StreamObserver<RivaAsr.StreamingRecognizeRequest> streamingRecognize(
        io.grpc.stub.StreamObserver<RivaAsr.StreamingRecognizeResponse> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getStreamingRecognizeMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service RivaSpeechRecognition.
   * <pre>
   * The RivaSpeechRecognition service provides two mechanisms for converting speech to text.
   * </pre>
   */
  public static abstract class RivaSpeechRecognitionImplBase
      implements io.grpc.BindableService, AsyncService {

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return RivaSpeechRecognitionGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service RivaSpeechRecognition.
   * <pre>
   * The RivaSpeechRecognition service provides two mechanisms for converting speech to text.
   * </pre>
   */
  public static final class RivaSpeechRecognitionStub
      extends io.grpc.stub.AbstractAsyncStub<RivaSpeechRecognitionStub> {
    private RivaSpeechRecognitionStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RivaSpeechRecognitionStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RivaSpeechRecognitionStub(channel, callOptions);
    }

    /**
     * <pre>
     * Recognize expects a RecognizeRequest and returns a RecognizeResponse. This request will block
     * until the audio is uploaded, processed, and a transcript is returned.
     * </pre>
     */
    public void recognize(RivaAsr.RecognizeRequest request,
                          io.grpc.stub.StreamObserver<RivaAsr.RecognizeResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRecognizeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * StreamingRecognize is a non-blocking API call that allows audio data to be fed to the server in
     * chunks as it becomes available. Depending on the configuration in the StreamingRecognizeRequest,
     * intermediate results can be sent back to the client. Recognition ends when the stream is closed
     * by the client.
     * </pre>
     */
    public io.grpc.stub.StreamObserver<RivaAsr.StreamingRecognizeRequest> streamingRecognize(
        io.grpc.stub.StreamObserver<RivaAsr.StreamingRecognizeResponse> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getStreamingRecognizeMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service RivaSpeechRecognition.
   * <pre>
   * The RivaSpeechRecognition service provides two mechanisms for converting speech to text.
   * </pre>
   */
  public static final class RivaSpeechRecognitionBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<RivaSpeechRecognitionBlockingStub> {
    private RivaSpeechRecognitionBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RivaSpeechRecognitionBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RivaSpeechRecognitionBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Recognize expects a RecognizeRequest and returns a RecognizeResponse. This request will block
     * until the audio is uploaded, processed, and a transcript is returned.
     * </pre>
     */
    public RivaAsr.RecognizeResponse recognize(RivaAsr.RecognizeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRecognizeMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service RivaSpeechRecognition.
   * <pre>
   * The RivaSpeechRecognition service provides two mechanisms for converting speech to text.
   * </pre>
   */
  public static final class RivaSpeechRecognitionFutureStub
      extends io.grpc.stub.AbstractFutureStub<RivaSpeechRecognitionFutureStub> {
    private RivaSpeechRecognitionFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RivaSpeechRecognitionFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RivaSpeechRecognitionFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Recognize expects a RecognizeRequest and returns a RecognizeResponse. This request will block
     * until the audio is uploaded, processed, and a transcript is returned.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<RivaAsr.RecognizeResponse> recognize(
        RivaAsr.RecognizeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRecognizeMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_RECOGNIZE = 0;
  private static final int METHODID_STREAMING_RECOGNIZE = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_RECOGNIZE:
          serviceImpl.recognize((RivaAsr.RecognizeRequest) request,
              (io.grpc.stub.StreamObserver<RivaAsr.RecognizeResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_STREAMING_RECOGNIZE:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.streamingRecognize(
              (io.grpc.stub.StreamObserver<RivaAsr.StreamingRecognizeResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getRecognizeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              RivaAsr.RecognizeRequest,
              RivaAsr.RecognizeResponse>(
                service, METHODID_RECOGNIZE)))
        .addMethod(
          getStreamingRecognizeMethod(),
          io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
            new MethodHandlers<
              RivaAsr.StreamingRecognizeRequest,
              RivaAsr.StreamingRecognizeResponse>(
                service, METHODID_STREAMING_RECOGNIZE)))
        .build();
  }

  private static abstract class RivaSpeechRecognitionBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RivaSpeechRecognitionBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return RivaAsr.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RivaSpeechRecognition");
    }
  }

  private static final class RivaSpeechRecognitionFileDescriptorSupplier
      extends RivaSpeechRecognitionBaseDescriptorSupplier {
    RivaSpeechRecognitionFileDescriptorSupplier() {}
  }

  private static final class RivaSpeechRecognitionMethodDescriptorSupplier
      extends RivaSpeechRecognitionBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    RivaSpeechRecognitionMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (RivaSpeechRecognitionGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RivaSpeechRecognitionFileDescriptorSupplier())
              .addMethod(getRecognizeMethod())
              .addMethod(getStreamingRecognizeMethod())
              .build();
        }
      }
    }
    return result;
  }
}
