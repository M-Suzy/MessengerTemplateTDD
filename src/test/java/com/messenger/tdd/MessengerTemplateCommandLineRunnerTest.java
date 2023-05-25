package com.messenger.tdd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessengerTemplateCommandLineRunnerTest
{

  @Mock
  private Messenger mockMessenger;

  MessengerTemplateCommandLineRunner runner;

  @Test
  void run_WithInvalidArguments_ThrowsIllegalArgumentException()
  {
    runner = new MessengerTemplateCommandLineRunner(mockMessenger, "console", "", "", "");
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, runner::run);
    assertEquals("Invalid arguments: " + MessengerTemplateCommandLineRunner.getUsageInfo(), exception.getMessage());
  }

  @Test
  void run_WithFileModeAndMissingFilePaths_ThrowsIllegalArgumentException()
  {
    runner = new MessengerTemplateCommandLineRunner(mockMessenger, "file", "", "", "");
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, runner::run);
    assertEquals("Invalid arguments: " + MessengerTemplateCommandLineRunner.getUsageInfo(),
        exception.getMessage());
  }

  @Test
  void run_WithConsoleMode_CallsRunConsoleMode()
  {
    runner = new MessengerTemplateCommandLineRunner(mockMessenger, "console", "", "", "");
    runner.run("console");
    verify(mockMessenger, times(1)).runConsoleMode();
  }

  @Test
  void run_WithFileModeAndValidFilePaths_CallsRunFileMode()
  {
    String input = "input.txt";
    String output = "output.txt";
    String template = "template.txt";
    MessengerTemplateCommandLineRunner runner
        = new MessengerTemplateCommandLineRunner(mockMessenger, "file", input, template, output);
    runner.run("file", input, template, output);
    verify(mockMessenger, times(1))
        .runFileMode(input, template, output);
  }
}

