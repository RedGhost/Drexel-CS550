javac=javac
java=java
cup_location=java-cup-11a.jar
cup_runtime=java-cup-11a-runtime.jar
CLASSPATH=./:$(cup_location):$(cup_runtime)
lex=jflex
pager=less
ram=./ram
RAM_MEM ?= 100

.PHONY : view compile view-trans view-link view-op run run-op clean
# view — display (using the more utility) all of your source code (excluding the modified RAM)
# compile — Does whatever you need to do to produce:
#    symbolic RAL code
#    linked RAL code
#    (optional) – You optimised RAL code

# You can name the the files as you wish. Your interpreter will read stdin, as previously.
# view-trans — Use cat to display your symbolic RAL program (produced in translate) to stdout.
# view-link — Use cat to display your compiled (not optimised) RAL program (produced in translate to stdout.
# view-op — Use cat to display your compiled, optimised RAL program (produced in translate to stdout. If you didn't provide optimisation, echo "NOT IMPLEMENTED"
# run — invoke ~jjohnson/bin/ram to run your program. Let output go to stdout.
# run-op — invoke ~jjohnson/bin/ram to run your optimised program. If you didn't provide optimisation, echo "NOT IMPLEMENTED"
# clean — remove all binaries and intermediate files

view : interpreterext.cup interpreterext.flex Program.java SymbolTable.java Linker.java Symbol.java
	-$(pager) interpreterext.cup
	-$(pager) interpreterext.flex
	-$(pager) Program.java
	-$(pager) SymbolTable.java
	-$(pager) Linker.java
	-$(pager) Symbol.java

compile-static :
	cp Program-static.java Program.java
	$(java) -classpath $(CLASSPATH) java_cup.Main interpreterext.cup
	$(lex) interpreterext.flex
	$(javac) -classpath $(CLASSPATH) parser.java sym.java Yylex.java Program.java SymbolTable.java Linker.java Symbol.java FunctionTable.java Instruction.java PseudoInstruction.java

compile-dynamic :
	cp Program-dynamic.java Program.java
	$(java) -classpath $(CLASSPATH) java_cup.Main interpreterext.cup
	$(lex) interpreterext.flex
	$(javac) -classpath $(CLASSPATH) parser.java sym.java Yylex.java Program.java SymbolTable.java Linker.java Symbol.java FunctionTable.java Instruction.java PseudoInstruction.java

run-static : compile-static run
run-dynamic : compile-dynamic run

run : interpreterext.cup interpreterext.flex Program.java SymbolTable.java Linker.java Symbol.java FunctionTable.java Instruction.java PseudoInstruction.java
	$(java) -classpath $(CLASSPATH) parser

clean :
	-\rm -v *.class
	-\rm -v parser.java
	-\rm -v sym.java
	-\rm -v Yylex.java*
	-\rm -v *.out
