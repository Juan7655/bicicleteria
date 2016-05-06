/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bicicol;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author OscarLopez
 */
public class DataBase {
    
    public DataBase(){
        
    }
    
     public Connection getConnection()  {
        try {
            
            System.out.println("Intentando conectar");
            String driver = "com.mysql.jdbc.Driver";
            //Esta opción es para la base de datos local
            String url = "jdbc:mysql://127.0.0.1:3306/bicicol";
            
            //Esta opción es para la Base de datos en Google SQL Cloud
                // String url = "jdbc:mysql://104.196.97.175:3306";
            String username = "root";
            String password = "OscarSQL4596";
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected");
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Not connected: " + e);
        }
        return null;
    }
     
     public void post (String tabla, String datos){
         
          try (Connection conn = getConnection()) {
                PreparedStatement posted = conn.prepareStatement("USE bicicol");
                posted.executeUpdate();
                
                String query="INSERT INTO "+ tabla +" VALUE("+ datos +");";
                
                posted = conn.prepareStatement(query);
                posted.executeUpdate();
                System.out.println("Insert completed.");
            } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
         
     }
    
}
