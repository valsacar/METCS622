/**
 * 
 */
package targets;

import hints.Hint;

/**
 * @author Joseph Monk
 * 
 * This will be for targets that have sub targets listed under them with an indent
 *
 */
public class TargetBlockList extends TargetList {

	private int indentSize = 2;  // How much to indent the targets under this one
	
	public TargetBlockList(String text) {
		super(text);
	}

	public int getIndentSize() {
		return indentSize;
	}

	public void setIndentSize(int size) {
		this.indentSize = size;
	}

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
		System.out.println("========Block format subs, indented with " + this.getIndentSize() + " spaces========");
		
		for (Target target : this.getSubTargets()) {
			if (target instanceof TargetList)
				((TargetList) target).displayAll();
			else
				target.display();
		}
		System.out.println("========/Subs========");
	}

}
