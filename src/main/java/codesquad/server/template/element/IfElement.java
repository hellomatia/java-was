package codesquad.server.template.element;

public class IfElement implements TemplateElement {
    private final String condition;
    private final TemplateStructure body;
    private final TemplateStructure elseBody;

    public IfElement(String condition, TemplateStructure body, TemplateStructure elseBody) {
        this.condition = condition;
        this.body = body;
        this.elseBody = elseBody;
    }

    public String getCondition() {
        return condition;
    }

    public TemplateStructure getBody() {
        return body;
    }

    public TemplateStructure getElseBody() {
        return elseBody;
    }

    @Override
    public String toString() {
        return "IfElement{" +
                "condition='" + condition + '\'' +
                ", body=" + body +
                ", elseBody=" + elseBody +
                '}';
    }
}
