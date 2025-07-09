package dev.silal.soulnotes.config.database.util;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DatabaseEntry {
    private final Map<String, Object> data = new HashMap<>();

    public void put(String column, Object value) {
        data.put(column, value);
    }

    public String getString(String column) {
        Object value = data.get(column);
        return value != null ? value.toString() : null;
    }

    public int getInt(String column) {
        Object value = data.get(column);
        return value instanceof Number ? ((Number) value).intValue() : 0;
    }

    public long getLong(String column) {
        Object value = data.get(column);
        return value instanceof Number ? ((Number) value).longValue() : 0L;
    }

    public Date getDate(String column) {
        String d = getString(column);
        if (d == null) return null;
        return DatabaseUtil.getDate(d);
    }

    public Object get(String column) {
        return data.get(column);
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
