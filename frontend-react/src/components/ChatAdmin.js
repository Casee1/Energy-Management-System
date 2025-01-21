import React, { useEffect, useRef, useState } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

const ChatAdmin = () => {
    const [conversations, setConversations] = useState({}); // Stocăm conversațiile per client
    const [activeClient, setActiveClient] = useState(null); // Clientul activ la care răspundem
    const [message, setMessage] = useState("");
    const [isTyping, setIsTyping] = useState(false); // Flag pentru a verifica dacă notificarea de typing a fost trimisă
    const stompClient = useRef(null);

    const adminUsername = "admin"; // Presupunem că admin-ul este întotdeauna "admin", dar se poate modifica

    useEffect(() => {
        //const socket = new SockJS("http://localhost:8084/ws");
        const socket = new SockJS("http://chat.localhost:80/chat/ws");
        const client = new Client({
            webSocketFactory: () => socket,
            reconnectDelay: 5000,
            onConnect: () => {
                console.log(`Connected to WebSocket as ${adminUsername}`);

                // Abonare la mesajele private trimise acestui admin
                client.subscribe(`/user/${adminUsername}/queue/private`, (message) => {
                    const parsedMessage = JSON.parse(message.body);
                    console.log("Received private message:", parsedMessage);

                    // Adăugăm mesajul în conversația clientului corespunzător
                    setConversations((prevConversations) => ({
                        ...prevConversations,
                        [parsedMessage.sender]: [
                            ...(prevConversations[parsedMessage.sender] || []),
                            parsedMessage,
                        ],
                    }));

                    // Setăm clientul activ pe baza ultimului mesaj primit
                    setActiveClient(parsedMessage.sender);
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
    }, []);

    const handleConversationClick = (client) => {
        setActiveClient(client);

        // Trimitem mesajul "seen by admin" clientului
        const seenMessage = {
            content: `Seen by admin`,
            sender: adminUsername,
            recipient: client, // Mesajul este trimis clientului activ
        };

        // Publicăm mesajul de "seen" către clientul activ
        stompClient.current.publish({
            destination: "/app/sendMessage",
            body: JSON.stringify(seenMessage),
        });
    };

    const handleTypingNotification = (client) => {
        if (!isTyping) {
            setIsTyping(true);
            const typingMessage = {
                content: `${adminUsername} is typing...`,
                sender: adminUsername,
                recipient: client,
            };

            stompClient.current.publish({
                destination: "/app/sendMessage",
                body: JSON.stringify(typingMessage),
            });


            setTimeout(() => setIsTyping(false), 3000);
        }
    };

    const handleMessageSend = () => {
        if (!activeClient || !message.trim()) {
            alert("Please specify a client and a message.");
            return;
        }

        const chatMessage = {
            content: message,
            sender: adminUsername,
            recipient: activeClient, // Mesajul este trimis clientului activ
        };

        // Publicăm mesajul către clientul activ
        stompClient.current.publish({
            destination: "/app/sendMessage",
            body: JSON.stringify(chatMessage),
        });

        // Adăugăm mesajul trimis în conversația clientului activ
        setConversations((prevConversations) => ({
            ...prevConversations,
            [activeClient]: [
                ...(prevConversations[activeClient] || []),
                { ...chatMessage, sender: adminUsername },
            ],
        }));

        setMessage(""); // Resetăm input-ul de mesaj
    };

    const isSystemMessage = (message) => {
      // Identificăm mesajele de tip "seen" sau "typing"
      return message.content.startsWith("Seen by") || message.content.endsWith("is typing...");
    };

    return (
        <div>
            <h1>Admin Chat</h1>

            {/* Afișăm lista de clienți */}
            <div>
                {Object.keys(conversations).map((client, index) => (
                    <div
                        key={index}
                        style={{
                            border: "1px solid black",
                            margin: "10px",
                            padding: "10px",
                            backgroundColor: activeClient === client ? "#f0f0f0" : "white",
                            cursor: "pointer",
                        }}
                        onClick={() => handleConversationClick(client)} // Schimbăm clientul activ
                    >
                        <h3>Conversation with {client}</h3>
                        <ul>
                            {conversations[client].map((msg, idx) => (
                                <li key={idx}>
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
                ))}
            </div>

            {/* Input pentru mesaj, dacă avem un client activ */}
            {activeClient && (
                <div>
                    <h3>Reply to {activeClient}</h3>
                    <label>Message: </label>
                    <input
                        type="text"
                        value={message}
                        onChange={(e) => {
                            setMessage(e.target.value);
                            handleTypingNotification(activeClient); // Trimitem notificarea de typing
                        }}
                    />
                    <button onClick={handleMessageSend}>Send</button>
                </div>
            )}
        </div>
    );
};

export default ChatAdmin;
