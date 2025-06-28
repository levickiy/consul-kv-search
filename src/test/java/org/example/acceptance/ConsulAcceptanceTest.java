package org.example.acceptance;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.DockerClientFactory;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@QuarkusTestResource(ConsulTestResource.class)
public class ConsulAcceptanceTest {

    @BeforeAll
    static void checkDocker() {
        Assumptions.assumeTrue(DockerClientFactory.instance().isDockerAvailable());
    }

    @Test
    void testIndexingFromConsul() {
        given().when().get("/reindex").then().statusCode(anyOf(is(200), is(302)));

        given()
                .queryParam("q", "foo")
                .when().get("/api/search")
                .then()
                .statusCode(200)
                .body("[0].key", equalTo("foo"));
    }
}

