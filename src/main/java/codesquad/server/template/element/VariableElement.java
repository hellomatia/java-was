package codesquad.server.template.element;

public class VariableElement implements TemplateElement {
    private final String name;

    public VariableElement(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "VariableElement{" +
                "name='" + name + '\'' +
                '}';
    }
}
