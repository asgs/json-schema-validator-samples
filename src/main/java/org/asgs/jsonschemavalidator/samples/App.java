package org.asgs.jsonschemavalidator.samples;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Sample demo to use the json-schema-validator library to validate JSON strings against predefined
 * JSON Schemas.
 *
 * @author asgs
 */
public class App {
  private static final JsonSchemaFactory JSON_SCHEMA_FACTORY = JsonSchemaFactory.byDefault();
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public static final String INSTANCE = "instance";
  public static final String POINTER = "pointer";
  public static final String MESSAGE = "message";

  public static void main(String[] args) throws IOException, ProcessingException {
    validateSimple();
    validateJsonWithSomeOptionalObjectElementsHavingMandatoryElementsMissing();
    validateJsonHavingSomeOptionalObjectElementButMissingSomeOfItsMandatorySubElements();
  }

  private static void validateSimple() throws IOException, ProcessingException {
    // Incorrect data type for x's value and y is missing altogether.
    String json = "{\"x\":1}";
    validate(json, "schema.json");
  }

  private static void validateJsonWithSomeOptionalObjectElementsHavingMandatoryElementsMissing()
      throws IOException, ProcessingException {
    // This is totally fine. Only x and y are mandatory fields, rest are
    // optional.
    String json = "{\"x\":true, \"y\":1}";
    validate(json, "schema-with-optional-element-objects.json");
  }

  private static void
      validateJsonHavingSomeOptionalObjectElementButMissingSomeOfItsMandatorySubElements()
          throws IOException, ProcessingException {
    // This is not fine because eventhough optionalObjectx is optional, it
    // has a few mandatory elements which are missing here.
    String json =
        "{\"x\":true, \"y\":1, "
            + "\"optionalObject1\":{\"optionalProp\": \"sdfsdf\"}, "
            + "\"optionalObject2\":{}}";
    validate(json, "schema-with-optional-element-objects.json");
  }

  private static void validate(String json, String schemaFileName)
      throws IOException, ProcessingException {
    JsonNode jsonNode = OBJECT_MAPPER.readTree(json);
    InputStream schemaInputStream =
        Thread.currentThread().getContextClassLoader().getResourceAsStream(schemaFileName);
    JsonSchema jsonSchema =
        JSON_SCHEMA_FACTORY.getJsonSchema(OBJECT_MAPPER.readTree(schemaInputStream));
    ProcessingReport processingReport = jsonSchema.validateUnchecked(jsonNode, true);
    System.out.println("----------------");
    System.out.println("Validation successful? " + processingReport.isSuccess());
    processingReport.forEach(
        m -> {
          if (m.getLogLevel() == LogLevel.ERROR) {
            JsonNode errorNode = m.asJson();
            String fieldName = errorNode.get(INSTANCE).get(POINTER).asText();
            String errorDescription = "";
            if (fieldName != null && !fieldName.isEmpty()) {
              errorDescription += "The field " + fieldName + " of ";
            }
            System.out.println(errorDescription + errorNode.get(MESSAGE).asText());
          }
        });
  }
}
