package ie.cj.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    public static int fuzzyCompare(String str1, String str2) {
        char[] chars1 = str1.toCharArray();
        char[] slider = new char[str1.length()];

        char[] chars2 = str2.toCharArray();
        List<Character> unplaced = new ArrayList<>();
        for (char chr : chars2) {
            boolean placed = false;
            for(int i = 0; i < chars1.length; i++) {
                if (slider[i] == 0 && chars1[i] == chr) {
                    slider[i] = chr;
                    placed = true;
                    break;
                }
            }
            if (!placed) {
                unplaced.add(chr);
            }
        }
        print(slider);
        System.out.println(unplaced);
        return 100;
    }

    static void print(char[] chars) {
        for(int i = 0; i < chars.length; i++) {
            if (chars[i] == 0) {
                chars[i] = ' ';
                break;
            }
        }

        System.out.println(String.valueOf(chars));

    }
}
