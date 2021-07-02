package drone.grpc.electionservice;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.25.0)",
    comments = "Source: ElectionService.proto")
public final class ElectionGrpc {

  private ElectionGrpc() {}

  public static final String SERVICE_NAME = "drone.grpc.electionservice.Election";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<drone.grpc.electionservice.ElectionService.ElectionRequest,
      com.google.protobuf.Empty> getElectionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Election",
      requestType = drone.grpc.electionservice.ElectionService.ElectionRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<drone.grpc.electionservice.ElectionService.ElectionRequest,
      com.google.protobuf.Empty> getElectionMethod() {
    io.grpc.MethodDescriptor<drone.grpc.electionservice.ElectionService.ElectionRequest, com.google.protobuf.Empty> getElectionMethod;
    if ((getElectionMethod = ElectionGrpc.getElectionMethod) == null) {
      synchronized (ElectionGrpc.class) {
        if ((getElectionMethod = ElectionGrpc.getElectionMethod) == null) {
          ElectionGrpc.getElectionMethod = getElectionMethod =
              io.grpc.MethodDescriptor.<drone.grpc.electionservice.ElectionService.ElectionRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Election"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  drone.grpc.electionservice.ElectionService.ElectionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new ElectionMethodDescriptorSupplier("Election"))
              .build();
        }
      }
    }
    return getElectionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<drone.grpc.electionservice.ElectionService.ShoutRequest,
      com.google.protobuf.Empty> getShoutMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Shout",
      requestType = drone.grpc.electionservice.ElectionService.ShoutRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<drone.grpc.electionservice.ElectionService.ShoutRequest,
      com.google.protobuf.Empty> getShoutMethod() {
    io.grpc.MethodDescriptor<drone.grpc.electionservice.ElectionService.ShoutRequest, com.google.protobuf.Empty> getShoutMethod;
    if ((getShoutMethod = ElectionGrpc.getShoutMethod) == null) {
      synchronized (ElectionGrpc.class) {
        if ((getShoutMethod = ElectionGrpc.getShoutMethod) == null) {
          ElectionGrpc.getShoutMethod = getShoutMethod =
              io.grpc.MethodDescriptor.<drone.grpc.electionservice.ElectionService.ShoutRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Shout"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  drone.grpc.electionservice.ElectionService.ShoutRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new ElectionMethodDescriptorSupplier("Shout"))
              .build();
        }
      }
    }
    return getShoutMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ElectionStub newStub(io.grpc.Channel channel) {
    return new ElectionStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ElectionBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ElectionBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ElectionFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ElectionFutureStub(channel);
  }

  /**
   */
  public static abstract class ElectionImplBase implements io.grpc.BindableService {

    /**
     */
    public void election(drone.grpc.electionservice.ElectionService.ElectionRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getElectionMethod(), responseObserver);
    }

    /**
     */
    public void shout(drone.grpc.electionservice.ElectionService.ShoutRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getShoutMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getElectionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                drone.grpc.electionservice.ElectionService.ElectionRequest,
                com.google.protobuf.Empty>(
                  this, METHODID_ELECTION)))
          .addMethod(
            getShoutMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                drone.grpc.electionservice.ElectionService.ShoutRequest,
                com.google.protobuf.Empty>(
                  this, METHODID_SHOUT)))
          .build();
    }
  }

  /**
   */
  public static final class ElectionStub extends io.grpc.stub.AbstractStub<ElectionStub> {
    private ElectionStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ElectionStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ElectionStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ElectionStub(channel, callOptions);
    }

    /**
     */
    public void election(drone.grpc.electionservice.ElectionService.ElectionRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getElectionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void shout(drone.grpc.electionservice.ElectionService.ShoutRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getShoutMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ElectionBlockingStub extends io.grpc.stub.AbstractStub<ElectionBlockingStub> {
    private ElectionBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ElectionBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ElectionBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ElectionBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.google.protobuf.Empty election(drone.grpc.electionservice.ElectionService.ElectionRequest request) {
      return blockingUnaryCall(
          getChannel(), getElectionMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty shout(drone.grpc.electionservice.ElectionService.ShoutRequest request) {
      return blockingUnaryCall(
          getChannel(), getShoutMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ElectionFutureStub extends io.grpc.stub.AbstractStub<ElectionFutureStub> {
    private ElectionFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ElectionFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ElectionFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ElectionFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> election(
        drone.grpc.electionservice.ElectionService.ElectionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getElectionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> shout(
        drone.grpc.electionservice.ElectionService.ShoutRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getShoutMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ELECTION = 0;
  private static final int METHODID_SHOUT = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ElectionImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ElectionImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ELECTION:
          serviceImpl.election((drone.grpc.electionservice.ElectionService.ElectionRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_SHOUT:
          serviceImpl.shout((drone.grpc.electionservice.ElectionService.ShoutRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ElectionBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ElectionBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return drone.grpc.electionservice.ElectionService.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Election");
    }
  }

  private static final class ElectionFileDescriptorSupplier
      extends ElectionBaseDescriptorSupplier {
    ElectionFileDescriptorSupplier() {}
  }

  private static final class ElectionMethodDescriptorSupplier
      extends ElectionBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ElectionMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ElectionGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ElectionFileDescriptorSupplier())
              .addMethod(getElectionMethod())
              .addMethod(getShoutMethod())
              .build();
        }
      }
    }
    return result;
  }
}
