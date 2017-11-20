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
 * Sample demo to use the json-schema-validator library to validate a JSON doc
 * against a predefined JSON Schema.
 *
 * @author asgs
 */
public class App {
    public static void main(String[] args) throws IOException, ProcessingException {
        JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        // Incorrect data type for x's value and y is missing altogether.
        String json = "{\"x\":1}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        InputStream schemaInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("schema.json");
        JsonSchema jsonSchema = factory.getJsonSchema(mapper.readTree(schemaInputStream));
        ProcessingReport processingReport = jsonSchema.validateUnchecked(jsonNode, true);
        System.out.println(processingReport);
        System.out.println("----------------");
        processingReport.forEach(m -> {
                    if (m.getLogLevel() == LogLevel.ERROR) {
                        JsonNode errorNode = m.asJson();
                        String fieldName = errorNode.get("instance").get("pointer").asText();
                        String errorDescription = "";
                        if (fieldName != null && !fieldName.isEmpty()) {
                            errorDescription += "The field " + fieldName + " of ";
                        }
                        System.out.println(errorDescription + errorNode.get("message").asText());
                    }
                }
        );
    }
}
