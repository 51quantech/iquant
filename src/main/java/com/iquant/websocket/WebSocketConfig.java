package com.iquant.websocket;

import com.iquant.websocket.handler.MyHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

public class WebSocketConfig implements WebSocketConfigurer {

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "/myHandler");
    }

    @Bean
    public WebSocketHandler myHandler() {
        return new MyHandler();
    }
    // public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
    // {
    // registry.addHandler(new MyHandler(), "/myHandler").addInterceptors(
    // new HttpSessionHandshakeInterceptor());
    // }
    //
    // @Bean
    // public ServletServerContainerFactoryBean createWebSocketContainer() {
    // ServletServerContainerFactoryBean container = new
    // ServletServerContainerFactoryBean();
    // container.setMaxTextMessageBufferSize(8192);
    // container.setMaxBinaryMessageBufferSize(8192);
    // return container;
    // }

}
