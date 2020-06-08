/**
 * 
 */
package kHintNoJUnitTest;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import fragments.Fragment;
import hints.Hint;
import session.KHintSession;
import targets.*;

/**
 * @author Joseph Monk
 *
 */
public class KHintExample {

	KHintSession mySession = new KHintSession();
	
	
	/*
	 * Setup a test session
	 */
	public void createTestSession() {
		//Fragments that we don't use are created here, ones we will use will be created with their target
		Fragment frag = new Fragment("int i;");
		frag.addHint(new Hint("This initializes a variable with no initial value."));
		frag.addHint(new Hint("You may want to make sure the variable i has a starting value of 0.", 15));
		mySession.addFragment(frag);
				
		
		//-----Targets-----
		Target subTarg;
		Target targ = new StandardTarget("//Print the numbers 0 through 10 on the screen, one line at a time then the word complete.");
		targ.makeDisplayOnly(); // This is not a drop target
		mySession.addTarget(targ);
		
		
		TargetBlockList blockTarg = new TargetBlockList("public void printNumbersFor() {");
		blockTarg.makeDisplayOnly(); // This is not a drop target
		
		
		TargetSideList forSideTarg = new TargetSideList("for (");
		forSideTarg.makeDisplayOnly(); // This is not a drop target
		//It has subs
		// First sub
		frag = new Fragment("int i = 0;"); // Fragment for this one
		frag.addHint(new Hint("This initializes a variable, a good way to start a for loop.", 20));  // Out of weight order so we can see the priorityqueue is working
		frag.addHint(new Hint("This initializes a variable with an initial value of 0."));		
		mySession.addFragment(frag); // Add it to the overall list
		subTarg = new StandardTarget(frag, "A");
		subTarg.addHint(new Hint("Normally you need to initialize a variable to start a for loop.", 30));
		forSideTarg.add(subTarg); // Add it to the targetlist
		
		//Second sub
		frag = new Fragment("i <= 10;"); // Fragment for this one
		mySession.addFragment(frag); // Add it to the overall list
		subTarg = new StandardTarget(frag, "B");
		forSideTarg.add(subTarg); // Add it to the targetlist
		
		//Third sub
		frag = new Fragment("i++;"); // Fragment for this one
		mySession.addFragment(frag); // Add it to the overall list
		subTarg = new StandardTarget(frag, "C");
		forSideTarg.add(subTarg); // Add it to the targetlist
		
		
		//Fourth sub, starting the block below it
		TargetBlockList forBlock = new TargetBlockList(") {");
		forBlock.makeDisplayOnly();
		forSideTarg.add(forBlock);
		
		//Sub for forBlock
		frag = new Fragment("System.out.println(i);"); // Fragment for this one
		mySession.addFragment(frag); // Add it to the overall list
		StandardTarget forSubTarg = new StandardTarget(frag, "D");
		forBlock.add(forSubTarg); // Add target to the sub
		
		
		blockTarg.add(forSideTarg);// Add the forBlock to the targets list
		
		// Add the closing brace for the for loop
		targ = new StandardTarget("}");
		blockTarg.add(targ);
		targ.makeDisplayOnly(); // This is not a drop target		
		
		mySession.addTarget(blockTarg);
		// Outside of the for loop now
		frag = new Fragment("System.out.println(“Complete”);");
		mySession.addFragment(frag);
		targ = new StandardTarget(frag, "E");
		blockTarg.add(targ);
		
		
		//Add closing brace for the function
		targ = new StandardTarget("}");
		targ.makeDisplayOnly(); // This is not a drop target
		mySession.addTarget(targ);

	}
	
	/*
	 * Not really needed for the project, implemented as required by homework.
	 * Precondition1: the session has been loaded with fragments we can save.
	 * Postcondition1: fragments.dat file is written
	 */
	public void saveFragments() {
		
		 try (ObjectOutputStream outfile = new ObjectOutputStream(new FileOutputStream("fragments.dat"));)  {
             outfile.writeObject(this.mySession.getFragments());
		 } catch (FileNotFoundException e) {
			 System.out.println("File not found.");
			 e.printStackTrace();
		 } catch (IOException e) {
			 System.out.println("IO Exception");
			 e.printStackTrace();
		 }
	}
	
	/*
	 * Not really needed for the project, implemented as required by homework.
	 * Precondition1: fragments.dat file exists with data
	 * Postcondition1: fragments.dat is read into an ArrayList<Fragment>
	 */
	public void readFragments() {
		try (ObjectInputStream infile = new ObjectInputStream(new FileInputStream("fragments.dat"));)  {
            ArrayList<Fragment> readFrags =  (ArrayList<Fragment>) (infile.readObject());
            for (Fragment f : readFrags) {
            	System.out.println(f);
            	System.out.println(f.getHints());
            	System.out.println();
            }
		} catch (FileNotFoundException e) {
			 System.out.println("File not found.");
			 e.printStackTrace();
		 } catch (IOException e) {
			 System.out.println("IO Exception");
			 e.printStackTrace();
		 } catch (ClassNotFoundException e) {
			System.out.println("Class not found");
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args
	 * Postcondition: Test code to display reports of all fragments, targets and related hints
	 */
	public static void main(String[] args) {
		
		KHintExample myExample = new KHintExample();
		myExample.createTestSession();
		myExample.mySession.startSession();
		
		myExample.saveFragments();
		myExample.readFragments();
		

	}

}
