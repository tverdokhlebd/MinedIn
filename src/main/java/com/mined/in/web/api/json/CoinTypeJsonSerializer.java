package com.mined.in.web.api.json;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mined.in.coin.CoinType;

/**
 * JSON serializer for coin types.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@JsonComponent
public class CoinTypeJsonSerializer extends JsonSerializer<CoinType> {

    @Override
    public void serialize(CoinType value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeStringField("id", value.name());
        gen.writeStringField("name", value.getName());
        gen.writeStringField("symbol", value.getSymbol());
        gen.writeStringField("website", value.getWebsite());
        gen.writeBooleanField("enabled", value.isEnabled());
        gen.writeEndObject();
    }

}
