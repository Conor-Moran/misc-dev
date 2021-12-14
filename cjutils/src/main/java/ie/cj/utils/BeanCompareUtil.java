package ie.cj.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BeanCompareUtil {

    public static Set<String> fieldByFieldCompare(Object bean1, Object bean2) {
        final Map<String, String> flattenedBean1 = new HashMap<>();
        final Map<String, String> flattenedBean2 = new HashMap<>();
        final Set<String> comparisonResult = new HashSet<>();
        final Set<String> allKeys = new HashSet<>();
        final Set<String> intersection = new HashSet<>();
        intersection.addAll(flattenedBean1);
        intersection.retainAll(flattenedBean2);


        allKeys.addAll(flattenedBean1.keySet());
        allKeys.addAll(flattenedBean2.keySet());

        allKeys.forEach(key -> {
            if (!eachMapContainsKey(key, flattenedBean1, flattenedBean2)
                    || !mapValuesAreEqual(key, flattenedBean1, flattenedBean2)) {

                comparisonResult.add(key);
            }
        });

        return comparisonResult;
    }

    private static boolean eachMapContainsKey(String key, Map<String, String> map1, Map<String, String> map2) {
        return map1.containsKey(key) && map2.containsKey(key);
    }

    private static boolean mapValuesAreEqual(String key, Map<String, String> map1, Map<String, String> map2) {
        return map1.get(key).equals(map2.get(key));
    }

}
