/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, chools | Templates
 * and open the template in the editor.
 */
package scheduleapp.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author erick
 */
public class DBConnection {
    private static final String databaseName = "U05wO0";
    private static final String DB_URL = "jdbc:mysql://52.206.157.109:3306/" + databaseName + "?zeroDateTimeBehavior=convertToNull";
    private static final String userName = databaseName;
    private static final String password = "53688629632";
    private static final String driver = "com.mysql.jdbc.Driver";
    
    public static Connection conn;
    
    public static void makeConnection() throws ClassNotFoundException, SQLException{
        Class.forName(driver);
        conn = DriverManager.getConnection(DB_URL, userName, password);
        System.out.println("Connection Successful");
            
    }
    
     public static void closeConnection() throws ClassNotFoundException, SQLException{
         conn.close();
         System.out.println("Connection Closed");
     }
     
         
     }
    

