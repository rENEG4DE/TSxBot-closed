package tsxdk.parser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class TsFieldStack {
	private LinkedList<TsField> stack = new LinkedList<>();

	//Reuse Field-Objects!
	
	private Map<AbstractSymbol,TsField> fieldSlots = new HashMap<>();		//One slot for every field
	
	public void push(LibTsSym symbol, Object value) {
		TsField field;
		
		//Init slot if not existent
		if ((field = fieldSlots.get(symbol)) == null) {
//			System.out.println("Filling slot of" + symbol);
			field = new TsField(symbol,value);
			fieldSlots.put(symbol, field);
		}
		
		//at this point we can assume field is filled in all cases !
		
		if (field.stacked == false) {
//			System.out.println("Reusing Field in Slot of" + symbol);
			field.stacked = true;
			field.value = value;
			stack.add(field);
		} else {
			//This should not be the case in our current parser algorithm!
//			System.out.println("Creating new field cause slot in use of " + symbol);
			stack.add(new TsField(symbol,value));
		}
		
		
//		if (fieldList.size() > 50) {
//			fieldList.clear();
//			System.out.println("Clearing fieldlist");
//		}
	}
	
	public Object ifSet (Object currentValue, LibTsSym symbol) {
		for (TsField current : stack) {
			if (current.eqTo(symbol)) {
				stack.remove(current);
//				System.out.println("Symbol: " + id + " field: " + current.symbol);
				current.stacked = false;					//the slot becomes available again
				return current.value;
			}
		}
		return currentValue;
	}
	
	//Ideally, data is always collected the way it comes in,
	//this method is designed to go through it's own list and return the first occurrence of a Field with the given ID
	//In fact, the highest performance possible is by popping the Fields the same order as they come in
	public Object popFirst(LibTsSym id) throws NullPointerException {
		for (TsField current : stack) {
			if (current.eqTo(id)) {
				stack.remove(current);
//				System.out.println("Symbol: " + id + " field: " + current.symbol);
				current.stacked = false;					//the slot becomes available again
				return current.value;
			}
		}

		throw new NullPointerException("Symbol [" + id + "] not on stack!");
//		return null;
	}
	
	public Object peekFirst(LibTsSym id) throws NullPointerException {
		for (TsField current : stack) {
			if (current.eqTo(id)) {
				return current.value;
			}
		}

		throw new NullPointerException("Symbol [" + id + "] not on stack!");
//		return null;
	}
	
	public Map<String, Object> getRaw() {
		final Map<String, Object> ret = new HashMap<>();

		for (TsField current : stack) {
			ret.put(current.symbol.toString(), current.value);
		}

		return ret;
	}
	
	public boolean hasFields (LibTsSym[] fields)  {
		outer:
		for (LibTsSym current : fields) {
			for (TsField field : stack) {
				if (field.eqTo(current))
					continue outer;
			}
			return false;
		}
		return true;
	}
	
	public boolean hasField(LibTsSym current) {
		for (TsField field : stack)
			if (field.eqTo(current))
				return true;
		return false;
	}
	
//	public void pop () {
//		TsField field;
//		if ((field = stack.pollFirst()) != null) {
//			field.stacked = false;
//		}
//	}

	public String getContent () {
		StringBuilder ret = new StringBuilder("Stack:{\n");
		for (TsField current : stack) {
			ret.append(current.symbol.toString());
			ret.append(":");
			ret.append(current.value.toString());
			ret.append("\n");
		}
		ret.append("}\n");
		return ret.toString();
	}
	
	private static class TsField {
		final public LibTsSym symbol;
		public Object value;

		private boolean stacked;		//This TsField is already on stack
		
		TsField (LibTsSym symbol, Object value) {
			this.symbol = symbol;
			this.value = value;
		}

		public boolean eqTo(Object obj) {
			if (obj instanceof LibTsSym) {
//				System.out.println ("Comparing : " + symbol.toString() + " to " + ((Symbol)obj).toString());
				return ((LibTsSym) obj).getValue() == this.symbol.getValue();
			} else if (obj instanceof TsField) {
				return eqTo(((TsField)obj).symbol);			//Overhead ?
			}
			return super.equals(obj);
		}
	}

	
	public int ensureEmpty() {
		int ret = stack.size();
		stack.clear();
		return ret;
	}

	public int getFieldCount() {
		return stack.size();
	}
}