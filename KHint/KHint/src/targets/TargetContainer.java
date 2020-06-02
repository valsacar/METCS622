/**
 * 
 */
package targets;

import hints.Hint;

/**
 * @author Joseph Monk
 *
 */
public class TargetContainer<T extends Target> {
	private T target;

	
	public TargetContainer(T targ) {
		this.target = targ;
	}
	
	public T getTarget() {
		return this.target;
	}
	
	public int getHintWeight() {
		return addHintWeights(this.getTarget());		
	}
	
	private static int addHintWeights(Target t) {
		int weight = 0;
		
		for (Hint h : t.getHints()) {
			weight += h.getWeight();
		}
		
		if (t instanceof TargetList) {
			for (Target targ : ((TargetList) t).getSubTargets()) {
				weight += addHintWeights(targ);
			}
		}
		
		return weight;
	}
}
