import java.util.LinkedList;

class PseudoInstruction extends Instruction {
    private Symbol returnSymbol;
    private LinkedList<Symbol> symbols;

    public static PseudoInstruction NOP() {
        return new PseudoInstruction("NOPu", null);
    }
    public static PseudoInstruction Loadu(Symbol symbol) {
        return new PseudoInstruction("LDu", symbol);
    }
    public static PseudoInstruction Storeu(Symbol symbol) {
        return new PseudoInstruction("STu", symbol);
    }
    public static PseudoInstruction Addu(Symbol symbol) {
        return new PseudoInstruction("ADDu", symbol);
    }
    public static PseudoInstruction Mulu(Symbol symbol) {
	return new PseudoInstruction("MULu", symbol);
    }
    public static PseudoInstruction Subtractu(Symbol symbol) {
        return new PseudoInstruction("SUBu", symbol);
    }
    public static PseudoInstruction ReturnUnlinked(Symbol symbol) {
        return new PseudoInstruction("RETu", symbol);
    }
    public static PseudoInstruction CallUnlinked(Symbol symbol, LinkedList<Symbol> symbols, Symbol returnSymbol) {
        return new PseudoInstruction("CALu", symbol, symbols, returnSymbol);
    }

    public PseudoInstruction(String operator, Symbol symbol){
	super(operator, symbol);
    }

    public PseudoInstruction(String operator, Symbol symbol, LinkedList<Symbol> symbols){
	super(operator, symbol);
	this.symbols = symbols;
    }

    public PseudoInstruction(String operator, Symbol symbol, LinkedList<Symbol> symbols, Symbol returnSymbol){
	super(operator, symbol);
	this.symbols = symbols;
	this.returnSymbol = returnSymbol;
    }

    public Symbol getReturnSymbol(){
        return returnSymbol;
    }

    public LinkedList<Symbol> getSymbols(){
        return symbols;
    }

    public void link(SymbolTable st, FunctionTable ft, Function function, LinkedList<Instruction> linkedInstructions) {
        if(getOperator().equals("LDu")) {
	    linkLD(st, ft, function, linkedInstructions);
	}
	else if(getOperator().equals("STu")) {
	    linkST(st, ft, function, linkedInstructions);
	}
	else if(getOperator().equals("ADDu")) {
	    linkADD(st, ft, function, linkedInstructions);
	}
	else if(getOperator().equals("SUBu")) {
	    linkSUB(st, ft, function, linkedInstructions);
	}
	else if(getOperator().equals("MULu")) {
	    linkMUL(st, ft, function, linkedInstructions);
	}
	else if(getOperator().equals("RETu")) {
	    linkRET(st, ft, function, linkedInstructions);
	}
	else if(getOperator().equals("CALu")) {
	    linkCAL(st, ft, function, linkedInstructions);
	}
    }

    private void linkLD(SymbolTable st, FunctionTable ft, Function function, LinkedList<Instruction> linkedInstructions) {
	Symbol symbol = this.getSymbol();

	int position = 0;
	if(symbol.getType() == Symbol.VARIABLE) {
		position = function.getVariables().indexOf(symbol);
	}
	else if(symbol.getType() == Symbol.TEMP) {
		position = function.getVariables().size() + function.getTemps().indexOf(symbol);
	}
	else {
		linkedInstructions.addLast(Instruction.Loadd(symbol));
		return;
	}
	// Get the proper space in memory
	linkedInstructions.addLast(Instruction.Loadd(st.getFP()));
	if(position > 0) {
		linkedInstructions.addLast(Instruction.Add(st.addConstant(new Integer(position))));
	}
	linkedInstructions.addLast(Instruction.Stored(st.getScratch1()));

	// Load proper space in memory
	linkedInstructions.addLast(Instruction.Loadi(st.getScratch1()));	
    }

    private void linkST(SymbolTable st, FunctionTable ft, Function function, LinkedList<Instruction> linkedInstructions) {
	Symbol symbol = this.getSymbol();
	int position = 0;
	if(symbol.getType() == Symbol.VARIABLE) {
		position = function.getVariables().indexOf(symbol);
	}
	else if(symbol.getType() == Symbol.TEMP) {
		position = function.getVariables().size() + function.getTemps().indexOf(symbol);
	}
	else {
		linkedInstructions.addLast(Instruction.Stored(symbol));
		return;
	}

	// Store current value in a scratch register
	linkedInstructions.addLast(Instruction.Stored(st.getScratch1()));

	// Calculate proper place to store
	linkedInstructions.addLast(Instruction.Loadd(st.getFP()));
	if(position > 0) {
		linkedInstructions.addLast(Instruction.Add(st.addConstant(new Integer(position))));
	}

	linkedInstructions.addLast(Instruction.Stored(st.getScratch2()));
	linkedInstructions.addLast(Instruction.Loadd(st.getScratch1()));
	linkedInstructions.addLast(Instruction.Storei(st.getScratch2()));
    }

    private void linkADD(SymbolTable st, FunctionTable ft, Function function, LinkedList<Instruction> linkedInstructions) {
	linkedInstructions.addLast(Instruction.Stored(st.getScratch2()));
	PseudoInstruction.Loadu(this.getSymbol()).link(st, ft, function, linkedInstructions);
	linkedInstructions.addLast(Instruction.Stored(st.getScratch1()));
	linkedInstructions.addLast(Instruction.Loadd(st.getScratch2()));
	linkedInstructions.addLast(Instruction.Add(st.getScratch1()));
    }

    private void linkSUB(SymbolTable st, FunctionTable ft, Function function, LinkedList<Instruction> linkedInstructions) {
	linkedInstructions.addLast(Instruction.Stored(st.getScratch2()));
	PseudoInstruction.Loadu(this.getSymbol()).link(st, ft, function, linkedInstructions);
	linkedInstructions.addLast(Instruction.Stored(st.getScratch1()));
	linkedInstructions.addLast(Instruction.Loadd(st.getScratch2()));
	linkedInstructions.addLast(Instruction.Subtract(st.getScratch1()));
    }

    private void linkMUL(SymbolTable st, FunctionTable ft, Function function, LinkedList<Instruction> linkedInstructions) {
	linkedInstructions.addLast(Instruction.Stored(st.getScratch2()));
	PseudoInstruction.Loadu(this.getSymbol()).link(st, ft, function, linkedInstructions);
	linkedInstructions.addLast(Instruction.Stored(st.getScratch1()));
	linkedInstructions.addLast(Instruction.Loadd(st.getScratch2()));
	linkedInstructions.addLast(Instruction.Mul(st.getScratch1()));
    }

    private void linkRET(SymbolTable st, FunctionTable ft, Function function, LinkedList<Instruction> linkedInstructions) {
	Symbol returnSymbol = this.getSymbol();

	// Load the return value
	PseudoInstruction.Loadu(returnSymbol).link(st, ft, function, linkedInstructions);
	linkedInstructions.addLast(Instruction.Stored(st.getScratch1()));

	// Load proper space in memory
	linkedInstructions.addLast(Instruction.Loadd(st.getSP()));
	Symbol constant2 = st.addConstant(new Integer(2));
	linkedInstructions.addLast(Instruction.Subtract(constant2));
	linkedInstructions.addLast(Instruction.Stored(st.getScratch2()));
	linkedInstructions.addLast(Instruction.Loadd(st.getScratch1()));
	linkedInstructions.addLast(Instruction.Storei(st.getScratch2()));

	// Jump back to return address
	linkedInstructions.addLast(Instruction.Loadi(st.getSP()));
	linkedInstructions.addLast(Instruction.Stored(st.getScratch1()));
	linkedInstructions.addLast(Instruction.JumpIndirect(st.getScratch1()));
    }

    private void linkCAL(SymbolTable st, FunctionTable ft, Function function, LinkedList<Instruction> linkedInstructions) {
	Symbol symbol = this.getSymbol();
	Function callFunction = ft.get(symbol.getName());
	LinkedList<Symbol> symbols = this.getSymbols();
	if (callFunction.getNumParams() != symbols.size()) {
		System.out.println("Syntax Error: Param count does not match");
		System.exit(1);
	}

	// Place all the parameters at the start of the record
	Symbol constant0 = st.addConstant(new Integer(0));
	Symbol constant1 = st.addConstant(new Integer(1));
	Symbol constant2 = st.addConstant(new Integer(2));

	for (Symbol paramSymbol : symbols) {
		PseudoInstruction.Loadu(paramSymbol).link(st, ft, function, linkedInstructions);

		linkedInstructions.addLast(Instruction.Storei(st.getSP()));
		linkedInstructions.addLast(Instruction.Loadd(st.getSP()));
		linkedInstructions.addLast(Instruction.Add(constant1));
		linkedInstructions.addLast(Instruction.Stored(st.getSP()));
	}
	if(symbols.size() == 0) {
		linkedInstructions.addLast(Instruction.Loadd(st.getSP()));
	}

	// Allocate space for the variables and temporaries and return val
	Symbol constantVarSize = st.addConstant(new Integer(callFunction.getVariables().size() + callFunction.getTemps().size() + 1));
	linkedInstructions.addLast(Instruction.Add(constantVarSize));
	linkedInstructions.addLast(Instruction.Stored(st.getSP()));

	// Save previous FP
	linkedInstructions.addLast(Instruction.Loadd(st.getFP()));
	linkedInstructions.addLast(Instruction.Storei(st.getSP()));

	// Update FP to be the start of this record
	Symbol constantActivationSize = st.addConstant(new Integer(callFunction.getVariables().size() + callFunction.getTemps().size() + 1));
	linkedInstructions.addLast(Instruction.Loadd(st.getSP()));
	linkedInstructions.addLast(Instruction.Subtract(constantActivationSize));
	linkedInstructions.addLast(Instruction.Stored(st.getFP()));

	// Set the return address
	linkedInstructions.addLast(Instruction.Loadd(st.getSP()));
	linkedInstructions.addLast(Instruction.Add(constant1));
	linkedInstructions.addLast(Instruction.Stored(st.getSP()));

	// Call the Function
	linkedInstructions.addLast(Instruction.Call(callFunction.getLabel(), callFunction.getName()));

	// Set the return value symbol
	Symbol returnTemp = this.getReturnSymbol();
	linkedInstructions.addLast(Instruction.Loadd(st.getSP()));
	linkedInstructions.addLast(Instruction.Subtract(constant2));
	linkedInstructions.addLast(Instruction.Stored(st.getScratch1()));
	linkedInstructions.addLast(Instruction.Loadi(st.getScratch1()));

	if(returnTemp.getType() == Symbol.TEMP) {
		int position = function.getVariables().size() + function.getTemps().indexOf(returnTemp);

		// Store current value in a scratch register
		linkedInstructions.addLast(Instruction.Stored(st.getScratch1()));

		// Calculate proper place to store
		linkedInstructions.addLast(Instruction.Loadd(st.getFP()));
		if(position > 0) {
			linkedInstructions.addLast(Instruction.Add(st.addConstant(new Integer(position))));
		}

		linkedInstructions.addLast(Instruction.Stored(st.getScratch2()));
		linkedInstructions.addLast(Instruction.Loadd(st.getScratch1()));
		linkedInstructions.addLast(Instruction.Storei(st.getScratch2()));
	}
	else {
		linkedInstructions.addLast(Instruction.Stored(returnTemp));
	}
	// Return value has now been stored in temporary symbol

	// Revert the FP
	linkedInstructions.addLast(Instruction.Loadd(st.getSP()));
	linkedInstructions.addLast(Instruction.Subtract(constant1));
	linkedInstructions.addLast(Instruction.Stored(st.getSP()));
	linkedInstructions.addLast(Instruction.Loadi(st.getSP()));
	linkedInstructions.addLast(Instruction.Stored(st.getFP()));

	linkedInstructions.addLast(Instruction.Loadd(st.getSP()));
	linkedInstructions.addLast(Instruction.Subtract(constant1));
	linkedInstructions.addLast(Instruction.Stored(st.getSP()));

	// Revert the SP
	Symbol constantSize = st.addConstant(new Integer(callFunction.getVariables().size() + callFunction.getTemps().size()));
	linkedInstructions.addLast(Instruction.Loadd(st.getSP()));
	linkedInstructions.addLast(Instruction.Subtract(constantSize));
	linkedInstructions.addLast(Instruction.Stored(st.getSP()));
    }
}
