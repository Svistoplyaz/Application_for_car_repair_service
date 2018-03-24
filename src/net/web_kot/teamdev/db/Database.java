package net.web_kot.teamdev.db;

import org.intellij.lang.annotations.Language;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;

public class Database {
    
    private boolean debug = false;
    
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
    
    public Database setDebug(boolean d) {
        debug = d;
        return this;
    }
    
    public int getLastInsertRowId() throws SQLException {
        return statement.getGeneratedKeys().getInt(1);
    }
    
    public void exec(@Language("SQL")String sql, Object... args) throws SQLException {
        String query = formatQuery(sql, args);
        if(debug) System.out.println("> " + query);
        statement.execute(query);
    }
    
    public int insert(@Language("SQL")String sql, Object... args) throws SQLException {
        exec(sql, args);
        return getLastInsertRowId();
    }
    
    public void update(@Language("SQL")String sql, Object... args) throws SQLException {
        String query = formatQuery(sql, args);
        if(debug) System.out.println("> " + query);
        statement.executeUpdate(query);
    }
    
    public ResultSet select(@Language("SQL")String sql, Object... args) throws SQLException {
        String query = formatQuery(sql, args);
        if(debug) System.out.println("> " + query);
        return statement.executeQuery(query);
    }
    
    public String formatQuery(@Language("SQL")String sql, Object... args) {
        return String.format(sql, wrapArguments(args));
    }
    
    private Object[] wrapArguments(Object[] args) {
        Object[] nw = new Object[args.length];
        for(int i = 0; i < args.length; i++)
            if(args[i] == null)
                nw[i] = "NULL";
            else if(args[i] instanceof String)
                nw[i] = "\"" + ((String)args[i]).replace("\"", "").trim() + "\"";
            else
                nw[i] = args[i];
        return nw;
    }
    
}
