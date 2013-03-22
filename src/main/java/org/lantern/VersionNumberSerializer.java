package org.lantern;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class VersionNumberSerializer extends JsonSerializer<VersionNumber> {

    @Override
    public void serialize(VersionNumber value, JsonGenerator jgen,
            SerializerProvider provider) throws IOException,
            JsonProcessingException {
        jgen.writeString(value.toString());
    }

}
