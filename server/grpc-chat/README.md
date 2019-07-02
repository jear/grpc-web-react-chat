# grpc-chat

## Build Server
`
mvn clean compile assembly:single
`

## Run Server
`
java -cp  target/grpc-chat-1.0-SNAPSHOT-jar-with-dependencies.jar grpc.chat.Server.ChatServer
`

## Run Client
`
java -cp  target/grpc-chat-1.0-SNAPSHOT-jar-with-dependencies.jar grpc.chat.Server.ConsoleChatClient
`

### Send Message
1. Add users to redis map. Example: `hset userNamePasswordMap user1 "pass1"`
2. Send a message in this format: "user:message". So if you want to send user2 "i am fine", you will need to type "user2:i am fine". quotes for clarity only.
3. Messages will be received as soon as the user logs in (or as received). No history of messages is maintained, messages will be deleted as soon as it is read.
4. After logging in, just type the message in the format (as given in point2) to send message.