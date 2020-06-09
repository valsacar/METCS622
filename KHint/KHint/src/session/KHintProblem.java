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
import targets.DragTarget;

/**
 * @author Joseph Monk
 *
 *This class is currently a skeleton, implemented such that a single session can have multiple problems in the future
 */
public class KHintProblem {
	
	// Some temp data for testing
	private static final ArrayList<DragTarget> tempTargets = new ArrayList<DragTarget>(Arrays.asList(
            new DragTarget(new Fragment("test frag")),
            new DragTarget(new Fragment("test frag2")),
            new DragTarget(new Fragment("test frag3")),
            new DragTarget(new Fragment("test frag4")),
            new DragTarget(new Fragment("test frag5")),
            new DragTarget(new Fragment("test frag6"))
    ));
	
	private ArrayList<DragTarget> targets;
	private String description;
	
	public KHintProblem() {
		this.targets = tempTargets;
		this.shuffleFragments();
		this.description = "This is a test of the KHint Application";
	}
	
	public ArrayList<DragTarget> getTargets() {return this.targets;}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	
	
	private void shuffleFragments() {
		Stack<Fragment> frags = new Stack<Fragment>();
        targets.forEach(targ -> frags.add(targ.getAnswer()));
        shuffleFrags(frags);
        targets.forEach(targ -> targ.setCurrentFrag(frags.pop()));
	}
	
	/*
	 * Postcondition1: List is randomized such that no element remains at the same index
	 * This was private, made public to easily do junit testing
	 */
	
	public static void shuffleFrags(List<Fragment> list) {
		Random r = new Random();
		int size = list.size();
		List<Fragment> goldList = new ArrayList<>(list); // Gold standard, so we know the original order

		int loop = 0;
		for (int i = size-1; i >= 0; i--) {
			int pos = r.nextInt(i + 1); // Where we will swap
			System.out.println("Loop " + (++loop));
			if (pos == goldList.indexOf(list.get(i)) || // Make sure I'm not moving to where I used to be
					i == goldList.indexOf(list.get(pos))) { // Make sure I'm not swapping him where he used to be
				
				i++; // reset this run
				continue;
			}
			
			// Swap the fragments at i and pos
			Fragment swap = list.get(i);
			list.set(i, list.get(pos));
			list.set(pos, swap);
			
		}
		Collections.reverse(list);
	}
	
}
