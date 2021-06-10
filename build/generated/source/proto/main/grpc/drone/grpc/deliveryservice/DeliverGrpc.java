package drone.grpc.deliveryservice;

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
    comments = "Source: DeliveryService.proto")
public final class DeliverGrpc {

  private DeliverGrpc() {}

  public static final String SERVICE_NAME = "drone.grpc.deliveryservice.Deliver";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<drone.grpc.deliveryservice.DeliveryService.DeliveryRequest,
      drone.grpc.deliveryservice.DeliveryService.DeliveryResponse> getAssignMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Assign",
      requestType = drone.grpc.deliveryservice.DeliveryService.DeliveryRequest.class,
      responseType = drone.grpc.deliveryservice.DeliveryService.DeliveryResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<drone.grpc.deliveryservice.DeliveryService.DeliveryRequest,
      drone.grpc.deliveryservice.DeliveryService.DeliveryResponse> getAssignMethod() {
    io.grpc.MethodDescriptor<drone.grpc.deliveryservice.DeliveryService.DeliveryRequest, drone.grpc.deliveryservice.DeliveryService.DeliveryResponse> getAssignMethod;
    if ((getAssignMethod = DeliverGrpc.getAssignMethod) == null) {
      synchronized (DeliverGrpc.class) {
        if ((getAssignMethod = DeliverGrpc.getAssignMethod) == null) {
          DeliverGrpc.getAssignMethod = getAssignMethod =
              io.grpc.MethodDescriptor.<drone.grpc.deliveryservice.DeliveryService.DeliveryRequest, drone.grpc.deliveryservice.DeliveryService.DeliveryResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Assign"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  drone.grpc.deliveryservice.DeliveryService.DeliveryRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  drone.grpc.deliveryservice.DeliveryService.DeliveryResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DeliverMethodDescriptorSupplier("Assign"))
              .build();
        }
      }
    }
    return getAssignMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DeliverStub newStub(io.grpc.Channel channel) {
    return new DeliverStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DeliverBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new DeliverBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DeliverFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new DeliverFutureStub(channel);
  }

  /**
   */
  public static abstract class DeliverImplBase implements io.grpc.BindableService {

    /**
     */
    public void assign(drone.grpc.deliveryservice.DeliveryService.DeliveryRequest request,
        io.grpc.stub.StreamObserver<drone.grpc.deliveryservice.DeliveryService.DeliveryResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAssignMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getAssignMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                drone.grpc.deliveryservice.DeliveryService.DeliveryRequest,
                drone.grpc.deliveryservice.DeliveryService.DeliveryResponse>(
                  this, METHODID_ASSIGN)))
          .build();
    }
  }

  /**
   */
  public static final class DeliverStub extends io.grpc.stub.AbstractStub<DeliverStub> {
    private DeliverStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DeliverStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DeliverStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DeliverStub(channel, callOptions);
    }

    /**
     */
    public void assign(drone.grpc.deliveryservice.DeliveryService.DeliveryRequest request,
        io.grpc.stub.StreamObserver<drone.grpc.deliveryservice.DeliveryService.DeliveryResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getAssignMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class DeliverBlockingStub extends io.grpc.stub.AbstractStub<DeliverBlockingStub> {
    private DeliverBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DeliverBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DeliverBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DeliverBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<drone.grpc.deliveryservice.DeliveryService.DeliveryResponse> assign(
        drone.grpc.deliveryservice.DeliveryService.DeliveryRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getAssignMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class DeliverFutureStub extends io.grpc.stub.AbstractStub<DeliverFutureStub> {
    private DeliverFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DeliverFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DeliverFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DeliverFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_ASSIGN = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final DeliverImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(DeliverImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ASSIGN:
          serviceImpl.assign((drone.grpc.deliveryservice.DeliveryService.DeliveryRequest) request,
              (io.grpc.stub.StreamObserver<drone.grpc.deliveryservice.DeliveryService.DeliveryResponse>) responseObserver);
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

  private static abstract class DeliverBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DeliverBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return drone.grpc.deliveryservice.DeliveryService.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Deliver");
    }
  }

  private static final class DeliverFileDescriptorSupplier
      extends DeliverBaseDescriptorSupplier {
    DeliverFileDescriptorSupplier() {}
  }

  private static final class DeliverMethodDescriptorSupplier
      extends DeliverBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    DeliverMethodDescriptorSupplier(String methodName) {
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
      synchronized (DeliverGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DeliverFileDescriptorSupplier())
              .addMethod(getAssignMethod())
              .build();
        }
      }
    }
    return result;
  }
}
