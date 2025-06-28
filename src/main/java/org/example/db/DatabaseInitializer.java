// src/main/java/org/example/db/DatabaseInitializer.java
package org.example.db;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@ApplicationScoped
public class DatabaseInitializer {
    private final DataSource ds;

    public DatabaseInitializer(DataSource ds) {
        this.ds = ds;
    }

    void onStart(@Observes StartupEvent ev) {
        try (Connection c = ds.getConnection();
             Statement s = c.createStatement()) {
            s.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS search_entry (" +
                            "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "  query TEXT NOT NULL" +
                            ")");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}
