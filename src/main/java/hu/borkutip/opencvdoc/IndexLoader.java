package hu.borkutip.opencvdoc;

import com.google.common.io.Files;
import com.intellij.openapi.diagnostic.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class IndexLoader {
    private static final Logger log = Logger.getInstance(OpenCVDocumentationProvider.class);

    private IndexLoader() {
    }

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

    public static void saveResource(String fileName, String extension, String content) throws IOException {
        File got = File.createTempFile(fileName, extension);
        String outputPath = got.getAbsolutePath();
        log.debug("outputPath: " + outputPath);
        try (PrintWriter writer = new PrintWriter(got)){
            writer.println(content);
        } catch (FileNotFoundException e) {
            log.error("Can not save resource", e);
        }
    }
}
