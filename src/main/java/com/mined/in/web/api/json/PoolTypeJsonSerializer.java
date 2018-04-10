package com.mined.in.web.api.json;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mined.in.pool.PoolType;

/**
 * JSON serializer for pool types.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@JsonComponent
public class PoolTypeJsonSerializer extends JsonSerializer<PoolType> {

    @Override
    public void serialize(PoolType value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeStringField("id", value.name());
        gen.writeStringField("name", value.getName());
        gen.writeStringField("website", value.getWebsite());
        gen.writeEndObject();
    }

}
