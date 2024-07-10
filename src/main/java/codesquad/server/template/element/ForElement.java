package codesquad.server.template.element;

public class ForElement implements TemplateElement {
    private final String varName;
    private final String listName;
    private final TemplateStructure body;

    public ForElement(String varName, String listName, TemplateStructure body) {
        this.varName = varName;
        this.listName = listName;
        this.body = body;
    }

    public String getVarName() {
        return varName;
    }

    public String getListName() {
        return listName;
    }

    public TemplateStructure getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "ForElement{" +
                "varName='" + varName + '\'' +
                ", listName='" + listName + '\'' +
                ", body=" + body +
                '}';
    }
}
