package org.example.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.example.model.KvEntry;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@ApplicationScoped
public class ConsulClient {
    @ConfigProperty(name="consul.host")
    String consulHost;
    @ConfigProperty(name="consul.port")
    int consulPort;
    @Inject
    ObjectMapper mapper;

    public List<KvEntry> fetchAll() {
        try {
            String endpoint = "http://" + consulHost + ":" + consulPort + "/v1/kv/?recurse=true";
            HttpURLConnection conn = (HttpURLConnection)new URL(endpoint).openConnection();
            conn.setRequestMethod("GET");
            try (InputStream in = conn.getInputStream()) {
                return mapper.readValue(in, new TypeReference<List<KvEntry>>() {});
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
