Project renamed to KHint based off suggestion from Dr. Braude
Design: KHint/KHint-Model.jpg


Run (non-JUnit) test from KHintNoJUnitTest/KHintExample.main() 

JUnit tests are in package KHintTests.

At this stage the application handles property formatting the target text based off indents and/or spacing, and then displays the fragment choices below it.

User is prompted to enter a fragment and the target location to put it, input is validated to ensure one letter and one number is given (ie B 4 or 4 B are both accepted).  If invalid input is given, the user it reminded of the format and prompted again.

Currently no action is taken with the input, the program ends once correct input is provided.
