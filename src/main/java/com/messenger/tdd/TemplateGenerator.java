package com.messenger.tdd;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TemplateGenerator {

  public String generateTemplate(String template, Map<String, String> runtimeValues) {
    List<String> missed = checkAllPlaceholdersAreProvided(template, runtimeValues);
    if(!missed.isEmpty()){
      throw new MissingPlaceholderValueException(missed);
    }
    String generatedTemplate = template;
    for (Map.Entry<String, String> entry : runtimeValues.entrySet()) {
      String placeholder = "#{" + entry.getKey() + "}";
      String value = entry.getValue();
      generatedTemplate = generatedTemplate.replace(placeholder, value);
    }
    return generatedTemplate;
  }

  private List<String> checkAllPlaceholdersAreProvided(String template, Map<String, String> values) {
    int startIndex = template.indexOf("#{");
    int endIndex = template.indexOf("}", startIndex + 1);
    List<String> missingPlaceholders = new ArrayList<>();
    while (startIndex != -1 && endIndex != -1) {
      String placeholder = template.substring(startIndex+2, endIndex);
      startIndex = template.indexOf("#{", endIndex + 1);
      endIndex = template.indexOf("}", startIndex + 1);
      if(!values.containsKey(placeholder)){
        missingPlaceholders.add(placeholder);
      }
    }
    return missingPlaceholders;
  }

}
