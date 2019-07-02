package grpc.chat.Client;

import grpc.chat.ChatServerGrpc;
import grpc.chat.Message;
import grpc.chat.ReceiveMessageRequest;
import io.grpc.stub.StreamObserver;

public class ReceiveMessage implements Runnable {
    private String userName;
    private String token;
    private ChatServerGrpc.ChatServerStub serverStub;
    private StreamObserver<Message> responseStreamObserver;

    ReceiveMessage(ChatServerGrpc.ChatServerStub serverStub, final String userName, String token) {
        this.userName = userName;
        this.token = token;
        this.serverStub = serverStub;

        responseStreamObserver = new StreamObserver<Message>() {
            public void onNext(Message message) {
                if (userName.equals(message.getTo())) {
                    System.out.println("New Message From " + message.getFrom() + ":" + message.getMessageText());
                }
            }

            public void onError(Throwable throwable) {
                System.out.println("ReceiveMessage Error");
            }

            public void onCompleted() {
                System.out.println("ReceiveMessage Completed");
            }
        };
    }

    public void run() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.newBuilder()
                .setToken(token)
                .build();

        serverStub.receiveMessage(receiveMessageRequest, responseStreamObserver);
    }
}
