/**
 * 
 */
package targets;

import fragments.Fragment;

/**
 * @author Joseph Monk
 *
 * Targets that hold both an answer and a current fragment.  To be used with KHintController.
 */
public class DragTarget extends Target {
	private Fragment currentFrag;
	
	public DragTarget(Fragment answer) {
		super(answer);
	}
	
	public DragTarget(Fragment answer, String text) {
		super(answer, text);
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
