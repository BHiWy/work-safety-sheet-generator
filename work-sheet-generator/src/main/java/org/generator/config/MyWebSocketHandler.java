package org.generator.config;

import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
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
     * @param session The established WebSocket session.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("Conexiune WebSocket deschisa: " + session.getId());
    }

    /**
     * Handles incoming text messages from a WebSocket session.
     * Logs the received message and echoes it back to the client.
     * @param session The WebSocket session that received the message.
     * @param  message The text message received.
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
     * @param session The closed WebSocket session.
     * @param status The status of the connection closure.
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session,@NonNull CloseStatus status) {
        System.out.println("Conexiune inchisa: " + session.getId());
    }
}
