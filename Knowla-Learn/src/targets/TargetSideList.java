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

}
