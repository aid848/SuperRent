package ca.ubc.cs304.database;

import ca.ubc.cs304.model.BranchModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
	private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";
	
	public Connection connection = null;
	
	public DatabaseConnectionHandler() {
		try {
			// Load the Oracle JDBC driver
			// Note that the path could change for new drivers
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}
	
	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	public void deleteBranch(int branchId) {
		try {
			PreparedStatement ps = connection.prepareStatement("DELETE FROM branch WHERE branch_id = ?");
			ps.setInt(1, branchId);
			
			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				System.out.println(WARNING_TAG + " Branch " + branchId + " does not exist!");
			}
			
			connection.commit();
	
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}
	
	public void insertBranch(BranchModel model) {
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO branch VALUES (?,?,?,?,?)");
			ps.setInt(1, model.getId());
			ps.setString(2, model.getName());
			ps.setString(3, model.getAddress());
			ps.setString(4, model.getCity());
			if (model.getPhoneNumber() == 0) {
				ps.setNull(5, java.sql.Types.INTEGER);
			} else {
				ps.setInt(5, model.getPhoneNumber());
			}

			ps.executeUpdate();
			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}
	
	public BranchModel[] getBranchInfo() {
		ArrayList<BranchModel> result = new ArrayList<BranchModel>();
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM branch");
		
//    		// get info on ResultSet
//    		ResultSetMetaData rsmd = rs.getMetaData();
//
//    		System.out.println(" ");
//
//    		// display column names;
//    		for (int i = 0; i < rsmd.getColumnCount(); i++) {
//    			// get column name and print it
//    			System.out.printf("%-15s", rsmd.getColumnName(i + 1));
//    		}
			
			while(rs.next()) {
				BranchModel model = new BranchModel(rs.getString("branch_addr"),
													rs.getString("branch_city"),
													rs.getInt("branch_id"),
													rs.getString("branch_name"),
													rs.getInt("branch_phone"));
				result.add(model);
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}	
		
		return result.toArray(new BranchModel[result.size()]);
	}
	
	public void updateBranch(int id, String name) {
		try {
		  PreparedStatement ps = connection.prepareStatement("UPDATE branch SET branch_name = ? WHERE branch_id = ?");
		  ps.setString(1, name);
		  ps.setInt(2, id);
		
		  int rowCount = ps.executeUpdate();
		  if (rowCount == 0) {
		      System.out.println(WARNING_TAG + " Branch " + id + " does not exist!");
		  }
	
		  connection.commit();
		  
		  ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}	
	}
	
	public boolean login(String username, String password) {
		try {
			if (connection != null) {
				connection.close();
			}
	
			connection = DriverManager.getConnection(ORACLE_URL, username, password);
			connection.setAutoCommit(false);
	
			System.out.println("\nConnected to Oracle!");
			return true;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return false;
		}
	}

	private void rollbackConnection() {
		try  {
			connection.rollback();	
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	public void firstTimeSetupOld() {
		firstTimeSetupBool(1);
		firstTimeSetupBool(2);
	}

	public Boolean firstTimeSetupBool(int stage) throws FileNotFoundException {
		String path;
		Boolean successFlag = false;
		if(stage == 1) {
			path = "setuptables.sql";
		} else if (stage == 2) {
			path = "populatetables.sql";
		}else {
			path = "droptables.sql";
		}
		ArrayList<String> statements = new ArrayList<String>();
		try {
			Scanner in = new Scanner(new File("SQL\\" + path));
			in.useDelimiter(";");
			while (in.hasNext())
				statements.add(in.next());
			for (String statement: statements) {
				PreparedStatement p = connection.prepareStatement(statement);
				p.executeUpdate();
				connection.commit();
				p.close();
			}
			successFlag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return successFlag;
		}
		return successFlag;
	}

	//execute query and return result tuples as formatted string
	public String executeStringQuery(String stringQuery){
		String result = "";
		try {
			Statement stmt = connection.createStatement();
			String lowerCase = stringQuery.toLowerCase();
			if (lowerCase.startsWith("insert") || lowerCase.startsWith("delete") || lowerCase.startsWith("update")){
				stmt.executeUpdate(stringQuery);
				connection.commit();
				result = "Executed";
			}else {
				ResultSet rs = stmt.executeQuery(stringQuery);
				result = prettyPrintTuples(rs);
				rs.close();
				stmt.close();
			}

		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			result = EXCEPTION_TAG + " " + e.getMessage();
		}

		return result;
	}

	public String prettyPrintTuples(ResultSet rs) throws SQLException {
		ArrayList<String> columnNames = new ArrayList<>();
		ArrayList<ArrayList<String>> rows = new ArrayList<>();
		ArrayList<Integer> maxLengths = new ArrayList<>();
		String result = "";

		ResultSetMetaData rsmd = rs.getMetaData();

		for (int i = 0; i < rsmd.getColumnCount(); i++) {
			columnNames.add(rsmd.getColumnName(i + 1));
			String columnName = rsmd.getColumnName(i + 1);
			maxLengths.add(i, columnName.length());
		}

		while (rs.next()) {
			ArrayList<String> tempAttributeList = new ArrayList<>();
			for (int i = 0; i < columnNames.size(); i++) {
				String columnName = columnNames.get(i);
				String attribute = rs.getObject(columnName) == null ? "null" : rs.getObject(columnName).toString();
				tempAttributeList.add(attribute);
				if (attribute.length() > maxLengths.get(i)) {
					maxLengths.set(i, attribute.length());
				}
			}
			rows.add(tempAttributeList);
		}

		for (int i = 0; i < columnNames.size(); i++) {
			String columnName = columnNames.get(i);
			result += String.format("%1$" + maxLengths.get(i) + "s", columnName) + " | ";
		}
		result += "\n";
		for (ArrayList<String> row : rows) {
			for (int i = 0; i < row.size(); i++) {
				String attribute = row.get(i);
				result += String.format("%1$" + maxLengths.get(i) + "s", attribute) + " | ";
			}
			result += "\n";
		}
		return result;
	}

}
