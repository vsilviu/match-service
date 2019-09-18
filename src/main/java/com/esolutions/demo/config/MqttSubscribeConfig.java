package com.esolutions.demo.config;

import com.esolutions.demo.model.Content;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MqttSubscribeConfig {

    private final MqttClient mqttClient;
    private final SimpMessagingTemplate webSocket;

    @Autowired
    public MqttSubscribeConfig(MqttClient mqttClient, SimpMessagingTemplate webSocket) {
        this.mqttClient = mqttClient;
        this.webSocket = webSocket;
    }

    @PostConstruct
    public void listen() throws MqttException {
        mqttClient.subscribe("/stats", (topic, msg) -> {
            try {
                String payload = new String(msg.getPayload());
                String clientId = payload.split("#")[0];
                String deviceMessage = payload.split("#")[1];
                System.out.println(String.format("[%s] : %s", clientId, deviceMessage));

                webSocket.convertAndSend("/topic/stats", new Content(String.format("[%s] : %s", clientId, deviceMessage)));

            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
