package ie.cj.data;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GenericDataObjectTest {
    @Test
    public void test() {
        Person person = GenericDataObject.create(Person.class);
        person.setFirstName("Harry");
        assertEquals("Harry", person.getFirstName());

        MagicBean mb = (MagicBean) person;
        assertEquals("Harry", mb.getField("firstName"));
    }

}
