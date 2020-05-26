package kHintTests;

import static org.junit.Assert.*;

import org.junit.Test;

import fragments.Fragment;
import targets.StandardTarget;
import targets.TargetBlockList;

public class TargetBlockListTest {

	@Test
	public void testGetDisplay() {
		Fragment frag = new Fragment("Test Fragment");
		TargetBlockList targ = new TargetBlockList(frag, "Test Target");
		
		// First test standard, should return the targets text
		assertEquals("Test Target", targ.getDisplay());
		
		// Second test if solved, should return the fragments text
		targ.setSolved(true);
		assertEquals("Test Fragment", targ.getDisplay());
		
		// Third test if display only, should return the targets text
		targ.setDisplayOnly(true);
		assertEquals("Test Target", targ.getDisplay());
		
		// Fourth test standard with a sub target
		StandardTarget subTarg = new StandardTarget("This target");
		subTarg.setDisplayOnly(true);
		targ.add(subTarg);
		
		assertEquals("Test Target\n" + 
				"  This target", targ.getDisplay());
	}

}
