package com.mined.in.api.web.json;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mined.in.reward.RewardType;

/**
 * JSON serializer for reward types.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@JsonComponent
public class RewardTypeJsonSerializer extends JsonSerializer<RewardType> {

    @Override
    public void serialize(RewardType value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeStringField("id", value.name());
        gen.writeStringField("name", value.getName());
        gen.writeStringField("website", value.getWebsite());
        gen.writeEndObject();
    }

}
