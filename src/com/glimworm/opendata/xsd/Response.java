package com.glimworm.opendata.xsd;

//import java.util.Enumeration;
//import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;

public class Response extends SerializableObject {

	public org.json.JSONObject jobj = new org.json.JSONObject();
	public Object[] obj = null;
	
	public Response() {
		
	}
	public Response(int status, String message) {
		setStatus(status,message);
	}
	
	public void setStatus(int status, String message) {
		put("status",status);
		put("status_msg",message);
	}
	
	public String getOpt(String PROP, String DEF) {
		return jobj.optString(PROP, DEF);
	}
	
	public String get(String PROP) {
		return getString(PROP);
	}
	public String get(String PROP, String DEF) {
		return getString(PROP, DEF);
	}
	public String getString(String PROP) {
		return jobj.optString(PROP,"");
	}
	public String getString(String PROP, String DEF) {
		return jobj.optString(PROP,DEF);
	}
	public int getInt(String PROP) {
		return jobj.optInt(PROP,0);
	}
	public int getInt(String PROP, int DEF) {
		return jobj.optInt(PROP,DEF);
	}
	public double getDouble(String PROP) {
		return jobj.optDouble(PROP,0);
	}
	public double getDouble(String PROP, double DEF) {
		return jobj.optDouble(PROP,DEF);
	}
	
	public void put(String PROP,String VAL) {
		try {
			jobj.put(PROP, VAL);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void put(String PROP,int VAL) {
		try {
			jobj.put(PROP, VAL);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void put(String PROP,double VAL) {
		try {
			jobj.put(PROP, VAL);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void oput(String PROP,Object[] objA) {
		obj = objA;
		jput(PROP);
	}
	public void jput(String S) {
		jput(S,obj);
	}
	public void jput(String PROP,Object[] objA) {
		try {
			jobj.put(PROP, com.glimworm.opendata.utils.jsonUtils.string2json(obj2json(objA)));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String obj2json(Object[] obj) {
		com.thoughtworks.xstream.XStream xstream = new com.thoughtworks.xstream.XStream(new com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver());
		xstream.setMode(com.thoughtworks.xstream.XStream.NO_REFERENCES);
//        xstream.alias("line", gwdb.gwEPayTransactionLine.class);
        xstream.alias("items", obj.getClass());
        String retval = xstream.toXML(obj);
        return retval;
	}
	
	public String format(String format) {
		if (format.equals("json")) {
			return jobj.toString();
		}
		if (format.equals("xml")) {
			String retval = "";
			retval += "<status>"+getInt("status")+"<status>\n";
			retval += "<status_msg>"+getString("status_msg")+"<status_msg>\n";
			if (obj!= null) {
				com.thoughtworks.xstream.XStream xstream = new com.thoughtworks.xstream.XStream();
				xstream.setMode(com.thoughtworks.xstream.XStream.NO_REFERENCES);
		        xstream.alias("data", obj.getClass());
		        xstream.alias("items", obj[0].getClass());
		        retval += xstream.toXML(obj);
			}
	        return retval;
		}
		
		return "";
	}

}
