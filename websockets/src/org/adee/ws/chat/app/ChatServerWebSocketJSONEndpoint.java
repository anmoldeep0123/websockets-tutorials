package org.adee.ws.chat.app;

import static org.adee.ws.chat.utils.Utils.broadcastBasicRemote;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.adee.ws.chat.coders.MessageDecoder;
import org.adee.ws.chat.coders.MessageEncoder;
import org.adee.ws.chat.model.Message;

@ServerEndpoint(value = "/websocket/json", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatServerWebSocketJSONEndpoint {

	private Session session;
	private static List<ChatServerWebSocketJSONEndpoint> jsonChatClients = new CopyOnWriteArrayList<>();

	@OnOpen
	public void onOpen(Session session, EndpointConfig __) {
		System.out.println(this.getClass().getName() + " - " + "onOpen");
		this.session = session;
		jsonChatClients.add(this);
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		System.out.println("Socket Closed : " + reason.getReasonPhrase());
		jsonChatClients.remove(this);
	}

	@OnMessage
	public void onMessage(Message message) {
		System.out.println(this.getClass().getName() + " - " + "onMessage - Text");
		broadcastBasicRemote(message, jsonChatClients, this);
	}

	public Session getSession() {
		return session;
	}
}
