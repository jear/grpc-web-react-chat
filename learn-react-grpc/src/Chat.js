import React, { Component } from 'react'
import ChatInput from './ChatInput'
import ChatMessage from './ChatMessage'

const { LoginRequest, LoginResponse, SendMessageRequest, SendMessageResponse, Message, ReceiveMessageRequest } = require('./chat_pb.js');
const { ChatServerClient } = require('./chat_grpc_web_pb.js');

class Chat extends Component {
  state = {
    name: 'Bob',
    messages: [],
  }

  client = new ChatServerClient('http://localhost:9090', null, null);

  componentDidMount() {
    // on connecting, do nothing but log it to the console
    console.log('connected')
    // login
    var request = new LoginRequest();
    request.setUsername('user2');
    request.setPassword('pass2');

    var token;
    this.client.login(request, {}, (err, response) => {
      if (response == null) {
         console.log(err)
       } else {
        token = response.getToken();
        console.log(response.getToken())
      }
     });

    // this.ws.onmessage = evt => {
    //   // on receiving a message, add it to the list of messages
    //   const message = JSON.parse(evt.data)
    //   this.addMessage(message)
    // }
  }

  addMessage = message =>
    this.setState(state => ({ messages: [message, ...state.messages] }))

  submitMessage = messageString => {
    // on submitting the ChatInput form, send the message, add it to the list and reset the input
    const message = { name: this.state.name, message: messageString }
    this.addMessage(message)

    var body = new Message();
    body.setTo('user1');
    body.setFrom('user2');
    body.setMessagetext(messageString);

    var messageRequest = new SendMessageRequest();
    messageRequest.setMessage(body);
    messageRequest.setToken('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMiJ9.S5Utal1PLmFVZLF07Xk6Oluxr8pmTRGflBIkPpB1Jnk');

    this.client.sendMessage(messageRequest, {}, (err, response) => {
      if (response == null) {
        console.log(err)
      } else {
        console.log(response.getStatus())
      }
    });
  }

  render() {
    return (
      <div>
        <label htmlFor="name">
          Name:&nbsp;
          <input
            type="text"
            id={'name'}
            placeholder={'Enter your name...'}
            value={this.state.name}
            onChange={e => this.setState({ name: e.target.value })}
          />
        </label>
        <ChatInput
          ws={this.ws}
          onSubmitMessage={messageString => this.submitMessage(messageString)}
        />
        {this.state.messages.map((message, index) =>
          <ChatMessage
            key={index}
            message={message.message}
            name={message.name}
          />,
        )}
      </div>
    )
  }
}

export default Chat