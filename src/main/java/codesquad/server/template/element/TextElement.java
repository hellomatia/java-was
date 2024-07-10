package codesquad.server.template.element;

public class TextElement implements TemplateElement {
    private final String text;

    public TextElement(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "TextElement{" +
                "text='" + text + '\'' +
                '}';
    }
}
