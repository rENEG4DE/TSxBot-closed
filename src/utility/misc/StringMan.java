package utility.misc;

import api.data.LibFieldType;

public class StringMan {
	
	/**
	 * Create a left padded String with fixed length
	 * @param str the string to pad
	 * @param len the length of the returned string
	 * @return a left padded (len) sized string, filled up with spaces
	 */
	public static final String makeFixedLen(String str, int len) {
		if (str.length() < len) {
			return str + (new String(new char[len - str.length()]).replace("\0", " "));
		} else {
			return str.substring(0,len-1);
		}
	}

	public static String join(String[] s, String glue)
	{
		int k = s.length;
		if (k == 0)
			return "";
		StringBuilder out = new StringBuilder();
		out.append(s[0]);
		for (int x = 1; x < k; ++x)
			out.append(glue).append(s[x]);
		return out.toString();
	}
	
	
	/**
	 * Encode t s3 string.
	 *
	 * @param str the str
	 * @return the string
	 */
	public static String encodeTS3String(String str)
	{

		str = str.replace("\\", "\\\\");
		str = str.replace(" ", "\\s");
		str = str.replace("/", "\\/");
		str = str.replace("|", "\\p");
		str = str.replace("\b", "\\b");
		str = str.replace("\f", "\\f");
		str = str.replace("\n", "\\n");
		str = str.replace("\r", "\\r");
		str = str.replace("\t", "\\t");

		str = str.replace(Character.valueOf((char)7).toString(), "\\a");
		str = str.replace(Character.valueOf((char)11).toString(), "\\v");

		return str;
	}

	/**
	 * Encode t s3 string.
	 *
	 * @param str the str
	 * @return the string
	 */
	public static String decodeTS3String(String str)
	{

		return (String) LibFieldType.STRING.parse(str);
		//str = str.replace(Character.valueOf((char)7).toString(), "\\a");
		//str = str.replace(Character.valueOf((char)11).toString().toString(), "\\v");

//		return str;
	}
	
	
	/**
	 * Tests if a string is empty
	 * (That is: either it is null or the string equals trimmed "")
	 */
	public static boolean isEmptyString (String str) {
		return str == null || str.trim().equals("");
	}

	/**
	 * Convert a string of format [1,2,n] to an integer-array
	 */
	
	public static Integer[] getArrayFromString (String toParse) {
		// test if String is given format
		if (toParse.matches("null"))
			return new Integer[0];
		if (!toParse.matches("\\{.*\\}")) {
			throw new IllegalArgumentException(
					"Array does not have right format: " + toParse);
		}
		Integer ret[] = null;
		try { 
			toParse = toParse.substring(1, toParse.length() - 1);
			String temp[] = toParse.split(", ");
			ret = new Integer[temp.length];
			for (int i = 0; i < temp.length; ++i) {
				ret[i] = Integer.parseInt(temp[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static String getStringFromArray (Object[] array) {
		return java.util.Arrays.deepToString(array).replace("[", "{").replace("]", "}");
	}
}
