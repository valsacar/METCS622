package kHintTests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import fragments.Fragment;
import session.KHintProblem;

class KHintProblemTest {

	@Test
	public void testShuffleFrags() {
		ArrayList<Fragment> origList = new ArrayList<Fragment>();
		for (int i = 1; i <= 20; i++) { // Make 20 fragments
			origList.add(new Fragment("Frag " + i));
		}
		
		//We'll run 10000 tests since this uses random in it
		for (int t = 0; t < 10000; t++) {
			ArrayList<Fragment> randomList = new ArrayList<>(origList);
			KHintProblem.shuffleFrags(randomList); // Shuffle it

			for (int j = 0; j < origList.size(); j++) { // Loop through each and test		
				assertNotEquals(origList.get(j), randomList.get(j));
			}
		}
	}

}
