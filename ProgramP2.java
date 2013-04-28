//
// Group 3
// Assignment 2
//
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;

class ConsCell extends ValueType {
  private int location;
  private boolean marked;
  private boolean list;
  private Expr car;
  private int cdr;

  public ConsCell(int location, Expr car, int cdr) {
    this.location = location;
    this.car = car;
    this.cdr = cdr;
    this.list = false;
    this.marked = false;
  }  
    
  public void setCar(Expr car) {
    if(car instanceof ConsCell) {
      this.list = true;
      this.car = new Number(((ConsCell)car).getLocation());
    }
    else {
      this.car = car;
      this.list = false;
    }
  }

  public void setCdr(int cdr) {
    this.cdr = cdr;
  }

  public int getCdr() {
    return cdr;
  }

  public Expr getCar() {
    return car;
  }

  public boolean isList() {
    return list;
  }

  public int getLocation() {
    return location;
  }

  public String toString(HashMap<String, ValueType> nametable,
    HashMap<String, Proc> functiontable, LinkedList var) {
    StringBuilder returnString = new StringBuilder("[");

    int nextCell = cdr;
    ConsCell lastCell = this;
    if(lastCell.isList()) {
      ConsCell cell = (ConsCell)Memory.getInstance().cellAt(((Number)car.eval(nametable, functiontable, var)).intValue());
      returnString.append(cell.eval(nametable, functiontable, var).toString(nametable, functiontable, var));
    }
    else {
      returnString.append(car.eval(nametable, functiontable, var).toString(nametable, functiontable, var));
    }
    while(nextCell >= 0) {
      lastCell = ((ConsCell)Memory.getInstance().cellAt(nextCell));
      nextCell = lastCell.getCdr();
      returnString.append(",");
      if(lastCell.isList()) {
        ConsCell cell = (ConsCell)Memory.getInstance().cellAt(((Number)lastCell.getCar().eval(nametable, functiontable, var)).intValue());
        returnString.append(cell.eval(nametable, functiontable, var).toString(nametable, functiontable, var));
      }
      else {
        returnString.append(lastCell.getCar().eval(nametable, functiontable, var).toString(nametable, functiontable, var));
      }
    }

    returnString.append("]");
    return returnString.toString();
  }

  public ValueType eval(HashMap<String, ValueType> nametable,
    HashMap<String, Proc> functiontable, LinkedList var) {
    return this;
  }
}

class Memory {
  private static Memory memory = new Memory(500);

  private ArrayList allCells;
  private LinkedList available;

  public static Memory getInstance() {
    return memory;
  }

  public Memory(int size) {
    allCells = new ArrayList(size);
    available = new LinkedList();
    for(int i = 0; i < size; i++) {
      ConsCell cell = new ConsCell(i, new Number(0), -1);
      allCells.add(cell);
      available.add(cell);
    }
  }

  public ConsCell cellAt(int i) {
    if(i < allCells.size()) {
      return (ConsCell) allCells.get(i);
    }
    else {
      throw new RuntimeException("Memory location out of bounds error.");
    }
  }

  public ConsCell allocateCell() {
    if(available.size() == 0) {
      // DO GARBAGE COLLECTION
      return null;
    }
    else {
      return (ConsCell)available.remove();
    }
  }

  public void deallocateCell(ConsCell cell) {
    available.add(cell);
  }
}


class Expr {

	public Expr() {
	}

	public ValueType eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var) {
		return new Number(0);
	}
}

class ValueType extends Expr {
	public ValueType() {
		super();
	}

	public String toString(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var) {
		return new String("");
	}
}

class Ident extends Expr {

	private String name;

	public Ident(String s) {
		name = s;
	}

	public ValueType eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var) {
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

	public String toString(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var) {
		return value.toString();
	}

	public static boolean numberp(ValueType vt) {
		return (vt instanceof Number);
	}

	public ValueType eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var) {
		return this;
	}
}

class Times extends Expr {

	private Expr expr1, expr2;

	public Times(Expr op1, Expr op2) {
		expr1 = op1;
		expr2 = op2;
	}

	public ValueType eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var) {
		// FIXME: need to do type checking for other operators (+,-) and write
		// better exception text
		ValueType vt1 = expr1.eval(nametable, functiontable, var);
		ValueType vt2 = expr2.eval(nametable, functiontable, var);
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

	public ValueType eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var) {
                
		int value = ((Number) expr1.eval(nametable, functiontable, var)).intValue();
                if(value <= 0) {
                  value = 1;
                }
                else {
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

	public ValueType eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var) {
		return new Number(
				((Number) expr1.eval(nametable, functiontable, var)).intValue()
						+ ((Number) expr2.eval(nametable, functiontable, var))
								.intValue());
	}
}

class Minus extends Expr {

	private Expr expr1, expr2;

	public Minus(Expr op1, Expr op2) {
		expr1 = op1;
		expr2 = op2;
	}

	public ValueType eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var) {
		return new Number(
				((Number) expr1.eval(nametable, functiontable, var)).intValue()
						- ((Number) expr2.eval(nametable, functiontable, var))
								.intValue());
	}
}

// added for 2c
class FunctionCall extends Expr {

	private String funcid;
	private ExpressionList explist;

	public FunctionCall(String id, ExpressionList el) {
		funcid = id;
		explist = el;
	}

	public ValueType eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var) {
		return functiontable.get(funcid).apply(nametable, functiontable, var,
				explist);
	}
}

abstract class Statement {

	public Statement() {
	}

	public void eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var)
			throws Exception {
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

	public void eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functable, LinkedList var) {
		// get the named proc object from the function table.
		// System.out.println("Adding Process:"+name+" to Functiontable");
		functable.put(name, proc);
	}
}

class ReturnStatement extends Statement {

	private Expr expr;

	public ReturnStatement(Expr e) {
		expr = e;
	}

	public void eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var)
			throws Exception {
		// Java can't throw exceptions of numbers, so we'll convert it to a
		// string
		// and then on the other end we'll reconvert back to Integer..
		throw new Exception(expr.eval(nametable, functiontable, var).toString(
				nametable, functiontable, var));
	}
}

class AssignStatement extends Statement {

	private String name;
	private Expr expr;

	public AssignStatement(String id, Expr e) {
		name = id;
		expr = e;
	}

	public void eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var) {
		/* add name to the statementlist of variable names */
		if (!var.contains(name)) {
			var.add(name);
			// insert the variable with the specified name into the table with
			// the
			// evaluated result (which must be an integer
		}
		nametable.put(name, expr.eval(nametable, functiontable, var));
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

	public void eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var)
			throws Exception {
		ValueType result = expr.eval(nametable, functiontable, var);
		if (!Number.numberp(result)) {
			throw new RuntimeException(
					"Expression in if statement does not evaluate to a Number!.");
		}
		if (((Number) expr.eval(nametable, functiontable, var)).intValue() > 0) {
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

	public void eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var)
			throws Exception {
		ValueType result = expr.eval(nametable, functiontable, var);
		if (!Number.numberp(result)) {
			throw new RuntimeException(
					"Expression in while statement does not evaluate to a Number!.");
		}
		while (((Number) expr.eval(nametable, functiontable, var)).intValue() > 0) {
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

	public void eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var)
			throws Exception {
		ValueType result = expr.eval(nametable, functiontable, var);
		if (!Number.numberp(result)) {
			throw new RuntimeException(
					"Expression in if statement does not evaluate to a Number!.");
		}
		do {
			sl.eval(nametable, functiontable, var);
		} while (((Number) expr.eval(nametable, functiontable, var)).intValue() > 0);

	}
}

// added for 2c
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
		// we need ot add the expression to the front of the list
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

	public void eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var)
			throws Exception {

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

	public ValueType apply(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var,
			ExpressionList expressionlist) {
		// System.out.println("Executing Proceedure");
		HashMap<String, ValueType> newnametable = new HashMap<String, ValueType>();

		// bind parameters in new name table
		// we need to get the underlying List structure that the ParamList
		// uses...
		Iterator<String> p = parameterlist.getParamList().iterator();
		Iterator<Expr> e = expressionlist.getExpressions().iterator();

		if (parameterlist.getParamList().size() != expressionlist
				.getExpressions().size()) {
			System.out.println("Param count does not match");
			System.exit(1);
		}
		while (p.hasNext() && e.hasNext()) {

			// assign the evaluation of the expression to the parameter name.
			newnametable.put(p.next(),
					e.next().eval(nametable, functiontable, var));
			// System.out.println("Loading Nametable for procedure with: "+p+" = "+nametable.get(p));

		}
		// evaluate function body using new name table and
		// old function table
		// eval statement list and catch return
		// System.out.println("Beginning Proceedure Execution..");
		try {
			stmtlist.eval(newnametable, functiontable, var);
		} catch (Exception result) {
			// Note, the result shold contain the proceedure's return value as a
			// String
			System.out.println();
			result.printStackTrace();
			System.out.println();
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

	public void eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var) {
		try {
			stmtlist.eval(nametable, functiontable, var);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	public void dump(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var) {
		// System.out.println(hm.values());
		System.out.println("Dumping out all the variables...");
		if (nametable != null) {
			for (String name : nametable.keySet()) {
				System.out.println(name
						+ "="
						+ nametable.get(name).toString(nametable,
								functiontable, var));
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

	public ValueType eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var) {
		return this;
	}

	public String toString(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var) {
		StringBuilder returnString = new StringBuilder("[");
		boolean flag = false; //Check if the list is empty
		for (Expr vt : s.seq) {
			flag = true;
			returnString.append(vt.eval(nametable, functiontable, var)
					.toString(nametable, functiontable, var));
			returnString.append(",");
		}
		// Remove the trailing ','
		returnString.deleteCharAt(returnString.length() - 1);
		returnString.append("]");
		if (flag == false) //Delete the "]"
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
	public ValueType eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var) {
		ValueType element = e.eval(nametable, functiontable, var);
		ValueType list = L.eval(nametable, functiontable, var);

		if (list instanceof ConsCell) {
                        ConsCell cell = Memory.getInstance().allocateCell();
                        cell.setCar(element);
                        cell.setCdr(((ConsCell)list).getLocation());
                        return cell;
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

	public ValueType eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var) {
		ValueType list = L.eval(nametable, functiontable, var);
                if(list instanceof ConsCell) {
                  Number car = ((Number)((ConsCell)list).getCar());
                  if(((ConsCell)list).isList()) {
                    int value = ((Number)car.eval(nametable, functiontable, var)).intValue();
                    return Memory.getInstance().cellAt(value);
                  }
                  else {
                    return car;
                  } 
                }
          	else {
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

	public ValueType eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var) {
				
		ValueType list = L.eval(nametable, functiontable, var);
                if(list instanceof ConsCell) {
                  int cdr = ((ConsCell)list).getCdr();
                  if(cdr < 0) {
                    // TODO: probably fix this
                    return null;
                  }
                  else {
                    return (ConsCell)Memory.getInstance().cellAt(cdr);
                  } 
                }
		else {
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

	public ValueType eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var) {
		ValueType list = L.eval(nametable, functiontable, var);
                if(list == null) {
                  return new Number(1);
                }
                else if(list instanceof ConsCell) {
                  return new Number(0);
                }
		else {
			throw new RuntimeException("Invalid value type passed to nullp: "
					+ L.eval(nametable, functiontable, var).getClass());
		}
	}
}

class Intp extends Expr {

	private Expr e;

	public Intp(Expr e) {
		this.e = e;
	}

	public ValueType eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var) {
		if (e.eval(nametable, functiontable, var) instanceof Number) {
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

	public ValueType eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var) {
		if (e.eval(nametable, functiontable, var) instanceof ConsCell) {
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

	public ValueType eval(HashMap<String, ValueType> nametable,
			HashMap<String, Proc> functiontable, LinkedList var) {
		ValueType list1 = l1.eval(nametable, functiontable, var);
		ValueType list2 = l2.eval(nametable, functiontable, var);

		if (list1 instanceof ConsCell && list2 instanceof ConsCell) {
                        ConsCell lastCell = (ConsCell)list1;
                        int nextCell = lastCell.getCdr();
                        while (nextCell >= 0) {
System.out.println("Location at: " + nextCell);
                          lastCell = ((ConsCell)Memory.getInstance().cellAt(nextCell));
                          nextCell = lastCell.getCdr();
                        }
                        lastCell.setCdr(((ConsCell)list2).getLocation());
                        return list1;
		} else {
			// Must pass a list to car. Otherwise, error.
			throw new RuntimeException("Invalid value type passed to concat: "
					+ list1.getClass() + " - " + list2.getClass());
		}

	}
}

