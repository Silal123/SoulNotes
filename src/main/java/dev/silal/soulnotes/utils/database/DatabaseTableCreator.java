package dev.silal.soulnotes.utils.database;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class DatabaseTableCreator {

    private String tableName;
    private boolean checkExists;
    private final Map<String, Map<String, Object>> cols = new LinkedHashMap<>();

    public DatabaseTableCreator(String tableName, boolean checkExists) {
        this.tableName = tableName;
        this.checkExists = checkExists;
    }

    public DatabaseTableCreator(String tableName) {
        this(tableName, false);
    }

    public DatabaseTableCreator() {
        this(null, false);
    }

    public void setTableName(String name) {
        this.tableName = name;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setCheckExists(boolean b) {
        this.checkExists = b;
    }

    public boolean getCheckExists() {
        return this.checkExists;
    }

    public void addCol(String name, ColType type) {
        cols.put(name, Map.of("type", type));
    }

    public void addCol(String name, ColType type, boolean nn) {
        cols.put(name, Map.of("type", type, "nn", nn));
    }

    public void addCol(String name, ColType type, boolean nn, boolean pk) {
        cols.put(name, Map.of("type", type, "nn", nn, "pk", pk));
    }

    public void addColWithDefault(String name, ColType type, boolean nn, boolean pk, String defaultVal) {
        Map<String, Object> colProps = new HashMap<>();
        colProps.put("type", type);
        colProps.put("nn", nn);
        colProps.put("pk", pk);
        colProps.put("default", defaultVal);
        cols.put(name, colProps);
    }

    public void removeCol(String name) {
        cols.remove(name);
    }

    public void removeCol(ColType type) {
        cols.entrySet().removeIf(entry -> type.equals(entry.getValue().get("type")));
    }

    public List<String> getSQL() {
        if (tableName == null || cols.isEmpty()) return List.of();

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        if (checkExists) sb.append("IF NOT EXISTS ");
        sb.append(tableName).append(" (\n");

        List<String> colDefs = new ArrayList<>();
        List<String> primaryKeys = new ArrayList<>();

        for (Map.Entry<String, Map<String, Object>> entry : cols.entrySet()) {
            String name = entry.getKey();
            Map<String, Object> properties = entry.getValue();
            ColType type = (ColType) properties.get("type");

            StringBuilder colDef = new StringBuilder();
            colDef.append(name).append(" ").append(type);

            if (Boolean.TRUE.equals(properties.get("nn"))) {
                colDef.append(" NOT NULL");
            }

            if (properties.containsKey("default")) {
                String defaultVal = (String) properties.get("default");

                // Strings und BLOBs mit Quotes
                if (type == ColType.TEXT || type == ColType.BLOB) {
                    defaultVal = "'" + defaultVal.replace("'", "''") + "'";
                }

                colDef.append(" DEFAULT ").append(defaultVal);
            }

            if (Boolean.TRUE.equals(properties.get("pk"))) {
                primaryKeys.add(name);
            }

            colDefs.add(colDef.toString());
        }

        sb.append(String.join(",\n", colDefs));

        if (!primaryKeys.isEmpty()) {
            sb.append(",\nPRIMARY KEY (")
                    .append(String.join(", ", primaryKeys))
                    .append(")");
        }

        sb.append("\n);");

        return List.of(sb.toString());
    }

    public boolean create(Statement statement) {
        List<String> sqlList = getSQL();
        if (sqlList.isEmpty()) return false;

        try {
            for (String sql : sqlList) {
                statement.execute(sql);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
