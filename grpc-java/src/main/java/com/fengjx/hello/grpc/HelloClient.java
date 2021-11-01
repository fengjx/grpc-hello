package com.fengjx.hello.grpc;

import com.fengjx.hello.grpc.proto.GreetServiceGrpc;
import com.fengjx.hello.grpc.proto.HelloRequest;
import com.fengjx.hello.grpc.proto.HelloResponse;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloClient {

    private static final Logger logger = Logger.getLogger(HelloClient.class.getName());

    private final GreetServiceGrpc.GreetServiceBlockingStub greetService;

    public HelloClient(Channel channel) {
        greetService = GreetServiceGrpc.newBlockingStub(channel);
    }

    public void greet(String name) {
        logger.info("Will try to greet " + name + " ...");
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloResponse response;
        try {
            response = greetService.sayHello(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Greeting: " + response.getMessage());
    }

    public static void main(String[] args) throws InterruptedException {
        String name = "fengjx";
        String host = "localhost:50052";
        // Allow passing in the user and target strings as command line arguments
        if (args.length > 0) {
            if ("--help".equals(args[0])) {
                System.err.println("Usage: [name [target]]");
                System.err.println("");
                System.err.println("  name    The name you wish to be greeted by. Defaults to " + name);
                System.err.println("  target  The server to connect to. Defaults to " + host);
                System.exit(1);
            }
            name = args[0];
        }
        if (args.length > 1) {
            host = args[1];
        }

        ManagedChannel channel = ManagedChannelBuilder.forTarget(host)
                .usePlaintext()
                .build();
        try {
            HelloClient client = new HelloClient(channel);
            client.greet(name);
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }


}
