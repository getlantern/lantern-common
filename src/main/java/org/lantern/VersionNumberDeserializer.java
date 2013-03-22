package org.lantern;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

public class VersionNumberDeserializer extends JsonDeserializer<VersionNumber> {

    @Override
    public VersionNumber deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        return new VersionNumber(jp.readValueAsTree().getTextValue());
    }

}
