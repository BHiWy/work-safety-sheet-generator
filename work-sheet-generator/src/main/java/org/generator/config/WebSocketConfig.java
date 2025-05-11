package org.generator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Configuration class for WebSocket settings.
 * Enables WebSocket support for the application and registers WebSocket handlers.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    /**
     * Registers WebSocket handlers to specific endpoints.
     * @param registry The registry for configuring WebSocket handlers.
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "/your-websocket-endpoint").setAllowedOrigins("*");
    }

    /**
     * Creates and returns an instance of the {@link MyWebSocketHandler}.
     * This bean will be used to handle WebSocket connections and messages.
     * @return {WebSocketHandler} An instance of the `MyWebSocketHandler`.
     */
    @Bean
    public WebSocketHandler myHandler() {
        return new MyWebSocketHandler();
    }
}
