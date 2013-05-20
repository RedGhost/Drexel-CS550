import java.util.HashMap;
import java.util.Vector;
import java.util.Stack;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class Linker {
	/**
	 * Linking function: updates address of labels in SymbolTable.
	 * 
	 * @param st
	 *            - SymbolTable to update.
	 */
        public void link(SymbolTable st, FunctionTable ft) {
            int memoryAddr = 1;
            st.getFP().setAddr(memoryAddr++);
            st.getSP().setAddr(memoryAddr++);
            st.getMainLocation().setAddr(memoryAddr++);
            
            for(Symbol symbol : st.getConstants()) {
              symbol.setAddr(memoryAddr++);
            }

            st.getFP().setValue(memoryAddr);
            st.getSP().setValue(memoryAddr);

	    int addr = 1;
	    HashMap<String, Function> functions = ft.getFunctions();
	    for(String functionName : functions.keySet()) {
		Function function = functions.get(functionName);
		function.setStartingAddress(addr);
		if(functionName.equals("main")) {
			st.getMainLocation().setValue(addr);
		}

		addr += function.numInstructions();
	    }

	    for(String functionName : functions.keySet()) {
		Function function = functions.get(functionName);
	    }
        }

        private void writeFile(String filename, String text) {
	    try{
		// Create file 
		FileWriter fstream = new FileWriter(filename);
		BufferedWriter out = new BufferedWriter(fstream);
		out.write(text);
		//Close the output stream
		out.close();
	    }catch (Exception e){//Catch exception if any
		System.err.println("Error: " + e.getMessage());
	    }
        }

	public void printRAL(String output, SymbolTable st, FunctionTable ft) {
		System.out.println("Dumping out instructions to file " + output + "...");
		StringBuilder builder = new StringBuilder();

		FunctionCall mainCall = new FunctionCall("main", new ExpressionList());

		Function primerFunction = new Function("primer", null);
		mainCall.translate(st, ft, primerFunction);

                builder.append(primerFunction);
                builder.append("HLT\n");

		HashMap<String, Function> functions = ft.getFunctions();
		for(String functionName : functions.keySet()) {
			Function function = functions.get(functionName);
			builder.append(function);
		}

		writeFile(output, builder.toString());
	}

	public void printInitialMemory(String output, SymbolTable st, FunctionTable ft) {
		System.out.println("Dumping out initial memory to file " + output + "...");
		StringBuilder builder = new StringBuilder();
		builder.append(st.getFP().getAddr() + " " + st.getFP().getValue() + "\n");
		builder.append(st.getSP().getAddr() + " " + st.getSP().getValue() + "\n");
		builder.append(st.getMainLocation().getAddr() + " " + st.getMainLocation().getValue() + "\n");

            	for(Symbol symbol : st.getConstants()) {
			builder.append(symbol.getAddr() + " " + symbol.getValue() + "\n");
            	}

		writeFile(output, builder.toString());
	}

	public void optimize(SymbolTable st) {
/*
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
		}*/
	}//end optimize

	public String toStringOpt(SymbolTable st) {
		/*
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
		return b.toString();*/
return "";
	}
}
