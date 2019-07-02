This project is bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

## Prerequisites
### `docker-compose up -d`

### Generate Protobuf Messages and Client Service Stub
```sh
$ protoc -I=. chat.proto \
  --js_out=import_style=commonjs:. \
  --grpc-web_out=import_style=commonjs,mode=grpcwebtext:.
```

## Run Client

In the learn-react-grpc project directory, you can run:

### `npm install`

### `npm start`

It will run the app in the development mode.<br>
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.<br>
You will also see any lint errors in the console.

## Run Server
Navigate to the server/grpc-chat project directory, you can then build and run the grpc-chat server.
### Build Server
`
mvn clean compile assembly:single
`

### Run Server
`
java -cp  target/grpc-chat-1.0-SNAPSHOT-jar-with-dependencies.jar grpc.chat.Server.ChatServer
`