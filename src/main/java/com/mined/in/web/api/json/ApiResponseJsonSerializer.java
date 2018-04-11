package com.mined.in.web.api.json;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mined.in.web.api.ApiResponse;

/**
 * JSON serializer for API response.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@JsonComponent
public class ApiResponseJsonSerializer extends JsonSerializer<ApiResponse<?>> {

    @Override
    public void serialize(ApiResponse<?> value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeBooleanField("error", value.isError());
        if (value.isError()) {
            gen.writeStringField("error_type", value.getErrorType().name());
            gen.writeStringField("error_message", value.getErrorMessage());
        } else {
            gen.writeObjectField("data", value.getData());
        }
        gen.writeEndObject();
    }

}
