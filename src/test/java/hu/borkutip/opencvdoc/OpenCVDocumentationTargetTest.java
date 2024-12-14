package hu.borkutip.opencvdoc;

import com.intellij.platform.backend.documentation.DocumentationResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenCVDocumentationTargetTest {
    @Test
    void replaceContentWhenAnchorIsType1() {
        String html="<body>\nX\nX\n<a id=\"B\" name=\"B\"></a>\nY\n";
        String url = "any#B";
        OpenCVDocumentationTarget target = new OpenCVDocumentationTarget("", url);
        String removedHtml = target.transformContent(html, url);

        assertEquals("<body><h1><a href=\"any#B\">OPENCV DOCUMENTATION LINK</a></h1>Y", removedHtml);
    }

    @Test
    void replaceContentWhenAnchorIsType2() {
        String html="<body>\nX\nX\n<a class=\"anchor\" + id=\"B\"></a>\nY\n";
        String url = "any#B";
        OpenCVDocumentationTarget target = new OpenCVDocumentationTarget(url, "");
        String removedHtml = target.transformContent(html, url);

        assertEquals("<body><h1><a href=\"any#B\">OPENCV DOCUMENTATION LINK</a></h1>Y", removedHtml);
    }

    //@Test
    void replaceRelativeŁHREFsWithAbsolute() {
        String content = "<a href=\"http://a.b.com/main/a.html\">\n</a>\n<a href=\"../x.html#123\">\n</a>";
        OpenCVDocumentationTarget target = new OpenCVDocumentationTarget("", "");
        String replaced = target.replaceRelativeURIsWithAbsolute(content, "http://d.e.com/q/r/main.html", "href");

        assertEquals("<a href=\"http://a.b.com/main/a.html\">\n</a>\n<a href=\"http://d.e.com/q/x.html\">\n</a>", replaced);
    }

    //@Test
    void replaceFileContent() {
        String html=String.join("\n", IndexLoader.loadResource("/page.html"));
        String url = "https://docs.opencv.org/4.x/d6/d6e/group__imgproc__draw.html#ga5126f47f883d730f633d74f07456c576";
        OpenCVDocumentationTarget target = new OpenCVDocumentationTarget("", url);
        String removedHtml = target.transformContent(html, url);

        assertEquals("<body>Y", removedHtml);
    }

}