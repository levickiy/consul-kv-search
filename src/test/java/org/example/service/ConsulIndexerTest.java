package org.example.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;

import jakarta.inject.Inject;
import org.example.client.ConsulClient;
import org.example.db.SearchHistoryDao;
import org.example.model.KvEntry;
import org.example.model.KvMatch;
import org.example.util.FuzzySearchEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
public class ConsulIndexerTest {

    // ...
    @InjectMock
    ConsulClient consulClient;

    @Inject
    FuzzySearchEngine engine;

    @Inject
    SearchHistoryDao historyDao;

    @Inject
    ConsulIndexer indexer;

    @BeforeEach
    void setup() {
        // замокаєм fetchAll()
        when(consulClient.fetchAll()).thenReturn(List.of(new KvEntry() {
            @Override public String getKey() { return "foo"; }
            @Override public String getValue() { return "bar"; }
        }));
        indexer.reindex();
    }

    @Test
    void testSearchInsertsHistoryAndReturnsMatch() {
        List<KvMatch> matches = indexer.search("foo");
        assertFalse(matches.isEmpty());
        assertEquals("foo", matches.get(0).getKey());

        List<String> history = historyDao.findAll();
        assertTrue(history.contains("foo"), "Search query should be recorded in history");
    }
}
