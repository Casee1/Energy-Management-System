import React, { useState, useEffect } from 'react';
import axios from 'axios';

function Users() {
    const [users, setUsers] = useState([]);
    const [newUser, setNewUser] = useState({ name: '', address: '', age: '', role: '', username: '', password: '' });
    const [editUser, setEditUser] = useState(null);

    const getToken = () => localStorage.getItem('token');

    // Axios instance with Authorization header
    const axiosInstance = axios.create({
        headers: {
            Authorization: `Bearer ${getToken()}`,
        },
    });

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                //const response = await axios.get('http://localhost:8080/api/v1/auth/user/getUser');
                const response = await axiosInstance.get('http://user.localhost/api/v1/auth/user/getUser')
                setUsers(response.data);
            } catch (error) {
                console.error("Error:", error);
            }
        };
        fetchUsers();
    }, []);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNewUser((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleNewUser = async () => {
        try {
            //const response = await axios.post('http://localhost:8080/api/v1/auth/user/insertUser', newUser);
            const response = await axiosInstance.post('http://user.localhost/api/v1/auth/user/insertUser', newUser);
            setUsers([...users, response.data]);
            setNewUser({ name: '', address: '', age: '', role: '', username: '', password: '' });
        } catch (error) {
            console.error("Error: ", error);
        }
    };

    const handleDeleteUser = async (id) => {
        try {
            //await axios.delete(`http://localhost:8080/api/v1/auth/user/deleteUserById/${id}`);
            await axiosInstance.delete(`http://user.localhost/api/v1/auth/user/deleteUserById/${id}`);
            setUsers(users.filter((user) => user.id !== id));
        } catch (error) {
            console.error("Error: ", error);
        }
    };

    const handleEditUser = (user) => {
        setEditUser(user);
        setNewUser({
            name: user.name,
            address: user.address,
            age: user.age,
            role: user.role,
            username: user.username || '', // Include existing username
            password: '', // Reset password to be re-entered for security
        });
    };

    const handleUpdateUser = async () => {
        try {
            //await axios.put('http://localhost:8080/api/v1/auth/user/updateUser', {
            await axiosInstance.put('http://user.localhost/api/v1/auth/user/updateUser', {
                id: editUser.id,
                ...newUser,
            });

            // Update the user in the local state
            setUsers(users.map((user) => (user.id === editUser.id ? { ...user, ...newUser } : user)));

            // Clear form and editing state
            setNewUser({ name: '', address: '', age: '', role: '', username: '', password: '' });
            setEditUser(null);
        } catch (error) {
            console.error("Error: ", error);
        }
    };

    return (
        <div>
            <table border="1" cellPadding="10" cellSpacing="0">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Address</th>
                        <th>Age</th>
                        <th>Role</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map((user) => (
                        <tr key={user.id}>
                            <td>{user.name}</td>
                            <td>{user.address}</td>
                            <td>{user.age}</td>
                            <td>{user.role}</td>
                            <td>
                                <button onClick={() => handleEditUser(user)}>Edit</button>
                                <button onClick={() => handleDeleteUser(user.id)}>Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <h3>{editUser ? 'Update User' : 'Add New User'}</h3>
            <div>
                <input
                    type="text"
                    name="name"
                    placeholder="Name"
                    value={newUser.name}
                    onChange={handleInputChange}
                />
                <input
                    type="text"
                    name="address"
                    placeholder="Address"
                    value={newUser.address}
                    onChange={handleInputChange}
                />
                <input
                    type="number"
                    name="age"
                    placeholder="Age"
                    value={newUser.age}
                    onChange={handleInputChange}
                />
                <input
                    type="text"
                    name="role"
                    placeholder="Role"
                    value={newUser.role}
                    onChange={handleInputChange}
                />
                <input
                    type="text"
                    name="username"
                    placeholder="Username"
                    value={newUser.username}
                    onChange={handleInputChange}
                />
                <input
                    type="password"
                    name="password"
                    placeholder="Password"
                    value={newUser.password}
                    onChange={handleInputChange}
                />
                <button onClick={editUser ? handleUpdateUser : handleNewUser}>
                    {editUser ? 'Update User' : 'Insert User'}
                </button>
            </div>
        </div>
    );
}

export default Users;
