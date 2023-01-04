package com.example.demo.Services;

import java.io.InputStream;
import java.util.Set;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

public class SchemaValidator {

    // create instance of the ObjectMapper class
    private  ObjectMapper objectMapper = new ObjectMapper();

    // create an instance of the JsonSchemaFactory using version flag
    private  JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);

    //Singleton
    private static final SchemaValidator schemaValidator = new SchemaValidator();
    public static SchemaValidator getInstance(){
        return schemaValidator ;
    }

    public boolean validate(String schemaIn, String jsonIn) throws Exception {

        // read data from the stream and store it into JsonNode
        JsonNode json = objectMapper.readTree(jsonIn);
        // get schema from the schemaStream and store it into JsonSchema
        JsonSchema schema = schemaFactory.getSchema(schemaIn);
        // create set of validation message and store result in it
        Set<ValidationMessage> validationResult = schema.validate(json);
        // show the validation errors
        if (validationResult.isEmpty()) {
            // show custom message if there is no validation error
            System.out.println( "There is no validation errors" );
            return true;
        } else {
            // show all the validation error
            validationResult.forEach(vm -> System.out.println(vm.getMessage()));
            return false;
        }
    }
}


