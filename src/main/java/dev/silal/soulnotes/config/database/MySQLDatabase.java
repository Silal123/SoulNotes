package dev.silal.soulnotes.config.database;

import dev.silal.soulnotes.utils.database.ColType;
import dev.silal.soulnotes.utils.database.DatabaseTableCreator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MySQLDatabase implements Database {

    private Connection connection;
    private Statement statement;

    private final String host, database, user, password;
    private final int port;

    public MySQLDatabase(String host, int port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }


    @Override
    public void connect() throws Exception {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false";
        connection = DriverManager.getConnection(url, user, password);
        if (connection != null) {
            statement = connection.createStatement();
        }
    }

    @Override
    public void disconnect() {
        try {
            if (connection != null) connection.close();
        } catch (Exception e) {}
    }

    @Override
    public void setup() throws Exception {
        statement.execute("CREATE TABLE IF NOT EXISTS notes (id BIGINT PRIMARY KEY AUTO_INCREMENT, creator VARCHAR(36), message TEXT, created_at TEXT, x DOUBLE, y DOUBLE, z DOUBLE, world TEXT);");
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
