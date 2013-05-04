import java.util.TreeMap;
import java.util.Vector;
import java.util.Stack;

public class Translator {
	/**
	 * Used for conditional and while statements. Maps a name (e.g. "L1") to a
	 * set of instructions. The first set of instructions is known as "L0".
	 */
	private TreeMap<Integer, Vector<Instruction>> instructions;
	private int currentLabel;

	public Translator() {
		instructions = new TreeMap<Integer, Vector<Instruction>>();
		currentLabel = 0;
		instructions.put(currentLabel, new Vector<Instruction>());
	}

	/**
	 * Linking function: updates address of labels in SymbolTable.
	 * 
	 * @param st
	 *            - SymbolTable to update.
	 */
        public void link(SymbolTable st) {
	    int addr = 1;
	    for (int l = 0; l < instructions.size(); l++) {
		Symbol labelSymbol = st.getSymbol("L" + l);
		if(labelSymbol != null){
		    labelSymbol.setAddr(addr);
		}
		addr += instructions.get(l).size();
	    }	    
        }

	/**
	 * Add a load instruction for a constant.
	 * 
	 * @param n
	 *            - Constant to load.
	 */
	public void addLoad(Integer n, SymbolTable st) {
	    instructions.get(currentLabel).add(new Instruction("LDA", st.getSymbol("C" + n)));
	}

	/**
	 * Add a load instruction for a variable. Called from ID.
	 * 
	 * @param n
	 *            - Constant to load.
	 */
	public void addLoad(String n, SymbolTable st) {
		instructions.get(currentLabel).add(new Instruction("LDA", st.getSymbol(n)));
	}

	/**
	 * Add a store instruction.
	 * 
	 * @param t
	 *            - Name of the temp variable to store into.
	 */
	public void addStore(String t, SymbolTable st) {
	    instructions.get(currentLabel).add(new Instruction("STA", st.getSymbol(t)));
	}

	/**
	 * Adds a add instruction.
	 * 
	 * @param e2Tmp
	 *            - Name of the temp variable to add.
	 */
	public void addAdd(String e2Tmp, SymbolTable st) {
	    instructions.get(currentLabel).add(new Instruction("ADD", st.getSymbol(e2Tmp)));
	}

	/**
	 * Adds a subtract instruction.
	 * 
	 * @param e2Tmp
	 *            - Name of the temp variable to subtract.
	 */
	public void addSub(String e2Tmp, SymbolTable st) {
	    instructions.get(currentLabel).add(new Instruction("SUB", st.getSymbol(e2Tmp)));
	}

	/**
	 * @return The last temporary variable that was stored to.
	 */
	public String getLastTempStore(SymbolTable st) {
		for (int i = instructions.get(currentLabel).size() - 1; i >= 0; i--) {
		    if (instructions.get(currentLabel).get(i).getOperator().equals("STA") &&
			instructions.get(currentLabel).get(i).getSymbol().getType() == Symbol.TEMP) {
			return st.getName(instructions.get(currentLabel).get(i).getSymbol());
			}
		}
		return null;
	}

	/**
	 * Add a new chunk of instructions to the translator. Caused by a if or
	 * while statement.
	 * 
	 * @return The number (e.g. 1 if "L1") of the new set of instructions added.
	 */
	public int addNewInstructionSet() {
		int newSet = instructions.size();
		instructions.put(newSet, new Vector<Instruction>());
		return newSet;
	}

	public void addJumpNegative(int newL, SymbolTable st) {
	    instructions.get(currentLabel).add(new Instruction("JMN", st.addLabel(newL)));
	}

	public void addJumpZero(int newL, SymbolTable st) {
	    instructions.get(currentLabel).add(new Instruction("JMZ", st.addLabel(newL)));
	}

	public void addJump(int newL, SymbolTable st) {
	    instructions.get(currentLabel).add(new Instruction("JMP", st.addLabel(newL)));
	}

	public void addJump(SymbolTable st) {
	    instructions.get(currentLabel).add(new Instruction("JMP", st.addLabel(currentLabel)));
	}

	public void setCurrentLabel(int newLabel) {
		currentLabel = newLabel;
	}

	public void addHalt(SymbolTable st) {
	    instructions.get(currentLabel).add(new Instruction("HLT", null));
	}

	public String toString(SymbolTable st) {
		System.out.println("instructions.size: " + instructions.size());

		StringBuilder b = new StringBuilder();

		b.append("Translated Instructions:\n");
		for (int l = 0; l < instructions.size(); l++) {
		    if (l != 0) {
			b.append("L" + l);
		    }
		    for (Instruction i : instructions.get(l)) {
			String name = "";
			if(i.getSymbol() != null){
			    name = st.getName(i.getSymbol());
			}
			b.append((l != 0 ? "\t" : "") + i.getOperator() + " " + name + "\n");
		    }
		}
		return b.toString();

	}

	public String toStringLink(SymbolTable st) {
		System.out.println("instructions.size: " + instructions.size());

		StringBuilder b = new StringBuilder();

		for (int l = 0; l < instructions.size(); l++) {
		    for (Instruction i : instructions.get(l)) {
			if(i.getSymbol() != null){
			    b.append(i.getOperator() + " " + i.getSymbol().getAddr() + "\n");
			}
			else {
			    b.append(i.getOperator() + "\n");
			}
		    }
		}
		return b.toString();

	}

	public void optimize(SymbolTable st) {

		TreeMap<Integer, Stack<Integer>> remove = new TreeMap<Integer, Stack<Integer>>();
	   	//Looking to remove LD right after ST of the same variable
	   	boolean stFlag = false;
	   	int stNum = -1;
	   	int delCount = 0;
		int delCount2 = 0;
		int count = 0;
		
		for (int l = 0; l < instructions.size(); l++) {
		    count = 0;
		    Stack<Integer> tempvec = new Stack<Integer>();
		    for (Instruction i : instructions.get(l)) {
			if(i.getSymbol() != null){
			    if(stFlag == true && i.getOperator() != "LDA") {
				stFlag = false;
			        stNum = -1;
			    }
			    if(i.getOperator() == "STA") {
				stFlag = true;
				stNum = (int)i.getSymbol().getAddr();
			    }
			    if(i.getOperator() == "LDA" && stFlag == true && (int)i.getSymbol().getAddr() == stNum) {
			     	delCount++;
				tempvec.push(count); 
			    } //end if
			} //end if
			count++;
		    } //end for
		    remove.put(l, tempvec);
		    
		    Symbol labelSymbol = st.getSymbol("L" + l);
		    if(labelSymbol != null){
		        labelSymbol.setAddr(labelSymbol.getAddr()-delCount2);
		    } //end if

			//It goes one ahead of what I want, so I use this to go back
		    delCount2 = delCount;
		} //end for
		for (int l = 0; l < remove.size(); l++) {
		    while(!remove.get(l).empty()) {
			int r = remove.get(l).pop();
			instructions.get(l).remove(r);
		    }
		}
	}//end optimize

	public String toStringOpt(SymbolTable st) {
		
		optimize(st);
		System.out.println("instructions.size: " + instructions.size());

		StringBuilder b = new StringBuilder();
		
		for (int l = 0; l < instructions.size(); l++) {
		    for (Instruction i : instructions.get(l)) {
			if(i.getSymbol() != null){
			    if(i.getSymbol() != null){
				b.append(i.getOperator() + " " + i.getSymbol().getAddr() + "\n");
			    }
			}
			else {
			    b.append(i.getOperator() + "\n");
			}
		    }
		}
		return b.toString();
	}



    private class Instruction
    {
	private String operator;
	private Symbol symbol;

	public Instruction(String operator, Symbol symbol){
	    this.operator = operator;
	    this.symbol = symbol;
	}

	public String getOperator(){
	    return operator;
	}

	public Symbol getSymbol(){
	    return symbol;
	}
    }
}
