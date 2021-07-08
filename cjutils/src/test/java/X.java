import com.fasterxml.jackson.databind.JsonNode;
import ie.cj.json.JsonModelEnricher;
import ie.cj.utils.ResourceUtils;

public class X {
    public static void main(String[] args) {
        JsonNode jn = ResourceUtils.<JsonNode>loadJson("x.json");
        JsonModelEnricher jme = new JsonModelEnricher();
        jme.process(jn);
        System.out.println(jn.toPrettyString());
    }
}
