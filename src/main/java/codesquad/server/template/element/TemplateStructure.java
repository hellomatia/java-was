package codesquad.server.template.element;

import java.util.List;

public class TemplateStructure {
    private final List<TemplateElement> elements;

    public TemplateStructure(List<TemplateElement> elements) {
        this.elements = elements;
    }

    public List<TemplateElement> getElements() {
        return elements;
    }

    @Override
    public String toString() {
        return "TemplateStructure{" +
                "elements=" + elements +
                '}';
    }
}
