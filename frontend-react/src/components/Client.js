import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import React, { useState, useEffect, useRef } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { Line } from 'react-chartjs-2';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
} from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

function ClientDashboard() {
    const [devices, setDevices] = useState([]);
    const [alerts, setAlerts] = useState([]);
    const [graphData, setGraphData] = useState(null);
    const [selectedDevice, setSelectedDevice] = useState(null);
    const [date, setDate] = useState('');
    const navigate = useNavigate();
    const stompClient = useRef(null);

    const getToken = () => localStorage.getItem('token');

    // Axios instance with Authorization header
    const axiosInstance = axios.create({
        headers: {
            Authorization: `Bearer ${getToken()}`,
        },
    });

    useEffect(() => {
        // Fetch devices from the device microservice
        const fetchDevices = async () => {
            try {
                const id = localStorage.getItem('userId');
                if (!id) {
                    console.error("User ID not found");
                    return;
                }
                const response = await axiosInstance.get(`http://device.localhost/api/v1/auth/deviceuser/getDeviceUser/${id}`);
                //const response = await axios.get(`http://localhost:8081/api/v1/auth/device/getDeviceUser/${id}`);
                setDevices(response.data);
            } catch (error) {
                console.error("Error fetching devices:", error);
            }
        };

        fetchDevices();

        // Connect to WebSocket
        const connectWebSocket = () => {
            const socket = new SockJS('http://monitoring.localhost/monitoring/ws');
            //const socket = new SockJS(`http://localhost:8082/api/v1/auth/monitoring/ws`);
            stompClient.current = new Client({
                webSocketFactory: () => socket,
                onConnect: onConnected,
                onStompError: onError,
            });

            stompClient.current.activate();
        };

        const onConnected = () => {
            console.log('Connected to WebSocket');
            stompClient.current.subscribe('/topic/alerts', (message) => {
                console.log('Received message from WebSocket:', message.body);
                setAlerts((prevAlerts) => [...prevAlerts, message.body]);
            });
        };

        const onError = (frame) => {
            console.error('WebSocket error:', frame);
        };

        connectWebSocket();

        // Cleanup WebSocket on component unmount
        return () => {
            if (stompClient.current) {
                stompClient.current.deactivate(() => {
                    console.log('WebSocket disconnected');
                });
            }
        };
    }, []);

    const fetchGraphData = async (deviceId, date) => {
        try {
            const response = await axiosInstance.get(
                `http://monitoring.localhost/monitoring/${deviceId}/daily`,
                //`http://localhost:8082/api/v1/auth/monitoring/${deviceId}/daily`,
                { params: { date } }
            );
            const data = response.data;

            // Prepare data for the graph
            const labels = data.map((entry) => new Date(entry.timestamp * 1000).getHours());
            const values = data.map((entry) => entry.measurement);

            console.log(response.data);

            setGraphData({
                labels,
                datasets: [
                    {
                        label: 'Energy Consumption (kWh)',
                        data: values,
                        borderColor: 'rgba(75, 192, 192, 1)',
                        backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    },
                ],
            });
        } catch (error) {
            console.error("Error fetching graph data:", error);
        }
    };

    const handleDeviceSelection = (deviceId) => {
        setSelectedDevice(deviceId);
        if (date) {
            fetchGraphData(deviceId, date);
        }
    };

    const handleDateChange = (e) => {
        setDate(e.target.value);
        if (selectedDevice) {
            fetchGraphData(selectedDevice, e.target.value);
        }
    };

    const handleBackToLogin = () => {
        navigate('/');
    };

    const navigateToChat = () => {
        navigate('/client/chat')
    }

    return (
        <div>
            <h2>Welcome, Client!</h2>
            <p>This is the client dashboard.</p>
            <button onClick={handleBackToLogin}>LogOut</button>

            <h3>Device List</h3>
            <ul>
                {devices.map((device) => (
                    <li key={device.id_commontable} onClick={() => handleDeviceSelection(device.device.id_device)}>
                        Device ID: {device.device.id_device} - Device Name: {device.device.deviceName}
                    </li>
                ))}
            </ul>

            <h3>Select Date for Graph</h3>
            <input type="date" value={date} onChange={handleDateChange} />

            <h3>Energy Consumption Chart</h3>
            {graphData ? (
                <Line data={graphData} />
            ) : (
                <p>Select a device and date to view the graph.</p>
            )}

            <h3>Real-Time Alerts</h3>
            {alerts.length > 0 ? (
                <ul>
                    {alerts.map((alert, index) => (
                        <li key={index}>{alert}</li>
                    ))}
                </ul>
            ) : (
                <p>No alerts received yet.</p>
            )}

            <h3>Chat</h3>
            <button onClick={navigateToChat}>Chat</button>
        </div>
    );
}

export default ClientDashboard;
