package dev.silal.soulnotes.config.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public interface Database {

    void connect() throws Exception;
    void disconnect();

    void setup() throws Exception;

    Connection getConnection();
    Statement getStatement();

}
