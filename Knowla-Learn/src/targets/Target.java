/**
 * 
 */
package targets;

import java.util.PriorityQueue;

import fragments.Fragment;
import hints.Hint;

/**
 * @author Joseph Monk
 *
 */
public abstract class Target {
	
	private Fragment answer;  // Which fragment should go here
	private String text; // Default place holder text
	private boolean isDisplayOnly = false; // This only displays text, it is not a target for a fragment to be placed in
	private boolean isSolved = false;
	
	private PriorityQueue<Hint> myHints = new PriorityQueue<Hint>();
	
	
	public Target() {}
	
	public Target(Fragment myAnswer) {
		this.answer = myAnswer;
	}
	
	public Target(Fragment myAnswer, String placeHolder) {
		this.answer = myAnswer;
		this.setText(placeHolder);
	}
	
	public Target(String placeHolder) {
		this.text = placeHolder;
	}
	
	public boolean isDisplayOnly() {
		return this.isDisplayOnly;
	}
	
	/*
	 * Sets the target to be a display only target and returns the fragment that was stored as answer.
	 * PostCondition 1: Target is made display only.
	 * PostCondition 2: Answer fragment is null.
	 * PostCondition 3: What was the answer is returned.
	 */
	public Fragment makeDisplayOnly() {
		Fragment oldFrag = this.answer;
		
		this.isDisplayOnly = true;
		this.answer = null;
		
		return oldFrag;
	}	
	
	/*
	 * Sets the isDisplayOnly setting
	 * Post1: isDisplayOnly set to true/false
	 * Post2: if set to true, run makeDisplayOnly to remove the answer
	 */
	public void setDisplayOnly(boolean value) {
		this.isDisplayOnly = value;
		if (value) makeDisplayOnly();
	}
	
	
	public boolean isSolved() {
		return isSolved;
	}

	public void setSolved(boolean isSolved) {
		this.isSolved = isSolved;
	}

	public Fragment getAnswer() {
		return this.answer;
	}
	
	/*
	 * Postcondition: Sets the answer to a new fragment, throws TargetIsDisplayOnlyException if the target is displayOnly and has no answer.
	 */
	public void setAnswer(Fragment newAnswer) throws TargetIsDisplayOnlyException {
		if (this.isDisplayOnly)
			throw new TargetIsDisplayOnlyException();
		this.answer = newAnswer;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String placeHolder) {
		this.text = placeHolder;
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
	

	public abstract String getDisplay();

	public abstract void display();
	
	public abstract String toString();

}
