package com.glimworm.common.TypeConversions;


public class Math {
	public static String className = "gwdb.v6.TypeConversions.Math";

	/**
	 * Convert String to int with a default value
	 * @param S
	 * @param def
	 * @return
	 */
	public static int Int(String S, int def) {
		try {
			return Integer.parseInt(S);			
		} catch (Exception E) {
			return def; 
		}
	}
	
	/**
	 * Convert String to int (with an implied default of 0)
	 * @param S
	 * @return
	 */
	public static int Int(String S) {
		return Int(S,0);
	}
	/**
	 * Convert String to float with a default value
	 * @param S
	 * @param def
	 * @return
	 */
	public static float Float(String S, int def) {
		try {
			return Float.parseFloat(S);			
		} catch (Exception E) {
			return def; 
		}
	}
	
	/**
	 * Convert String to float (with an implied default of 0)
	 * @param S
	 * @return
	 */
	public static float Float(String S) {
		return Float(S,0);
	}
	/**
	 * Convert String to float with a default value
	 * @param S
	 * @param def
	 * @return
	 */
	public static double Double(String S, int def) {
		try {
			return Double.parseDouble(S);			
		} catch (Exception E) {
			return def; 
		}
	}
	/**
	 * Convert String to float with a default value
	 * @param S
	 * @param def
	 * @return
	 */
	public static double Double(String S, double def) {
		try {
			return Double.parseDouble(S);			
		} catch (Exception E) {
			return def; 
		}
	}
	
	/**
	 * Convert String to float (with an implied default of 0)
	 * @param S
	 * @return
	 */
	public static double Double(String S) {
		return Double(S,0);
	}
		
	/**
	 * convert an integer to String
	 * @param I
	 * @return
	 */
	public static String string(int I) {
		return Integer.toString(I);
	}
	
	public static java.text.DecimalFormat getFormat(String S) {
		try {
			java.text.DecimalFormat df = new java.text.DecimalFormat(S);
			return df;
		} catch (Exception E) {
			com.glimworm.common.logging.logUtils.error("S", className, "getFormat(String)", "error converting ["+S+"]", E,true);
		}
		return new java.text.DecimalFormat("0");
	}
	/**
	 * convert double to formatted string 
	 * @param d
	 * @param S
	 * @return
	 */
	public static String decimal(double d, String S) {
		String retval = "";
		java.text.DecimalFormat df = getFormat(S);
		try {
			return df.format(d);
		} catch (Exception E) {
			com.glimworm.common.logging.logUtils.error("S", className, "decimal(double, String)", "error converting ["+d+"] ["+S+"]", E,true);
			return df.format(0);
		}
	}
	
	/**
	 * convert float to double and then to formatted string
	 * @param F
	 * @param S
	 * @return
	 */
	public static String decimal(float F, String S) {
		double d = 0;
		try {
			d = new Float(F).doubleValue();
		} catch (Exception E) {
			com.glimworm.common.logging.logUtils.error("S", className, "decimal(float, String)", "error converting ["+F+"] to double",E, true);
		}
		return decimal(d, S);
	}
	
}
