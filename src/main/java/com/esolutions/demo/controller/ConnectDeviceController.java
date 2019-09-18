package com.esolutions.demo.controller;

import com.esolutions.demo.config.ConnectedDeviceConfig;
import com.esolutions.demo.model.Content;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ConnectDeviceController {

    @MessageMapping("/connect/{clientId}")
    @SendTo("/topic/connect")
    public Content register(@DestinationVariable("clientId") String clientId, @Header("simpSessionId") String sessionId) {
        //todo implement the mqtt logic here
        ConnectedDeviceConfig.persistDeviceConnection(clientId, sessionId);
        return new Content(String.format("[%s] : Successfully connected to client id %s", sessionId, clientId));
    }

}
