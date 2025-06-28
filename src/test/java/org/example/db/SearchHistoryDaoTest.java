package org.example.db;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

public class SearchHistoryDaoTest {

    SearchHistoryDao dao = new SearchHistoryDao();
    private static final String DB_FILE = "history.sqlite";

    @BeforeAll
    static void loadDriver() throws Exception {
        Class.forName("org.sqlite.JDBC");
    }

    @BeforeEach
    void resetDb() {
        // видаляємо попередній файл
        File f = new File(DB_FILE);
        if (f.exists()) {
            f.delete();
        }
        // створюємо таблицю заново
        dao.insert("init"); // викликає створення БД + таблиці через SQL DDL налаштування
    }

    @Test
    void testInsertAndFindAll() {
        dao.insert("first");
        dao.insert("second");

        List<String> all = dao.findAll();
        assertTrue(all.contains("first"));
        assertTrue(all.contains("second"));
    }

    @Test
    void testDeleteAll() {
        dao.insert("first");
        dao.insert("second");
        assertFalse(dao.findAll().isEmpty());

        dao.deleteAll();
        List<String> all = dao.findAll();
        assertTrue(all.isEmpty(), "History should be empty after deleteAll()");
    }
}
