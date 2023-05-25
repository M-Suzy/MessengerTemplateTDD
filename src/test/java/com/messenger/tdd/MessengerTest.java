package com.messenger.tdd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

@ExtendWith(MockitoExtension.class)
class MessengerTest {

  @Spy
  private Messenger messenger;
  private ByteArrayOutputStream consoleOutput;

  @BeforeEach
  public void setup() {
    consoleOutput = new ByteArrayOutputStream();
    System.setOut(new PrintStream(consoleOutput));
  }

  @Test
  void testRunConsoleMode() {
    String template = "Hello, #{name}!";
    String runtimeValuesInput = "name=John";
    String input = template + System.lineSeparator() + runtimeValuesInput;
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);
    String expectedOutput = "Enter the template:" + System.lineSeparator() +
        " Enter the runtime values (key=value pairs separated by commas):" + System.lineSeparator() +
        " Hello, John!";

    messenger.runConsoleMode();

    String outputWithoutLogs = removeLogMessage(consoleOutput.toString());
    Assertions.assertEquals(expectedOutput.trim(), outputWithoutLogs);
  }

  private String removeLogMessage(String consoleOutput) {
    String[] lines = consoleOutput.split(System.lineSeparator());
    StringBuilder outputWithoutLogs = new StringBuilder();
    for (String line : lines) {
      int startIndex = line.indexOf("--") + 2;
      if (startIndex >= 0 && startIndex < line.length()) {
        outputWithoutLogs.append(line.substring(startIndex)).append(System.lineSeparator());
      }
    }
    return outputWithoutLogs.toString().trim();
  }

  @Test
  void testFileMode(@TempDir File temporaryFolder) throws IOException {
    String inputFileName = "input.txt";
    String templateFileName = "template.txt";
    String outputFileName = "output.txt";

    File inputTempFile = new File(temporaryFolder, inputFileName);
    BufferedWriter inputWriter = new BufferedWriter(new FileWriter(inputTempFile));
    inputWriter.write("name=John");
    inputWriter.close();

    File templateTempFile = new File(temporaryFolder, templateFileName);
    BufferedWriter templateWriter = new BufferedWriter(new FileWriter(templateTempFile));
    templateWriter.write("Hello, #{name}!");
    templateWriter.close();

    File outputTempFile = new File(temporaryFolder, outputFileName);

    messenger.runFileMode(inputTempFile.getAbsolutePath(), templateTempFile.getAbsolutePath(), outputTempFile.getAbsolutePath());

    BufferedReader outputReader = new BufferedReader(new FileReader(outputTempFile));
    String output = outputReader.readLine();
    outputReader.close();

    String expectedOutput = "Hello, John!";
    Assertions.assertEquals(expectedOutput, output);
  }
}

