package com.qyy.jyshop.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer{

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //注册一个STOMP的endpoint,并指定使用SockJS协议
        registry.addEndpoint("/ricky-websocket").setAllowedOrigins("*").withSockJS();
    }

    @Override 
    public void configureMessageBroker(MessageBrokerRegistry config) {
        
        config.enableSimpleBroker("/topic");// /users 默认通知
//        config.setApplicationDestinationPrefixes("/app");
    }
    
    @Bean
    public SocketSessionRegistry SocketSessionRegistry(){
        return new SocketSessionRegistry();
    }
    
    @Bean
    public STOMPConnectEventListener STOMPConnectEventListener(){
        return new STOMPConnectEventListener();
    }
}
