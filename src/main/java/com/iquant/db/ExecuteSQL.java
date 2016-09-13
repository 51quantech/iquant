package com.iquant.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by yonggangli on 2016/8/29.
 */
public class ExecuteSQL {

    public static void main(String[] args) throws IOException {

        System.out.println(ExecuteSQL.class.getResource("/quant.sql"));
        InputStream in = ExecuteSQL.class.getResourceAsStream("/quant.sql");

        BufferedReader read = new BufferedReader(new InputStreamReader(in));
        String line = null;
        StringBuilder sql = new StringBuilder();
        while((line = read.readLine()) != null){
            sql.append(line).append("\n");
        }
        read.close();

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:quant.db");
            stmt = c.createStatement();
            stmt.executeUpdate(sql.toString());
            stmt.close();
            c.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");

    }

}
