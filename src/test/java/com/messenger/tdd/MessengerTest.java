package com.messenger.tdd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessengerTest {

  @TempDir
  File temporaryFolder;
  @Spy
  Messenger messengerSpy = new Messenger();

  @Test
  void testRunConsoleMode() {
    String input = "John";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);
    messengerSpy.runConsoleMode();
    verify(messengerSpy).sendEmail("John");
  }

  @Test
  void testFileMode() throws IOException {
    String inputFileName = "input.txt";
    String outputFileName = "output.txt";

    // Create a temporary input file with test data
    File inputTempFile = new File(temporaryFolder, inputFileName);
    BufferedWriter inputWriter = new BufferedWriter(new FileWriter(inputTempFile));
    inputWriter.write("John");
    inputWriter.close();

    messengerSpy.runFileMode(inputTempFile.getAbsolutePath(), new File(temporaryFolder, outputFileName).getAbsolutePath());

    verify(messengerSpy).sendEmail("John");

    File outputTempFile = new File(temporaryFolder, outputFileName);
    BufferedReader outputReader = new BufferedReader(new FileReader(outputTempFile));
    String output = outputReader.readLine();
    outputReader.close();

    String expectedOutput = "Email sent to: John";
    assertEquals(expectedOutput, output);
  }
}

