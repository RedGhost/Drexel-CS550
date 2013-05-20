public class Symbol {
    public static final int UNDEFINED = -1;
    public static final int BUILT_IN = 0;
    public static final int CONSTANT = 1;
    public static final int VARIABLE = 2;
    public static final int TEMP = 3;
    public static final int LABEL = 4;
    public static final int FUNCTION = 5;

    private String name;
    private int value;
    private int type;
    private int addr;

    public Symbol(String name, int value, int type, int addr) {
	this.name = name;
	this.value = value;
	this.type = type;
	this.addr = addr;
    }

    public String getName() {
        return name;
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

    public void setValue(int value) {
        this.value = value;
    }

    public void setAddr(int addr) {
	this.addr = addr;
    }

    @Override
    public String toString() {
	StringBuilder b = new StringBuilder();
	if(addr == UNDEFINED) {
		b.append(name);
	}
	else {
		b.append(addr);
	}
	return b.toString();
    }
}
