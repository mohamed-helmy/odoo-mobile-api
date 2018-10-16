package oogbox.api.odoo.client.helper.utils;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class OdooValues {
    private HashMap<String, Object> _data = new HashMap<>();

    public OdooValues put(String key, Object value) {
        _data.put(key, value);
        return this;
    }

    public OdooValues put(String key, RelValues values) {
        _data.put(key, values);
        return this;
    }

    public OdooValues remove(String key) {
        _data.remove(key);
        return this;
    }

    public JSONObject toJSON(String... ignoreKeys) {
        JSONObject values = new JSONObject();
        List<String> ignore = Arrays.asList(ignoreKeys);
        try {
            for (String key : _data.keySet()) {
                if (ignore.indexOf(key) == -1) {
                    Object value = _data.get(key);
                    if (value instanceof RelValues) {
                        values.put(key, ((RelValues) value).getItems());
                    } else {
                        values.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    public void putAll(HashMap<String, Object> data) {
        _data.putAll(data);
    }

    public HashMap<String, Object> getData() {
        return _data;
    }

    @Override
    public String toString() {
        return _data.toString();
    }
}
