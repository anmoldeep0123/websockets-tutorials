package org.adee.ws.chat.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import javax.websocket.EncodeException;
import javax.websocket.SendHandler;
import javax.websocket.SendResult;

import org.adee.ws.chat.app.ChatRoomsServerWebSocketEndpoint;
import org.adee.ws.chat.app.ChatServerWebSocketEndpoint;
import org.adee.ws.chat.app.ChatServerWebSocketJSONEndpoint;
import org.adee.ws.chat.model.Message;

public class Utils {

	private static ByteArrayOutputStream baos = new ByteArrayOutputStream();

	public static void processBinaryData(ByteBuffer byteBuffer, boolean complete,
			List<ChatServerWebSocketEndpoint> endpointInstance,
			ChatServerWebSocketEndpoint chatServerWebSocketEndpoint) {
		try {
			baos.write(byteBuffer.array());
			if (complete) {
				storeFile();
				broadcastBasicRemote("{\"message\" : \"File Received\"}", endpointInstance,
						chatServerWebSocketEndpoint);
			}
		} catch (IOException e) {
		}
	}

	public static void sendBackBinaryData(ChatServerWebSocketEndpoint curChatClient,
			List<ChatServerWebSocketEndpoint> chatClients) {
		ByteBuffer sendBuffer = ByteBuffer.allocate(baos.toByteArray().length);
		sendBuffer.put(baos.toByteArray());
		sendBuffer.rewind();
		broadcastAsyncRemote(sendBuffer, chatClients);
	}

	public static void broadcastBasicRemote(String message, List<ChatServerWebSocketEndpoint> chatClients,
			ChatServerWebSocketEndpoint curChatClient) {
		chatClients.forEach(client -> {
			try {
				client.getSession().getBasicRemote().sendText(message);
			} catch (IOException e) {
				chatClients.remove(curChatClient);
				try {
					client.getSession().close();
				} catch (IOException e1) {
				}
			}
		});
	}

	public static void broadcastBasicRemote(String message, List<ChatRoomsServerWebSocketEndpoint> chatRoomClients,
			ChatRoomsServerWebSocketEndpoint curChatRoomClient) {
		chatRoomClients.forEach(client -> {
			try {
				client.getSession().getBasicRemote().sendText(message);
			} catch (IOException e) {
				chatRoomClients.remove(curChatRoomClient);
				try {
					client.getSession().close();
				} catch (IOException e1) {
				}
			}
		});
	}

	public static void broadcastBasicRemote(Message message, List<ChatServerWebSocketJSONEndpoint> jsonChatClients,
			ChatServerWebSocketJSONEndpoint curJsonChatClient) {
		jsonChatClients.forEach(client -> {
			try {
				client.getSession().getBasicRemote().sendObject(message);
			} catch (IOException | EncodeException e) {
				jsonChatClients.remove(curJsonChatClient);
				try {
					client.getSession().close();
				} catch (IOException e1) {
				}
			}
		});
	}

	public static void broadcastAsyncRemote(ByteBuffer data, List<ChatServerWebSocketEndpoint> chatClients) {
		chatClients.forEach(client -> {
			client.getSession().getAsyncRemote().sendBinary(data, new SendHandler() {

				@Override
				public void onResult(SendResult sendResult) {
					System.out.println(this.getClass().getName() + " - " + sendResult.isOK());
				}
			});
		});
	}

	public static void storeFile() {
		try (FileOutputStream fos = new FileOutputStream(new File("/tmp/img.jpeg"))) {
			fos.write(baos.toByteArray());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
