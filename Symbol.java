public class Symbol {
    public static final int UNDEFINED = -1;
    public static final int CONSTANT = 0;
    public static final int VARIABLE = 1;
    public static final int TEMP = 2;
    public static final int LABEL = 3;

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

    public void setAddr(int addr) {
	this.addr = addr;
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