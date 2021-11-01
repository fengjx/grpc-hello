
import grpc
from proto import hello_pb2
from proto import hello_pb2_grpc


def run():
    host = "localhost:50051"
    with grpc.insecure_channel(host) as channel:
        greet_service = hello_pb2_grpc.GreetServiceStub(channel)
        resp = greet_service.sayHello(hello_pb2.HelloRequest(name="fengjx"))
        print(f"received: {resp.message}")


if __name__ == '__main__':
    run()
