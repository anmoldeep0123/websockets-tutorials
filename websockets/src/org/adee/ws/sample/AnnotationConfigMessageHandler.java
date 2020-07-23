package org.adee.ws.sample;

import java.io.IOException;

import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint;

/*
 * Annotation WS endpoint onMessage Handler
 */
public class AnnotationConfigMessageHandler implements MessageHandler.Whole<String> {

	private RemoteEndpoint.Basic basic;

	AnnotationConfigMessageHandler(RemoteEndpoint.Basic basic) {
		this.basic = basic;
	}

	@Override
	public void onMessage(String message) {
		if(this.basic != null) {
			try {
				this.basic.sendText("Annotated -> " + message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String toString() {
		return "EchoMessageHandler [basic=" + basic + "]";
	}	
}
