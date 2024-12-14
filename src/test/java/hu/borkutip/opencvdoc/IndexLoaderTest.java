package hu.borkutip.opencvdoc;



import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


class IndexLoaderTest {
    @Test
    void load() {
        List<String> lines = IndexLoader.loadResource("/test_index.csv");
        assertTrue(lines.size() > 10);
    }
}