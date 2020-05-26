/**
 * 
 */
package session;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.StringTokenizer;

import fragments.Fragment;
import hints.Hint;
import targets.Target;
import targets.TargetList;

/**
 * @author Joseph Monk
 *
 */
public class KHintSession {
	
	private static final String SESSION_LOG = "session.txt";
	private static final String INVALID_INPUT = "Invalid input, there must be a letter and a number.";
	private static final String PROP_TARGET = "target";
	private static final String PROP_FRAG = "fragment";
	
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private ArrayList<Target>targets = new ArrayList<Target>();
	private ArrayList<Hint> usedHints = new ArrayList<Hint>();
	
	public KHintSession() {}

	public ArrayList<Fragment> getFragments() {
		return fragments;
	}

	public void setFragments(ArrayList<Fragment> fragments) {
		this.fragments = fragments;
	}
	
	public void addFragment(Fragment frag) {
		fragments.add(frag);
	}

	public ArrayList<Target> getTargets() {
		return targets;
	}
	
	/*
	 * Protected because we don't want bypassing the addTarget method, but there may be a use for it within the package
	 */
	protected void setTargets(ArrayList<Target> targets) {
		this.targets = targets;
	}
	
	
	
	/*
	 * Postcondition1: targets list is updated
	 */
	public void addTarget(Target targ) {
		targets.add(targ);
	}
	
	/*
	 * Postcondition 1: Session information is saved to file SESSION_LOG
	 * Post2: Session information is displayed to console
	 * Note: By design this appends and does not overwrite previous sessions.
	 */
	private static void displayAndLog(String text) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(SESSION_LOG, true)); // Open with append

			bw.write(text);  // Save to file
			System.out.println(text);  // Display to user

			bw.newLine();
			bw.flush();
		} 
		catch (IOException e) {
			System.out.println("Could not append to " + SESSION_LOG);
			e.printStackTrace();
		} 
		finally { // (close file)
			if (bw != null) 
				try {
					bw.close();
				} 
			catch (IOException e) {
				e.printStackTrace();
			}
		} 
	}
	
	/*
	 * Precondition1: Student is in session and ready for a new prompt
	 * PostCondition1: Obtain user choice for fragment and target
	 * Post2: Return selection as Properties object
	 * Post3: If user response is not correct, explain why and prompt again 
	 */
	private Properties getUserResponse() {
		displayAndLog("Please enter a fragment and target, ie. 'A 4':  ");
		
		Scanner reader = new Scanner(System.in);
		String answer = reader.nextLine();		
		
		displayAndLog("You entered: " + answer);
		
		StringTokenizer answerTokens = new StringTokenizer(answer);
		Properties answerReturn = new Properties();
		
		if (answerTokens.countTokens() != 2) {// Must have 2 tokens.
			displayAndLog(INVALID_INPUT);
			return getUserResponse();
		}
		
		// Parse the input into individual tokens
		while (answerTokens.hasMoreTokens()) {
			String token = new String(answerTokens.nextToken());
			
			if (token.length() > 1) {  // Should just be 1 char
				displayAndLog(INVALID_INPUT);
				return getUserResponse();
			}
			
			// We won't assume the user always follows the target fragment order, we'll accept either
			char c = token.charAt(0);
			if (c > '0' && c < '9') { // It's a number
				if (answerReturn.getProperty(PROP_FRAG) != null) { // They already gave us a fragmnent
					displayAndLog(INVALID_INPUT);
					return getUserResponse();
				}
				
				answerReturn.setProperty(PROP_FRAG, token); // Store the answer
			} else { // Must be the target then, we don't care if it's not a letter here since that will be caught later
				if (answerReturn.getProperty(PROP_TARGET) != null) { // But they already gave us one
					displayAndLog(INVALID_INPUT);
					return getUserResponse();
				}
				
				answerReturn.setProperty(PROP_TARGET, token);
			}
		}
		
		reader.close();
		return answerReturn;
	}
	
	private void displayTargets() {
		String displayString = "";
		
		for (Target targ : targets) {
			displayString += targ.getDisplay() + "\n";
		}
		
		displayAndLog(displayString);
	}
	
	private void displayFragments() {
		String displayString = "Fragments to choose from: \n";
		int i = 1;
		
		for (Fragment frag : fragments) {
			if (!frag.isUsed())
				displayString += String.valueOf(i++) + ". " + frag + "\n";
		}
		
		displayAndLog(displayString);		
	}
	
	/*
	 * Begins the KHint session
	 */
	public void startSession() {
		String startTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
		displayAndLog("Session started: " + startTime);
		displayTargets();
		displayAndLog("\n\n");
		displayFragments();
		getUserResponse();
		
		String endTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
		displayAndLog("Session ended: " + endTime + "\n\n\n");
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
