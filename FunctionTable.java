import java.util.HashMap;

public class FunctionTable {

	private HashMap<String, Proc> procs;

        private HashMap<String, Integer> addresses;

        private SymbolTable st;

	public FunctionTable() {
		procs = new HashMap<String, Proc>();
	}

        public HashMap<String, Proc> getProcs(){
	    return procs;
        }

        public void put(String name, Proc proc, int address) {
	    procs.put(name, proc);
	    addresses.put(name, address);
        }

        public void put(String name, Proc proc) {
	    put(name,proc,-1);
	}

        public void setAddress(String name, int address) {
	    addresses.put(name, address);
	}

        public Proc get(String name){
	    if(procs.containsKey(name)) {
		return procs.get(name);
	    }
	    else{
		return null;
	    }
        }

        public String getName(Proc proc){
	    for(String name : procs.keySet()){
		if(procs.get(name).equals(proc)){
		    return name;
		}
	    }
	    return null;
	}
}
