package org.example.controller;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class SearchEndpointTest {

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
}
