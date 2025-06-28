package org.example.controller;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import org.example.client.ConsulClient;
import org.example.model.KvEntry;
import org.example.service.ConsulIndexer;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@QuarkusTest
public class SearchEndpointTest {

    @InjectMock
    ConsulClient consulClient;

    @Inject
    ConsulIndexer indexer;

    @Inject
    org.example.db.SearchHistoryDao searchHistoryDao;

    @BeforeEach
    void setup() {
        when(consulClient.fetchAll()).thenReturn(List.of(new KvEntry() {
            @Override public String getKey() { return "foo"; }
            @Override public String getValue() { return "bar"; }
        }));
        indexer.reindex();
    }

    @Test
    void testHealthEndpoint() {
        given()
                .when().get("/health")
                .then()
                .statusCode(200)
                .body(equalTo("OK"));
    }

    @Test
    void testSearchPageLoads() {
        given()
                .when().get("/search")
                .then()
                .statusCode(200)
                .body(containsString("Consul KV Search"));
    }

    @Test
    void testReindexRedirectsOrLoads() {
        given()
                .when().get("/reindex")
                .then()
                .statusCode(anyOf(is(200), is(302)));
    }

    @Test
    void testApiSearchReturnsJson() {
        given()
                .queryParam("q", "foo")
                .when().get("/api/search")
                .then()
                .statusCode(200)
                .body("[0].key", equalTo("foo"));
    }

    @Test
    void testSearchCleanEndpoint() {
        // Add entries to history
        searchHistoryDao.insert("foo");
        searchHistoryDao.insert("bar");

        // Ensure history is not empty
        assertFalse(searchHistoryDao.findAll().isEmpty());

        // Call /search/clean and expect redirect
        given()
                .redirects().follow(false)
                .when().get("/search/clean")
                .then()
                .statusCode(303)
                .header("Location", Matchers.containsString("/search?msg=History%20cleaned"));

        // Ensure history is empty after cleaning
        assertTrue(searchHistoryDao.findAll().isEmpty());
    }
}
