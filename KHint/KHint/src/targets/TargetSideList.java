/**
 * 
 */
package targets;

import fragments.Fragment;
import hints.Hint;

/**
 * @author Joseph Monk
 * 
 * Target list that has sub targets displayed on one line
 *
 */
public class TargetSideList extends TargetList {

	private String delim = " "; // What to use as a delimiter between the targets that come after this one
	
	public TargetSideList() {super();}
	
	public TargetSideList(Fragment myAnswer, String text) {
		super(myAnswer, text);
	}
	
	public TargetSideList(String text) {
		super(text);
	}
	
	public String getDelim() {return this.delim;}
	
	public void setDelim(String newDelim) {this.delim = newDelim;}
	
	/*
	 * Postcondition: Displays the Target text, it's fragment text and hints to the console.
	 */
	@Override
	public void display() {
		if (this.isDisplayOnly())
			System.out.println("Target: " + this.getText() + "    //(Display Only)");
		else {
			System.out.println("Target: " + this.getText() + "    //(Frag: " + this.getAnswer().getText() + ")");
			System.out.println("    ========HINTS========");
			for (Hint hint : this.getHints()) {
				hint.display();
			}
			System.out.println("    ========/HINTS========");
		}
	}

	@Override
	public String toString() {
		return this.getText();
	}

	/*
	 * Postcondition: Displays the Target text, it's fragment text and hints to the console.
	 * Post2: Displays all subtargets below this one
	 */
	@Override
	public void displayAll() {
		this.display();
		System.out.println("========Single line format subs, seperated by '" + this.getDelim() + "'========");
		
		for (Target target : this.getSubTargets()) {
			if (target instanceof TargetList)
				((TargetList) target).displayAll();
			else
				target.display();
		}
		System.out.println("========/Subs========");
		
	}

	/*
	 * Postcondition1: If display only, return the text plus all sub targets separated by delim
	 * Post2: If solved return Fragment text plus all sub targets separated by delim
	 * Post3: Otherwise return my text plus all sub targets separated by delim
	 */
	@Override
	public String getDisplay() {
		String retDisplay = "";
		
		if (this.isDisplayOnly()) retDisplay = this.getText(); // Checked first just in case isSolved() got set somehow
		else if (this.isSolved() && this.getAnswer() != null) retDisplay = this.getAnswer().getText();
		else retDisplay = this.getText();
		
		
		for (Target target : this.getSubTargets()) {
			retDisplay += delim + target.getDisplay();
			
		}
					
		return retDisplay;
	}

}
