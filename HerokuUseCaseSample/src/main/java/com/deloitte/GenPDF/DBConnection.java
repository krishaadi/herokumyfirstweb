package com.deloitte.GenPDF;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
 
/**
 *
 * @author postgresqltutorial.com
 */
public class DBConnection {
	private final String url = "jdbc:postgresql://ec2-3-211-48-92.compute-1.amazonaws.com:5432/dcts2fdesu0pdk";//127.0.0.1:5432/postgres";
    private final String user = "zpaxtrhepdehvw";
    private final String password = "09e0e0c3ae58d30581580d77de02b64df94ef7aed355634c56c524bac6af9851";
 
    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
 
        return conn;
    }
 

}

