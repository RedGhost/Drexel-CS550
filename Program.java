//
// Group 3
// Assignment 2
//
import java.util.*;

class Expr {

    public Expr() {
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Proc> functiontable, LinkedList var) {
        return new Number(0);
    }
}

class ValueType extends Expr{
    public ValueType() {
	super();
    }

    public String toString(HashMap<String, ValueType> nametable, HashMap<String, Proc> functiontable, LinkedList var) {
	return new String("");
    }    
}

class Ident extends Expr {

    private String name;

    public Ident(String s) {
        name = s;
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Proc> functiontable, LinkedList var) {
 	ValueType vt = nametable.get(name);
	if(!Number.numberp(vt))
	{
	    return new Number(vt.toString(nametable, functiontable, var));
	}
	else
	{
	    return vt;
	}
    }
}

class Number extends ValueType {

    private Integer value;

    public Number(int n) {
        value = new Integer(n);
    }

    public Number(Integer n) {
        value = n;
    }

    public Number(String n) {
        value = Integer.parseInt(n);
    }

    public int intValue(){
	return value.intValue();
    }

    public String toString(HashMap<String, ValueType> nametable, HashMap<String, Proc> functiontable, LinkedList var) {
	return value.toString();
    }

    public static boolean numberp(ValueType vt){
	return (vt instanceof Number);
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Proc> functiontable, LinkedList var) {
        return this;
    }
}

class Times extends Expr {

    private Expr expr1,  expr2;

    public Times(Expr op1, Expr op2) {
        expr1 = op1;
        expr2 = op2;
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Proc> functiontable, LinkedList var) {
	// FIXME: need to do type checking for other operators (+,-) and write better exception text
	ValueType vt1 = expr1.eval(nametable, functiontable, var);
	ValueType vt2 = expr2.eval(nametable, functiontable, var);
	if(!Number.numberp(vt1)){
	    throw new RuntimeException("ERROR");
	}
	else if(!Number.numberp(vt2)){
	    throw new RuntimeException("ERROR");
	}
        return new Number(((Number)vt1).intValue() *
			  ((Number)vt2).intValue());
    }
}

class Plus extends Expr {

    private Expr expr1,  expr2;

    public Plus(Expr op1, Expr op2) {
        expr1 = op1;
        expr2 = op2;
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Proc> functiontable, LinkedList var) {
        return new Number(((Number)expr1.eval(nametable, functiontable, var)).intValue() +
			  ((Number)expr2.eval(nametable, functiontable, var)).intValue());
    }
}

class Minus extends Expr {

    private Expr expr1,  expr2;

    public Minus(Expr op1, Expr op2) {
        expr1 = op1;
        expr2 = op2;
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Proc> functiontable, LinkedList var) {
        return new Number(((Number)expr1.eval(nametable, functiontable, var)).intValue() -
			  ((Number)expr2.eval(nametable, functiontable, var)).intValue());
    }
}

//added for 2c
class FunctionCall extends Expr {

    private String funcid;
    private ExpressionList explist;

    public FunctionCall(String id, ExpressionList el) {
        funcid = id;
        explist = el;
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Proc> functiontable, LinkedList var) {
        return functiontable.get(funcid).apply(nametable, functiontable, var, explist);
    }
}

abstract class Statement {

    public Statement() {
    }

    public void eval(HashMap<String, ValueType> nametable, HashMap<String, Proc> functiontable, LinkedList var) throws Exception {
    }
}

// added for 2c
class DefineStatement extends Statement {

    private String name;
    private Proc proc;
    private ParamList paramlist;
    private StatementList statementlist;

    public DefineStatement(String id, Proc process) {
        name = id;
        proc = process;
    }

    public void eval(HashMap<String, ValueType> nametable, HashMap<String, Proc> functable, LinkedList var) {
        // get the named proc object from the function table.
        //System.out.println("Adding Process:"+name+" to Functiontable");
        functable.put(name, proc);
    }
}

class ReturnStatement extends Statement {

    private Expr expr;

    public ReturnStatement(Expr e) {
        expr = e;
    }

    public void eval(HashMap<String, ValueType> nametable, HashMap<String, Proc> functiontable, LinkedList var) throws Exception {
        //Java can't throw exceptions of numbers, so we'll convert it to a string
        //and then on the other end we'll reconvert back to Integer..
        throw new Exception(expr.eval(nametable, functiontable, var).toString(nametable, functiontable, var));
    }
}

class AssignStatement extends Statement {

    private String name;
    private Expr expr;

    public AssignStatement(String id, Expr e) {
        name = id;
        expr = e;
    }

    public void eval(HashMap<String, ValueType> nametable, HashMap<String, Proc> functiontable, LinkedList var) {
        /* add name to the statementlist of variable names */
        if (!var.contains(name)) {
            var.add(name);
        //insert the variable with the specified name into the table with the 
        // evaluated result (which must be an integer
        }
        nametable.put(name, expr.eval(nametable, functiontable, var));
    }
}

class IfStatement extends Statement {

    private Expr expr;
    private StatementList stmtlist1,  stmtlist2;

    public IfStatement(Expr e, StatementList list1, StatementList list2) {
        expr = e;
        stmtlist1 = list1;
        stmtlist2 = list2;
    }

    public IfStatement(Expr e, StatementList list) {
        expr = e;
        stmtlist1 = list;
    }

    public void eval(HashMap<String, ValueType> nametable, HashMap<String, Proc> functiontable, LinkedList var) throws Exception {
	ValueType result = expr.eval(nametable, functiontable, var);
	if(Number.numberp(result)) {
	    throw new RuntimeException("Expression in if statement does not evaluate to a Number!.");
	}
        if (((Number)expr.eval(nametable, functiontable, var)).intValue() > 0) {
            stmtlist1.eval(nametable, functiontable, var);
        } else {
            stmtlist2.eval(nametable, functiontable, var);
        }
    }
}

class WhileStatement extends Statement {

    private Expr expr;
    private StatementList stmtlist;

    public WhileStatement(Expr e, StatementList list) {
        expr = e;
        stmtlist = list;
    }

    public void eval(HashMap<String, ValueType> nametable, HashMap<String, Proc> functiontable, LinkedList var) throws Exception {
	ValueType result = expr.eval(nametable, functiontable, var);
	if(!Number.numberp(result)) {
	    throw new RuntimeException("Expression in while statement does not evaluate to a Number!.");
	}
        while (((Number)expr.eval(nametable, functiontable, var)).intValue() > 0) {
            stmtlist.eval(nametable, functiontable, var);
        }
    }
}

class RepeatStatement extends Statement {

    private Expr expr;
    private StatementList sl;

    public RepeatStatement(StatementList list, Expr e) {
        expr = e;
        sl = list;
    }

    public void eval(HashMap<String, ValueType> nametable, HashMap<String, Proc> functiontable, LinkedList var) throws Exception {
	ValueType result = expr.eval(nametable, functiontable, var);
	if(!Number.numberp(result)) {
	    throw new RuntimeException("Expression in if statement does not evaluate to a Number!.");
	}
        do {
            sl.eval(nametable, functiontable, var);
        } while (((Number)expr.eval(nametable, functiontable, var)).intValue() > 0);

    }
}

//added for 2c
class ParamList {

    private LinkedList<String> parameterlist;

    public ParamList(String name) {
        parameterlist = new LinkedList<String>();
        parameterlist.add(name);
    }

    public ParamList(String name, ParamList parlist) {
        parameterlist = parlist.getParamList();
        parameterlist.add(name);
    }

    public LinkedList<String> getParamList() {
        return parameterlist;
    }
}

// Added for 2c
class ExpressionList {

    private LinkedList<Expr> list;

    public ExpressionList(Expr ex) {
        list = new LinkedList<Expr>();
        list.add(ex);
    }

    public ExpressionList(Expr ex, ExpressionList el) {
        list = new LinkedList<Expr>();
        //we need ot add the expression to the front of the list
        list.add(0, ex);

    }

    public LinkedList<Expr> getExpressions() {
        return list;
    }
}

class StatementList {

    private LinkedList<Statement> statementlist;

    public StatementList(Statement statement) {
        statementlist = new LinkedList<Statement>();
        statementlist.add(statement);
    }

    public void eval(HashMap<String, ValueType> nametable, HashMap<String, Proc> functiontable, LinkedList var) throws Exception {


        for (Statement stmt : statementlist) {
            stmt.eval(nametable, functiontable, var);

        }
    }

    public void insert(Statement s) {
        // we need to add it to the front of the list
        statementlist.add(0, s);
    }

    public LinkedList<Statement> getStatements() {
        return statementlist;
    }
}

class Proc {

    private ParamList parameterlist;
    private StatementList stmtlist;

    public Proc(ParamList pl, StatementList sl) {
        parameterlist = pl;
        stmtlist = sl;
    }

    public ValueType apply(HashMap<String, ValueType> nametable, HashMap<String, Proc> functiontable, LinkedList var, ExpressionList expressionlist) {
        //System.out.println("Executing Proceedure");
        HashMap<String, ValueType> newnametable = new HashMap<String, ValueType>();

        // bind parameters in new name table
        // we need to get the underlying List structure that the ParamList uses...
        Iterator<String> p = parameterlist.getParamList().iterator();
        Iterator<Expr> e = expressionlist.getExpressions().iterator();

        if (parameterlist.getParamList().size() != expressionlist.getExpressions().size()) {
            System.out.println("Param count does not match");
            System.exit(1);
        }
        while (p.hasNext() && e.hasNext()) {

            // assign the evaluation of the expression to the parameter name.
            newnametable.put(p.next(), e.next().eval(nametable, functiontable, var));
        //System.out.println("Loading Nametable for procedure with: "+p+" = "+nametable.get(p));

        }
        // evaluate function body using new name table and 
        // old function table
        // eval statement list and catch return
        //System.out.println("Beginning Proceedure Execution..");
        try {
            stmtlist.eval(newnametable, functiontable, var);
        } catch (Exception result) {
            // Note, the result shold contain the proceedure's return value as a String
            //System.out.println("return value = "+result.getMessage());
            return new Number(result.getMessage());
        }
        System.out.println("Error:  no return value");
        System.exit(1);
        // need this or the compiler will complain, but should never
        // reach this...
        return null;
    }
}

class Program {

    private StatementList stmtlist;

    public Program(StatementList list) {
        stmtlist = list;
    }

    public void eval(HashMap<String, ValueType> nametable, HashMap<String, Proc> functiontable, LinkedList var) {
        try {
            stmtlist.eval(nametable, functiontable, var);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void dump(HashMap<String, ValueType> nametable, HashMap<String, Proc> functiontable, LinkedList var) {
        //System.out.println(hm.values());
        System.out.println("Dumping out all the variables...");
        if (nametable != null) {
            for (String name : nametable.keySet()) {
		System.out.println(name + "=" + nametable.get(name).toString(nametable, functiontable, var));
            }
        }
        if (functiontable != null) {
            for (String name : functiontable.keySet()) {
                System.out.println("Function: " + name + " defined...");
            }
        }
    }
}


// Assignment 2 (mwa29)

class Sequence{
    LinkedList<Expr> seq;
    public Sequence()
    {
	seq = new LinkedList<Expr>();
    }

    public Sequence(Expr e)
    {
	seq = new LinkedList<Expr>();
	seq.add(e);
    }

    public void add(Expr e)
    {
	seq.add(e);
    }
}

class List extends ValueType {
    private Sequence s;

    public List(Sequence seq){
	
	s = seq;
    }

    public List(){
	s = new Sequence();
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Proc> functiontable, LinkedList var) {
	return this;
	//	throw new RuntimeException("Cannot eval a list");
    }

    public String toString(HashMap<String, ValueType> nametable, HashMap<String, Proc> functiontable, LinkedList var) {
	StringBuilder returnString = new StringBuilder("[");
	for(Expr vt : s.seq)
	{
	    returnString.append(vt.eval(nametable, functiontable, var).toString(nametable, functiontable, var));
	    returnString.append(",");
	}
	returnString.deleteCharAt(returnString.length() - 1); // Remove the trailing ','
	returnString.append("]");
	return returnString.toString();
    }

    public static boolean listp(ValueType vt)
    {
	return (vt instanceof List);
    }
}

