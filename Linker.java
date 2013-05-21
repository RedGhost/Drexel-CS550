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
	    Function main = ft.get("main");
	    st.getMainLocation().setValue(1);

	    System.out.println("Linking main");
	    main.link(st,ft,1);
	    int addr = main.numInstructions()+1;
	    System.out.println("Main has " + main.numInstructions() + " instructions, " + main.getVariables().size() + " variables, and " + main.getTemps().size() + " temps.");

	    HashMap<String, Function> functions = ft.getFunctions();
	    for(String functionName : functions.keySet()) {
		if(functionName.equals("main")) {
			continue;
		}

		System.out.println("Linking " + functionName);
		Function function = functions.get(functionName);
		function.link(st, ft, addr);
		System.out.println(functionName + " has " + function.numInstructions() + " instructions, " + function.getVariables().size() + " variables, and " + function.getTemps().size() + " temps.");

		addr += function.numInstructions();
	    }

	    for(String functionName : functions.keySet()) {
		Function function = functions.get(functionName);
	    }

	    int memoryAddr = 1;
            st.getSP().setAddr(memoryAddr++);
            st.getFP().setAddr(memoryAddr++);
	    st.getScratch1().setAddr(memoryAddr++);
	    st.getScratch2().setAddr(memoryAddr++);
            
            for(Symbol symbol : st.getConstants()) {
              symbol.setAddr(memoryAddr++);
            }

            st.getFP().setValue(memoryAddr);
            st.getSP().setValue(memoryAddr + main.getVariables().size() + main.getTemps().size() + 2);
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

	    Function main = ft.get("main");
	    builder.append(main);

		HashMap<String, Function> functions = ft.getFunctions();
		for(String functionName : functions.keySet()) {
			if(functionName.equals("main")) {
				continue;
			}
			Function function = functions.get(functionName);
			builder.append(function);
		}

		writeFile(output, builder.toString());
	}

	public void printInitialMemory(String output, SymbolTable st, FunctionTable ft) {
		System.out.println("Dumping out initial memory to file " + output + "...");
		StringBuilder builder = new StringBuilder();
		builder.append(st.getSP().getAddr() + " " + st.getSP().getValue() + "\t; SP\n");
		builder.append(st.getFP().getAddr() + " " + st.getFP().getValue() + "\t; FP\n");
		builder.append(st.getScratch1().getAddr() + " " + st.getScratch1().getValue() + "\t; SCRATCH 1\n");
		builder.append(st.getScratch2().getAddr() + " " + st.getScratch2().getValue() + "\t; SCRATCH 2\n");

            	for(Symbol symbol : st.getConstants()) {
		    builder.append(symbol.getAddr() + " " + symbol.getValue() + "\t; " + symbol.getName() + "\n");
            	}
		
		
	    	Function main = ft.get("main");
		int addr = 5 + st.getConstants().size();
		for(Symbol symbol : main.getVariables()) {
			builder.append(addr + " 0\t; " + symbol.getName() + "\n");
			addr++;
		}
		for(Symbol symbol : main.getTemps()) {
			builder.append(addr + " 0\t; " + symbol.getName() + "\n");
			addr++;
		}
		builder.append((addr++) + " 0\t; Main Return Value\n");
		builder.append((addr++) + " 0\t; Main's Previous FP (not used)\n");
		builder.append((addr++) + " " + main.numInstructions() + "\t; Main's Return Address (HLT at the end)\n");

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
