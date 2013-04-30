import java.util.Comparator;
import java.util.TreeMap;
import java.util.Vector;

public class Translator {
	/**
	 * Used for conditional and while statements. Maps a name (e.g. "L1") to a
	 * set of instructions. The first set of instructions is known as "L0".
	 */
	private TreeMap<String, Vector<String>> instructions;
	private String currentLabel;

	public Translator() {
		instructions = new TreeMap<String, Vector<String>>(
				new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {
						// Compare the number after the 'L'.
						return new Integer(Integer.parseInt(o1.substring(1)))
								.compareTo(new Integer(Integer.parseInt(o2
										.substring(1))));
					}
				});
		currentLabel = "L0";
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

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();

		b.append("Translated Instructions:\n");
		for (int l = 0; l < instructions.size(); l++) {
			if (l != 0) {
				System.out.println("L" + l);
			}
			for (String i : instructions.get("L" + l)) {
				b.append((l != 0 ? "\t" : "") + i + "\n");
			}
		}
		return b.toString();

	}
}
