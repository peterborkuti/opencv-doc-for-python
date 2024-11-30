package hu.borkutip.opencvdoc;

import com.intellij.openapi.diagnostic.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class IndexLoader {
        private static final Logger log = Logger.getInstance(OpenCVDocumentationProvider.class);

        private IndexLoader() {}

        public static List<String> loadResource(String resourcePath) {
            try (InputStream inputStream = IndexLoader.class.getResourceAsStream(resourcePath)) {
                if (inputStream == null) {
                    log.error("Resource not found: " + resourcePath);
                    return Collections.emptyList();
                }

                return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                        .lines().toList();
            } catch (Exception e) {
                log.error("Can not read resource", e);
            }

            return Collections.emptyList();
        }
}
