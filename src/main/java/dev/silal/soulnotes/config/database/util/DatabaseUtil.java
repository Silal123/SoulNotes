package dev.silal.soulnotes.config.database.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class DatabaseUtil {

    public static Date getDate(String text) {
        if (text == null) return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDateString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static void setDouble(String table, String column, double value, String where, Statement statement) throws SQLException {
        String query = "UPDATE " + table + " SET " + column + " = " + value + " WHERE " + where;
        statement.executeUpdate(query);
    }

    public static Optional<Double> getDouble(String table, String column, String where, Statement statement) throws SQLException {
        String query = "SELECT " + column + " FROM " + table + " WHERE " + where;
        ResultSet result = statement.executeQuery(query);
        if (result.next()) {
            double val = result.getDouble(column);
            if (result.wasNull()) return Optional.empty();
            return Optional.of(val);
        }
        return Optional.empty();
    }

    public static void setLong(String table, String column, long value, String where, Statement statement) throws SQLException {
        String query = "UPDATE " + table + " SET " + column + " = " + value + " WHERE " + where;
        statement.executeUpdate(query);
    }

    public static Optional<Long> getLong(String table, String column, String where, Statement statement) throws SQLException {
        String query = "SELECT " + column + " FROM " + table + " WHERE " + where;
        ResultSet result = statement.executeQuery(query);
        if (result.next()) {
            long val = result.getLong(column);
            if (result.wasNull()) return Optional.empty();
            return Optional.of(val);
        }
        return Optional.empty();
    }

    public static void setText(String table, String column, String value, String where, Statement statement) throws SQLException {
        String query = "UPDATE " + table + " SET " + column + " = '" + value.replace("'", "''") + "' WHERE " + where;
        statement.executeUpdate(query);
    }

    public static Optional<String> getText(String table, String column, String where, Statement statement) throws SQLException {
        String query = "SELECT " + column + " FROM " + table + " WHERE " + where;
        ResultSet result = statement.executeQuery(query);
        if (result.next()) {
            String value = result.getString(column);
            if (result.wasNull()) return Optional.empty();
            return Optional.ofNullable(value);
        }
        return Optional.empty();
    }

    public static boolean entryExists(String table, String where, Statement statement) throws SQLException {
        String query = "SELECT 1 FROM " + table + " WHERE " + where + " LIMIT 1";
        ResultSet result = statement.executeQuery(query);
        return result.next();
    }

    public static List<DatabaseEntry> getAllEntries(String tableName, String where, Statement statement) throws SQLException {
        List<DatabaseEntry> entries = new ArrayList<>();

        String sql = "SELECT * FROM " + tableName + (where == null ? "": " WHERE " + where);
        ResultSet rs = statement.executeQuery(sql);

        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        while (rs.next()) {
            DatabaseEntry entry = new DatabaseEntry();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = meta.getColumnLabel(i);
                Object value = rs.getObject(i);
                entry.put(columnName, value);
            }

            entries.add(entry);
        }

        return entries;
    }

    public static DatabaseEntry getEntry(String table, String where, Statement statement) throws SQLException {
        String sql = "SELECT 1 FROM " + table + (where == null ? "": " WHERE " + where) + " LIMIT 1";
        ResultSet rs = statement.executeQuery(sql);

        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        if (rs.next()) {
            DatabaseEntry entry = new DatabaseEntry();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = meta.getColumnLabel(i);
                Object value = rs.getObject(i);
                entry.put(columnName, value);
            }

            return entry;
        }
        return null;
    }

    public static void delete(String table, String where, Statement statement) {
        String sql = "DELETE FROM " + table + " WHERE " + where + ";";
        try {
            int rowsAffected = statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static long countEntries(String table, String where, Statement statement) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM " + table + " WHERE " + where;
        ResultSet rs = statement.executeQuery(query);
        if (rs.next()) {
            return rs.getLong("count");
        }
        return 0;
    }

}
