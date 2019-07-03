import '../styles/ChatApp.css';

import React, { Component } from 'react'
import Messages from './Messages';
import ChatInput from './ChatInput';

const { SendMessageRequest, SendMessageResponse, Message, ReceiveMessageRequest } = require('./chat_pb.js');

class ChatApp extends Component {
  constructor(props) {
    super(props);
    console.log(this.props);
    this.state = { messages: [] };
    this.sendHandler = this.sendHandler.bind(this);

    // server streaming call
    var streamRequest = new ReceiveMessageRequest();
    streamRequest.setToken(this.props.token);

    var stream = this.props.client.receiveMessage(streamRequest, {});
    stream.on('data', (response) => {
      var message = response.getMessagetext();
      var from = response.getFrom();
      if (this.props.username !== from) {
        const messageObject = {
          username: from,
          fromMe: false,
          message: message
        };
        this.addMessage(messageObject);
      }
      
    });
    stream.on('end', (response) => {
      console.log('stream end: ' + response);
    });
  }

  sendHandler(message) {
    const messageObject = {
      username: this.props.username,
      message
    };

    // Emit the message to the server
    var body = new Message();
    // todo: ui to select which user to chat with
    body.setTo('user1');

    body.setFrom(this.props.username);
    body.setMessagetext(message);

    var messageRequest = new SendMessageRequest();
    messageRequest.setMessage(body);
    messageRequest.setToken(this.props.token);

    this.props.client.sendMessage(messageRequest, {}, (err, response) => {
      if (response == null) {
        console.log(err)
      } else {
        console.log(response.getStatus())
      }
    });

    messageObject.fromMe = true;
    this.addMessage(messageObject);
  }

  addMessage(message) {
    // Append the message to the component state
    const messages = this.state.messages;
    messages.push(message);
    this.setState({ messages });
  }

    render() {
    return (
      <div className="container">
        <h3>React Chat App</h3>
        <Messages messages={this.state.messages} />
        <ChatInput onSend={this.sendHandler} />
      </div>
    );
  }

}

ChatApp.defaultProps = {
  username: 'Anonymous'
};

export default ChatApp;