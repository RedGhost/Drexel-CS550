class Instruction
{
    private String operator;
    private Symbol symbol;

    public static Instruction Load(Symbol symbol) {
        return new Instruction("LDA", symbol);
    }
    public static Instruction Store(Symbol symbol) {
        return new Instruction("STA", symbol);
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

    public String getOperator(){
        return operator;
    }

    public Symbol getSymbol(){
        return symbol;
    }
}
