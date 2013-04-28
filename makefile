# Project Group 3
# assignment 2
#

javac=javac
java=java
cup_location=java-cup-11a.jar
cup_runtime=java-cup-11a-runtime.jar
CLASSPATH=./:$(cup_location):$(cup_runtime)
lex=jflex
pager=less

.PHONY : view-part1 buildP1 run-part1 view-func1 view-func2 view-part2 buildP2 run-part2 clean
# view-part1 — display (using the more utility) all source code for part 1
# buildP1 — target to build your parser for part 1
# run-part1 — run your parser from part 1
# view-func1 — display (using the more utility) your iterative length function in the mini language
# view-func2 — display (using the more utility) your recursive length function in the mini language
# view-part2 — display (using the more utility) all source code for part 2
# buildP2 - target to build your parser for part 2
# run-part2 — run your parser from part 2
# clean — cleans up all intermediate and resulting files

view-part1 : interpreterext.cup interpreterext.flex Program.java
	-$(pager) interpreterext.cup
	-$(pager) interpreterext.flex
	-$(pager) Program.java

buildP1 : interpreterext.cup interpreterext.flex Program.java
	$(java) -classpath $(CLASSPATH) java_cup.Main interpreterext.cup
	$(lex) interpreterext.flex
	$(javac) -classpath $(CLASSPATH) parser.java sym.java Yylex.java Program.java

run-part1 : interpreterext.cup interpreterext.flex Program.java
	-$(java) -classpath $(CLASSPATH) parser

view-func1 : 
	-$(pager) input/length_iterative.p

view-func2 : 
	-$(pager) input/length_recursive.p

# Part 2

view-part2 : interpreterextP2.cup interpreterext.flex ProgramP2.java 
	-$(pager) interpreterextP2.cup
	-$(pager) interpreterext.flex
	-$(pager) ProgramP2.java

buildP2: interpreterextP2.cup interpreterext.flex ProgramP2.java
	$(java) -classpath $(CLASSPATH) java_cup.Main interpreterextP2.cup
	$(lex) interpreterext.flex
	$(javac) -classpath $(CLASSPATH) parser.java sym.java Yylex.java ProgramP2.java

run-part2 : interpreterextP2.cup interpreterext.flex ProgramP2.java 
	-$(java) -classpath $(CLASSPATH) parser

### clean ###

clean :
	-\rm -v *.class
	-\rm -v parser.java
	-\rm -v sym.java
	-\rm -v Yylex.java*
