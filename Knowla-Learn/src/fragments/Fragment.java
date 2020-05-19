/**
 * 
 */
package fragments;

import java.util.PriorityQueue;

import hints.Hint;

/**
 * @author Joseph Monk
 *
 */
public class Fragment {
	private String text;
	private PriorityQueue<Hint> myHints = new PriorityQueue<Hint>();
	
	public Fragment(String myText) {
		this.text = myText;
	}
	
	public String getText() {
		return this.text;
	}
	
	public void setText(String newText) {
		this.text = newText;
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
	
	public void display() {
		System.out.println("Fragment: " + this.text);
		System.out.println("    ========HINTS========");
		for (Hint hint : this.getHints()) {
			hint.display();
		}
		System.out.println("    ========/HINTS========");
	}

}
