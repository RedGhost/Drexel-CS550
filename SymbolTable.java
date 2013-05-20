import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class SymbolTable {
        private HashMap<String, Symbol> constants;
        private Symbol FP;
        private Symbol SP;
        private Symbol mainLocation;

	private int labelCount;

	public SymbolTable() {
                FP = new Symbol(1, Symbol.BUILT_IN, Symbol.UNDEFINED);
                SP = new Symbol(2, Symbol.BUILT_IN, Symbol.UNDEFINED);
                mainLocation = new Symbol(3, Symbol.BUILT_IN, Symbol.UNDEFINED);
                
                constants = new HashMap<String, Symbol>();

		labelCount = 0;
	}

	public Symbol createLabel() {
		Symbol newLabel = new Symbol(labelCount, Symbol.LABEL, Symbol.UNDEFINED);
		labelCount++;
		return newLabel;
	}

        public Collection<Symbol> getConstants() {
            return constants.values();
        }

        public Symbol getConstantSymbol(String name){
	    if(constants.containsKey(name)) {
		return constants.get(name);
	    }
	    else{
		return null;
	    }
        }

        public String getConstantName(Symbol symbol){
	    for(String name : constants.keySet()){
		if(constants.get(name).equals(symbol)){
		    return name;
		}
	    }
	    return null;
	}

        public Symbol getFP() {
            return FP;
        }

        public Symbol getSP() {
            return SP;
        }

        public Symbol getMainLocation() {
            return mainLocation;
        }

	public Symbol addConstant(Integer c) {
		if (!constants.containsKey("C" + c.toString())) {
			Symbol newConstant = new Symbol(c, Symbol.CONSTANT,
					Symbol.UNDEFINED);
			constants.put("C" + c.toString(), newConstant);
			return newConstant;
		}
		else {
			return constants.get("C" + c.toString());
		}
	}
}
