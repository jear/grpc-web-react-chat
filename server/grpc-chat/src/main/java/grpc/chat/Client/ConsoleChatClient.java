package grpc.chat.Client;

import grpc.chat.ChatServerGrpc;
import grpc.chat.LoginRequest;
import grpc.chat.LoginResponse;
import grpc.chat.Message;
import grpc.chat.SendMessageRequest;
import grpc.chat.SendMessageResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConsoleChatClient {

    private final ManagedChannel channel;
    private final ChatServerGrpc.ChatServerBlockingStub blockingStub;
    private final ChatServerGrpc.ChatServerStub asyncStub;

    /**
     * Construct client for accessing RouteGuide server at {@code host:port}.
     */
    public ConsoleChatClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    /**
     * Construct client for accessing RouteGuide server using the existing channel.
     */
    public ConsoleChatClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        blockingStub = ChatServerGrpc.newBlockingStub(channel);
        asyncStub = ChatServerGrpc.newStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    private static class State {
        boolean isLoggedIn;
        private String userName;
        private String password;
        private String token;

        public State() {
            isLoggedIn = false;
        }

        public synchronized boolean isLoggedIn() {
            return isLoggedIn;
        }

        public void setLoggedIn(boolean loggedIn) {
            isLoggedIn = loggedIn;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    private State state = new State();

    private static TreeSet<Long> threeMessagesTimes = new TreeSet<Long>();


    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        final ConsoleChatClient client = new ConsoleChatClient("localhost", 8980);

        while (true) {
            try {
                if (!client.state.isLoggedIn()) {
                    Thread.sleep(2000);
                    if (!client.state.isLoggedIn()) {

                        System.out.println("Please Login First");
                        System.out.println("UserName:");
                        String userName = scanner.nextLine();
                        System.out.println("Password:");
                        String password = scanner.nextLine();

                        client.login(userName, password);
                    }

                } else {
                    String input = scanner.nextLine();
                    try {
                        String[] receiverMessageSplit = input.split(":", 2);
                        String to = receiverMessageSplit[0], message = receiverMessageSplit[1];
                        client.sendMessage(to, message);
                    } catch (Exception ignored) {
                    }

                }
            } catch (Exception e) {
                System.out.println("Exception thrown");
                e.printStackTrace();
            }
        }
    }


    private void login(String userName, String password) {
        if (state.isLoggedIn()) {
            System.out.println("Already Logged In as " + state.getUserName());
            return;
        }

        LoginResponse loginResponse = blockingStub.login(LoginRequest.newBuilder()
                .setUserName(userName)
                .setPassword(password)
                .build());

        System.out.println(loginResponse.getStatus());
        if (loginResponse.getStatus().equals("Login Successful!")) {
            state.setLoggedIn(true);
            state.setToken(loginResponse.getToken());
            state.setUserName(userName);

            //Executors.newScheduledThreadPool(2).scheduleAtFixedRate(new ReceiveMessage(asyncStub, loginResponse.getToken()), 0, 1, TimeUnit.SECONDS);
            Executors.newScheduledThreadPool(2).schedule(new ReceiveMessage(asyncStub, userName, loginResponse.getToken()), 1, TimeUnit.SECONDS);
        }
    }

    private void sendMessage(String receiver, String message) {
        SendMessageRequest sendMessageRequest = SendMessageRequest.newBuilder()
                .setMessage(
                        Message.newBuilder()
                                .setMessageText(message)
                                .setTo(receiver)
                                .build())
                .setToken(state.getToken())
                .build();

        try {
            if (message.getBytes("UTF-8").length > 4000) {
                System.out.println("Message in UTF-8 can't be more than 4KB");
            }
        } catch (UnsupportedEncodingException ignored) {
        }

        if (!check3mIn5s()) {
            System.out.println("Can't send more than 3 messages in a window of 5 seconds");
            return;
        }

        SendMessageResponse sendMessageResponse = blockingStub.sendMessage(sendMessageRequest);

        if (!sendMessageResponse.getStatus().toLowerCase().contains("sent")) {
            System.out.print(sendMessageResponse.getStatus());
            System.out.println(" To:" + receiver);
        } else {
            System.out.println(sendMessageResponse.getStatus());
        }
    }

    private boolean check3mIn5s() {
        Long now = System.currentTimeMillis();

        if (threeMessagesTimes.size() == 3) {
            Long oldestTime = threeMessagesTimes.first();
            if (now - oldestTime >= 5000) {
                threeMessagesTimes.remove(oldestTime);
                threeMessagesTimes.add(now);
                return true;
            } else {
                return false;
            }
        }

        threeMessagesTimes.add(now);
        return true;
    }
}
