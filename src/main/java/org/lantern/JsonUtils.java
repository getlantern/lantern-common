package org.lantern;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);
    public static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER
                .configure(
                        DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
                        false);
        OBJECT_MAPPER.configure(Feature.INDENT_OUTPUT, true);
    }

    public static String jsonify(final Object all) {

        try {
            return OBJECT_MAPPER.writeValueAsString(all);
        } catch (final JsonGenerationException e) {
            LOG.warn("Error generating JSON", e);
        } catch (final JsonMappingException e) {
            LOG.warn("Error generating JSON", e);
        } catch (final IOException e) {
            LOG.warn("Error generating JSON", e);
        }
        return "";
    }

    public static String jsonify(final Object all, final Class<?> view) {
        final ObjectWriter writer = OBJECT_MAPPER.writerWithView(view);
        try {
            return writer.writeValueAsString(all);
        } catch (final JsonGenerationException e) {
            LOG.warn("Error generating JSON", e);
        } catch (final JsonMappingException e) {
            LOG.warn("Error generating JSON", e);
        } catch (final IOException e) {
            LOG.warn("Error generating JSON", e);
        }
        return "";
    }

    public static String getValueFromJson(final String key, final String json) {
        try {
            final Map<String, Object> map = OBJECT_MAPPER.readValue(json,
                    Map.class);
            return (String) map.get(key);
        } catch (final JsonGenerationException e) {
            LOG.warn("Error getting JSON string: " + json, e);
        } catch (final JsonMappingException e) {
            LOG.warn("Error getting JSON string: " + json, e);
        } catch (final IOException e) {
            LOG.warn("Error getting JSON string: " + json, e);
        }
        return "";
    }

}
