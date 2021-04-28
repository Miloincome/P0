package com.app.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreConnection {
    private PostgreConnection(){}
    private static Connection connection;
    static {
        //Step 1 - Load Driver
        try {
            Class.forName("org.postgresql.Driver");
            //Step2 - Open Connection
            String url="jdbc:postgresql://localhost:5432/postgres";
            String username="postgres";
            String password="Mascarade007!";
            connection= DriverManager.getConnection(url,username,password);
            System.out.println("Connection/Ping Success");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
    }
    public static Connection getConnection(){
        return connection;
    }

}
/* How to create a singleton java class
disable constructor by making it private
to deny access to the object.
 */
