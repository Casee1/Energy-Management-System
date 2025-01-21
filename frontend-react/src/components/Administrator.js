import React from 'react';
import { useNavigate } from 'react-router-dom';


function Administrator () {

    const navigate = useNavigate();

    const handleBackToLogin = () => {
        navigate('/');
    }

    const handleUsersButton = () => {
        navigate('/admin/users');
    }

    const handleDevicesButton = () => {
        navigate('/admin/devices');
    }

    const handleChatButton = () => {
        navigate('/admin/chat');
    }

    return (
        <div>
          <h2>Welcome, Administrator!</h2>
          <p>This is the Administrator dashboard.</p>
          <button onClick={handleBackToLogin}>LogOut</button>
          <button onClick={handleUsersButton}>Users</button>
          <button onClick={handleDevicesButton}>Devices</button>
          <button onClick={handleChatButton}>Chat</button>
        </div>
    );
}

export default Administrator