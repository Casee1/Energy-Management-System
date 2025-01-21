import './App.css';
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './components/Login';
import Administrator from './components/Administrator';
import ClientDashboard from './components/Client';
import Users from './components/Users';
import Devices from './components/Devices';
import { AuthProvider } from './components/AuthContext';
import Chat from './components/Chat';
import ChatAdmin from './components/ChatAdmin';

function App() {
  return (
    <AuthProvider> {/* Wrap everything in AuthProvider */}
      <Router>
        <div className="App">
          <Routes>
            <Route path='/' element={<Login />} />
            <Route path='/admin' element={<Administrator />} />
            <Route path='/client' element={<ClientDashboard />} />
            <Route path='/admin/users' element={<Users />} />
            <Route path='/admin/devices' element={<Devices />} />
            <Route path='/client/chat' element={<Chat />} />
            <Route path='/admin/chat' element={<ChatAdmin />} />
          </Routes>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
