package org.generator.config;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


/**
 * Handles WebSocket connections and messages.
 * Extends {@link TextWebSocketHandler} for processing text-based WebSocket communication.
 */
public class MyWebSocketHandler extends TextWebSocketHandler {

    /**
     * Called after a WebSocket connection has been successfully established.
     * Logs the ID of the newly opened session.
     * @param {WebSocketSession} session The established WebSocket session.
     * @throws Exception If an error occurs during the process.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Conexiune WebSocket deschisă: " + session.getId());
    }

    /**
     * Handles incoming text messages from a WebSocket session.
     * Logs the received message and echoes it back to the client.
     * @param {WebSocketSession} session The WebSocket session that received the message.
     * @param {TextMessage} message The text message received.
     * @throws Exception If an error occurs while handling the message or sending a response.
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Mesaj primit: " + message.getPayload());

        // Sends message to the client
        session.sendMessage(new TextMessage(message.getPayload()));
    }

    /**
     * Called after a WebSocket connection has been closed.
     * Logs the ID of the closed session.
     * @param {WebSocketSession} session The closed WebSocket session.
     * @param {org.springframework.web.socket.CloseStatus} status The status of the connection closure.
     * @throws Exception If an error occurs during the process.
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        System.out.println("Conexiune închisă: " + session.getId());
    }
}
