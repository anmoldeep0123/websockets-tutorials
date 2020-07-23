package org.adee.ws.chat.app;

import static org.adee.ws.chat.utils.Utils.broadcastBasicRemote;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket/chat/{room-name}")
public class ChatRoomsServerWebSocketEndpoint {

	private Session session;
	private static Map<String, List<ChatRoomsServerWebSocketEndpoint>> chatRooms = new HashMap<>();

	@OnError
	public void onError(Session session, Throwable t, @PathParam("room-name") String roomName) {
		try {
			session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		t.printStackTrace();
	}

	@OnOpen
	public void onOpen(Session session, EndpointConfig __, @PathParam("room-name") String roomName) {
		System.out.println(this.getClass().getName() + " - " + "onOpen - roomName " + roomName);
		this.session = session;
		addClientToChatRoom(roomName);
	}

	@OnClose
	public void onClose(Session session, CloseReason reason, @PathParam("room-name") String roomName) throws Exception {
		System.out.println("Socket Closed : " + reason.getReasonPhrase());
		removeClientFromChatRoom(roomName);
	}

	@OnMessage
	public void onMessage(String message, @PathParam("room-name") String roomName) {
		System.out.println(this.getClass().getName() + " - " + "onMessage - roomName " + roomName + " Text " + message);
		broadcastBasicRemote(message, chatRooms.get(roomName), this);
	}

	public Session getSession() {
		return session;
	}

	private void removeClientFromChatRoom(String roomName) throws Exception {
		System.out.println(this.getClass().getName() + " - " + "removeClientFromChatRoom " + roomName);
		List<ChatRoomsServerWebSocketEndpoint> chatRoomClients = chatRooms.get(roomName);
		if (chatRoomClients == null) {
			throw new Exception("Invalid Chat Room");
		}
		chatRoomClients.remove(this);
	}

	private void addClientToChatRoom(String roomName) {
		List<ChatRoomsServerWebSocketEndpoint> chatRoomClients = chatRooms.get(roomName);
		if (chatRoomClients == null) {
			chatRoomClients = new CopyOnWriteArrayList<>();
			chatRooms.put(roomName, chatRoomClients);
		}
		chatRoomClients.add(this);
		System.out.println("ChatRooms Are " + chatRooms);
	}
}
