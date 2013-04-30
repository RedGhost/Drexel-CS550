import java.util.TreeMap;
import java.util.Vector;

public class Translator {
	/**
	 * Used for conditional and while statements. Maps a name (e.g. "L1") to a
	 * set of instructions. The first set of instructions is known as "L0".
	 */
	private TreeMap<Integer, Vector<String>> instructions;
	private int currentLabel;

	public Translator() {
		instructions = new TreeMap<Integer, Vector<String>>();
		currentLabel = 0;
		instructions.put(currentLabel, new Vector<String>());
	}

	/**
	 * Add a load instruction for a constant.
	 * 
	 * @param n
	 *            - Constant to load.
	 */
	public void addLoad(Integer n) {
		instructions.get(currentLabel).add("LD C" + n);
	}

	/**
	 * Add a load instruction for a variable. Called from ID.
	 * 
	 * @param n
	 *            - Constant to load.
	 */
	public void addLoad(String n) {
		instructions.get(currentLabel).add("LD " + n);
	}

	/**
	 * Add a store instruction.
	 * 
	 * @param t
	 *            - Name of the temp variable to store into.
	 */
	public void addStore(String t) {
		instructions.get(currentLabel).add("ST " + t);
	}

	/**
	 * Adds a add instruction.
	 * 
	 * @param e2Tmp
	 *            - Name of the temp variable to add.
	 */
	public void addAdd(String e2Tmp) {
		instructions.get(currentLabel).add("ADD " + e2Tmp);

	}

	/**
	 * Adds a subtract instruction.
	 * 
	 * @param e2Tmp
	 *            - Name of the temp variable to subtract.
	 */
	public void addSub(String e2Tmp) {
		instructions.get(currentLabel).add("SUB " + e2Tmp);

	}

	/**
	 * @return The last temporary variable that was stored to.
	 */
	public String getLastTempStore() {
		for (int i = instructions.get(currentLabel).size() - 1; i >= 0; i--) {
			if (instructions.get(currentLabel).get(i).startsWith("ST ")) {
				String[] pieces = instructions.get(currentLabel).get(i)
						.split(" ");
				return pieces[1];
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
		instructions.put(newSet, new Vector<String>());
		return newSet;
	}

	public void addJumpNegative(int newL) {
		instructions.get(currentLabel).add("JMN L" + newL);
	}

	public void addJumpZero(int newL) {
		instructions.get(currentLabel).add("JMZ L" + newL);
	}

	public void addJump(int newL) {
		instructions.get(currentLabel).add("JMP L" + newL);
	}

	public void addJump() {
		instructions.get(currentLabel).add("JMP L" + currentLabel);
	}

	public void setCurrentLabel(int newLabel) {
		currentLabel = newLabel;
	}

	public void addHalt() {
		instructions.get(currentLabel).add("HLT");
	}

	@Override
	public String toString() {
		System.out.println("instructions.size: " + instructions.size());

		StringBuilder b = new StringBuilder();

		b.append("Translated Instructions:\n");
		for (int l = 0; l < instructions.size(); l++) {
			if (l != 0) {
				b.append("L" + l);
			}
			for (String i : instructions.get(l)) {
				b.append((l != 0 ? "\t" : "") + i + "\n");
			}
		}
		return b.toString();

	}
}
