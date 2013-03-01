package org.lantern;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);

    public static String jsonify(final Object all) {

        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.INDENT_OUTPUT, true);
        // mapper.configure(Feature.SORT_PROPERTIES_ALPHABETICALLY, false);

        try {
            return mapper.writeValueAsString(all);
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
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.INDENT_OUTPUT, true);
        final ObjectWriter writer = mapper.writerWithView(view);
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
        final ObjectMapper om = new ObjectMapper();
        try {
            final Map<String, Object> map = om.readValue(json, Map.class);
            return (String) map.get(key);
        } catch (final JsonGenerationException e) {
            LOG.warn("Error getting JSON string: "+json, e);
        } catch (final JsonMappingException e) {
            LOG.warn("Error getting JSON string: "+json, e);
        } catch (final IOException e) {
            LOG.warn("Error getting JSON string: "+json, e);
        }
        return "";
    }

}
