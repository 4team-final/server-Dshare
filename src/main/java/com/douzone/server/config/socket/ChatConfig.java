package com.douzone.server.config.socket;

import com.douzone.server.config.socket.vehicle.VehicleSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class ChatConfig implements WebSocketConfigurer {

	private final VehicleSocketHandler vehicleSocketHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(vehicleSocketHandler, "emp/vehicle/chat").setAllowedOrigins("*");
	}

}

