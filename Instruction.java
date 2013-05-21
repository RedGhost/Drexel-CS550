import java.util.LinkedList;

class Instruction
{
    private String operator;
    private Symbol symbol;
    private String comment;

    public static Instruction Loadd(Symbol symbol) {
        return new Instruction("LDA", symbol);
    }
    public static Instruction Loadi(Symbol symbol) {
        return new Instruction("LDI", symbol);
    }
    public static Instruction Stored(Symbol symbol) {
        return new Instruction("STA", symbol);
    }
    public static Instruction Storei(Symbol symbol) {
        return new Instruction("STI", symbol);
    }
    public static Instruction Add(Symbol symbol) {
        return new Instruction("ADD", symbol);
    }
    public static Instruction Mul(Symbol symbol) {
	return new Instruction("MUL", symbol);
    }
    public static Instruction Subtract(Symbol symbol) {
        return new Instruction("SUB", symbol);
    }
    public static Instruction JumpNegative(Symbol symbol) {
        return new Instruction("JMN", symbol);
    }
    public static Instruction JumpZero(Symbol symbol) {
        return new Instruction("JMZ", symbol);
    }
    public static Instruction JumpIndirect(Symbol symbol) {
        return new Instruction("JMI", symbol);
    }
    public static Instruction Call(Symbol symbol, String name) {
        return new Instruction("CAL", symbol, name);
    }
    public static Instruction Jump(Symbol symbol) {
        return new Instruction("JMP", symbol);
    }
    public static Instruction Halt() {
        return new Instruction("HLT", null);
    }

    public Instruction(String operator, Symbol symbol){
        this.operator = operator;
        this.symbol = symbol;
    }

    public Instruction(String operator, Symbol symbol, String comment){
	this(operator, symbol);
	this.comment = comment;
    }

    public String getOperator(){
        return operator;
    }

    public Symbol getSymbol(){
        return symbol;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(operator);
        if(symbol != null) {
            builder.append(" " + symbol);
        }
	if(comment != null) {
	    builder.append("\t; " + comment);
	}
        return builder.toString();
    }
}
