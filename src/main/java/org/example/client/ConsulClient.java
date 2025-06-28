package org.example.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.example.model.KvEntry;
import org.jboss.logging.Logger;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@ApplicationScoped
public class ConsulClient {
    private static final Logger LOG = Logger.getLogger(ConsulClient.class);

    @ConfigProperty(name="consul.host")
    String consulHost;
    @ConfigProperty(name="consul.port")
    int consulPort;
    @Inject
    ObjectMapper mapper;

    public List<KvEntry> fetchAll() {
        String endpoint = "http://" + consulHost + ":" + consulPort + "/v1/kv/?recurse=true";
        LOG.infof("Fetching all KV entries from Consul at %s", endpoint);
        try {
            HttpURLConnection conn = (HttpURLConnection)new URL(endpoint).openConnection();
            conn.setRequestMethod("GET");
            try (InputStream in = conn.getInputStream()) {
                List<KvEntry> entries = mapper.readValue(in, new TypeReference<>() {
                });
                LOG.infof("Fetched %d entries from Consul", entries.size());
                return entries;
            }
        } catch (Exception e) {
            LOG.error("Failed to fetch KV entries from Consul", e);
            throw new RuntimeException(e);
        }
    }

    public String getBaseUrl() {
        // Adjust datacenter (dc1) if needed
        return "http://" + consulHost + ":" + consulPort + "/ui/dc1/kv/";
    }
}