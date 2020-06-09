/**
 * 
 */
package targets;

import fragments.Fragment;

/**
 * @author Joseph Monk
 *
 */
public class DragTarget extends Target {
	private Fragment currentFrag;
	
	public DragTarget(Fragment answer) {
		super(answer);
	}

	public Fragment getCurrentFrag() {
		return currentFrag;
	}

	public void setCurrentFrag(Fragment currentFrag) {
		this.currentFrag = currentFrag;
	}
	
	public boolean isSolved() {
		return (this.currentFrag == this.getAnswer());
	}

	@Override
	public String getDisplay() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void display() {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return currentFrag.getText();
	}

}
