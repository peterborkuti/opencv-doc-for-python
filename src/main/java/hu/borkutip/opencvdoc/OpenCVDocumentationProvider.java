// Copyright 2000-2023 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package hu.borkutip.opencvdoc;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.platform.backend.documentation.DocumentationTarget;
import com.intellij.platform.backend.documentation.PsiDocumentationTargetProvider;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * TODO Please note, it is recommended to utilize the new DocumentationTarget API for
 * plugins targeting IntelliJ Platform version 2023.1 or later.
 *
 * @see <a href="https://plugins.jetbrains.com/docs/intellij/documentation.html">Documentation (IntelliJ Platform Docs)</a>
 */
public class OpenCVDocumentationProvider implements PsiDocumentationTargetProvider {
    private static final Logger log = Logger.getInstance(OpenCVDocumentationProvider.class);
    private static final String DOC_URL = "https://docs.opencv.org/4.x/";

    private final Map<String, String> keywordUrlMap;
    public OpenCVDocumentationProvider() {
        keywordUrlMap = IndexLoader.loadResource("/index.csv")
                .stream()
                .map(String::toLowerCase)
                .map(s -> s.split(","))
                .filter(a -> a.length > 1)
                .collect(Collectors.toMap(s -> s[0], s -> s[1], (first, second) -> first));

        log.warn("OpenCVDocumentationProvider created");
    }

    @Override
    public @Nullable DocumentationTarget documentationTarget(@NotNull PsiElement element, @Nullable PsiElement originalElement) {
        return getKeyWord(originalElement)
                .flatMap(kw -> getUrlFor(kw)
                        .map(url -> new OpenCVDocumentationTarget(kw, url))
                )
                .orElse(null);
    }

    private Optional<String> getKeyWord(PsiElement originalElement) {
        if (originalElement == null) return Optional.empty();
        String word = originalElement.getText();
        if (word == null) return Optional.empty();
        word = word.trim();
        if (word.isEmpty()) return Optional.empty();

        return Optional.of(word.toLowerCase());
    }

    private Optional<String> getUrlFor(String word) {
        log.warn("getUrlFor :" + word);

        String url = keywordUrlMap.get(word);
        if (url != null) {
            url = DOC_URL + url;
        }

        log.warn("url for keyword " + word + ":" + url);

        return Optional.ofNullable(url);
    }

}
