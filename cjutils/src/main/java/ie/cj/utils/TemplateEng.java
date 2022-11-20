
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TemplateEng {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage - provide 2 files");
            throw new IllegalArgumentException();
        }


        Path varsPath = Paths.get(args[0]);
        Path tplPath = Paths.get(args[1]);
        List<String> vars = Files.readAllLines(varsPath);
        List<String> tpl = Files.readAllLines(tplPath);

        for (int x = 0; x < vars.size(); x++) {
            String varsLine = vars.get(x);
            for (String tplLine : tpl) {
                String line = tplLine;
                line = line.replace("{lineNum}", String.format("%03d", x + 1));
                String[] valArr = varsLine.split(" ");

                for (int i = 0; i < valArr.length; i++) {
                    line = line.replace(String.format("{%d}", i + 1), valArr[i]);
                }
                System.out.println(line);
            }
        };
    }
}
