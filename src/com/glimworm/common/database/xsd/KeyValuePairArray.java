package com.glimworm.common.database.xsd;

import java.util.ArrayList;

public class KeyValuePairArray {
	
	public ArrayList<KeyValuePair> kvpa = new ArrayList<KeyValuePair>();
	public String selectedKey = "";
	
	public void clear() {
		kvpa.clear();
	}
	
	public void add(String Key, String Value) {
		KeyValuePair kvp = new KeyValuePair(Key, Value, selectedKey);
		kvpa.add(kvp);
	}
	public void add(String Key, String Value, String Def) {
		KeyValuePair kvp = new KeyValuePair(Key, Value, Def);
		kvpa.add(kvp);
	}
	public void add(String Key, String Value, Object ob) {
		KeyValuePair kvp = new KeyValuePair(Key, Value, ob);
		kvpa.add(kvp);
	}
	public void add(String Key, String Value, String Def, Object ob) {
		KeyValuePair kvp = new KeyValuePair(Key, Value, Def, ob);
		kvpa.add(kvp);
	}
	
	public int size() {
		return kvpa.size();
	}
	
	public String[] getKeys() {
		ArrayList<String> retval = new ArrayList<String>();
		for (int i=0; i < kvpa.size(); i++) {
			KeyValuePair kvp = kvpa.get(i);
			retval.add(kvp.getKey());
		}
		String str [] = new String [retval.size()];
		retval.toArray (str);
		return str;
	}
	
	public KeyValuePair get(int i) {
		if (i < kvpa.size()) return kvpa.get(i);
		return null;
	}
	
	public KeyValuePair[] getPairs() {
		KeyValuePair retval [] = new KeyValuePair [kvpa.size()];
		kvpa.toArray (retval);
		return retval;
	}

}
