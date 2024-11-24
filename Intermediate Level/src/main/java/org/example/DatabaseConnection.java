package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
   public static Connection getConnection() {
       String url = "jdbc:mysql://localhost:3306/event_management_system";
       String user = "root";
       String password = "Awan@2212";

       try {
           return DriverManager.getConnection(url, user, password);
       } catch (SQLException e) {
           e.printStackTrace(); // Print stack trace for debugging
           throw new RuntimeException("Error connecting to the database: " + e.getMessage());
       }
   }
      
  
  
}

