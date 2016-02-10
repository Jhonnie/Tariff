package program;
import java.sql.*;

public class MakeDB {
	public static void main(String[] args) {
		
		Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:test.db");
	      System.out.println("Opened database successfully");

	      stmt = c.createStatement();
	      String sql = "CREATE TABLE LEADERBOARD " +
	                   "(ID 			INT 	PRIMARY KEY  NOT NULL," +
	                   " NAME           TEXT    NOT NULL, " + 
	                   " STREET         TEXT    NOT NULL, " + 
	                   " HOUSENUMBER    TEXT    NOT NULL, " + 
	                   " CITY         	TEXT 	NOT NULL," +
	                   " POSTAL			TEXT	NOT NULL," +
	                   " CURRENTUSE		REAL	NOT NULL," +
	                   " CURRENTCOST	REAL	NOT NULL," +
	                   " NEWTARIFF		REAL	NOT NULL," +
	                   " TOP5			INT		NOT NULL)";
	      stmt.executeUpdate(sql);
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Table created successfully");
		
		
	}
}
