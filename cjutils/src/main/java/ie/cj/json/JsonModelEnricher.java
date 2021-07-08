package ie.cj.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class JsonModelEnricher implements JsonModelProcessor {
    private static Map<JsonNode, JsonNode> parentsByChild = new LinkedHashMap<>();

    @Override
    public void process(JsonNode jsonNode) {
        enrich(jsonNode);
        parentsByChild.forEach((child, parent) -> {
            parent.fields().forEachRemaining(field -> {
                if (field.getValue().isValueNode()) {
                    ObjectNode childObjectNode = (ObjectNode) child;
                    childObjectNode.put("parent_" + field.getKey(), field.getValue().textValue());
                }
            });
        });
    }

    public void enrich(JsonNode jsonNode) {
        if (jsonNode.isObject()) {
            final ObjectNode objNode = (ObjectNode) jsonNode;
            objNode.put("unique", uuid());
            objNode.forEach(childNode -> {
                enrich(jsonNode, childNode);
            });
        } else if (jsonNode.isArray()) {
            jsonNode.forEach(childNode -> enrich(jsonNode, childNode));
        }
    }

    public void enrich(JsonNode parentJsonNode, JsonNode jsonNode) {
        if (jsonNode.isObject()) {
            parentsByChild.put(jsonNode, parentJsonNode);
            enrich(jsonNode);
        } else if (jsonNode.isArray()) {
            jsonNode.forEach(childNode -> enrich(parentJsonNode, childNode));
        }
    }

    static String uuid() {
        return UUID.randomUUID().toString();
    }
}
