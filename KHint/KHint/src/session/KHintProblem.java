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

import fragments.Fragment;
import hints.Hint;
import targets.DragTarget;

/**
 * @author Joseph Monk
 *
 *This class is currently a skeleton, implemented such that a single session can have multiple problems in the future
 */
public class KHintProblem {
	
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
	}
	
	public ArrayList<DragTarget> getTargets() {return this.targets;}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
