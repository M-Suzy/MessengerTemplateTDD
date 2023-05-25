package com.messenger.tdd;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Slf4j
@Component
public class Messenger {

  private final TemplateGenerator templateGenerator;

  public Messenger() {
    this.templateGenerator = new TemplateGenerator();
  }

  public void runConsoleMode() {
    Scanner scanner = new Scanner(System.in);
    log.info("Enter the template:");
    String template = scanner.nextLine();
    log.info("Enter the runtime values (key=value pairs separated by commas):");
    String runtimeValuesInput = scanner.nextLine();
    String[] runtimeValuesArray = runtimeValuesInput.split(",");
    for (String runtimeValue : runtimeValuesArray) {
      String[] keyValue = runtimeValue.split("=");
      if (keyValue.length == 2) {
        String key = keyValue[0].trim();
        String value = keyValue[1].trim();
        template = templateGenerator.generateTemplate(template, Map.of(key, value));
      }
    }
    log.info(template);
  }

  public void runFileMode(String inputPath, String templatePath, String outputPath) {
    try {
      String template = Files.readString(Path.of(templatePath));
      String generatedTemplate = templateGenerator.generateTemplate(template, readRuntimeValuesFromFile(inputPath));
      Files.writeString(Path.of(outputPath), generatedTemplate);
      log.info("Generated Template saved to file: " + outputPath);
    } catch (IOException e) {
      throw new CannotReadFileException("IOException: Unable to process file!", e);
    }
  }

  private Map<String, String> readRuntimeValuesFromFile(String inputPath) throws IOException {
    Map<String, String> runtimeValues = new HashMap<>();
    List<String> lines = Files.readAllLines(Path.of(inputPath));
    for (String line : lines) {
      String[] keyValue = line.split("=", 2);
      if (keyValue.length == 2) {
        String key = keyValue[0].trim();
        String value = keyValue[1].trim();
        runtimeValues.put(key, value);
      }
    }
    return runtimeValues;
  }
}
