from concurrent import futures

import grpc

from proto import hello_pb2
from proto import hello_pb2_grpc


class GreetService(hello_pb2_grpc.GreetServiceServicer):

    def sayHello(self, request, context):
        print(f"request: {request.name}")
        msg = f"[python] Hello: {request.name}"
        return hello_pb2.HelloResponse(message=msg)


def run():
    host = "0.0.0.0:50051"
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=4))
    hello_pb2_grpc.add_GreetServiceServicer_to_server(GreetService(), server)
    server.add_insecure_port(host)
    print(f"server start: {host}")
    server.start()
    server.wait_for_termination()


if __name__ == '__main__':
    run()
