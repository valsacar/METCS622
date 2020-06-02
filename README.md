Project renamed to KHint based off suggestion from Dr. Braude
Design: KHint/KHint-Model.jpg


Run (non-JUnit) test from KHintNoJUnitTest/KHintExample.main() 

JUnit tests are in package KHintTests.

At this stage the application handles property formatting the target text based off indents and/or spacing, and then displays the fragment choices below it.

User is prompted to enter a fragment and the target location to put it, input is validated to ensure one letter and one number is given (ie B 4 or 4 B are both accepted).  If invalid input is given, the user it reminded of the format and prompted again.

If the choice is a wrong match, the user is given a hint (if available) for the correct placement.  Otherwise they are told that is the correct choice.  Program ends at this point.
