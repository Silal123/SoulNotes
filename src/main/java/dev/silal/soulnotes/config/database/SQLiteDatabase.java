package dev.silal.soulnotes.config.database;

import dev.silal.soulnotes.utils.database.ColType;
import dev.silal.soulnotes.utils.database.DatabaseTableCreator;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SQLiteDatabase implements Database {

    private Connection connection;
    private Statement statement;

    private final File dbFile;

    public SQLiteDatabase(File dbFile) {
        this.dbFile = dbFile;
    }


    @Override
    public void connect() throws Exception {
        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        connection = DriverManager.getConnection(url);
        if (connection != null) {
            statement = connection.createStatement();
        }
    }

    @Override
    public void disconnect() {
        try {
            if (connection != null) connection.close();
        } catch (Exception ignored) {}
    }

    @Override
    public void setup() throws Exception {
        statement.execute("CREATE TABLE IF NOT EXISTS notes (id INTEGER PRIMARY KEY AUTOINCREMENT, creator TEXT, message TEXT, created_at TEXT, x DOUBLE, y DOUBLE, z DOUBLE, world TEXT);");
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public Statement getStatement() {
        return statement;
    }
}
