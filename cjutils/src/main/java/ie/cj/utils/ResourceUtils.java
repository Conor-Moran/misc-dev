package ie.cj.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.function.BiFunction;

public class ResourceUtils {

    public static URI uri(String cpResourcePath) {
        try {
            return Thread.currentThread().getContextClassLoader().getResource(cpResourcePath).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static Path path(URI uri) {
        return Paths.get(uri);
    }

    public static byte[] readAllBytes(String cpResourcePath) {
        return readAllBytes(path(uri(cpResourcePath)));
    }

    public static byte[] readAllBytes(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T loadJson(String cpResourcePath) {
        return (T) loadJson(cpResourcePath, ResourceUtils::readTree);
    }

    static JsonNode readTree(ObjectMapper om, byte[] bytes) {
        try {
            return om.readTree(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T loadJson(String cpResourcePath, BiFunction<ObjectMapper, byte[], T> loader) {
        byte[] bytes = readAllBytes(cpResourcePath);
        ObjectMapper om = new ObjectMapper();
        return loader.apply(om, bytes);
    }
}
