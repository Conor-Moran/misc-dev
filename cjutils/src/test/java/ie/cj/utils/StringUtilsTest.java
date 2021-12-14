package ie.cj.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    public void test() {
        StringUtils.fuzzyCompare("fred", "fleds");
    }
}
