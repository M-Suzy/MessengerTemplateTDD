package com.messenger.tdd;

import java.util.List;

public class MissingPlaceholderValueException extends RuntimeException {

  private String message;

  public MissingPlaceholderValueException(List<String> placeholders){
    generateMessage(placeholders);
  }

  private void generateMessage(List<String> placeholders){
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("{ ");
    placeholders.forEach(stringBuilder::append);
    stringBuilder.append(" }");
    message = String.format("Value for placeholder(s) %s is missing!", stringBuilder);
  }

  @Override
  public String getMessage() {
    return message;
  }
}
