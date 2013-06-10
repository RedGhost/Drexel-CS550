import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

class Expr extends Exception {

    public Expr() {
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	return new Number(0);
    }
}

class ValueType extends Expr {
    public ValueType() {
	super();
    }

    public String toString(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	return new String("");
    }
}

class Ident extends Expr {

    private String name;

    public Ident(String s) {
	name = s;
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	return nametable.get(name);
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

    public int intValue() {
	return value.intValue();
    }

    public String toString(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	return value.toString();
    }

    public static boolean numberp(ValueType vt) {
	return (vt instanceof Number);
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	return this;
    }
}

class Times extends Expr {

    private Expr expr1, expr2;

    public Times(Expr op1, Expr op2) {
	expr1 = op1;
	expr2 = op2;
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	// FIXME: need to do type checking for other operators (+,-) and write
	// better exception text
	ValueType vt1 = expr1.eval(nametable, klasstable);
	ValueType vt2 = expr2.eval(nametable, klasstable);
	if (!Number.numberp(vt1)) {
	    throw new RuntimeException("ERROR");
	} else if (!Number.numberp(vt2)) {
	    throw new RuntimeException("ERROR");
	}
	return new Number(((Number) vt1).intValue() * ((Number) vt2).intValue());
    }
}

class Not extends Expr {

    private Expr expr1;

    public Not(Expr op1) {
	expr1 = op1;
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {

	int value = ((Number) expr1.eval(nametable, klasstable))
	    .intValue();
	if (value <= 0) {
	    value = 1;
	} else {
	    value = 0;
	}
	return new Number(value);
    }
}

class Plus extends Expr {

    private Expr expr1, expr2;

    public Plus(Expr op1, Expr op2) {
	expr1 = op1;
	expr2 = op2;
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	Number e1 = ((Number) expr1.eval(nametable, klasstable));
	Number e2 = ((Number) expr2.eval(nametable, klasstable));

	return new Number(e1.intValue() + e2.intValue());
    }
}

class Minus extends Expr {

    private Expr expr1, expr2;

    public Minus(Expr op1, Expr op2) {
	expr1 = op1;
	expr2 = op2;
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	Number e1 = ((Number) expr1.eval(nametable, klasstable));
	Number e2 = ((Number) expr2.eval(nametable, klasstable));

	return new Number(e1.intValue() - e2.intValue());
    }
}

// added for 2c
class FunctionCall extends Expr {

    private String funcid;
    private ExpressionList explist;

    public FunctionCall(String id, ExpressionList el) {
	funcid = id;
	explist = el;
	//System.out.println(funcid + " " + explist.getExpressions());
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	//System.out.println(funcid);
	//System.out.println(nametable.get(funcid).getClass());
	if(nametable.get(funcid) != null &&
	   nametable.get(funcid) instanceof Proc) {
	    return ((Proc)nametable.get(funcid)).apply(nametable, klasstable, explist);
	}
	else if (klasstable.get(funcid) != null) {
	    return klasstable.get(funcid).apply(nametable, klasstable, explist);
	}
	else {
	    throw new RuntimeException("Tried to call function that doesn't exist!!!");
	}
    }
}

class MethodCall extends Expr {

    private String objectid;
    private String funcid;
    private ExpressionList explist;

    public MethodCall(String objid, String funcid, ExpressionList el) {
	this.objectid = objid;
	this.funcid = funcid;
	this.explist = el;
	//System.out.println(funcid + " " + explist.getExpressions());
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	//System.out.println("objid = " + objectid + ", funcid = " + funcid);
	//System.out.println(nametable.get(funcid).getClass());
	ValueType vt = nametable.get(objectid);
	if(!(vt instanceof Klass)){
	    throw new RuntimeException("Attempted to call method on non-object: " + objectid);
	}
	
	return ((Klass)vt).getProc(funcid).apply(((Klass)vt).getNametable(), klasstable, explist);
    }
}

abstract class Statement {

    public Statement() {
    }

    public void eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) throws Exception {
    }
}


class ReturnStatement extends Statement {

    private Expr expr;

    public ReturnStatement(Expr e) {
	expr = e;
    }

    public void eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) throws Exception {
	// Java can't throw exceptions of numbers, so we'll convert it to a
	// string
	// and then on the other end we'll reconvert back to Integer..
	throw expr.eval(nametable, klasstable);
    }
}

class AssignStatement extends Statement {

    private String name;
    private Expr expr;

    public AssignStatement(String id, Expr e) {
	name = id;
	expr = e;
    }

    public void eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	ValueType vt = expr.eval(nametable, klasstable);
	nametable.put(name, vt);
    }
}

class KlassDefineStatement extends Statement {

    private String name;
    private ParamList paramList;
    private StatementList statementList;
    private String parent_name;

    public KlassDefineStatement(String id, ParamList pl, StatementList sl) {
	name = id;
	paramList = pl;
	statementList = sl;
    }

    public KlassDefineStatement(String id, ParamList pl, StatementList sl, String p) {
	this(id, pl, sl);
	parent_name = p;
    }

    public void eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	Klass k;
	if(parent_name != null) {
	    k = new Klass(paramList, statementList, parent_name);
	} else {
	    k = new Klass(paramList, statementList);
	}
	klasstable.put(name, k);
    }
}

class IfStatement extends Statement {

    private Expr expr;
    private StatementList stmtlist1, stmtlist2;

    public IfStatement(Expr e, StatementList list1, StatementList list2) {
	expr = e;
	stmtlist1 = list1;
	stmtlist2 = list2;
    }

    public IfStatement(Expr e, StatementList list) {
	expr = e;
	stmtlist1 = list;
    }

    public void eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) throws Exception {
	ValueType result = expr.eval(nametable, klasstable);
	if (!Number.numberp(result)) {
	    throw new RuntimeException(
				       "Expression in if statement does not evaluate to a Number!.");
	}

	if (((Number) result).intValue() > 0) {
	    stmtlist1.eval(nametable, klasstable);
	} else {
	    stmtlist2.eval(nametable, klasstable);
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

    public void eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) throws Exception {

	ValueType result = expr.eval(nametable, klasstable);

	if (!Number.numberp(result)) {
	    throw new RuntimeException(
				       "Expression in while statement does not evaluate to a Number!.");
	}

	while (((Number) expr.eval(nametable, klasstable))
	       .intValue() > 0) {
	    stmtlist.eval(nametable, klasstable);
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

    public void eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) throws Exception {
	ValueType result = expr.eval(nametable, klasstable);
	if (!Number.numberp(result)) {
	    throw new RuntimeException(
				       "Expression in if statement does not evaluate to a Number!.");
	}
	do {
	    sl.eval(nametable, klasstable);
	} while (((Number) expr.eval(nametable, klasstable))
		 .intValue() > 0);

    }
}

// added for 2c
class ParamList {

    private LinkedList<String> parameterlist;

    public ParamList() {
	parameterlist = new LinkedList<String>();
    }

    public ParamList(String name) {
	parameterlist = new LinkedList<String>();
	parameterlist.add(name);
    }

    public ParamList(String name, ParamList parlist) {
	parameterlist = parlist.getParamList();
	parameterlist.add(0,name); // Want to add at front
    }

    public LinkedList<String> getParamList() {
	return parameterlist;
    }
}

// Added for 2c
class ExpressionList {

    private LinkedList<Expr> list;

    public ExpressionList() {
	list = new LinkedList<Expr>();
    }

    public ExpressionList(Expr ex) {
	list = new LinkedList<Expr>();
	list.add(ex);
    }

    public ExpressionList(Expr ex, ExpressionList el) {
	list = el.getExpressions();
	list.add(0, ex);
    }

    public LinkedList<Expr> getExpressions() {
	return list;
    }
}

class StatementList {

    private LinkedList<Statement> statementlist;

    public StatementList() {
	statementlist = new LinkedList<Statement>();
    }

    public StatementList(Statement statement) {
	statementlist = new LinkedList<Statement>();
	statementlist.add(statement);
    }

    public void eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) throws Exception {

	for (Statement stmt : statementlist) {
	    stmt.eval(nametable, klasstable);
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

class Proc extends ValueType {
    private ParamList parameterlist;
    private StatementList stmtlist;
    private HashMap<String, ValueType> newnametable;

    public Proc(ParamList pl, StatementList sl) {
	parameterlist = pl;
	stmtlist = sl;
    }

    public String toString(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	// TODO: Implement functions toString()...
	return new String("");
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	return this;
    }

    public ValueType apply(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable, ExpressionList expressionlist) {
	// System.out.println("Executing Proceedure");

	// bind parameters in new name table
	// we need to get the underlying List structure that the ParamList
	// uses...
	newnametable = nametable;
	Iterator<String> p = parameterlist.getParamList().iterator();
	Iterator<Expr> e = expressionlist.getExpressions().iterator();

	if (parameterlist.getParamList().size() != expressionlist
	    .getExpressions().size()) {
	    System.out.println("Param count does not match: param list=" + parameterlist.getParamList() +
			       " expression list=" + expressionlist.getExpressions());
	    System.exit(1);
	}
	while (p.hasNext() && e.hasNext()) {
	    // assign the evaluation of the expression to the parameter name.
	    newnametable.put(p.next(),
			     e.next().eval(nametable, klasstable));
	    // System.out.println("Loading Nametable for procedure with: "+p+" = "+nametable.get(p));
	}
	// evaluate function body using new name table and
	// old function table
	// eval statement list and catch return
	// System.out.println("Beginning Proceedure Execution..");
	try {
	    stmtlist.eval(newnametable, klasstable);
	} catch (Exception result) {
	    // Note, the result shold contain the proceedure's return value as a
	    // String
	    //System.out.println();
	    //		        result.printStackTrace();
	    // System.out.println();
	    return (ValueType)result;
	}
	System.out.println("Error:  no return value");
	System.exit(1);
	// need this or the compiler will complain, but should never
	// reach this...
	return null;
    }
}

class UnnamedProc extends ValueType {

    private ParamList parameterlist;
    private StatementList stmtlist;
    private ExpressionList exprList;
    private HashMap<String, ValueType> newnametable;

    public UnnamedProc(ParamList pl, StatementList sl, ExpressionList el) {
	parameterlist = pl;
	stmtlist = sl;
	exprList = el;
    }

    public String toString(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	// TODO: Implement functions toString()...
	return new String("");
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	return apply(nametable, klasstable, exprList);
    }

    public ValueType apply(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable, ExpressionList expressionlist) {
	// System.out.println("Executing Proceedure");

	// bind parameters in new name table
	// we need to get the underlying List structure that the ParamList
	// uses...
	newnametable = nametable;
	Iterator<String> p = parameterlist.getParamList().iterator();
	Iterator<Expr> e = expressionlist.getExpressions().iterator();

	if (parameterlist.getParamList().size() != expressionlist
	    .getExpressions().size()) {
	    System.out.println("Param count does not match: param list=" + parameterlist.getParamList() +
			       " expression list=" + expressionlist.getExpressions());
	    System.exit(1);
	}
	while (p.hasNext() && e.hasNext()) {

	    // assign the evaluation of the expression to the parameter name.
	    newnametable.put(p.next(),
			     e.next().eval(nametable, klasstable));
	    // System.out.println("Loading Nametable for procedure with: "+p+" = "+nametable.get(p));

	}
	// evaluate function body using new name table and
	// old function table
	// eval statement list and catch return
	// System.out.println("Beginning Proceedure Execution..");
	try {
	    stmtlist.eval(newnametable, klasstable);
	} catch (Exception result) {
	    // Note, the result shold contain the proceedure's return value as a
	    // String
	    // System.out.println();
	    //		        result.printStackTrace();
	    // System.out.println();
	    return (ValueType)result;
	}
	System.out.println("Error:  no return value");
	System.exit(1);
	// need this or the compiler will complain, but should never
	// reach this...
	return null;
    }
}

class Klass extends ValueType {
    private ParamList parameterlist;
    private StatementList stmtlist;
    private String parentName;
    private HashMap<String, ValueType> newnametable = new HashMap<String, ValueType>();

    public Klass(ParamList pl, StatementList sl) {
	parameterlist = pl;
	stmtlist = sl;
    }

    public Klass(ParamList pl, StatementList sl, String parent) {
	this(pl,sl);
	parentName=parent;
    }

    public String toString(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	StringBuilder returnString = new StringBuilder();
	returnString.append("<");
	for(String name : newnametable.keySet()) {
	    if(newnametable.get(name) instanceof Proc) {
		returnString.append(name + " defined,");
	    } else {
		returnString.append(name + '=' + newnametable.get(name).toString(nametable, klasstable) + ',');
	    }
	}
	returnString.deleteCharAt(returnString.length() - 1);
	returnString.append(">");
	return returnString.toString();
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	return this;
    }

    public ValueType apply(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable, ExpressionList expressionlist) {
	// System.out.println("Executing Proceedure");

	// bind parameters in new name table
	// we need to get the underlying List structure that the ParamList
	// uses...
	Iterator<String> p = parameterlist.getParamList().iterator();
	Iterator<Expr> e = expressionlist.getExpressions().iterator();

	if (parameterlist.getParamList().size() != expressionlist
	    .getExpressions().size()) {
	    System.out.println("Param count does not match: param list=" + parameterlist.getParamList() +
			       " expression list=" + expressionlist.getExpressions());
	    System.exit(1);
	}
	while (p.hasNext() && e.hasNext()) {
	    // assign the evaluation of the expression to the parameter name.
	    newnametable.put(p.next(),
			     e.next().eval(nametable, klasstable));
	    // System.out.println("Loading Nametable for procedure with: "+p+" = "+nametable.get(p));
	}
	try{
	    stmtlist.eval(newnametable, klasstable);
	} catch (Exception result) {
	    // Note, the result shold contain the proceedure's return value as a
	    // String
	    // System.out.println();
	    //		        result.printStackTrace();
	    // System.out.println();
	    return this;
	}
	// need this or the compiler will complain, but should never
	// reach this...
	return this;
    }

    public HashMap<String, ValueType> getNametable() {
	return newnametable;
    }

    public Proc getProc(String funcid) {
	return (Proc) newnametable.get(funcid);
    }
}

class Program {

    private StatementList stmtlist;

    private int startingAddress;
    private Symbol label;

    public Program(StatementList list) {
	stmtlist = list;
    }

    public void eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	try {
	    stmtlist.eval(nametable, klasstable);
	} catch (Exception e) {
	    //			e.printStackTrace();
	    System.out.println(e.getMessage());
	}
    }

    public void dump(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable, LinkedList var, SymbolTable st,
		     Linker l) {
	System.out.println(nametable.values());
	System.out.println("Dumping out all the variables...");
	if (nametable != null) {
	    for (String name : nametable.keySet()) {
		if(nametable.get(name) instanceof Proc) {
		    System.out.println(name + " defined");
		}
		else {
		    System.out.println(name
				       + "="
				       + nametable.get(name).toString(nametable, klasstable));
		}
	    }
	}
    }
}

// Assignment 2 (mwa29)

class Sequence {
    LinkedList<Expr> seq;

    public Sequence(Sequence s, Expr e) {
	seq = s.expressions();
	seq.add(0, e);
    }

    public Sequence(Expr e) {
	seq = new LinkedList<Expr>();
	seq.add(e);
    }

    public Sequence() {
	seq = new LinkedList<Expr>();
    }

    public LinkedList<Expr> expressions() {
	return seq;
    }

    public Sequence clone() {
	Sequence temp = new Sequence();
	for (Expr e : seq) {
	    temp.expressions().add(e);
	}
	return temp;
    }
}

class List extends ValueType {
    private Sequence s;

    public List(Sequence seq) {
	s = seq;
    }

    public List() {
	s = new Sequence();
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	return this;
    }

    public String toString(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	StringBuilder returnString = new StringBuilder("[");
	boolean flag = false; // Check if the list is empty
	for (Expr vt : s.seq) {
	    flag = true;
	    returnString.append(vt.eval(nametable, klasstable)
				.toString(nametable, klasstable));
	    returnString.append(",");
	}
	// Remove the trailing ','
	returnString.deleteCharAt(returnString.length() - 1);
	returnString.append("]");
	if (flag == false) // Delete the "]"
	    returnString.deleteCharAt(returnString.length() - 1);
	return returnString.toString();
    }

    public List clone() {
	Sequence clone = s.clone();
	return new List(clone);
    }

    public Sequence sequence() {
	return s;
    }

    public static boolean listp(ValueType vt) {
	return (vt instanceof List);
    }

    public List cons(ValueType e) {
	s.expressions().add(0, e);
	return this;
    }

    public Expr car() {
	return s.expressions().getFirst();
    }

    public Expr cdr() {
	Sequence copy = s.clone();
	copy.expressions().remove(0);
	return new List(copy);
    }

    public void concat(List list2) {
	for (Expr e : list2.sequence().expressions()) {
	    s.expressions().add(e);
	}
    }
}

class Cons extends Expr {

    private Expr e;
    private Expr L;

    public Cons(Expr e, Expr L) {
	this.e = e;
	this.L = L;
    }

    // FIXME The parser crashes when it encounters variables passed into this
    // function that are not surrounded by parentheses. (i.e. y :=
    // cons(x,[1,2]) fails to parse but y := cons((x),[1,2]) works)
    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	ValueType element = e.eval(nametable, klasstable);
	ValueType list = L.eval(nametable, klasstable);

	if (list instanceof List) {
	    List copy = ((List) list).clone();
	    return copy.cons(element)
		.eval(nametable, klasstable);
	} else {
	    // Must pass a list to car. Otherwise, error.
	    throw new RuntimeException("Invalid value type passed to cons: "
				       + list.getClass());
	}

    }
}

class Car extends Expr {

    private Expr L;

    public Car(Expr L) {
	this.L = L;
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	ValueType list = L.eval(nametable, klasstable);
	// If the list is empty, throw an exception saying so
	List temp = (List) list;
	if (temp.sequence().expressions().size() == 0)
	    throw new RuntimeException("Attempting to Car an empty list");
	else if (list instanceof List) {
	    return ((List) list).car().eval(nametable, klasstable);
	} else {
	    // Must pass a list to car. Otherwise, error.
	    throw new RuntimeException("Invalid value type passed to car: "
				       + list.getClass());
	}
    }
}

class Cdr extends Expr {

    private Expr L;

    public Cdr(Expr L) {
	this.L = L;
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {

	ValueType list = L.eval(nametable, klasstable);
	// If there's only one element in the list, return an empty list
	List temp = (List) list;
	if (temp.sequence().expressions().size() == 1)
	    return new List();
	else if (list instanceof List) {
	    try { // Check if the list is empty
		return ((List) list).cdr().eval(nametable, klasstable);
	    } catch (Exception e) {
		throw new RuntimeException("Attempting to Access Null Value");
	    }
	} else {
	    // Must pass a list to cdr. Otherwise, error.
	    throw new RuntimeException("Invalid value type passed to cdr: "
				       + list.getClass());
	}
    }
}

class Nullp extends Expr {

    private Expr L;

    public Nullp(Expr L) {
	this.L = L;
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	ValueType list = L.eval(nametable, klasstable);
	// Check for empty list as well as null
	List temp = (List) list;
	if (list == null || temp.sequence().expressions().size() == 0) {
	    return new Number(1);
	} else if (list instanceof List) {
	    return new Number(0);
	} else {
	    throw new RuntimeException("Invalid value type passed to nullp: "
				       + L.eval(nametable, klasstable).getClass());
	}
    }
}

class Intp extends Expr {

    private Expr e;

    public Intp(Expr e) {
	this.e = e;
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	if (e.eval(nametable, klasstable) instanceof Number) {
	    return new Number(1);
	} else {
	    return new Number(0);
	}
    }
}

class Listp extends Expr {

    private Expr e;

    public Listp(Expr e) {
	this.e = e;
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	if (e.eval(nametable, klasstable) instanceof List) {
	    return new Number(1);
	} else {
	    return new Number(0);
	}
    }
}

class Concat extends Expr {

    private Expr l1;
    private Expr l2;

    public Concat(Expr l1, Expr l2) {
	this.l1 = l1;
	this.l2 = l2;
    }

    public ValueType eval(HashMap<String, ValueType> nametable, HashMap<String, Klass> klasstable) {
	ValueType list1 = l1.eval(nametable, klasstable);
	ValueType list2 = l2.eval(nametable, klasstable);

	if (list1 instanceof List && list2 instanceof List) {
	    List copy = ((List) list1).clone();
	    copy.concat((List) list2);
	    return copy;
	} else {
	    // Must pass a list to car. Otherwise, error.
	    throw new RuntimeException("Invalid value type passed to concat: "
				       + list1.getClass() + " - " + list2.getClass());
	}

    }
}
