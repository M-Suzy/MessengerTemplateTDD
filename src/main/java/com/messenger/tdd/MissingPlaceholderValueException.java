package com.messenger.tdd;

import java.util.List;

public class MissingPlaceholderValueException extends RuntimeException
{

  public MissingPlaceholderValueException(List<String> placeholders)
  {
    super(generateMessage(placeholders));
  }

  private static String generateMessage(List<String> placeholders)
  {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("{ ");
    placeholders.forEach(stringBuilder::append);
    stringBuilder.append(" }");
    return String.format("Value for placeholder(s) %s is missing!", stringBuilder);
  }

}
