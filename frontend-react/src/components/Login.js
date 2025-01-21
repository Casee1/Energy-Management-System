import axios from 'axios';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');

  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post('http://user.localhost/api/v1/auth/authenticate', {
      //const response = await axios.post('http://localhost:8080/api/v1/auth/authenticate', {
        username: username,
        password: password,
      });

      if (response.data.token) {
        
        localStorage.setItem('token', response.data.token);
        

        if (response.data.role) {
          localStorage.setItem('role', response.data.role);
        }

        // Redirect based on user role
        if (response.data.role === 'ADMINISTRATOR') {
          navigate('/admin');
        } else if (response.data.role === 'CLIENT') {
          localStorage.setItem('username', username); // Save username in localStorage
          navigate('/client');
        } else {
          setMessage('Unknown user role');
        }
      } else {
        setMessage('Invalid login response');
      }
    } catch (error) {
      console.error(error);
      setMessage('Invalid username or password');
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <h2>Login</h2>
        <div className="form-group">
          <label>Username</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className="form-control"
            placeholder="Enter username"
          />
        </div>
        <div className="form-group">
          <label>Password</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="form-control"
            placeholder="Enter password"
          />
        </div>
        {message && <p>{message}</p>}
        <button className="btn btn-primary" onClick={handleLogin}>
          Log In
        </button>
      </header>
    </div>
  );
}

export default Login;
