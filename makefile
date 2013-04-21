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

.PHONY : view-part1 build run-part1 view-func1 view-func2 view-part2 run-part2 clean
# view-part1 — display (using the more utility) all source code for part 1
# build — target to build your parser
# run-part1 — run your parser from part 1
# view-func1 — display (using the more utility) your iterative length function in the mini language
# view-func2 — display (using the more utility) your recursive length function in the mini language
# view-part2 — display (using the more utility) all source code for part 2
# run-part2 — run your parser from part 2
# clean — cleans up all intermediate and resulting files


build : interpreterext.cup interpreterext.flex Program.java
	$(java) -classpath $(CLASSPATH) java_cup.Main interpreterext.cup
	$(lex) interpreterext.flex
	$(javac) -classpath $(CLASSPATH) parser.java sym.java Yylex.java Program.java

view-part1 : interpreterext.cup interpreterext.flex Program.java
	-$(pager) interpreterext.cup
	-$(pager) interpreterext.flex
	-$(pager) Program.java


run-part1 : interpreterext.cup interpreterext.flex Program.java
	-$(java) -classpath $(CLASSPATH) parser

view-func1 : 

view-func2 : 

view-part2 : 

run-part2 : 

### clean ###

clean :
	-\rm -v *.class
	-\rm -v parser.java
	-\rm -v sym.java
	-\rm -v Yylex.java*
	-\rm -v a.out
	-\rm -v gen2.tab.c
	-\rm -v gen2.tab.h
	-\rm -v lex.yy.c
