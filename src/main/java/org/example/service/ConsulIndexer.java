package org.example.service;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.quarkus.scheduler.Scheduled;
import org.example.client.ConsulClient;
import org.example.db.SearchHistoryDao;
import org.example.model.KvEntry;
import org.example.model.KvMatch;
import org.example.util.FuzzySearchEngine;

import java.util.List;

@ApplicationScoped
public class ConsulIndexer {
    @Inject ConsulClient client;
    @Inject FuzzySearchEngine engine;
    @Inject SearchHistoryDao historyDao;

    @Scheduled(every="10m")
    @PostConstruct
    void start() {
        reindex();
    }

    public void reindex() {
        List<KvEntry> all = client.fetchAll();
        engine.index(all);
    }

    public List<KvMatch> search(String q) {
        historyDao.insert(q);
        return engine.search(q);
    }

    public String health() {
        reindex(); // перевіряємо, чи індексатор працює
        return "OK";
    }
}