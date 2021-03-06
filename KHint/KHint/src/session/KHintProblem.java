/**
 * 
 */
package session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import database.KHintDB;
import fragments.Fragment;
import hints.Hint;
import targets.DragTarget;

/**
 * @author Joseph Monk
 *
 *This class is currently a skeleton, implemented such that a single session can have multiple problems in the future
 */
public class KHintProblem {
	private int totalHintScore = 0;
	private int currentHintScore = 0;
	
	// Some temp data for testing
	private static final ArrayList<DragTarget> tempTargets = new ArrayList<DragTarget>(Arrays.asList(
            new DragTarget(new Fragment("test frag"), "Position 1"),
            new DragTarget(new Fragment("test frag2"), "Position 2"),
            new DragTarget(new Fragment("test frag3"), "Position 3"),
            new DragTarget(new Fragment("test frag4"), "Position 4"),
            new DragTarget(new Fragment("test frag5"), "Position 5"),
            new DragTarget(new Fragment("test frag6"), "Position 6")
    ));
	
	private ArrayList<DragTarget> targets;
	private String title;
	private String description;
	
	public KHintProblem() {
		this.targets = tempTargets;
		targets.get(0).addHint(new Hint("A hint", 10));
		targets.get(0).addHint(new Hint("A hint2", 20));
		targets.get(0).addHint(new Hint("A hint3", 30));
		targets.get(0).getAnswer().addHint(new Hint("A frag hint", 10));
		targets.get(0).getAnswer().addHint(new Hint("A frag hint2", 15));
		targets.get(0).getAnswer().addHint(new Hint("A frag hint3", 40));
		this.shuffleFragments();
		targets.get(0).getCurrentFrag().addHint(new Hint("A frag2 hint", 10));
		targets.get(0).getCurrentFrag().addHint(new Hint("A frag2 hint2", 25));
		targets.get(0).getCurrentFrag().addHint(new Hint("A frag2 hint3", 45));
		this.description = "This is a test of the KHint Application";
		this.setTotalHint();
	}
	
	public KHintProblem(int problemNumber) {
		KHintDB db = new KHintDB("test.db");
		this.targets = db.loadProblem(problemNumber);
		this.shuffleFragments();
		
		this.title = db.getTitle(problemNumber);		
		this.description = db.getDesc(problemNumber);
		db.close();
		this.setTotalHint();
	}
	
	
	public ArrayList<DragTarget> getTargets() {return this.targets;}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	
	
	public String getTitle() {
		return title;
	}
	
	public int getTotalHintScore() {
		return totalHintScore;
	}

	public int getCurrentHintScore() {
		return currentHintScore;
	}
	
	private void setTotalHint() {
		int total = 0;
		
		for (DragTarget t : this.targets) {
			// Add the hint weight for all the hints in this target
			for (Hint h : t.getHints()) {
				total += h.getWeight();
			}
			
			// And do the same for the fragment answer
			for (Hint h : t.getAnswer().getHints()) {
				total += h.getWeight();
			}			
		}
		
		this.currentHintScore = total;
		this.totalHintScore = total;
	}
	
	/*
	 * Postcondition: Lowers the current hint score by the weight of the used hint
	 * Returns the new current hint score.
	 */
	public int useHint(Hint h) {
		this.currentHintScore -= h.getWeight();
		
		return this.currentHintScore;
	}

	/*
	 * Precondition1: this.targets is populated with the correct fragment answers
	 * Postcondition1: Answers (fragments) are extracted from this.targets and shuffled into random order
	 * Postcondition2: Shuffuled fragments are added back to targets as currentFragment
	 */
	private void shuffleFragments() {
		Stack<Fragment> frags = new Stack<Fragment>();
        targets.forEach(targ -> frags.add(targ.getAnswer()));
        shuffleFrags(frags);
        Collections.reverse(frags);  // Reverse it so when we pop them back in it's in the correct order
        targets.forEach(targ -> targ.setCurrentFrag(frags.pop()));
	}
	
	/*
	 * Implementation of Sattolo's algorithm
	 * Postcondition1: List is randomized such that no element remains at the same index
	 * This was private, made public to easily do junit testing 
	 */

	public static void shuffleFrags(List<Fragment> list) {
		Random r = new Random();
		int i = list.size();
		
		while (i > 1) {
			i = i - 1;
			int j = r.nextInt(i);
			
			Fragment swap = list.get(i);
			list.set(i, list.get(j));
			list.set(j, swap);
		}		
	}
	
	
}
