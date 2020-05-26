/**
 * 
 */
package targets;

import fragments.Fragment;
import hints.Hint;

/**
 * @author Joseph Monk
 *
 */
public class StandardTarget extends Target {

	public StandardTarget(String text) {
		super(text);
	}

	public StandardTarget(Fragment frag, String string) {
		super(frag, string);
	}

	/*
	 * Postcondition: Displays the Target text and hints to the console.
	 */
	@Override
	public void display() {
		if (this.isDisplayOnly())
			System.out.println("Target: " + this.getText() + "     //(Display Only)");
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
	 * Postcondition1: If display only, return the text
	 * Post2: If solved return Fragment text
	 * Post3: Otherwise return my text
	 */
	@Override
	public String getDisplay() {
		if (this.isDisplayOnly()) return this.getText(); // Checked first just in case isSolved() got set somehow
		else if (this.isSolved()) return this.getAnswer().getText();
					
		return this.getText();
	}

}
