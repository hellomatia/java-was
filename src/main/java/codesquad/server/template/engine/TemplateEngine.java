package codesquad.server.template.engine;

import codesquad.server.template.element.Element;
import codesquad.server.template.parser.TemplateParser;
import codesquad.server.template.renderer.TemplateRenderer;

import java.io.IOException;
import java.util.Map;

import static codesquad.server.util.FileUtils.readFileContent;

public class TemplateEngine {
    private final TemplateParser parser;
    private final TemplateRenderer renderer;

    public TemplateEngine() {
        this.parser = new TemplateParser();
        this.renderer = new TemplateRenderer();
    }

    public String render(String templatePath, Map<String, Object> data) throws IOException {
        String templateContent = readFile(templatePath);
        Element root = parser.parse(templateContent);
        return renderer.render(root, data);
    }

    private String readFile(String filePath) throws IOException {
        return new String(readFileContent(filePath));
    }
}
