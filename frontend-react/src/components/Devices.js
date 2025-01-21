import axios from "axios";
import React, {useState, useEffect} from 'react';


function Devices() {

    const [devices, setDevices] =  useState([]);
    const [newDevice, setNewDevice] = useState({ name_device: '', address: '', energy_consumption: '', description: '' });
    const [editDevice, setEditDevice] = useState(null);

    const getToken = () => localStorage.getItem('token');

    // Axios instance with Authorization header
    const axiosInstance = axios.create({
        headers: {
            Authorization: `Bearer ${getToken()}`,
        },
    });

    useEffect(() => {
        const fetchDevices = async () => {
            try {
                //const response = await axios.get('http://localhost:8081/device/getDevices');
                const response = await axiosInstance.get('http://device.localhost/api/v1/auth/device/device/getDevices');
                setDevices(response.data);
            } catch (error) {
                console.error("Error:", error);
            }
        };
        fetchDevices();
    }, []);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNewDevice(prevState => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleNewDevice = async () => {
        try {
            //const response = await axios.post('http://localhost:8081/api/v1/auth/device/insertDevice', newDevice);
            const response = await axiosInstance.post('http://device.localhost/api/v1/auth/device/insertDevice', newDevice);
            setDevices([...devices, response.data]);
            setNewDevice({ name_device: '', address: '', energy_consumption: '', description: '' });
        } catch(error) {
            console.error("Error: ", error);
        }
    }

    const handleDeleteDevice = async (id) => {
        try {
            //await axios.delete(`http://localhost:8081/device/deleteDeviceById/${id}`);
            await axiosInstance.delete(`http://device.localhost/api/v1/auth/device/deleteDeviceById/${id}`);
            setDevices(devices.filter(device => device.id_device !== id));
        } catch (error) {
            console.error("Error: ", error);
        }
    }

    const handleEditDevice = (device) => {
        setEditDevice(device);
        setNewDevice({
            name_device:device.name_device,
            address:device.address,
            energy_consumption:device.energy_consumption,
            description:device.description
        })
    };

    const handleUpdateDevice = async () => {
        try {
            //await axios.put('http://localhost:8081/api/v1/auth/device/updateDevice', {
            await axiosInstance.put('http://device.localhost/api/v1/auth/device/updateDevice', {
                id_device: editDevice.id_device,
                ...newDevice,
            });

            setDevices(devices.map(device => (device.id_device === editDevice.id_device ? { ...device, ...newDevice } : device)));

            // Reset the newUser state and clear editUser
            setNewDevice({ name: '', address: '', energy_consumption: '', description: '' });
            setEditDevice(null);

        } catch (error) {
            console.error("Error:", error);
        }
    }

    return (
        <div>
        <table border="1" cellPadding="10" cellSpacing="0">
        <thead>
          <tr>
            <th>Name</th>
            <th>Address</th>
            <th>Energy consumption</th>
            <th>Description</th>
          </tr>
        </thead>
        <tbody>
          {devices.map(device => (
            <tr key={device.id_device}>
              <td>{device.name_device}</td>
              <td>{device.address}</td>
              <td>{device.energy_consumption}</td>
              <td>{device.description}</td>
              <td>
                <button onClick={() => handleEditDevice(device)}>Edit</button>
                <button onClick={() => handleDeleteDevice(device.id_device)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <h3>Add new device</h3>
      <div>
        <input
            type="text"
            name="name_device"
            placeholder="Name"
            value={newDevice.name_device}
            onChange={handleInputChange}
        />
        <input
            type="text"
            name="address"
            placeholder="Address"
            value={newDevice.address}
            onChange={handleInputChange}
        />
        <input
            type="number"
            name="energy_consumption"
            placeholder="Energy consumption"
            value={newDevice.energy_consumption}
            onChange={handleInputChange}
        />
        <input
            type="text"
            name="description"
            placeholder="Description"
            value={newDevice.description}
            onChange={handleInputChange}
        />
        <button onClick={editDevice ? handleUpdateDevice : handleNewDevice}>{editDevice ? 'Update Device' : 'Insert Device'}</button>
       </div>
    </div>
    );

}

export default Devices