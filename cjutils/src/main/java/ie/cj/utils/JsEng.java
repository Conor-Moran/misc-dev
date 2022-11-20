import javax.script.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JsEng {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage - provide 2 files");
            throw new IllegalArgumentException();
        }

        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("nashorn");

        Path varsPath = Paths.get(args[0]);
        Path tplPath = Paths.get(args[1]);
        List<String> varSets = Files.readAllLines(varsPath);
        List<String> tpl = Files.readAllLines(tplPath);

        for (int x = 0; x < varSets.size(); x++) {
            String varsLine = varSets.get(x);
            for (String tplLine : tpl) {
                String line = tplLine;
                engine.put("lineNum", x + 1);
                String[] valArr = varsLine.split(" ");

                for (int i = 0; i < valArr.length; i++) {
                    engine.put(String.format("var%d", i + 1), valArr[i]);
                }

                engine.eval(String.format("print(`%s`);", line));
            }
        };
    }
}
