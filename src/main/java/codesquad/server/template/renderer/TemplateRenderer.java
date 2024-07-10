package codesquad.server.template.renderer;

import codesquad.server.template.element.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateRenderer {
    public String render(TemplateStructure structure, Map<String, Object> data) {
        StringBuilder result = new StringBuilder();
        renderElements(structure.getElements(), data, result);
        return result.toString();
    }

    private void renderElements(List<TemplateElement> elements, Map<String, Object> data, StringBuilder result) {
        for (TemplateElement element : elements) {
            if (element instanceof TextElement) {
                result.append(((TextElement) element).getText());
            } else if (element instanceof VariableElement) {
                String varName = ((VariableElement) element).getName();
                Object value = data.get(varName);
                result.append(value != null ? value.toString() : "");
            } else if (element instanceof IfElement) {
                renderIfElement((IfElement) element, data, result);
            } else if (element instanceof ForElement) {
                renderForElement((ForElement) element, data, result);
            }
        }
    }

    private void renderIfElement(IfElement ifElement, Map<String, Object> data, StringBuilder result) {
        if (evaluateCondition(ifElement.getCondition(), data)) {
            renderElements(ifElement.getBody().getElements(), data, result);
        } else if (ifElement.getElseBody() != null) {
            renderElements(ifElement.getElseBody().getElements(), data, result);
        }
    }

    private void renderForElement(ForElement forElement, Map<String, Object> data, StringBuilder result) {
        Object listObj = data.get(forElement.getListName());
        if (listObj instanceof List) {
            List<?> list = (List<?>) listObj;
            for (Object item : list) {
                Map<String, Object> loopData = new HashMap<>(data);
                loopData.put(forElement.getVarName(), item);
                renderElements(forElement.getBody().getElements(), loopData, result);
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
