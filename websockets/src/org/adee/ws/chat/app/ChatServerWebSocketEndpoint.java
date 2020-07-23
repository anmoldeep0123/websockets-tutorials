package org.adee.ws.chat.app;

import static org.adee.ws.chat.utils.Utils.broadcastBasicRemote;
import static org.adee.ws.chat.utils.Utils.processBinaryData;
import static org.adee.ws.chat.utils.Utils.sendBackBinaryData;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket/chat")
public class ChatServerWebSocketEndpoint {

	private Session session;
	private static List<ChatServerWebSocketEndpoint> chatClients = new CopyOnWriteArrayList<>();

	@OnOpen
	public void onOpen(Session session, EndpointConfig __) {
		System.out.println(this.getClass().getName() + " - " + "onOpen");
		this.session = session;
		chatClients.add(this);
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		System.out.println("Socket Closed : " + reason.getReasonPhrase());
		chatClients.remove(this);
	}

	@OnMessage
	public void onMessage(String message) {
		System.out.println(this.getClass().getName() + " - " + "onMessage - Text");
		broadcastBasicRemote(message, chatClients, this);
	}

	@OnMessage
	public void onMessage(ByteBuffer byteBuffer, boolean complete) {
		System.out.println(this.getClass().getName() + " - " + "onMessage - Binary");
		processBinaryData(byteBuffer, complete, chatClients, this);
		sendBackBinaryData(this, chatClients);
	}

	public Session getSession() {
		return session;
	}
}
