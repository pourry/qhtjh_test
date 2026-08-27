package com.example.spring_boot_mode.config.websocket;

import com.example.spring_boot_mode.websocket.ChatWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

/**
 * WebSocket 配置类
 * 注册 WebSocket 端点和配置参数
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private ChatWebSocketHandler chatWebSocketHandler;

    /**
     * 允许的 WebSocket Origin（逗号分隔）
     * 为了开发便利默认 "*"; 生产环境建议配置为前端实际域名以降低跨站 WebSocket 风险
     */
    @Value("${websocket.allowed-origins:*}")
    private String allowedOrigins;

    /**
     * 注册 WebSocket 处理器
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册聊天室 WebSocket 端点
        registry.addHandler(chatWebSocketHandler, "/ws/chat")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOrigins(allowedOrigins.split(","));
    }

    /**
     * 配置 WebSocket 容器参数
     */
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        // 设置最大文本消息缓冲区
        container.setMaxTextMessageBufferSize(8192);
        // 设置最大二进制消息缓冲区
        container.setMaxBinaryMessageBufferSize(8192);
        // 设置最大空闲超时时间 (毫秒)
        container.setMaxSessionIdleTimeout(300000L);  // 5分钟
        // 设置异步发送超时
        container.setAsyncSendTimeout(10000L);
        return container;
    }
}
