/**
 * 
 */
package targets;

import fragments.Fragment;
import hints.Hint;

/**
 * @author Joseph Monk
 *
 */
public class StandardTarget extends Target {

	public StandardTarget(String text) {
		super(text);
	}

	public StandardTarget(Fragment frag, String string) {
		super(frag, string);
	}

	@Override
	public void display() {
		if (this.isDisplayOnly())
			System.out.println("Target: " + this.getText() + "     //(Display Only)");
		else {
			System.out.println("Target: " + this.getText() + "    //(Frag: " + this.getAnswer().getText() + ")");
			System.out.println("    ========HINTS========");
			for (Hint hint : this.getHints()) {
				hint.display();
			}
			System.out.println("    ========/HINTS========");
		}
	}

	@Override
	public String toString() {
		return this.getText();
	}

}
