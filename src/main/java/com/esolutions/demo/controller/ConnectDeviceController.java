package com.esolutions.demo.controller;

import com.esolutions.demo.config.ConnectedDeviceConfig;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ConnectDeviceController {

    @MessageMapping("/connect/{clientId}")
    @SendTo("/topic/connect")
    public String register(@DestinationVariable("clientId") String clientId, @Header("simpSessionId") String sessionId) {
        //todo implement the mqtt logic here
        ConnectedDeviceConfig.persistDeviceConnection(clientId, sessionId);
        return String.format("Successfully connected to client id %s", clientId);
    }

}
