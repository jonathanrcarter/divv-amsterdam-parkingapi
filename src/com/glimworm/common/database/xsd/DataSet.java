package com.glimworm.common.database.xsd;

import java.util.ArrayList;

import javolution.util.FastMap;

public class DataSet {
	public String[] fields = null;
	public ArrayList<String[]> row = new ArrayList<String[]>();
	
	public int rows() {
		if (row != null) return row.size();
		return 0;
	}
	public int cols() {
		if (fields != null) return fields.length;
		return 0;
	}
	public int getColumnIndex(String S) {
		if (fields == null) return -1;
		if (S == null) return -1;
		for (int i=0; i < fields.length; i++) {
			if (fields[i] != null && S.equals(fields[i])) return i;
		}
		return -1;
	}
	

	public String getString(int r, String C, String def) {
		String retval = getString(r,getColumnIndex(C));
		if (retval == null) return def;
		return retval;
	}
	public String getString(int r, int c, String def) {
		String retval = getString(r,c);
		if (retval == null) return def;
		return retval;
	}

	public String getString(int r, String C) {
		String retval = getString(r,getColumnIndex(C));
		return retval;
	}
	public String getString(int r, int c) {
		if (r < rows() && c < cols() && r >=0 && c >= 0) return row.get(r)[c];
		return null;
	}

	public int getInt(int r, int c, int def) {
		return com.glimworm.common.TypeConversions.Math.Int(getString(r,c), def);
	}
	public int getInt(int r, String C, int def) {
		return com.glimworm.common.TypeConversions.Math.Int(getString(r,getColumnIndex(C)), def);
	}

	public double getDouble(int r, String C, int def) {
		return com.glimworm.common.TypeConversions.Math.Double(getString(r,getColumnIndex(C)), def);
	}
	public double getDouble(int r, int c, int def) {
		return com.glimworm.common.TypeConversions.Math.Double(getString(r,c), def);
	}
	
	

}
