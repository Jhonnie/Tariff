package program;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JTabbedPane;
//import javax.swing.JToolBar;
//import javax.swing.JCheckBox;
import javax.swing.JTextField;
//import javax.swing.RowSorter;
//import javax.swing.SortOrder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
//import java.util.ArrayList;
//import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.sql.*;
//import javax.swing.ScrollPaneConstants;

// Create offer class
class offerNew {
	
	// Define class properties ug
	String name;
	String street;
	String housenr;
	String city;
	String postal;
	double usage;
	double expenditure;
	private double lowTariff = 0.12;
	private double highTariff = 0.14;
	private double cutoff = 100;  // cutoff point at 100 euros
	private double newTariff = highTariff;
	
	// Calculate current tariff
	public double currentTariff() {
		double oldTariff = expenditure/usage;
		return oldTariff;
	}
	
	// Determine new tariff
	public double calculate() {
		if (expenditure > cutoff) newTariff = lowTariff;
		//if (oldTariff < top5tariff) newTariff *= 0.95
		return newTariff;
	}
	
	// Estimate new costs
	public double estimate() {
		double estimation = usage*newTariff;		
		return estimation;
	}
	
	public boolean top5() {
		//if in top 5
		return false;
	}
}

// Create Java program
public class Newframe extends JFrame {
	
	// Define frame elements
	private static final long serialVersionUID = 1L;
	private static JPanel contentPane;
	private static JTable table;
	private static JTextField textField_name;
	private static JTextField textField_street;
	private static JTextField textField_nr;
	private static JTextField textField_city;
	private static JTextField textField_postal;
	private static JTextField textField_cu;
	private static JTextField textField_cc;
	private static JLabel costEntryError;
	private static JLabel usageEntryError;
	private static JPanel Calculator;
	private static JPanel Leaderboard;
	static Connection c = null;
    static Statement stmt = null;
    static offerNew offer1 = new offerNew();
    static String col[] = {"Name","Street","Nr", "City", "Postal", "Current Usage", "Current Costs", "New Tariff", "Top5"};
    static DefaultTableModel tableModel = new DefaultTableModel(col, 0);

	// Main
	public static void main(String[] args) {

		// Create frame
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Newframe frame = new Newframe();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	// Define frame	
	public Newframe() {
		
		// Set frame properties
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 638, 531);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		JFrame frame = new JFrame("JOptionPane showMessageDialog example");
		
		// Set default values for leaderboard
		
		// Declare tabs
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		// Leaderboard tab
		Leaderboard = new JPanel();
		tabbedPane.addTab("Leaderboard", null, Leaderboard, null);
			
			// populate table
			populateLeaderboard();
		
		// Calculater tab
		Calculator = new JPanel();
		tabbedPane.addTab("Calculator", null, Calculator, null);
		Calculator.setLayout(null);
		
			inputFields();
		
			// Output field
			JLabel outputLabel = new JLabel("");
			outputLabel.setBounds(16, 280, 379, 136);
			Calculator.add(outputLabel);
			
			// Error fields
			costEntryError = new JLabel("");
			costEntryError.setForeground(Color.RED);
			costEntryError.setBounds(258, 186, 22, 16);
			Calculator.add(costEntryError);
			
			usageEntryError = new JLabel("");
			usageEntryError.setForeground(Color.RED);
			usageEntryError.setBounds(109, 186, 21, 16);
			Calculator.add(usageEntryError);
			
			// Save to table
			JButton save = new JButton("Save");
			save.setEnabled(false);
			save.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					tableModel.addRow(new Object[]{offer1.name, offer1.street, offer1.housenr, offer1.city, offer1.postal, offer1.usage, offer1.expenditure, offer1.calculate(), 0});
					store();
					
					JOptionPane.showMessageDialog(frame,
						    "Correctly Saved!",
						    "Saved",
						    JOptionPane.PLAIN_MESSAGE);
				}
			});
			save.setBounds(177, 239, 128, 29);
			Calculator.add(save);
			
			// Calculate offer Button
			JButton calculate = new JButton("Calculate");
			calculate.addActionListener(new ActionListener() {
			    
				
			    // Onclick action
			    public void actionPerformed(ActionEvent e) {
					
			    	// Try to input strings
			    	try {
			    		 // enter strings in class
						 offer1.name = textField_name.getText();
						 offer1.street = textField_street.getText();
						 offer1.city = textField_city.getText();
						 offer1.postal = textField_postal.getText();
						 offer1.housenr = textField_nr.getText();
					} catch (Exception e1) {
						 // Not a string error
						 JOptionPane.showMessageDialog(frame,
						     "Please enter text.",
						     "Input error",
						     JOptionPane.ERROR_MESSAGE);
						 
					}
					
			    	// try to input numbers
					try {
						 // enter numbers in class
						 offer1.usage = Double.parseDouble(textField_cu.getText());
						 offer1.expenditure = Double.parseDouble(textField_cc.getText());
						 
						 //reset error fields
						 resetDoubleErrorFields();
						 outputLabel.setText(
								 "<html><p>"
								 + offer1.name+", your old tariff was "+ offer1.currentTariff()
								 + "<br>We can offer you a tariff of €"+ offer1.calculate()
								 + "<br>An estimation of your new montly cost is "+ offer1.estimate()
								 + "</p></html>");
						 save.setEnabled(true);
					} catch (Exception e2) {
						// Not a double error
						doubleErrorFields(textField_cu.getText(), textField_cc.getText());
						
						// Error dialog
						JOptionPane.showMessageDialog(frame,
						    "Please enter numbers for correct calculations.",
						    "Input error",
						    JOptionPane.ERROR_MESSAGE);
					}
					
				}
			});
			calculate.setBounds(6, 239, 128, 29);
			Calculator.add(calculate);
			
			// clear fields
			JButton clear = new JButton("Clear");
			clear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					// Replace all text fields with empty text
					clearFields(); //own method
				}
			});
			clear.setBounds(335, 239, 128, 29);
			Calculator.add(clear);	
		
	}
	public static void inputFields() {
		// Name field
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(6, 6, 61, 16);
		Calculator.add(lblName);
		
		textField_name = new JTextField();
		textField_name.setBounds(6, 22, 242, 26);
		Calculator.add(textField_name);
		textField_name.setColumns(1);
	
		// Street field
		JLabel label = new JLabel("Street");
		label.setBounds(6, 51, 61, 16);
		Calculator.add(label);
		
		textField_street = new JTextField();
		textField_street.setColumns(1);
		textField_street.setBounds(6, 71, 355, 26);
		Calculator.add(textField_street);
	
		// House nr field
		JLabel label_1 = new JLabel("Nr.");
		label_1.setBounds(420, 51, 61, 16);
		Calculator.add(label_1);
		
		textField_nr = new JTextField();
		textField_nr.setColumns(1);
		textField_nr.setBounds(420, 71, 153, 26);
		Calculator.add(textField_nr);
	
		// City field
		JLabel label_2 = new JLabel("City");
		label_2.setBounds(6, 102, 61, 16);
		Calculator.add(label_2);
		
		textField_city = new JTextField();
		textField_city.setColumns(1);
		textField_city.setBounds(6, 124, 357, 26);
		Calculator.add(textField_city);
	
		// Postal code field
		JLabel label_3 = new JLabel("Postal Code");
		label_3.setBounds(420, 102, 74, 16);
		Calculator.add(label_3);
	
		textField_postal = new JTextField();
		textField_postal.setColumns(1);
		textField_postal.setBounds(420, 124, 153, 26);
		Calculator.add(textField_postal);
		
		// Current usage field
		JLabel label_4 = new JLabel("Current usage");
		label_4.setBounds(6, 159, 128, 16);
		Calculator.add(label_4);
		
		textField_cu = new JTextField();
		textField_cu.setColumns(1);
		textField_cu.setBounds(6, 181, 103, 26);
		Calculator.add(textField_cu);
		
		// Current costs field
		JLabel label_5 = new JLabel("Current costs");
		label_5.setBounds(152, 162, 128, 16);
		Calculator.add(label_5);
		
		textField_cc = new JTextField();
		textField_cc.setColumns(1);
		textField_cc.setBounds(151, 181, 103, 26);
		Calculator.add(textField_cc);
	}

	public static void doubleErrorFields(String currentUsage, String currentCosts) {
		if (!isInteger(currentCosts)) {
			System.out.println(currentCosts);
			textField_cc.setBackground(Color.RED);
			textField_cc.setForeground(Color.WHITE);
			costEntryError.setText("!");
		}
		if (!isInteger(currentUsage)) {
			System.out.println(currentUsage);
			textField_cu.setBackground(Color.RED);
			textField_cu.setForeground(Color.WHITE);
			usageEntryError.setText("!");
		}
	}
	
	public static void resetDoubleErrorFields() {

		textField_cc.setBackground(Color.WHITE);
		textField_cc.setForeground(Color.BLACK);
		costEntryError.setText("");

		textField_cu.setBackground(Color.WHITE);
		textField_cu.setForeground(Color.BLACK);
		usageEntryError.setText("");
	}
	
	public static void clearFields() {
		
		textField_name.setText("");
		textField_street.setText("");
		textField_city.setText("");
		textField_nr.setText("");
		textField_postal.setText("");
		textField_cu.setText("");
		textField_cc.setText("");
		resetDoubleErrorFields();
	}
	
	public static void store() {
		    Connection c = null;
		    PreparedStatement stmt = null;

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
	
	public static void populateLeaderboard() {
		Connection c = null;
		PreparedStatement stmt = null;

		String selectSQL = "SELECT * FROM LEADERBOARD";

		try {
			Class.forName("org.sqlite.JDBC");
		    c = DriverManager.getConnection("jdbc:sqlite:test.db");
		    c.setAutoCommit(false);
			stmt = c.prepareStatement(selectSQL);
			
			
			// execute select SQL statement
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				String name = rs.getString("NAME");
				String street = rs.getString("STREET");
				String housenr = rs.getString("HOUSENUMBER");
				String city = rs.getString("CITY");
				String postal = rs.getString("POSTAL");
				int currentUse = rs.getInt("CURRENTUSE");
				int currentCost = rs.getInt("CURRENTCOST");
				double newTariff = rs.getDouble("NEWTARIFF");
				int top5 = rs.getInt("TOP5");

				tableModel.addRow(new Object[]{name, street, housenr, city, postal, currentUse, currentCost, newTariff, top5});
				
			}
			
			table = new JTable(tableModel);
			table.setFillsViewportHeight(true);
			table.setAutoCreateRowSorter(true);
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
			table.setRowSorter(sorter);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.getColumnModel().getColumn(0).setPreferredWidth(27);
			table.getColumnModel().getColumn(1).setPreferredWidth(120);
			table.getColumnModel().getColumn(2).setPreferredWidth(100);
			table.getColumnModel().getColumn(3).setPreferredWidth(90);
			table.getColumnModel().getColumn(4).setPreferredWidth(90);
			table.getColumnModel().getColumn(6).setPreferredWidth(120);
			
			JScrollPane scrollPane = new JScrollPane(table);

			Leaderboard.add(scrollPane);
			
			stmt.close();
		    c.commit();
		    c.close();
		} catch ( Exception e) {

			System.out.println(e.getMessage());
		}
	}
	
	public static boolean isInteger(String str) {
	    if (str == null) {
	        return false;
	    }
	    int length = str.length();
	    if (length == 0) {
	        return false;
	    }
	    int i = 0;
	    if (str.charAt(0) == '-') {
	        if (length == 1) {
	            return false;
	        }
	        i = 1;
	    }
	    for (; i < length; i++) {
	        char c = str.charAt(i);
	        if (c < '0' || c > '9') {
	            return false;
	        }
	    }
	    return true;
	}
}
