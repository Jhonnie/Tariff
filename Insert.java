package program;
import java.sql.*;

public class Insert
{
  public static void main( String args[] )
  {
    Connection c = null;
    PreparedStatement stmt = null;
    offerNew offer1 = new offerNew();
    
    offer1.name = "Jurgen";
	offer1.street = "Bergselaan";
	offer1.city = "Rotterdam";
	offer1.postal = "test";
	offer1.housenr = "313";
	offer1.usage = 2;
	offer1.expenditure = 3;
    
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:test.db");
      c.setAutoCommit(false);
      System.out.println("Opened database successfully");

      stmt = c.prepareStatement("INSERT INTO LEADERBOARD (NAME,STREET,HOUSENUMBER,CITY,POSTAL,CURRENTUSE,CURRENTCOST,NEWTARIFF,TOP5) VALUES (?,?,?,?,?,?,?,?,1)");
      stmt.setString(1, offer1.name);
      stmt.setString(2, offer1.street);
      stmt.setString(3, offer1.housenr);
      stmt.setString(4, offer1.city);
      stmt.setString(5, offer1.postal);
      stmt.setDouble(6, offer1.usage);
      stmt.setDouble(7, offer1.expenditure);
      stmt.setDouble(8, offer1.calculate());
      stmt.executeUpdate();

      stmt.close();
      c.commit();
      c.close();
    } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
    }
    System.out.println("Records created successfully");
  }
}