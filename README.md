# Energy Management System

This project implements an Energy Management System comprising a frontend and multiple backend microservices. The system provides functionalities for managing users, smart energy devices, and their associations. It also includes advanced features like real-time monitoring, communication, and role-based access control.

# The system follows a microservices architecture:

Frontend: User interface for clients and administrators.
Microservices:
User Management.
Device Management.
Monitoring and Communication.
Chat Service.

Middleware: RabbitMQ for asynchronous communication.

# Features

# User Management:

Perform CRUD operations on users (ID, name, role).
Role-based access restrictions with secure authentication.

# Device Management:
Manage smart energy devices (ID, description, address, max hourly energy consumption).
Assign devices to users with one-to-many mapping.

# Monitoring and Notifications:

Reads hourly energy consumption data from smart metering devices.
Stores and computes hourly energy values in the database.
Notifies users asynchronously if energy consumption exceeds predefined limits.
Displays historical energy consumption using charts.

# Communication and Chat:

Real-time chat functionality between users and administrators.
Notifications for message typing and reading statuses.
Admin can manage multiple chat sessions simultaneously.

# Integration and Asynchronous Communication:

Synchronization between device and monitoring databases using event-based messaging.
Simulated smart metering devices publish readings to RabbitMQ queues.
Real-time WebSocket notifications for clients.
