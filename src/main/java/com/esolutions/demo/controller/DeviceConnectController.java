package com.esolutions.demo.controller;

import com.esolutions.demo.config.ConnectedDeviceConfig;
import com.esolutions.demo.model.Content;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class DeviceConnectController {

    private final MqttClient mqttClient;

    @Autowired
    public DeviceConnectController(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    @MessageMapping("/connect/{clientId}")
    @SendTo("/topic/connect")
    public Content register(@DestinationVariable("clientId") String clientId, @Header("simpSessionId") String sessionId) throws MqttException {
        mqttClient.publish("/match/connect/" + clientId, new MqttMessage(sessionId.getBytes()));
        ConnectedDeviceConfig.persistDeviceConnection(clientId, sessionId);
        return new Content(String.format("[%s] : Successfully connected to client id %s", sessionId, clientId));
    }

}
