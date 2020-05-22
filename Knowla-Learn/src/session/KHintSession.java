/**
 * 
 */
package session;

import java.util.ArrayList;

import fragments.Fragment;
import targets.Target;

/**
 * @author Joseph Monk
 *
 */
public class KHintSession {
	
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private ArrayList<Target> targets = new ArrayList<Target>();
	
	

	public ArrayList<Fragment> getFragments() {
		return fragments;
	}



	public void setFragments(ArrayList<Fragment> fragments) {
		this.fragments = fragments;
	}



	public ArrayList<Target> getTargets() {
		return targets;
	}



	public void setTargets(ArrayList<Target> targets) {
		this.targets = targets;
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
