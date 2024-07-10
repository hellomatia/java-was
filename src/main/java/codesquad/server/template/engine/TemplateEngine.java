package codesquad.server.template.engine;

import codesquad.server.template.element.TemplateStructure;
import codesquad.server.template.parser.TemplateParser;
import codesquad.server.template.renderer.TemplateRenderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class TemplateEngine {
    private final TemplateParser parser;
    private final TemplateRenderer renderer;

    public TemplateEngine() {
        this.parser = new TemplateParser();
        this.renderer = new TemplateRenderer();
    }

    public String render(String templatePath, Map<String, Object> data) throws IOException {
        String templateContent = readFile(templatePath);
        TemplateStructure structure = parser.parse(templateContent);
        System.out.println("structure = " + structure);
        return renderer.render(structure, data);
    }

    private String readFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}