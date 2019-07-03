import '../styles/App.css'
import '../styles/Login.css'
import logo from './logo.svg'
import React from 'react'
import { Button, FormGroup, FormControl, FormLabel } from "react-bootstrap";
import ChatApp from './ChatApp'

const { LoginRequest, LoginResponse } = require('./chat_pb.js');
const { ChatServerClient } = require('./chat_grpc_web_pb.js');

class App extends React.Component {
  client = new ChatServerClient('http://localhost:9090', null, null);

  constructor(props) {
    super(props);

    this.state = { 
      username: '',
      password: '',
      token: '',
    };

    // Bind 'this' to event handlers. React ES6 does not do this by default
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleChange = event => {
    this.setState({
      [event.target.id]: event.target.value
     });
  }
   handleSubmit = event => {
    event.preventDefault();

    // login
     var request = new LoginRequest();
     request.setUsername(this.state.username);
     request.setPassword(this.state.password);

    this.client.login(request, {}, (err, response) => {
      if (response == null) {
         console.log(err)
       } else {
        this.setState({ submitted: true, username: this.state.username, password: this.state.password, token: response.getToken() });
       }
      });

    }

  render() {
    if (this.state.submitted) {
      // Form was submitted, now show the main App
      return (
        <ChatApp client = {this.client} username={this.state.username} token={this.state.token} />
      );
    }

    // Initial page load, show a simple login form
    return (
      <div className="Login">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1 className="App-title">React Chat App</h1>
        </header>

        <form onSubmit={this.handleSubmit}>
          <FormGroup controlId="username" size="lg">
            <FormLabel>UserName</FormLabel>
            <FormControl
              autoFocus
              size="lg"
              type="username"
              value={this.state.username}
              onChange={this.handleChange}
            />
          </FormGroup>
          <FormGroup controlId="password" size="lg">
            <FormLabel>Password</FormLabel>
            <FormControl
              size="lg"
              value={this.state.password}
              onChange={this.handleChange}
              type="password"
            />
          </FormGroup>
          <Button
            block
            size="lg"
            type="submit"
          >
            Login
          </Button>
        </form>
      </div>
    );
  }

}
App.defaultProps = {
};

export default App;