JOB SHOP SCHEDULING - TABU SEARCH ALGORITHM  

AUTHOR

	Thiebout Dewitte

README
	
	The project contains the Java implementation 
	of a tabu search algorithm. This README file
	contains the information on how to compile,
	run the code and how to interpret the results.
	
	Note that the source code itself, is also 
	extensively documented. 

COMPILING AND RUNNING

	The code of this project can be compiled,
	using a default Eclipse environment. 

	Create a new Java project, import “Dewitte_JSS.jar”. 
	You can run any of the methods in the class Main
	by adding the specific method name into the 
	main method and simply running the project.

	These methods will require a directory “instances”
	including the test instances in .txt files. These 
	files should be structured according to the structure
	of the OR test instances. An instance with 10 jobs
	and 5 machines should look as follows. The first line
	might be a comment line if it is preceded with a "#".

	# LA01 
	10 5
	1 21 0 53 4 95 3 55 2 34
	0 21 3 52 4 16 2 26 1 71
	3 39 4 98 1 42 2 31 0 12
	1 77 0 55 4 79 2 66 3 77
	0 83 3 34 2 64 1 19 4 37
	1 54 2 43 4 79 0 92 3 62
	3 69 4 77 1 87 2 87 0 93
	2 38 0 60 1 41 3 24 4 83
	3 17 1 49 4 25 0 44 2 98
	4 77 3 79 2 43 1 75 0 96

OUTPUT

	The output of methods makeTable1() and makeTable2() 
	will be written to "table1.txt" and "table2.txt". 
	
	Note that the output can be used directly as input 
	for a LaTeX document, by only adding a head and 
	a table environment.
	
	Table 1 and table 2 will be structured exactly as 
	mentioned in the final report tables.
	
HOW TO VERIFY THE SOLUTIONS

	Solutions of a given problem can easily be verified
	using the independent Java program "jss.jar".
	
	The first input arg of this program is the test
	instance itself and the second input arg is the
	solution as presented by the method printSolution() 
	in class Solution.

	In Terminal, one can verify the solution to a problem
	using the following statement:

	java -jar jss.jar problem.txt sol.txt

FUTURE RESEARCH
	
	An early version of the implementation of neighborhood A 
	has been added into the project. This one can still 
	cause invalid solutions, so the neighborhood should not 
	be used yet in the tabu search algorithm. 