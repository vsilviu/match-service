package com.esolutions.demo.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ConnectedDeviceConfig {

    private static final Collection<String> deviceIds = Arrays.asList("a", "b", "c", "d");
    private static final Map<String, String> connectedDeviceMap = new HashMap<>();

    public ConnectedDeviceConfig() {
        throw new UnsupportedOperationException("Cannot instantiate class " + ConnectedDeviceConfig.class.getCanonicalName());
    }

    public static void persistDeviceConnection(String clientId, String sessionId) {
        if (deviceIds.contains(clientId) == Boolean.FALSE) {
            throw new IllegalStateException("Non-Existent Client ID!");
        }
        if (connectedDeviceMap.keySet().contains(clientId)) {
            throw new IllegalStateException("Cannot add the same client id twice!");
        }
        connectedDeviceMap.put(clientId, sessionId);
    }

}
