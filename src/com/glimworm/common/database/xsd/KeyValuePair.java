package com.glimworm.common.database.xsd;

/**
 * a kay / value pair as a string
 * @author JC
 *
 */
public class KeyValuePair {
	public String key = null;
	public String value = null;
	public boolean selected = false;
	public Object obj = null;
	
	public KeyValuePair() {
		key = "";
		value = "";
		selected = false;
		obj = null;
	}
	
	
	/**
	 * contruct with a key and a value
	 * @param KEY
	 * @param VALUE
	 */
	public KeyValuePair(String KEY, String VALUE) {
		key = KEY;
		value = VALUE;
		selected = false;
		obj = null;
	}
	
	
	/**
	 * contruct with a key and a value and an object
	 * @param KEY
	 * @param VALUE
	 * @param OB
	 */
	public KeyValuePair(String KEY, String VALUE, Object OB) {
		key = KEY;
		value = VALUE;
		selected = false;
		obj = OB;
	}

	
	/**
	 * construct with key, value and default choice
	 * @param KEY
	 * @param VALUE
	 */
	public KeyValuePair(String KEY, String VALUE, String DEFTEXT) {
		key = KEY;
		value = VALUE;
		selected = (KEY != null && DEFTEXT != null && KEY.equals(DEFTEXT));
		obj = null;
	}
	
	
	/**
	 * construct with key, value and default choice and an object
	 * @param KEY
	 * @param VALUE
	 * @param DEFTEXT
	 * @param OB
	 */
	public KeyValuePair(String KEY, String VALUE, String DEFTEXT, Object OB) {
		key = KEY;
		value = VALUE;
		selected = (KEY != null && DEFTEXT != null && KEY.equals(DEFTEXT));
		obj = OB;
	}
	
	
	/**
	 * return the key as a string
	 * @return
	 */
	public String getKey() {
		return key;
	}
	
	
	/**
	 * return the value as a string
	 * @return
	 */
	public String getValue() {
		return value;
	}
	
	
	/**
	 * return the object as an object
	 * @return
	 */
	public Object getObject() {
		return obj;
	}
	
	
	/**
	 * return if the value is selected
	 * @return
	 */
	public boolean getSelected() {
		return selected;
	}
	
	
	/**
	 * return selected as a string if selected or ""
	 * @param S
	 * @return
	 */
	public String getSelectedAsString(String S) {
		return getSelectedAsString(S,"");
	}
	
	
	/**
	 * return selected as a string if selected or a string if not selected
	 * @param Y
	 * @param N
	 * @return
	 */
	public String getSelectedAsString(String Y, String N) {
		if (selected) return Y;
		return N;
	}

}
