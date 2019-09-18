package com.esolutions.demo.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MqttSubscribeConfig {

    private final MqttClient mqttClient;

    @Autowired
    public MqttSubscribeConfig(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    @PostConstruct
    public void listen() throws MqttException {
        mqttClient.subscribe("/stats", (topic, msg) -> {
            try {
                String payload = new String(msg.getPayload());
                String clientId = payload.split("#")[0];
                String deviceMessage = payload.split("#")[1];
                System.out.println(String.format("[%s] : %s", clientId, deviceMessage));
                System.out.println();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
