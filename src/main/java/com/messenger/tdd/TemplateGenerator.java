package com.messenger.tdd;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TemplateGenerator
{
  private static final int NOT_FOUND = -1;
  private static final int PLACEHOLDER_POS = 2;
  private static final int NEXT_PLACEHOLDER_INC = 1;

  public String generateTemplate(String template, Map<String, String> runtimeValues)
  {
    List<String> missed = checkAllPlaceholdersAreProvided(template, runtimeValues);
    if(!missed.isEmpty())
    {
      throw new MissingPlaceholderValueException(missed);
    }
    String generatedTemplate = template;
    for (Map.Entry<String, String> entry : runtimeValues.entrySet())
    {
      String placeholder = "#{" + entry.getKey() + "}";
      String value = entry.getValue();
      generatedTemplate = generatedTemplate.replace(placeholder, value);
    }
    return generatedTemplate;
  }

  private static List<String> checkAllPlaceholdersAreProvided(String template, Map<String, String> values)
  {
    int startIndex = template.indexOf("#{");
    int endIndex = template.indexOf("}", startIndex + NEXT_PLACEHOLDER_INC);
    List<String> missingPlaceholders = new ArrayList<>();
    while (startIndex != NOT_FOUND && endIndex != NOT_FOUND)
    {
      String placeholder = template.substring(startIndex+PLACEHOLDER_POS, endIndex);
      startIndex = template.indexOf("#{", endIndex + NEXT_PLACEHOLDER_INC);
      endIndex = template.indexOf("}", startIndex + NEXT_PLACEHOLDER_INC);
      if(!values.containsKey(placeholder))
      {
        missingPlaceholders.add(placeholder);
      }
    }
    return missingPlaceholders;
  }

}
