/*********************************************************************
 * Copyright 2020 Java Code Geeks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.adee.ws.chat.coders;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonException;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import org.adee.ws.chat.model.Message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageDecoder implements Decoder.Text<Message> {

	@Override
	public void destroy() {
	}

	@Override
	public void init(EndpointConfig arg0) {
	}

	@Override
	public Message decode(String message) throws DecodeException {
		Message _message = null;
		if (willDecode(message)) {
			try {
				_message = new ObjectMapper().readValue(message, Message.class);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		return _message;
	}

	@Override
	public boolean willDecode(String message) {
		try {
			Json.createReader(new StringReader(message));
			return true;
		} catch (JsonException jEx) {
			jEx.printStackTrace();
			return false;
		}
	}
}
