package codesquad.server.template.engine;

import codesquad.server.template.element.Element;
import codesquad.server.template.parser.TemplateParser;
import codesquad.server.template.renderer.TemplateRenderer;

import java.io.IOException;
import java.util.Map;

import static codesquad.server.util.FileUtils.readFileContent;

public class TemplateEngine {
    private static final String PREFIX = "/templates/";
    private static final String SUFFIX = ".html";
    private static final TemplateParser parser = new TemplateParser();
    private static final TemplateRenderer renderer = new TemplateRenderer();

    private TemplateEngine() {
    }

    public static String render(String templateName, Map<String, Object> data) throws IOException {
        String templateContent = readFile(PREFIX + templateName + SUFFIX);
        Element root = parser.parse(templateContent);
        return renderer.render(root, data);
    }

    private static String readFile(String filePath) {
        return new String(readFileContent(filePath));
    }
}
