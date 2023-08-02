package ai.mawdoo3.salma.utils.asr;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 *Define the service
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.35.0)",
    comments = "Source: asr.proto")
public final class asr_serviceGrpc {

  private asr_serviceGrpc() {}

  public static final String SERVICE_NAME = "_service";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<Asr.speak,
      Asr.transcription_blob> getTranscribeBlobMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "transcribe_blob",
      requestType = Asr.speak.class,
      responseType = Asr.transcription_blob.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<Asr.speak,
      Asr.transcription_blob> getTranscribeBlobMethod() {
    io.grpc.MethodDescriptor<Asr.speak, Asr.transcription_blob> getTranscribeBlobMethod;
    if ((getTranscribeBlobMethod = asr_serviceGrpc.getTranscribeBlobMethod) == null) {
      synchronized (asr_serviceGrpc.class) {
        if ((getTranscribeBlobMethod = asr_serviceGrpc.getTranscribeBlobMethod) == null) {
          asr_serviceGrpc.getTranscribeBlobMethod = getTranscribeBlobMethod =
              io.grpc.MethodDescriptor.<Asr.speak, Asr.transcription_blob>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "transcribe_blob"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  Asr.speak.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  Asr.transcription_blob.getDefaultInstance()))
              .build();
        }
      }
    }
    return getTranscribeBlobMethod;
  }

  private static volatile io.grpc.MethodDescriptor<Asr.speak,Asr
      .transcription_stream> getTranscribeStreamMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "transcribe_stream",
      requestType = Asr.speak.class,
      responseType = Asr.transcription_stream.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<Asr.speak,
      Asr.transcription_stream> getTranscribeStreamMethod() {
    io.grpc.MethodDescriptor<Asr.speak, Asr.transcription_stream> getTranscribeStreamMethod;
    if ((getTranscribeStreamMethod = asr_serviceGrpc.getTranscribeStreamMethod) == null) {
      synchronized (asr_serviceGrpc.class) {
        if ((getTranscribeStreamMethod = asr_serviceGrpc.getTranscribeStreamMethod) == null) {
          asr_serviceGrpc.getTranscribeStreamMethod = getTranscribeStreamMethod =
              io.grpc.MethodDescriptor.<Asr.speak, Asr.transcription_stream>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "transcribe_stream"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  Asr.speak.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  Asr.transcription_stream.getDefaultInstance()))
              .build();
        }
      }
    }
    return getTranscribeStreamMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static asr_serviceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<asr_serviceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<asr_serviceStub>() {
        @Override
        public asr_serviceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new asr_serviceStub(channel, callOptions);
        }
      };
    return asr_serviceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static asr_serviceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<asr_serviceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<asr_serviceBlockingStub>() {
        @Override
        public asr_serviceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new asr_serviceBlockingStub(channel, callOptions);
        }
      };
    return asr_serviceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static asr_serviceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<asr_serviceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<asr_serviceFutureStub>() {
        @Override
        public asr_serviceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new asr_serviceFutureStub(channel, callOptions);
        }
      };
    return asr_serviceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   *Define the service
   * </pre>
   */
  public static abstract class asr_serviceImplBase implements io.grpc.BindableService {

    /**
     */
    public void transcribeBlob(Asr.speak request,
        io.grpc.stub.StreamObserver<Asr.transcription_blob> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getTranscribeBlobMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<Asr.speak> transcribeStream(
        io.grpc.stub.StreamObserver<Asr.transcription_stream> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getTranscribeStreamMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getTranscribeBlobMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
               Asr.speak,
                Asr.transcription_blob>(
                  this, METHODID_TRANSCRIBE_BLOB)))
          .addMethod(
            getTranscribeStreamMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                Asr.speak,
                Asr.transcription_stream>(
                  this, METHODID_TRANSCRIBE_STREAM)))
          .build();
    }
  }

  /**
   * <pre>
   *Define the service
   * </pre>
   */
  public static final class asr_serviceStub extends io.grpc.stub.AbstractAsyncStub<asr_serviceStub> {
    private asr_serviceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected asr_serviceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new asr_serviceStub(channel, callOptions);
    }

    /**
     */
    public void transcribeBlob(Asr.speak request,
        io.grpc.stub.StreamObserver<Asr.transcription_blob> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getTranscribeBlobMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<Asr.speak> transcribeStream(
        io.grpc.stub.StreamObserver<Asr.transcription_stream> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getTranscribeStreamMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * <pre>
   *Define the service
   * </pre>
   */
  public static final class asr_serviceBlockingStub extends io.grpc.stub.AbstractBlockingStub<asr_serviceBlockingStub> {
    private asr_serviceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected asr_serviceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new asr_serviceBlockingStub(channel, callOptions);
    }

    /**
     */
    public Asr.transcription_blob transcribeBlob(Asr.speak request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getTranscribeBlobMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   *Define the service
   * </pre>
   */
  public static final class asr_serviceFutureStub extends io.grpc.stub.AbstractFutureStub<asr_serviceFutureStub> {
    private asr_serviceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected asr_serviceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new asr_serviceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<Asr.transcription_blob> transcribeBlob(
        Asr.speak request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getTranscribeBlobMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_TRANSCRIBE_BLOB = 0;
  private static final int METHODID_TRANSCRIBE_STREAM = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final asr_serviceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(asr_serviceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_TRANSCRIBE_BLOB:
          serviceImpl.transcribeBlob((Asr.speak) request,
              (io.grpc.stub.StreamObserver<Asr.transcription_blob>) responseObserver);
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
        case METHODID_TRANSCRIBE_STREAM:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.transcribeStream(
              (io.grpc.stub.StreamObserver<Asr.transcription_stream>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (asr_serviceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .addMethod(getTranscribeBlobMethod())
              .addMethod(getTranscribeStreamMethod())
              .build();
        }
      }
    }
    return result;
  }
}
