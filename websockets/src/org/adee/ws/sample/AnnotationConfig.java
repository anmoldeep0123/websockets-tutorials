package org.adee.ws.sample;

import javax.websocket.EndpointConfig;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

//wscat -c "ws://localhost:8080/websockets/websocket/annotation"

/*
 * Annotation based WS endpoint
 * Open Handler
 */
@ServerEndpoint("/websocket/annotation")
public class AnnotationConfig {

	@OnOpen
	public void onOpen(Session session, EndpointConfig arg1) {
		Basic basic = session.getBasicRemote();
		session.addMessageHandler(new AnnotationConfigMessageHandler(basic));
	}
}
