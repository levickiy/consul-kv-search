package org.example.util;

import static org.junit.jupiter.api.Assertions.*;

import org.example.model.KvMatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FuzzySearchEngineTest {
    FuzzySearchEngine engine;

    @BeforeEach
    void init() {
        engine = new FuzzySearchEngine();
        // індексуємо два елементи
        engine.index(List.of(
                new DummyEntry("apple", "fruit"),
                new DummyEntry("banana", "yellow")
        ));
    }

    @Test
    void testExactMatch() {
        List<KvMatch> results = engine.search("apple");
        assertFalse(results.isEmpty());
        assertEquals("apple", results.get(0).getKey());
    }

    @Test
    void testFuzzyMatch() {
        List<KvMatch> results = engine.search("aple"); // одруківка
        assertFalse(results.isEmpty(), "Should find fuzzy match for 'aple'");
        assertEquals("apple", results.get(0).getKey());
    }

    @Test
    void testCaseInsensitiveMatch() {
        List<KvMatch> results = engine.search("ApPlE");
        assertFalse(results.isEmpty(), "Search should be case-insensitive");
        assertEquals("apple", results.get(0).getKey());
    }

    @Test
    void testNoMatch() {
        List<KvMatch> results = engine.search("zzz");
        assertTrue(results.isEmpty());
    }
}
