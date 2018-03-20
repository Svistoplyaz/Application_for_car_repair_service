package net.web_kot.teamdev.db;

import org.intellij.lang.annotations.Language;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;

public class Database {
    
    private final Connection connection;
    private Statement statement;
    
    private final Model model = new Model(this);
    
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
    
    public void close() throws SQLException {
        connection.close();
    }
    
    public Model getModel() {
        return model;
    }
    
    public int getLastInsertRowId() throws SQLException {
        return statement.getGeneratedKeys().getInt(1);
    }
    
    public void exec(@Language("SQL")String sql, Object... args) throws SQLException {
        statement.execute(formatQuery(sql, args));
    }
    
    public int insert(@Language("SQL")String sql, Object... args) throws SQLException {
        exec(sql, args);
        return getLastInsertRowId();
    }
    
    public void update(@Language("SQL")String sql, Object... args) throws SQLException {
        statement.executeUpdate(formatQuery(sql, args));
    }
    
    public ResultSet select(@Language("SQL")String sql, Object... args) throws SQLException {
        return statement.executeQuery(formatQuery(sql, args));
    }
    
    public String formatQuery(@Language("SQL")String sql, Object... args) {
        wrapArguments(args);
        return String.format(sql, args);
    }
    
    private void wrapArguments(Object[] args) {
        for(int i = 0; i < args.length; i++)
            if(args[i] == null)
                args[i] = "NULL";
            else if(args[i] instanceof String)
                args[i] = "'" + args[i] + "'";
    }
    
}
