/**
 * 
 */
package fragments;

import java.io.Serializable;
import java.util.PriorityQueue;

import hints.Hint;

/**
 * @author Joseph Monk
 *
 */
public class Fragment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 912935984028583197L;
	
	private String text;
	private PriorityQueue<Hint> myHints = new PriorityQueue<Hint>();
	private boolean isUsed = false;
	
	public Fragment(String myText) {
		this.text = myText;
	}
	
	public String getText() {
		return this.text;
	}
	
	public void setText(String newText) {
		this.text = newText;
	}
	
	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	public PriorityQueue<Hint> getHints() {
		return this.myHints;
	}
	
	/*
	 * Postcondition: Returns the next hint from the priority queue or null
	 */
	public Hint getNextHint() {
		return this.myHints.poll();
	}
	
	public void addHint(Hint newHint) {
		this.myHints.add(newHint);
	}
	
	public String toString() {return this.text;}
	
	/*
	 * Postcondition: Displays the fragment text and hints to the console.
	 */
	public void display() {
		System.out.println("Fragment: " + this.text);
		System.out.println("    ========HINTS========");
		for (Hint hint : this.getHints()) {
			hint.display();
		}
		System.out.println("    ========/HINTS========");
	}

}
