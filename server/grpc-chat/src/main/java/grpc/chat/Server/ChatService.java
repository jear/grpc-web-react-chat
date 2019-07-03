package grpc.chat.Server;

import grpc.chat.ChatServerGrpc;
import grpc.chat.LoginRequest;
import grpc.chat.LoginResponse;
import grpc.chat.Message;
import grpc.chat.ReceiveMessageRequest;
import grpc.chat.SendMessageRequest;
import grpc.chat.SendMessageResponse;
import io.grpc.stub.StreamObserver;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ChatService extends ChatServerGrpc.ChatServerImplBase {
    private static final Logger logger = Logger.getLogger(ChatService.class.getName());

    private static Set<StreamObserver<Message>> observers = ConcurrentHashMap.newKeySet();

    private RedisService redisService;

    ChatService() {
        redisService = RedisService.getRedisService();
    }

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        if (redisService.isValidCredential(request.getUserName(), request.getPassword())) {
            String token = JWTService.getJwtService().getJWT(request.getUserName());

            LoginResponse response = LoginResponse
                    .newBuilder()
                    .setStatus("Login Successful!")
                    .setToken(token)
                    .build();

            responseObserver.onNext(response);

        } else {
            responseObserver.onNext(LoginResponse.newBuilder().setStatus("Login Failed. Try Again!").build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void sendMessage(SendMessageRequest request, StreamObserver<SendMessageResponse> responseObserver) {
        String from;
        try {
            String token = request.getToken();
            from = JWTService.getJwtService().parseJwt(token);
        } catch (Exception e) {
            responseObserver.onNext(SendMessageResponse.newBuilder().setStatus("Message Sending Failed").setTo(request.getMessage().getTo()).build());
            return;
        }

        if (userExists(request.getMessage().getTo())
                && userExists(from)) {

            sendMessage(request.getMessage().getTo(), from, request.getMessage().getMessageText());
            responseObserver.onNext(SendMessageResponse.newBuilder().setStatus("Message Sent!").build());

            for (StreamObserver<Message> observer : observers) {
                Message message = Message.newBuilder()
                        .setMessageText(request.getMessage().getMessageText())
                        .setFrom(from)
                        .setTo(request.getMessage().getTo())
                        .build();
                observer.onNext(message);
            }

        } else {
            responseObserver.onNext(SendMessageResponse.newBuilder().setStatus("Message Sending Failed").setTo(request.getMessage().getTo()).build());
        }

        responseObserver.onCompleted();
    }

    @Override
    public void receiveMessage(ReceiveMessageRequest request, final StreamObserver<Message> responseObserver) {
        observers.add(responseObserver);

        String receiver;
        try {
            String token = request.getToken();
            receiver = JWTService.getJwtService().parseJwt(token);
        } catch (Exception e) {
            return;
        }

        while((redisService.hasNextMessage(receiver))) {
            Message message = redisService.getNextMessage(receiver);
            responseObserver.onNext(message);
            redisService.removeLastMessage(receiver);
        }
    }

    @Override
    public StreamObserver<SendMessageRequest> chat(final StreamObserver<Message> responseObserver) {
        observers.add(responseObserver);

        return new StreamObserver<SendMessageRequest>() {
            public void onNext(SendMessageRequest request) {
                String from;
                try {
                    String token = request.getToken();
                    from = JWTService.getJwtService().parseJwt(token);
                } catch (Exception e) {
                    logger.log(Level.WARNING, "exception parsing token: " + e.getMessage());
                }
                Message message = Message.newBuilder()
                        .setMessageText(request.getMessage().getMessageText())
                        .setFrom(request.getMessage().getFrom())
                        .setTo(request.getMessage().getTo())
                        .build();

                for (StreamObserver<Message> observer : observers) {
                    observer.onNext(message);
                }
            }

            public void onError(Throwable t) {
                logger.log(Level.WARNING, "chat cancelled: " + t.getMessage());
            }

            public void onCompleted() {
                responseObserver.onCompleted();
                observers.remove(responseObserver);
            }
        };
    }

    private boolean userExists(String userName) {
        return redisService.userExists(userName);
    }

    private void sendMessage(String receiver, String sender, String message) {
        redisService.addMessage(sender, receiver, message);
    }
}
