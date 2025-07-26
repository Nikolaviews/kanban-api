package com.projectmanagement.kanbanapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // The endpoint that clients will connect to.
        // setAllowedOrigins("*") is for development; restrict in production.
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // "/topic" is the prefix for broadcasts (one-to-many).
        // All clients subscribed to a "/topic/..." destination will receive messages.
        registry.enableSimpleBroker("/topic");

        // "/app" is the prefix for messages bound for @MessageMapping-annotated methods.
        // When a client sends a message to "/app/...", it will be routed to a controller method.
        registry.setApplicationDestinationPrefixes("/app");
    }
}