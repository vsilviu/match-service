package com.esolutions.demo.controller;

import com.esolutions.demo.config.ConnectedDeviceConfig;
import com.esolutions.demo.model.Content;
import com.esolutions.demo.model.DeviceAction;
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
public class DeviceCommandController {

    private final MqttClient mqttClient;

    @Autowired
    public DeviceCommandController(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    @MessageMapping("/command/{action}")
    @SendTo("/topic/command")
    public Content sendCommand(@DestinationVariable DeviceAction action, @Header("simpSessionId") String sessionId) throws MqttException {
        String clientId = ConnectedDeviceConfig.getClientIdBySessionId(sessionId);
        mqttClient.publish(String.format("/match/command/%s", clientId), new MqttMessage((action.name() + "#" + sessionId).getBytes()));
        return new Content(String.format("[%s] : Successfully sent command [%s] to client id [%s]", sessionId, action, clientId));
    }

}
