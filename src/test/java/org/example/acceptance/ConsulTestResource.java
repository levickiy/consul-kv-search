package org.example.acceptance;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ConsulTestResource implements QuarkusTestResourceLifecycleManager {
    private GenericContainer<?> consul;

    @Override
    public Map<String, String> start() {
        consul = new GenericContainer<>(DockerImageName.parse("hashicorp/consul:1.17"))
                .withExposedPorts(8500);
        consul.start();

        String host = consul.getHost();
        int port = consul.getMappedPort(8500);

        putKv(host, port, "foo", "bar");
        putKv(host, port, "hello", "world");

        Map<String, String> props = new HashMap<>();
        props.put("CONSUL_HOST", host);
        props.put("CONSUL_PORT", Integer.toString(port));
        return props;
    }

    private static void putKv(String host, int port, String key, String value) {
        try {
            URL url = new URL("http://" + host + ":" + port + "/v1/kv/" + key);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(value.getBytes(StandardCharsets.UTF_8));
            }
            conn.getInputStream().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        if (consul != null) {
            consul.stop();
        }
    }
}
