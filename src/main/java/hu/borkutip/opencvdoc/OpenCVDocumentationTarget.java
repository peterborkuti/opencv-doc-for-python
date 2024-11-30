package hu.borkutip.opencvdoc;

import com.intellij.model.Pointer;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.platform.backend.documentation.DocumentationResult;
import com.intellij.platform.backend.documentation.DocumentationTarget;
import com.intellij.platform.backend.presentation.TargetPresentation;
import com.intellij.util.io.HttpRequests;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenCVDocumentationTarget implements DocumentationTarget {
    private static final String ANCHOR="<a.*?id=\"@\".*?</a>";
    private static final String BODY_OPENCV_DOC = "<body><h1><a href=\"@\">OPENCV DOCUMENTATION LINK</a></h1>";
    private static final String PYTHON_COLSPAN="<tr><th colspan=\"999\" style=\"text-align:left\">Python:</th></tr>";
    private static final String PYTHON_CAPTION="<caption>Python:</caption>";
    private static final String PYTHON_TABLE_BAD="<table class=\"python_language\">";
    private static final String PYTHON_TABLE_GOOD="<table style=\"width:100%; white-space:nowrap;\">";
    private final String url;
    private final String keyword;
    private static final Logger log = Logger.getInstance(OpenCVDocumentationTarget.class);

    OpenCVDocumentationTarget(String keyword, String url) {
        this.keyword = keyword;
        this.url = url;
    }

    @Nullable
    @Override
    public DocumentationResult computeDocumentation() {
        log.warn("computeDocumentation");
        return DocumentationResult.Companion.asyncDocumentation(() -> {
            try {
                log.warn("Fetching url: " + url);
                // Use IntelliJ's HttpRequests to fetch the documentation
                String htmlContent = HttpRequests.request(url).readString();
                log.warn("Fetched content length:" + htmlContent.length());

                htmlContent = transformContent(htmlContent, url);
                log.warn("Content length after remove:" + htmlContent.length());
                // Wrap the fetched HTML in a DocumentationResult.Documentation
                return DocumentationResult.Companion.documentation(htmlContent);
            } catch (Exception e) {
                log.error("Fetching url failed:" + url, e);
                return null;
            }
        });
    }

    public String transformContent(String content, String url) {
        content = replaceConstantParts(content);

        //content = replaceRelativeURIsWithAbsolute(content, url, "href");
        //content = replaceRelativeURIsWithAbsolute(content, url, "src");

        String bodyFirstLine = BODY_OPENCV_DOC.replace("@", url);
        String anchor = getAnchor(url);
        log.warn("Anchor:" + anchor);

        if (anchor != null) {
            content = content.replaceFirst("<body>.*?"+anchor, bodyFirstLine);
        }

        return content;
    }

    public String replaceConstantParts(String content) {
        if (content == null || content.isBlank()) return content;

        content = content.replace("\n", "");
        content = content.replace(PYTHON_COLSPAN, PYTHON_CAPTION);
        content = content.replace(PYTHON_TABLE_BAD, PYTHON_TABLE_GOOD);

        return content;
    }

    public String replaceRelativeURIsWithAbsolute(String content, String url, String attribute) {
        URI uri = URI.create(url);

        Pattern p = Pattern.compile(attribute + "=\"(.*?)\"");
        Matcher m = p.matcher(content);

        StringBuffer sb = new StringBuffer();
        while (m.find()) {

            try {
                URI href = URI.create(m.group(1));
                if (href.isAbsolute()) continue;

                String absoluteUrl = uri.resolve(href).toString();
                m.appendReplacement(sb, attribute + "=\""+absoluteUrl+"\"");
            }
            catch (Exception e) {
                log.warn(e.getMessage());
            }
        }
        m.appendTail(sb);

        return sb.toString();
    }

    private String getAnchor(String url) {
        String[] parts = url.split("#");
        if (parts.length != 2) return null;
        String hash = parts[1];

        return ANCHOR.replace("@", hash);
    }

    @NotNull
    @Override
    public TargetPresentation computePresentation() {
        log.warn("computePresentation");
        return TargetPresentation.builder(keyword)
                .containerText("containertext")
                .locationText(url)
                .presentation();
    }

    @NotNull
    @Override
    public Pointer<? extends DocumentationTarget> createPointer() {
        log.warn("createPointer");
        OpenCVDocumentationTarget target = new OpenCVDocumentationTarget(keyword, url);
        return () -> target;
    }
}
