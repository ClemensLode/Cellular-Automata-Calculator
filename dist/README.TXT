CATEST v1.059

========================
Changelog update 1.059:

- [GUI] Named permutation / variation options correctly
- [GUI] Added End / Save button
- [GUI] Added boolean representation of the rule
- [GUI] Added option to skip results already in the database
- [GUI] 
- [GUI]
- [GUI]
- [GUI]
- [GUI]
- [GUI]
- [GUI]
- [ALGORITHM

========================

========================
Changelog update 1.05:

- [ALGORITHM] Fixed output error with testing all balanced rules (error was introduced in 1.04), the significant rule number displayed in the table was the rule number of the PREVIOUS calculation. (Use for example 'neighborhood 0,1' and 'test all balanced rules'. The result shows  '12, 12, 10, 6, 9, 5' instead of '12, 10, 6, 9, 5, 3'.
- [ALGORITHM] Included C plugin. Due to overhead when calling an external program use this function only when testing a small number of test cases (<1000)
- [ALGORITHM] Fixed error that occurs when combining 'New CA rule definition' and 'All neighborhood permutations'
- [GUI] Set 'Output options' to all as default value
- [GUI] Added 'Use fast C plugin' option
- [GUI] Added function to generate graph directly in the program
- [GUI] Removed unnecessary output ('has graph') in output file
- [GUI] Added button to directly remove all entries
- [GUI] Set 'output all' as default
- [GUI] Removed confirmation screen (I don't see a point in that, it was primarily used for debugging)
- [GUI] Fixed simple output when not adding to the database
- [GUI] Disallowed use of automatic testing of 'all neighborhoods' and 'new ca rule definition' at the same time (would otherwise affect 'significant neighborhood size')
- [README] Changed '-ss32m' to '-Xss64m'
========================

========================
Changelog update 1.04:
- [ALGORITHM] Fixed an error where the rule number is overwritten by a standard value
- [GUI] Inserted a missing update of the neighborhood string after a neighborhood permutation
========================

========================
Changelog update 1.03:

- [ALGORITHM/GUI] Output now shows the correct rule number when using disordered neighborhoods
- [GUI] Fixed another small issue with updating the rule when not changing the number of cell states
========================

========================
Changelog update 1.02:

- [ALGORITHM] Included the option to test on disordered neighborhood permutations
- [GUI] Fixed small issue with updating the rule when changing the number of cell states
- [GUI] Fixed small issue with the horizontal/vertical size of the results table
========================

========================
Changelog update 1.01:

- [ALGORITHM] Fixed problem where some elements of a strongly connected component are ignored leading to erroneous results
- [GUI] Fixed 'Needed Memory' display counting the actual number of bytes not the number of ints needed for the calculation
- [GUI] Fixed 'Needed calculation time' display by correctly counting the number of remaining permutations / calculation steps
- [GUI] Fixed 'ArrayOutOfBounds' error that occurs when switching to the 'Results' tab during calculation
- [GUI] Fixed some issues with the 'Automatic tests' and the communication between GUI and ALGORITHM.
- [GUI] Disabled all buttons/fields during a calculation
========================


========================
FILES
========================

'/lib/'
This directory contains the classes from the Swing library that were used in the GUI of the program.

'/catest.jar'
Contains all classes of the main program (GUI and ALGORITHM).

========================
SYSTEM PREREQUISITES
========================

To run the programm Java 1.5 (JDK 5 from java.sum.com) is needed, usually already installed on a current system.

========================
RUNNING THE PROGRAM
========================

* double-click on 'catest.jar' (results in a small heap size)
or
* call 'java.exe -jar catest.jar -Xmx256m -Xss64m' in the command line
or
* - right-click on 'catest.jar'
  - select 'Create Shortcut'
  - right-click on 'Shortcut to catest.jar'
  - select 'Properties'
  - append ' -Xmx256m -Xss64m' in the 'Start in:' field
  - click on OK
  - double-click on 'Shortcut to catest.jar'

========================
Heap / stack size
========================

-Xmx<value>m defines the heap size in megabytes, 256m will allow problem sizes of around 9 (with 2 cell states)
-ss<value>m defines the stack size in megabytes, this is still an issue I'm working on

If too large values for neighborhood sizes are used the program might crash and needs to be restarted.

========================
USAGE
========================

0. General
- most items have explanatory tool tips
- all text fields will check their inputs and update other GUI elements (such as calculation time and 
  needed memory) as soon as they lose their focus, i.e. when a different GUI element is selected 

1.  "Neighborhood Configuration"
- "Neighborhood Size" is the distance between the first and last neighborhood position (e.g. 7 for "0,1,6")
- "Significant Neighborhood Size" is the number of significant neighborhood positions (e.g. 3 for "0,1,6")
- Automatic tests:
  * all neighborhood permutations (e.g. "0,1,2,5", "0,1,3,5", "0,1,4,5", "0,2,3,5", "0,2,4,5", "0,3,4,5" 
     for neighborhood size 6 and significant neighborhood size 4)
  * all neighborhood sizes (same as above, but it tests all neighborhood sizes up to the number specified)
  * all neighborhoods (same as above, but it also tests all significant neighborhood sizes up to the number specified)

2. "Rule Configuration"
- 'Test all balanced rules' will cycle through all balanced rules

3. "Calculation"
- Stopping the calculation will still produce output

4. "Output options"
- When 'add result to database' is activated all output is redirected to the table in the "Results" tab
- Non-surjective (non-injective) results will be surpressed when 'At least surjective' ('Only injective') is selected
- "Generate Graph" will generate a '.viz' file which can be plotted by DOT (see www.graphviz.org).
  This option should only be used for small neighborhood sizes.

5. "Results"
- All columns can be sorted by clicking on the header
- Single entries (or all with CTRL+a) can be removed
- "Show Graph(s)" does not work yet
- "Save results" will save the data in a random file with a simple format


========================
Contact
========================

Contact me at "clemens@lode.de" for support / bug reports etc.
