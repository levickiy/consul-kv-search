package org.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KvEntryTest {
    @Test
    void testDecodeBase64() {
        KvEntry e = new KvEntry();
        try {
            var field = KvEntry.class.getDeclaredField("rawValue");
            field.setAccessible(true);
            field.set(e, java.util.Base64.getEncoder().encodeToString("bar".getBytes()));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        assertEquals("bar", e.getValue());
    }

    @Test
    void testInvalidBase64() throws Exception {
        KvEntry e = new KvEntry();
        var field = KvEntry.class.getDeclaredField("rawValue");
        field.setAccessible(true);
        field.set(e, "@invalid@");
        assertEquals("[invalid base64]", e.getValue());
    }
}
