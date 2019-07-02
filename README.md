This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

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

Runs the app in the development mode.<br>
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.<br>
You will also see any lint errors in the console.

## Run Server
### `npm install grpc`
### `node server.js`