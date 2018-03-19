package net.web_kot.teamdev.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;

public class Database {
    
    private final Connection connection;
    private Statement statement;
    
    public Database(File file) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + file.getPath());
        
        statement = connection.createStatement();
        statement.setQueryTimeout(30); // 30 seconds timeout
        
        init();
    }
    
    private void init() throws SQLException {
        statement.execute("PRAGMA foreign_keys = ON");
        
        InputStreamReader reader = new InputStreamReader(this.getClass().getResourceAsStream("/init.sql"));
        BufferedReader in = new BufferedReader(reader);
        
        ArrayList<String> queries = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        in.lines().forEach((line) -> {
            String trimmed = line.trim();
            if(trimmed.equals("")) return;
            
            if(!trimmed.equals("---")) {
                builder.append(line);
                builder.append("\n");
            } else {
                queries.add(builder.toString());
                builder.setLength(0);
            }
        });
        if(builder.length() != 0) queries.add(builder.toString());
        
        for(String query : queries) statement.execute(query);
    }
    
    public Statement getStatement() {
        return statement;
    }
    
    public void close() throws SQLException {
        connection.close();
    }
    
}
