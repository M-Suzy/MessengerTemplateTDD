package com.messenger.tdd;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

public class MessengerTemplateCommandLineRunner implements CommandLineRunner
{
  private static final int MIN_ARG_NUMBER = 1;

  private final String mode;

  private final String inputPath;

  private final String templatePath;

  private final String outputPath;
  private final Messenger messenger;

  private static final String USAGE_INFO = """
      App should take the following arguments:
      --mode - optional, <console or file> default is console
      --file.inputPath - required only for file mode
      --file.templatePath - required only for file mode
      --file.outputPath - required only for file mode""";

  public MessengerTemplateCommandLineRunner(Messenger messenger, @Value("${--mode}") String mode,
      @Value("${file.inputPath}") String inputPath, @Value("${file.templatePath}")String templatePath,
      @Value("${file.outputPath}") String outputPath)
  {
    this.messenger = messenger;
    this.mode = mode;
    this.inputPath = inputPath;
    this.templatePath = templatePath;
    this.outputPath = outputPath;
  }

  @Override
  public void run(String... args)
  {
    if (args.length < MIN_ARG_NUMBER)
    {
      throw new IllegalArgumentException(String.format("Invalid arguments: %s", USAGE_INFO));
    }
    if(mode.equalsIgnoreCase("file"))
    {
      if(inputPath.isBlank() || outputPath.isBlank() || templatePath.isBlank())
      {
        throw new IllegalArgumentException(
            String.format("Providing file paths is required for this mode: %s", USAGE_INFO));
      }
      messenger.runFileMode(inputPath, templatePath, outputPath);
    } else
    {
      messenger.runConsoleMode();
    }
  }

  public static String getUsageInfo()
  {
    return USAGE_INFO;
  }
}
