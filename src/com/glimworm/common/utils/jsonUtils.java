package com.glimworm.common.utils;

public class jsonUtils {
	/**
	 * the name of the routing for debug 
	 */
	public static String className = "gwdb.v6.Json.jsonUtils";
	
	public static org.json.JSONObject string2json(String S) {
		org.json.JSONObject retval = new org.json.JSONObject();

		if (S == null || S.trim().length() == 0) return retval;
		
		try {
			retval = new org.json.JSONObject(S);
		} catch (Exception E) {
		}
		return retval;
	}
	
	public static String getString(org.json.JSONObject OBJ, String KEY, String DEF) {
		String retval = "";
		if (OBJ == null || KEY == null) return DEF;
		if (!OBJ.has(KEY)) return DEF;
		try {
			retval = OBJ.getString(KEY);
		} catch (Exception E) {
			com.glimworm.common.logging.logUtils.warning("", className, "getString", "json error getting KEY AS STRING ["+KEY+"]", true);
			retval = DEF;
		}
		return retval;
	}
	
	public static int getInt(org.json.JSONObject OBJ, String KEY, int DEF) {
		int retval = DEF;
		if (OBJ == null || KEY == null) return DEF;
		if (!OBJ.has(KEY)) return DEF;
		try {
			retval = OBJ.getInt(KEY);
		} catch (Exception E) {
			com.glimworm.common.logging.logUtils.warning("", className, "getString", "json error getting KEY AS INT["+KEY+"]", true);
			retval = DEF;
		}
		return retval;
	}

}
