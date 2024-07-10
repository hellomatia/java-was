package codesquad.server.template.renderer;

import codesquad.server.template.element.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateRenderer {
    public String render(Element root, Map<String, Object> data) {
        StringBuilder result = new StringBuilder();
        renderElement(root, data, result);
        return result.toString();
    }

    private void renderElement(Element element, Map<String, Object> data, StringBuilder result) {
        if (element instanceof TextElement) {
            result.append(((TextElement) element).content);
        } else if (element instanceof VariableElement) {
            String varName = ((VariableElement) element).variableName;
            Object value = data.get(varName);
            result.append(value != null ? value.toString() : "");
        } else if (element instanceof IfElement) {
            renderIfElement((IfElement) element, data, result);
        } else if (element instanceof ForElement) {
            renderForElement((ForElement) element, data, result);
        } else {
            for (Element child : element.children) {
                renderElement(child, data, result);
            }
        }
    }

    private void renderIfElement(IfElement ifElement, Map<String, Object> data, StringBuilder result) {
        if (evaluateCondition(ifElement.condition, data)) {
            for (Element child : ifElement.children) {
                if (!(child instanceof Element && ((Element) child).name.equals("else"))) {
                    renderElement(child, data, result);
                }
            }
        } else {
            Element elseElement = ifElement.children.stream()
                    .filter(child -> child instanceof Element && ((Element) child).name.equals("else"))
                    .findFirst()
                    .orElse(null);
            if (elseElement != null) {
                for (Element child : elseElement.children) {
                    renderElement(child, data, result);
                }
            }
        }
    }

    private void renderForElement(ForElement forElement, Map<String, Object> data, StringBuilder result) {
        Object listObj = data.get(forElement.collectionName);
        if (listObj instanceof List) {
            List<?> list = (List<?>) listObj;
            for (Object item : list) {
                Map<String, Object> loopData = new HashMap<>(data);
                loopData.put(forElement.itemName, item);
                for (Element child : forElement.children) {
                    renderElement(child, loopData, result);
                }
            }
        }
    }

    private boolean evaluateCondition(String condition, Map<String, Object> data) {
        String[] parts = condition.split("==");
        if (parts.length == 2) {
            String left = parts[0].trim();
            String right = parts[1].trim().replaceAll("\"", "");
            Object leftValue = data.get(left);
            if (leftValue != null) {
                if (leftValue instanceof Boolean) {
                    return (Boolean) leftValue == Boolean.parseBoolean(right);
                }
                return leftValue.toString().equals(right);
            }
        }
        return false;
    }
}
