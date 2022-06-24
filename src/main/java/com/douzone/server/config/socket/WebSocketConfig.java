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
public class WebSocketConfig implements WebSocketConfigurer {

	private final VehicleSocketHandler vehicleSocketHandler;
	private final WebSocketHandler webSocketHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(vehicleSocketHandler, "emp/vehicle/chat").setAllowedOrigins("*");
		registry.addHandler(webSocketHandler, "ws/room").setAllowedOrigins("*");
	}
}

