package org.adee.ws.sample;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;

/*
 * Old school Open Handler for WS
 */
public class SampleWsEndpoint extends Endpoint {

	@Override
	public void onOpen(Session session, EndpointConfig arg1) {
		Basic basic = session.getBasicRemote();
		session.addMessageHandler(new WSConfigMessageHandler(basic));
	}
}
