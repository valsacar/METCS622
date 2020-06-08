/**
 * 
 */
package hints;

import java.io.Serializable;

/**
 * @author Joseph Monk
 *
 */
public class Hint implements Comparable<Hint>, Serializable{


	private static final long serialVersionUID = 2449416940961302252L;
	
	private String text;
	private int weight = 5; // Used for both scoring and order that the hints should appear in, default is 5
	
	public Hint() {}
	
	public Hint(String myText) {
		this.text = myText;		
	}
	
	public Hint(String myText, int myWeight) {
		this.text = myText;
		this.weight = myWeight;
	}
	
	public String getText() {
		return this.text;
	}
	
	public void setText(String newText) {
		this.text = newText;
	}
	
	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	/*
	 * Postcondition: Displays the hint text to the console.
	 */
	public void display() {
		System.out.println("Hint: " + this.text + " weight: " + this.weight);
	}
	
	@Override
	public String toString() {
		return this.text;
	}

	/*
	 * Postcondition: Returns 1 if this has a higher weight, -1 if lower and 0 if they are the same
	 */
	@Override
	public int compareTo(Hint other) {
		if (this.getWeight() > other.getWeight())
			return 1;
		else if (this.getWeight() < other.getWeight())
			return -1;
		
		return 0;
	}
}
