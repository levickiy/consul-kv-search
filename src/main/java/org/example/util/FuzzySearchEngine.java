package org.example.util;

import info.debatty.java.stringsimilarity.JaroWinkler;
import jakarta.enterprise.context.ApplicationScoped;
import org.example.model.KvEntry;
import org.example.model.KvMatch;

import java.util.*;
import java.util.Locale;
import java.util.stream.Collectors;

@ApplicationScoped
public class FuzzySearchEngine {
    private Map<String, String> kv = new HashMap<>();
    private final JaroWinkler jw = new JaroWinkler();

    public void index(List<KvEntry> entries) {
        Map<String, String> map = new HashMap<>();
        for (KvEntry e : entries) {
            map.put(e.getKey(), e.getValue());
        }
        this.kv = map;
    }

    public List<KvMatch> search(String query) {
        String q = query.toLowerCase(Locale.ROOT);
        return kv.entrySet().stream()
            .filter(e -> jw.similarity(q, e.getKey().toLowerCase(Locale.ROOT)) > 0.7
                      || jw.similarity(q, e.getValue().toLowerCase(Locale.ROOT)) > 0.7)
            .map(e -> new AbstractMap.SimpleEntry<>(e,
                Math.max(jw.similarity(q, e.getKey().toLowerCase(Locale.ROOT)),
                         jw.similarity(q, e.getValue().toLowerCase(Locale.ROOT))))
            )
            .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
            .map(es -> new KvMatch(es.getKey().getKey(), es.getKey().getValue()))
            .collect(Collectors.toList());
    }
}