package org.example.controller;

import io.quarkus.qute.Template;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.example.db.SearchHistoryDao;
import org.example.model.KvMatch;
import org.example.service.ConsulIndexer;

import java.util.List;

@Path("/")
public class SearchController {
    @Inject
    Template search;
    @Inject
    ConsulIndexer indexer;
    @Inject
    SearchHistoryDao history;

    @GET
    @Path("/api/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<KvMatch> search(@QueryParam("q") String q) {
        return (q == null || q.isEmpty())
                ? List.of()
                : indexer.search(q);
    }

    @GET
    @Path("/search")
    @Produces(MediaType.TEXT_HTML)
    public String uiSearch(@QueryParam("q") String q) {
        String consulBaseUrl = indexer.getBaseUrl(); // Implement this in your client

        List<KvMatch> results = (q == null || q.isEmpty())
                ? List.of() : indexer.search(q);
        return search.data("results", results)
                .data("history", history.findAll())
                .data("q", q)
                .data("consulBaseUrl", consulBaseUrl)
                .render();
    }

    @GET
    @Path("reindex")
    public String reindex() {
        indexer.reindex();
        return uiSearch("");
    }

    @GET
    @Path("health")
    public String health() {
        try {
            return indexer.health();

        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }

    }
}
