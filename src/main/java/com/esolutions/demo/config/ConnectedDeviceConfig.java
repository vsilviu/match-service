package com.esolutions.demo.config;

import java.util.*;

public class ConnectedDeviceConfig {

    private static final Collection<String> deviceIds = Arrays.asList("tennis-1", "tennis-2", "tennis-3", "tennis-4");
    private static final Map<String, String> connectedDeviceMap = new HashMap<>();

    public ConnectedDeviceConfig() {
        throw new UnsupportedOperationException("Cannot instantiate class " + ConnectedDeviceConfig.class.getCanonicalName());
    }

    public static void persistDeviceConnection(String clientId, String sessionId) {
        if (deviceIds.contains(clientId) == Boolean.FALSE) {
            throw new IllegalStateException("Non-Existent Client ID!");
        }
        if (connectedDeviceMap.keySet().contains(sessionId)) {
            throw new IllegalStateException("Cannot add the same session id twice!");
        }
        connectedDeviceMap.put(sessionId, clientId);
    }

    public static String getClientIdBySessionId(String sessionId) {
        return Objects.requireNonNull(connectedDeviceMap.get(sessionId), "No Client ID for session " + sessionId);
    }

}
