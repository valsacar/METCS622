/**
 * 
 */
package targets;

import java.util.ArrayList;

import fragments.Fragment;

/**
 * @author Joseph Monk
 * 
 * Targets that have other targets
 *
 */
public abstract class TargetList extends Target {

	private ArrayList<Target> subTargets = new ArrayList<Target>();
	
	public TargetList() {
		super();
	}
	
	public TargetList(Fragment myAnswer, String text) {
		super(myAnswer, text);
	}
	
	public TargetList(Fragment myAnswer) {
		super(myAnswer);
	}
	
	public TargetList(String text) {
		super(text);
	}
	
	public void add(Target newTargetElem) {
		subTargets.add(newTargetElem);
	}
	
	public ArrayList<Target> getSubTargets() {
		return this.subTargets;
	}
	
	public abstract String toString();
	
	public abstract void displayAll();

}
