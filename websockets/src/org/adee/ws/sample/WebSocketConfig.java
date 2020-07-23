package org.adee.ws.sample;

import java.util.HashSet;
import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

import org.adee.ws.chat.app.ChatRoomsServerWebSocketEndpoint;
import org.adee.ws.chat.app.ChatServerWebSocketEndpoint;
import org.adee.ws.chat.app.ChatServerWebSocketJSONEndpoint;

/*
 * Config for Old School Server EndPoint & Annotation based WS Endpoint
 * Either remove this class or add each annotation based endPoint in the 
 * below method getAnnotatedEndpointClasses
 */
public class WebSocketConfig implements ServerApplicationConfig {

	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> set) {
		Set<Class<?>> result = new HashSet<>();
		result.add(AnnotationConfig.class);
		result.add(ChatServerWebSocketEndpoint.class);
		result.add(ChatServerWebSocketJSONEndpoint.class);
		result.add(ChatRoomsServerWebSocketEndpoint.class);
		return result;
	}

	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> set) {
		Set<ServerEndpointConfig> result = new HashSet<>();
		if (set.contains(SampleWsEndpoint.class)) {
			result.add(ServerEndpointConfig.Builder.create(SampleWsEndpoint.class, "/websocket/echo").build());
		}
		return result;
	}
}
