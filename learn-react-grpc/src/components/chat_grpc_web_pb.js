/**
 * @fileoverview gRPC-Web generated client stub for 
 * @enhanceable
 * @public
 */

// GENERATED CODE -- DO NOT EDIT!

/*  eslint-disable */

const grpc = {};
grpc.web = require('grpc-web');

const proto = require('./chat_pb.js');

/**
 * @param {string} hostname
 * @param {?Object} credentials
 * @param {?Object} options
 * @constructor
 * @struct
 * @final
 */
proto.ChatServerClient =
    function(hostname, credentials, options) {
  if (!options) options = {};
  options['format'] = 'text';

  /**
   * @private @const {!grpc.web.GrpcWebClientBase} The client
   */
  this.client_ = new grpc.web.GrpcWebClientBase(options);

  /**
   * @private @const {string} The hostname
   */
  this.hostname_ = hostname;

  /**
   * @private @const {?Object} The credentials to be used to connect
   *    to the server
   */
  this.credentials_ = credentials;

  /**
   * @private @const {?Object} Options for the client
   */
  this.options_ = options;
};


/**
 * @param {string} hostname
 * @param {?Object} credentials
 * @param {?Object} options
 * @constructor
 * @struct
 * @final
 */
proto.ChatServerPromiseClient =
    function(hostname, credentials, options) {
  if (!options) options = {};
  options['format'] = 'text';

  /**
   * @private @const {!grpc.web.GrpcWebClientBase} The client
   */
  this.client_ = new grpc.web.GrpcWebClientBase(options);

  /**
   * @private @const {string} The hostname
   */
  this.hostname_ = hostname;

  /**
   * @private @const {?Object} The credentials to be used to connect
   *    to the server
   */
  this.credentials_ = credentials;

  /**
   * @private @const {?Object} Options for the client
   */
  this.options_ = options;
};


/**
 * @const
 * @type {!grpc.web.AbstractClientBase.MethodInfo<
 *   !proto.LoginRequest,
 *   !proto.LoginResponse>}
 */
const methodInfo_ChatServer_Login = new grpc.web.AbstractClientBase.MethodInfo(
  proto.LoginResponse,
  /** @param {!proto.LoginRequest} request */
  function(request) {
    return request.serializeBinary();
  },
  proto.LoginResponse.deserializeBinary
);


/**
 * @param {!proto.LoginRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @param {function(?grpc.web.Error, ?proto.LoginResponse)}
 *     callback The callback function(error, response)
 * @return {!grpc.web.ClientReadableStream<!proto.LoginResponse>|undefined}
 *     The XHR Node Readable Stream
 */
proto.ChatServerClient.prototype.login =
    function(request, metadata, callback) {
  return this.client_.rpcCall(this.hostname_ +
      '/ChatServer/Login',
      request,
      metadata || {},
      methodInfo_ChatServer_Login,
      callback);
};


/**
 * @param {!proto.LoginRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!Promise<!proto.LoginResponse>}
 *     A native promise that resolves to the response
 */
proto.ChatServerPromiseClient.prototype.login =
    function(request, metadata) {
  return this.client_.unaryCall(this.hostname_ +
      '/ChatServer/Login',
      request,
      metadata || {},
      methodInfo_ChatServer_Login);
};


/**
 * @const
 * @type {!grpc.web.AbstractClientBase.MethodInfo<
 *   !proto.SendMessageRequest,
 *   !proto.SendMessageResponse>}
 */
const methodInfo_ChatServer_SendMessage = new grpc.web.AbstractClientBase.MethodInfo(
  proto.SendMessageResponse,
  /** @param {!proto.SendMessageRequest} request */
  function(request) {
    return request.serializeBinary();
  },
  proto.SendMessageResponse.deserializeBinary
);


/**
 * @param {!proto.SendMessageRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @param {function(?grpc.web.Error, ?proto.SendMessageResponse)}
 *     callback The callback function(error, response)
 * @return {!grpc.web.ClientReadableStream<!proto.SendMessageResponse>|undefined}
 *     The XHR Node Readable Stream
 */
proto.ChatServerClient.prototype.sendMessage =
    function(request, metadata, callback) {
  return this.client_.rpcCall(this.hostname_ +
      '/ChatServer/SendMessage',
      request,
      metadata || {},
      methodInfo_ChatServer_SendMessage,
      callback);
};


/**
 * @param {!proto.SendMessageRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!Promise<!proto.SendMessageResponse>}
 *     A native promise that resolves to the response
 */
proto.ChatServerPromiseClient.prototype.sendMessage =
    function(request, metadata) {
  return this.client_.unaryCall(this.hostname_ +
      '/ChatServer/SendMessage',
      request,
      metadata || {},
      methodInfo_ChatServer_SendMessage);
};


/**
 * @const
 * @type {!grpc.web.AbstractClientBase.MethodInfo<
 *   !proto.ReceiveMessageRequest,
 *   !proto.Message>}
 */
const methodInfo_ChatServer_ReceiveMessage = new grpc.web.AbstractClientBase.MethodInfo(
  proto.Message,
  /** @param {!proto.ReceiveMessageRequest} request */
  function(request) {
    return request.serializeBinary();
  },
  proto.Message.deserializeBinary
);


/**
 * @param {!proto.ReceiveMessageRequest} request The request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!grpc.web.ClientReadableStream<!proto.Message>}
 *     The XHR Node Readable Stream
 */
proto.ChatServerClient.prototype.receiveMessage =
    function(request, metadata) {
  return this.client_.serverStreaming(this.hostname_ +
      '/ChatServer/ReceiveMessage',
      request,
      metadata || {},
      methodInfo_ChatServer_ReceiveMessage);
};


/**
 * @param {!proto.ReceiveMessageRequest} request The request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!grpc.web.ClientReadableStream<!proto.Message>}
 *     The XHR Node Readable Stream
 */
proto.ChatServerPromiseClient.prototype.receiveMessage =
    function(request, metadata) {
  return this.client_.serverStreaming(this.hostname_ +
      '/ChatServer/ReceiveMessage',
      request,
      metadata || {},
      methodInfo_ChatServer_ReceiveMessage);
};


module.exports = proto;

