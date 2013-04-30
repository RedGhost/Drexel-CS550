import java.util.HashMap;

public class SymbolTable {

	private HashMap<String, Symbol> symbols;

	private int numConsts = 0;
	private int numVars = 0;
	private int numTemps = 0;

	public SymbolTable() {
		symbols = new HashMap<String, SymbolTable.Symbol>();
	}

	public void addConstant(Integer c) {
		if (symbols.get("C" + c.toString()) == null) {
			symbols.put("C" + c.toString(), new Symbol(c, Symbol.CONSTANT,
					Symbol.UNDEFINED));
			System.out.println("add constant");
			numConsts++;
		}
	}

	public void addVariable(String name) {
		if (symbols.get(name) == null) {
			symbols.put(name, new Symbol(Symbol.UNDEFINED, Symbol.VARIABLE,
					Symbol.UNDEFINED));
			numVars++;
		}
	}

	/**
	 * Add a temporary variable to the symbol table.
	 * 
	 * @return The name of the temporary variable symbol added to the table.
	 */
	public String addTemp() {
		String tempName = "T" + numTemps;
		symbols.put(tempName, new Symbol(Symbol.UNDEFINED, Symbol.TEMP,
				Symbol.UNDEFINED));
		numTemps++;
		return tempName;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("Symbol Table:\n");
		b.append("Num Constants: " + numConsts + ", Num Vars: " + numVars
				+ ", Num Temps: " + numConsts + "\n");
		b.append(symbols);
		return b.toString();
	}

	private class Symbol {
		public static final int UNDEFINED = -1;
		public static final int CONSTANT = 0;
		public static final int VARIABLE = 1;
		public static final int TEMP = 2;

		private int value;
		private int type;
		private int addr;

		public Symbol(int value, int type, int addr) {
			this.value = value;
			this.type = type;
			this.addr = addr;
		}

		public int getValue() {
			return value;
		}

		public int getType() {
			return type;
		}

		public int getAddr() {
			return addr;
		}

		@Override
		public String toString() {
			StringBuilder b = new StringBuilder();
			b.append("[" + value + ", ");
			if (type == CONSTANT) {
				b.append("const, ");
			} else if (type == VARIABLE) {
				b.append("var, ");
			} else if (type == TEMP) {
				b.append("temp, ");
			}
			b.append(addr + "]");
			return b.toString();
		}
	}
}
