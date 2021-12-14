package ie.cj.data;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface MagicBean {
    public Object getField(String fieldName);

    public void setField(String fieldName, Object value);

    public String toJson(ObjectMapper om);
}
