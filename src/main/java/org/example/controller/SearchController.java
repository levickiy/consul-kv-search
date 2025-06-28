package org.example.controller;

import io.quarkus.qute.Template;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.db.SearchHistoryDao;
import org.example.model.KvMatch;
import org.example.service.ConsulIndexer;

import java.net.URI;
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
    public Response rootRedirect() {
        return Response.seeOther(URI.create("/search")).build();
    }

    @GET
    @Path("/api/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<KvMatch> search(@QueryParam("q") String q) {
        try {
            return (q == null || q.isEmpty()) ? List.of() : indexer.search(q);
        } catch (Exception e) {
            // Optionally log error
            return List.of();
        }
    }

    @GET
    @Path("/search")
    @Produces(MediaType.TEXT_HTML)
    public String uiSearch(@QueryParam("q") String q, @QueryParam("msg") String msg) {
        String consulBaseUrl = indexer.getBaseUrl();
        List<KvMatch> results = (q == null || q.isEmpty()) ? List.of() : indexer.search(q);
        return search.data("results", results)
                .data("history", history.findAll())
                .data("q", q)
                .data("msg", msg)
                .data("consulBaseUrl", consulBaseUrl)
                .render();
    }

    @GET
    @Path("reindex")
    public Response reindex() {
        try {
            indexer.reindex();
            return Response.seeOther(URI.create("/search?msg=Reindex%20complete")).build();
        } catch (Exception e) {
            return Response.seeOther(URI.create("/search?msg=Reindex%20failed")).build();
        }
    }

    @GET
    @Path("/search/clean")
    public Response cleanSearchHistory(@QueryParam("q") String q) {
        history.deleteAll();
        String redirectUrl = "/search?msg=History%20cleaned";
        if (q != null && !q.isEmpty()) {
            redirectUrl += "&q=" + q;
        }
        return Response.seeOther(URI.create(redirectUrl)).build();
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