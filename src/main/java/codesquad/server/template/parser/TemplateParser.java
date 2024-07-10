package codesquad.server.template.parser;

import codesquad.server.template.element.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateParser {
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{\\s*(.*?)\\s*\\}\\}");
    private static final Pattern IF_PATTERN = Pattern.compile("\\{%\\s*if\\s+(.*?)\\s*%\\}(.*?)(?:\\{%\\s*else\\s*%\\}(.*?))?\\{%\\s*endif\\s*%\\}", Pattern.DOTALL);
    private static final Pattern FOR_PATTERN = Pattern.compile("\\{%\\s*for\\s+(\\w+)\\s+in\\s+(\\w+)\\s*%\\}(.*?)\\{%\\s*endfor\\s*%\\}", Pattern.DOTALL);

    public TemplateStructure parse(String template) {
        List<TemplateElement> elements = new ArrayList<>();
        parseElements(template, elements);
        return new TemplateStructure(elements);
    }

    private void parseElements(String template, List<TemplateElement> elements) {
        int lastIndex = 0;
        while (lastIndex < template.length()) {
            Matcher ifMatcher = IF_PATTERN.matcher(template);
            Matcher forMatcher = FOR_PATTERN.matcher(template);
            Matcher varMatcher = VARIABLE_PATTERN.matcher(template);

            int nextIndex = template.length();
            TemplateElement nextElement = null;

            if (ifMatcher.find(lastIndex) && ifMatcher.start() < nextIndex) {
                nextIndex = ifMatcher.start();
                nextElement = new IfElement(
                        ifMatcher.group(1),
                        parse(ifMatcher.group(2)),
                        ifMatcher.group(3) != null ? parse(ifMatcher.group(3)) : null
                );
            }

            if (forMatcher.find(lastIndex) && forMatcher.start() < nextIndex) {
                nextIndex = forMatcher.start();
                nextElement = new ForElement(
                        forMatcher.group(1),
                        forMatcher.group(2),
                        parse(forMatcher.group(3))
                );
            }

            if (varMatcher.find(lastIndex) && varMatcher.start() < nextIndex) {
                nextIndex = varMatcher.start();
                nextElement = new VariableElement(varMatcher.group(1).trim());
            }

            if (nextIndex > lastIndex) {
                elements.add(new TextElement(template.substring(lastIndex, nextIndex)));
            }

            if (nextElement != null) {
                elements.add(nextElement);
                lastIndex = (nextElement instanceof IfElement) ? ifMatcher.end() :
                        (nextElement instanceof ForElement) ? forMatcher.end() :
                                varMatcher.end();
            } else {
                break;
            }
        }

        if (lastIndex < template.length()) {
            elements.add(new TextElement(template.substring(lastIndex)));
        }
    }
}
