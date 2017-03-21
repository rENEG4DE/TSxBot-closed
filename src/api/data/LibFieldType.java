package api.data;

import java.math.BigInteger;

public enum LibFieldType{
	BOOLEAN {
		public Object parse(String val) { return Boolean.parseBoolean(val);}
	},
	INTEGER{
		public Object parse(String val) { return Integer.parseInt(val);}
	},
	LONG{
		public Object parse(String val) { return Long.parseLong(val);}
	},
	BIGINTEGER{
		public Object parse(String val) { return new BigInteger(val);}
	},
	STRING{
		public Object parse(String val) { return val.replace("\\\\", "\\")
												.replace("\\s", " ")
												.replace("\\/", "/")
												.replace("\\p", "|")
												.replace("\\b", "\b")
												.replace("\\f", "\f")
												.replace("\\n", "\n")
												.replace("\\r", "\r")
												.replace("\\t", "\t");}
		},
		ARRAY_OF_INTEGER{
			public Object parse(String val) {
				final String temp[] = val.split(",");
				final Integer ret[] = new Integer[temp.length];
				for (int i = 0; i < temp.length; ++i) {
				ret[i] = Integer.parseInt(temp[i]);
			}
		return ret;}
	},
	ARRAY_OF_BIG_INTEGER{
		public Object parse(String val) {
			final String temp[] = val.split(",");
			final BigInteger[] ret = new BigInteger[temp.length];
			for (int i = 0; i < temp.length; ++i) {
				ret[i] = new BigInteger(temp[i]);
			}
		return ret;}
	},
	ARRAY_OF_STRING{
		public Object parse(String val) { return val.split("[|]+");}
	},
	CVAR_ARRAY_OF_STRING{
		public Object parse(String val) { return val.split(",");}
	},
	CVAR_ARRAY_OF_INTEGER{
		public Object parse(String val) { return ARRAY_OF_INTEGER.parse(val);}
	},
	CVAR_ARRAY_OF_BIG_INTEGER{
		public Object parse(String val) { return ARRAY_OF_BIG_INTEGER.parse(val);}
	};
	public Object parse(String val) {return null;}
}