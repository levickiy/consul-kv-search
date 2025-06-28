package org.example.db;

import jakarta.enterprise.context.ApplicationScoped;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SearchHistoryDao {
    private static final String URL = "jdbc:sqlite:history.sqlite";

    public void insert(String query) {
        try (Connection c = DriverManager.getConnection(URL);
             Statement ddl = c.createStatement()) {
            // Створюємо таблицю, якщо її ще немає
            ddl.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS search_entry (" +
                            "  query TEXT NOT NULL" +
                            ")"
            );

            try (PreparedStatement p = c.prepareStatement(
                    "INSERT INTO search_entry(query) VALUES(?)")) {
                p.setString(1, query);
                p.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> findAll() {
        List<String> res = new ArrayList<>();
        try (Connection c = DriverManager.getConnection(URL);
             // тут таблиця вже мусить існувати
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT query FROM search_entry")) {
            while (rs.next()) {
                res.add(rs.getString("query"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
}
