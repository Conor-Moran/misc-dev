package ie.cj.json;

import com.fasterxml.jackson.databind.JsonNode;

public interface JsonModelProcessor {
    void process(JsonNode jsonNode);
}
