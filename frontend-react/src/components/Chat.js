import React, { useEffect, useRef, useState } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

const Chat = () => {
    const [messages, setMessages] = useState([]);
    const [message, setMessage] = useState("");
    const stompClient = useRef(null);

    const username = localStorage.getItem("username");
    const [activeAdmin, setActiveAdmin] = useState("admin");

    const isTyping = useRef(false);
    const typingTimeout = useRef(null);

    useEffect(() => {
        //const socket = new SockJS("http://localhost:8084/ws");
        const socket = new SockJS("http://chat.localhost:80/chat/ws");
        const client = new Client({
            webSocketFactory: () => socket,
            reconnectDelay: 5000,
            onConnect: () => {
                console.log(`Connected to WebSocket as ${username}`);

                // Abonare la mesajele private trimise acestui client
                client.subscribe(`/user/${username}/queue/private`, (message) => {
                    const parsedMessage = JSON.parse(message.body);
                    console.log("Received private message:", parsedMessage);

                    setMessages((prevMessages) => [...prevMessages, parsedMessage]);
                });
            },
            onStompError: (err) => {
                console.error("Error connecting to WebSocket:", err);
            },
        });

        client.activate();
        stompClient.current = client;

        return () => {
            if (stompClient.current) {
                stompClient.current.deactivate();
            }
        };
    }, [username]);

    const handleMessageSend = () => {
        if (!message.trim()) {
            alert("Please enter a message.");
            return;
        }

        const chatMessage = {
            content: message,
            sender: username,
            recipient: activeAdmin,
        };

        stompClient.current.publish({
            destination: "/app/sendMessage",
            body: JSON.stringify(chatMessage),
        });

        // Adăugăm mesajul trimis în conversația locală
        setMessages((prevMessages) => [...prevMessages, { ...chatMessage, sender: username }]);

        setMessage("");
    };

    const handleConversationClick = () => {
        // Trimiterea unui mesaj "seen by {client username}" către admin
        const seenMessage = {
            content: `Seen by ${username}`,
            sender: username, // Mesajul este trimis de client
            recipient: activeAdmin, // Mesajul este trimis adminului
        };

        // Publicăm mesajul de "seen" către admin
        stompClient.current.publish({
            destination: "/app/sendMessage",
            body: JSON.stringify(seenMessage),
        });
    };

    const handleTypingNotification = () => {
        if (!isTyping.current) {
            // Trimite notificarea "is typing" o singură dată
            const typingMessage = {
                content: `${username} is typing...`,
                sender: username,
                recipient: activeAdmin,
            };

            stompClient.current.publish({
                destination: "/app/sendMessage",
                body: JSON.stringify(typingMessage),
            });

            isTyping.current = true;

            // Setează un timeout pentru a reseta flag-ul după o perioadă de inactivitate
            if (typingTimeout.current) clearTimeout(typingTimeout.current);
            typingTimeout.current = setTimeout(() => {
                isTyping.current = false;
            }, 3000); // 3 secunde de inactivitate
        }
    };

    const isSystemMessage = (message) => {
        // Identificăm mesajele de tip "seen" sau "typing"
        return message.content.startsWith("Seen by") || message.content.endsWith("is typing...");
    };

    return (
        <div>
            <h1>Client Chat</h1>

            {/* Input pentru mesaj */}
            <div>
                <label>Message: </label>
                <input
                    type="text"
                    value={message}
                    onChange={(e) => {
                        setMessage(e.target.value);
                        handleTypingNotification(); // Trimite notificarea când clientul tastează
                    }}
                />
                <button onClick={handleMessageSend}>Send</button>
            </div>

            {/* Afișăm conversația cu admin */}
            <div
                style={{ border: "1px solid black", margin: "10px", padding: "10px" }}
                onClick={handleConversationClick} // Click pe conversație pentru a marca mesajul ca "seen"
            >
                <h3>Conversation with {activeAdmin}</h3>
                <ul>
                    {messages.map((msg, idx) => (
                        <li key={idx}>
                            {/* Afișează mesajul fără sender dacă este de tip system */}
                            {isSystemMessage(msg) ? (
                                <span>{msg.content}</span>
                            ) : (
                                <span>
                                    <strong>{msg.sender}:</strong> {msg.content}
                                </span>
                            )}
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default Chat;
