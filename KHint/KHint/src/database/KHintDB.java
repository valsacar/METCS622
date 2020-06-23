/**
 * 
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import fragments.Fragment;
import hints.Hint;
import targets.DragTarget;

/**
 * @author Joseph Monk
 *
 * Handles all database interfaces.  If the supplied database does not exist then it will create one with dummy data for testing.
 * 
 * Requires SqlLight JDBC.
 */
public class KHintDB {
	private static final String PROBLEM_TABLE = "Problems";
	private static final String TARGET_TABLE = "Targets";
	private static final String FRAG_TABLE = "Fragments";
	private static final String HINT_TABLE = "Hints";
	
	private Connection dbConn;
	
	public KHintDB(String dbName) {
		String url = "jdbc:sqlite:db/" + dbName;
		
		try {  
			Connection conn = DriverManager.getConnection(url); // This should create the database if it doesn't exist
            if (conn != null) {
            	this.dbConn = conn;
                // Check to see if the DB existed or not
            	boolean exists = conn.getMetaData().getTables(null, null, PROBLEM_TABLE, null).next();
                if (!exists) { // Wasn't there, so we'll populate it
                	createTables();
                	createDummy();
                }                
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	/*
	 * Precondition: sqllight database connection is available in dbConn
	 * Postcondition: Tables in the database are created for targets, fragments and hints.
	 */
	private void createTables() {
		try {
			Statement prob = this.dbConn.createStatement();
			Statement target = this.dbConn.createStatement();
			Statement frag = this.dbConn.createStatement();
			Statement hint = this.dbConn.createStatement();
			
			String sql = "CREATE TABLE " + PROBLEM_TABLE + "(" + 
					"prob_id INTEGER NOT NULL PRIMARY KEY, " + 
					"prob_title TEXT, " + 
					"prob_desc TEXT)";
			prob.executeUpdate(sql);
			
			sql = "CREATE TABLE " + FRAG_TABLE + "(" + 
					"frag_id INTEGER NOT NULL PRIMARY KEY, " + 
					"frag_text TEXT)";
			
			frag.executeUpdate(sql);
			
			sql = "CREATE TABLE " + TARGET_TABLE + "(" +
					"target_id INTEGER NOT NULL PRIMARY KEY, " + 
					"problem_id INTEGER NOT NULL REFERENCES " + PROBLEM_TABLE + "(prob_id), " +
					"target_answer INTEGER NOT NULL REFERENCES " + FRAG_TABLE + "(frag_id))";
			
			target.executeUpdate(sql);
			
			sql = "CREATE TABLE " + HINT_TABLE + "(" +
					"hint_id INTEGER NOT NULL PRIMARY KEY, " +
					"target_id INTEGER REFERENCES " + TARGET_TABLE + "(target_id), " +
					"frag_id INTEGER REFERENCES " + FRAG_TABLE + "(frag_id), " +
					"hint_text TEXT, " + 
					"hint_weight INTEGER NOT NULL)"; 
			
			hint.executeUpdate(sql);
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Precondition: dbConn is a valid database connection and tables for target, fragment and hint exist
	 * Postcondition: Dummy data is populated in the database.
	 */
	private void createDummy() {
		try {
			PreparedStatement prob = this.dbConn.prepareStatement("INSERT INTO " + PROBLEM_TABLE +
					" (prob_title, prob_desc) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			PreparedStatement target = this.dbConn.prepareStatement("INSERT INTO " + TARGET_TABLE +
					" (problem_id, target_answer) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			PreparedStatement frag = this.dbConn.prepareStatement("INSERT INTO " + FRAG_TABLE + 
					" (frag_text) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			
			PreparedStatement hint = this.dbConn.prepareStatement("INSERT INTO " + HINT_TABLE +
					" (target_id, frag_id, hint_text, hint_weight) VALUES (?, ?, ?, ?)");
			
			long fragID = 0, targetID = 0, probID = 0;
			
			prob.setString(1, "Gettysburg Address");
			prob.setString(2, "Ever since Lincoln wrote it in 1864, this version has been the most often reproduced, notably on" +
					" the walls of the Lincoln Memorial in Washington. It is named after Colonel Alexander Bliss, stepson of historian" +
					" George Bancroft. Bancroft asked President Lincoln for a copy to use as a fundraiser for soldiers. However," +
					" because Lincoln wrote on both sides of the paper, the speech could not be reprinted, so Lincoln made another" + 
					" copy at Bliss's request. It is the last known copy written by Lincoln and the only one signed and dated by him." +
					" Today it is on display at the Lincoln Room of the White House.");
			prob.executeUpdate();
			probID = prob.getGeneratedKeys().getLong(1); // Get the ID it created
			
			// First fragment
			frag.setString(1, "Four score and seven years ago our fathers brought forth on this continent, a new nation, conceived in Liberty," +
					" and dedicated to the proposition that all men are created equal.");
			frag.executeUpdate();
			fragID = frag.getGeneratedKeys().getLong(1); // Get the ID it created
			
			target.setLong(1, probID);
			target.setLong(2, fragID);
			target.executeUpdate();
			targetID = target.getGeneratedKeys().getLong(1);
			
			hint.setLong(1, targetID);
			hint.setLong(2, 0);
			hint.setString(3, "You might want to put the first thing here");
			hint.setInt(4, 40);
			hint.executeUpdate();
			
			hint.setLong(1, 0);
			hint.setLong(2, fragID);
			hint.setString(3, "Might come before other things");
			hint.setInt(4, 10);
			hint.executeUpdate();
			
			// Second fragment			
			frag.setString(1, "Now we are engaged in a great civil war, testing whether that nation, or any nation so conceived and so dedicated, can long endure.");
			frag.executeUpdate();
			fragID = frag.getGeneratedKeys().getLong(1); // Get the ID it created
			
			target.setLong(1, probID);
			target.setLong(2, fragID);
			target.executeUpdate();
			targetID = target.getGeneratedKeys().getLong(1);
			
			hint.setLong(1, targetID);
			hint.setLong(2, 0);
			hint.setString(3, "You might want to put the first thing here");
			hint.setInt(4, 40);
			hint.executeUpdate();
			
			hint.setLong(1, 0);
			hint.setLong(2, fragID);
			hint.setString(3, "Might come before other things");
			hint.setInt(4, 10);
			hint.executeUpdate();
			
			// Third fragment			
			frag.setString(1, "We are met on a great battle-field of that war. We have come to dedicate a portion of that field, as a final resting place" + 
					" for those who here gave their lives that that nation might live. It is altogether fitting and proper that we should do this.");
			frag.executeUpdate();
			fragID = frag.getGeneratedKeys().getLong(1); // Get the ID it created
			
			target.setLong(1, probID);
			target.setLong(2, fragID);
			target.executeUpdate();
			targetID = target.getGeneratedKeys().getLong(1);
			
			hint.setLong(1, targetID);
			hint.setLong(2, 0);
			hint.setString(3, "You might want to put the first thing here");
			hint.setInt(4, 40);
			hint.executeUpdate();
			
			hint.setLong(1, 0);
			hint.setLong(2, fragID);
			hint.setString(3, "Might come before other things");
			hint.setInt(4, 10);
			hint.executeUpdate();
			
			// Fourth fragment			
			frag.setString(1, "But, in a larger sense, we can not dedicate -- we can not consecrate -- we can not hallow -- this ground.");
			frag.executeUpdate();
			fragID = frag.getGeneratedKeys().getLong(1); // Get the ID it created
			
			target.setLong(1, probID);
			target.setLong(2, fragID);
			target.executeUpdate();
			targetID = target.getGeneratedKeys().getLong(1);
			
			hint.setLong(1, targetID);
			hint.setLong(2, 0);
			hint.setString(3, "You might want to put the first thing here");
			hint.setInt(4, 40);
			hint.executeUpdate();
			
			hint.setLong(1, 0);
			hint.setLong(2, fragID);
			hint.setString(3, "Might come before other things");
			hint.setInt(4, 10);
			hint.executeUpdate();
			
			// Fifth fragment			
			frag.setString(1, "The brave men, living and dead, who struggled here, have consecrated it, far above our poor power to add or detract.");
			frag.executeUpdate();
			fragID = frag.getGeneratedKeys().getLong(1); // Get the ID it created
			
			target.setLong(1, probID);
			target.setLong(2, fragID);
			target.executeUpdate();
			targetID = target.getGeneratedKeys().getLong(1);
			
			hint.setLong(1, targetID);
			hint.setLong(2, 0);
			hint.setString(3, "You might want to put the first thing here");
			hint.setInt(4, 40);
			hint.executeUpdate();
			
			hint.setLong(1, 0);
			hint.setLong(2, fragID);
			hint.setString(3, "Might come before other things");
			hint.setInt(4, 10);
			hint.executeUpdate();
			
			// Sixth fragment			
			frag.setString(1, "The world will little note, nor long remember what we say here, but it can never forget what they did here. It is for us the" + 
					" living, rather, to be dedicated here to the unfinished work which they who fought here have thus far so nobly advanced. It is rather" + 
					" for us to be here dedicated to the great task remaining before us --");
			frag.executeUpdate();
			fragID = frag.getGeneratedKeys().getLong(1); // Get the ID it created
			
			target.setLong(1, probID);
			target.setLong(2, fragID);
			target.executeUpdate();
			targetID = target.getGeneratedKeys().getLong(1);
			
			hint.setLong(1, targetID);
			hint.setLong(2, 0);
			hint.setString(3, "You might want to put the first thing here");
			hint.setInt(4, 40);
			hint.executeUpdate();
			
			hint.setLong(1, 0);
			hint.setLong(2, fragID);
			hint.setString(3, "Might come before other things");
			hint.setInt(4, 10);
			hint.executeUpdate();
			
			// Seventh fragment			
			frag.setString(1, "that from these honored dead we take increased devotion to that cause for which they gave the last full measure of devotion --");
			frag.executeUpdate();
			fragID = frag.getGeneratedKeys().getLong(1); // Get the ID it created
			
			target.setLong(1, probID);
			target.setLong(2, fragID);
			target.executeUpdate();
			targetID = target.getGeneratedKeys().getLong(1);
			
			hint.setLong(1, targetID);
			hint.setLong(2, 0);
			hint.setString(3, "You might want to put the first thing here");
			hint.setInt(4, 40);
			hint.executeUpdate();
			
			hint.setLong(1, 0);
			hint.setLong(2, fragID);
			hint.setString(3, "Might come before other things");
			hint.setInt(4, 10);
			hint.executeUpdate();
			
			// Eigth fragment			
			frag.setString(1, "that we here highly resolve that these dead shall not have died in vain --");
			frag.executeUpdate();
			fragID = frag.getGeneratedKeys().getLong(1); // Get the ID it created
			
			target.setLong(1, probID);
			target.setLong(2, fragID);
			target.executeUpdate();
			targetID = target.getGeneratedKeys().getLong(1);
			
			hint.setLong(1, targetID);
			hint.setLong(2, 0);
			hint.setString(3, "You might want to put the first thing here");
			hint.setInt(4, 40);
			hint.executeUpdate();
			
			hint.setLong(1, 0);
			hint.setLong(2, fragID);
			hint.setString(3, "Might come before other things");
			hint.setInt(4, 10);
			hint.executeUpdate();
			
			// ninth fragment			
			frag.setString(1, "that this nation, under God, shall have a new birth of freedom -- ");
			frag.executeUpdate();
			fragID = frag.getGeneratedKeys().getLong(1); // Get the ID it created
			
			target.setLong(1, probID);
			target.setLong(2, fragID);
			target.executeUpdate();
			targetID = target.getGeneratedKeys().getLong(1);
			
			hint.setLong(1, targetID);
			hint.setLong(2, 0);
			hint.setString(3, "You might want to put the first thing here");
			hint.setInt(4, 40);
			hint.executeUpdate();
			
			hint.setLong(1, 0);
			hint.setLong(2, fragID);
			hint.setString(3, "Might come before other things");
			hint.setInt(4, 10);
			hint.executeUpdate();
			
			// Tenth fragment			
			frag.setString(1, "and that government of the people, by the people, for the people, shall not perish from the earth.");
			frag.executeUpdate();
			fragID = frag.getGeneratedKeys().getLong(1); // Get the ID it created
			
			target.setLong(1, probID);
			target.setLong(2, fragID);
			target.executeUpdate();
			targetID = target.getGeneratedKeys().getLong(1);
			
			hint.setLong(1, targetID);
			hint.setLong(2, 0);
			hint.setString(3, "You might want to put the first thing here");
			hint.setInt(4, 40);
			hint.executeUpdate();
			
			hint.setLong(1, 0);
			hint.setLong(2, fragID);
			hint.setString(3, "Might come before other things");
			hint.setInt(4, 10);
			hint.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Precondition: dbConn points to a valid database
	 * Postcondition: Returns a KHintProblem with all targets/fragments and hints loaded from the database.
	 * 
	 * Note: Currently takes in a problemNumber, this is for future expansion so more than 1 problem can be stored in the database
	 */
	public ArrayList<DragTarget> loadProblem(int problemNumber) {
		ArrayList<DragTarget> retTargs = new ArrayList<DragTarget>();
		
		try {
			Statement prob = this.dbConn.createStatement();
			Statement target = this.dbConn.createStatement();
			Statement frag = this.dbConn.createStatement();
			Statement hint = this.dbConn.createStatement();
			
			ResultSet problem = prob.executeQuery("SELECT prob_id FROM " + PROBLEM_TABLE + " WHERE prob_id = " + problemNumber);
			
			if (problem != null && problem.getInt(1) > 0) {
				String sql = "SELECT target_id, target_answer FROM " + TARGET_TABLE +
						" WHERE problem_id = " + problem.getInt(1);
				
				ResultSet targets = target.executeQuery(sql);
				while (targets.next()) {
					// Get the ID for the target and it's answer
					int targID = targets.getInt(1);
					int fragID = targets.getInt(2);
					
					// Get that fragment/answer
					sql = "SELECT frag_text FROM " + FRAG_TABLE + 
							" WHERE frag_id = " + fragID;
					
					ResultSet f = frag.executeQuery(sql);
					Fragment fragment = new Fragment(f.getString(1));  // Create the fragment
					
					DragTarget t = new DragTarget(fragment); // Add it to the list
					
					// Add any hints to the target
					sql = "SELECT hint_text, hint_weight FROM " + HINT_TABLE +
							" WHERE target_id = " + targID + 
							" ORDER BY hint_weight"; // Not really needed, but for homework 
							
					ResultSet h = hint.executeQuery(sql);
					while (h.next()) {
						t.addHint(new Hint(h.getString(1), h.getInt(2)));
					}
					
					// Add any hints to the fragment
					sql = "SELECT hint_text, hint_weight FROM " + HINT_TABLE +
							" WHERE frag_id = " + fragID;
							
					h = hint.executeQuery(sql);
					while (h.next()) {
						fragment.addHint(new Hint(h.getString(1), h.getInt(2)));
					}
					
					retTargs.add(t);
				}
			} else {
				System.out.println("Invalid problem.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return retTargs;
	}
	
	public String getTitle(int problemNumber) {
		String retStr = "ERROR";
		try (Statement prob = this.dbConn.createStatement()){
			
			ResultSet problem = prob.executeQuery("SELECT prob_title FROM " + PROBLEM_TABLE + " WHERE prob_id = " + problemNumber);
			retStr = problem.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return retStr;
	}
	
	public String getDesc(int problemNumber) {
		String retStr = "ERROR";
		try (Statement prob = this.dbConn.createStatement()){
			
			ResultSet problem = prob.executeQuery("SELECT prob_desc FROM " + PROBLEM_TABLE + " WHERE prob_id = " + problemNumber);
			retStr = problem.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return retStr;
	}
	
	public void close() {
		try {
			this.dbConn.close();
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
	}
	
	/*
	 *  This is to meet homework requirements, serves no purpose here.
	 *  Postcondition: Returns the total number of hints for the provided problem.
	 */
	private int getHintCount(int problemNumber) {
		int count = 0;
		
		try {
			Statement test = this.dbConn.createStatement();
			
			String sql = "SELECT COUNT(*) FROM " + HINT_TABLE +
					" JOIN " + TARGET_TABLE + " t ON t.target_id = " + HINT_TABLE + ".target_id" + 
						" OR t.target_answer = " + HINT_TABLE + ".frag_id" +
					" JOIN " + PROBLEM_TABLE + " p ON t.problem_id = p.prob_id" + 
					" WHERE p.prob_id = " + problemNumber;
			
			ResultSet t = test.executeQuery(sql);
			count = t.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return count;
	}
	
	public static void main(String[] args) {
		KHintDB db = new KHintDB("test.db");

		System.out.println("There are: " + db.getHintCount(1) + " hints in this problem.");
	}
	
}
