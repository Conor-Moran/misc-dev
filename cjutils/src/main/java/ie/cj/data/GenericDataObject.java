package ie.cj.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GenericDataObject implements InvocationHandler, MagicBean {
    private static final String GET = "get";
    private static final String SET = "set";
    private static final int GET_SET_PFX_LEN = 3;
    private static final Set<Method> magicBeanMethods = new HashSet<>();
    static {
        for (Method m: MagicBean.class.getDeclaredMethods()) {
            magicBeanMethods.add(m);
        }
    }


    private Map<String, Object> valuesByField = new HashMap<>();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isMagicBeanMethod(method)) {
            return method.invoke(this, args);
        }

        if (isSimpleGetter(method)) {
            return getField(fieldNameFrom(method));
        }

        if (isSimpleSetter(method)) {
            setField(fieldNameFrom(method), args[0]);
            return null;
        }

        return null;
    }

    private boolean isMagicBeanMethod(Method method) {
        return magicBeanMethods.contains(method);
    }

    private boolean isSimpleGetter(Method method) {
        return method.getName().startsWith(GET);
    }

    private boolean isSimpleSetter(Method method) {
        return method.getName().startsWith(SET);
    }

    public Object getField(String fieldName) {
        return valuesByField.get(fieldName);
    }

    String fieldNameFrom(Method method) {
        String capitalizedFieldName = method.getName().substring(GET_SET_PFX_LEN);
        return StringUtils.uncapitalize(capitalizedFieldName);
    }

    public void setField(String fieldName, Object value) {
        valuesByField.put(fieldName, value);
    }

    @Override
    public String toJson(ObjectMapper om) {
        return null;
    }

    public static <T> T create(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(GenericDataObject.class.getClassLoader(),
                new Class[]{clazz, MagicBean.class},
                new GenericDataObject());
    }

}
