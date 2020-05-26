package kHintTests;

import static org.junit.Assert.*;

import org.junit.Test;

import fragments.Fragment;
import targets.StandardTarget;

public class StandardTargetTest {

	@Test
	public void testGetDisplay() {
		Fragment frag = new Fragment("Test Fragment");
		StandardTarget targ = new StandardTarget(frag, "Test Target");
		
		// First test standard, should return the targets text
		assertEquals("Test Target", targ.getDisplay());
		
		// Second test if solved, should return the fragments text
		targ.setSolved(true);
		assertEquals("Test Fragment", targ.getDisplay());
		
		// Third test if display only, should return the targets text
		targ.setDisplayOnly(true);
		assertEquals("Test Target", targ.getDisplay());
	}

}
