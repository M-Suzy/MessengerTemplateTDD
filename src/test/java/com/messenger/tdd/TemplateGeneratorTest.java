package com.messenger.tdd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TemplateGeneratorTest {

  @Mock
  TemplateGenerator templateGeneratorMock = new TemplateGenerator();

  @ParameterizedTest
  @MethodSource("templateTestCases")
  void testTemplateGeneration_placeholderValueProvided(String template, Map<String, String> runtimeValues,
      String expectedOutput) {
    when(templateGeneratorMock.generateTemplate(template, runtimeValues)).thenCallRealMethod();
    String generatedTemplate = templateGeneratorMock.generateTemplate(template, runtimeValues);
    assertEquals(expectedOutput, generatedTemplate);
    verify(templateGeneratorMock).generateTemplate(template, runtimeValues);
  }

  @Test
  void testTemplateGeneration_placeholderValueNotProvided() {
    String template = "I live in #{city}, #{country}.";
    Map<String, String> runtimeValues = createRuntimeValues(new String[] {"city"}, new String[] {"Yerevan"});
    when(templateGeneratorMock.generateTemplate(template, runtimeValues)).thenCallRealMethod();
    assertThrows(MissingPlaceholderValueException.class, () ->
        templateGeneratorMock.generateTemplate(template, runtimeValues)
    );
  }

  @Test
  void testLatin1CharacterSupport() {
    String template = "Template with Latin-1 characters: éàö, #{name}: #{text}";

    Map<String, String> runtimeValues
        = createRuntimeValues(new String[] {"name", "text"}, new String[] {"ü", "ñ"});
    String expectedOutput = "Template with Latin-1 characters: éàö, ü: ñ";
    when(templateGeneratorMock.generateTemplate(template, runtimeValues)).thenCallRealMethod();

    String generatedTemplate = templateGeneratorMock.generateTemplate(template, runtimeValues);

    assertEquals(expectedOutput, generatedTemplate);
  }

  private static Stream<Object[]> templateTestCases() {
    String[] keys = new String[] {"name"};
    String[] values = new String[] {"John"};
    Map<String, String> runtimeValues = createRuntimeValues(keys, values);
    return Stream.of(
        new Object[] {"Hello, #{name}! How r u, #{name}?", runtimeValues, "Hello, John! How r u, John?"},
        new Object[] {"Hello, {name}!", runtimeValues, "Hello, {name}!"},
        new Object[] {"Hello, World!", runtimeValues, "Hello, World!"}, // Test Ignoring Unused Runtime Values
        new Object[] {"Some text: #{value}", createRuntimeValues(new String[] {"value"}, new String[] {"#{tag}"}),
            "Some text: #{tag}"}
    );
  }

  private static Map<String, String> createRuntimeValues(String[] keys, String[] values) {
    Map<String, String> runtimeValues = new HashMap<>();
    for (int i = 0; i < keys.length; i++) {
      runtimeValues.put(keys[i], values[i]);
    }
    return runtimeValues;
  }
}
