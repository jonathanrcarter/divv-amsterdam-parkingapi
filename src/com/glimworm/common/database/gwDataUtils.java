package com.glimworm.common.database;

import java.util.*;
import java.net.*;
import java.io.*;
import java.security.*;
import org.json.*;
import org.dom4j.*;
import java.sql.*;

/* skeleton
import org.json.*;
JSONStringer myJson =  new JSONStringer();
myJson.array();
 myJson.object();
  myJson.key(rsFields[iCnt]);
  myJson.value(rsValue);
 myJson.endObject();
myJson.endArray();
myJson.toString()
*/

public class gwDataUtils {

	public static String getJSON (ResultSet RS, boolean already_at_next) {
		/**
		 * Returns the ResultSet as a JSON string
		 */
		JSONStringer myJson =  new JSONStringer();
		try {
			ResultSetMetaData RSMD = RS.getMetaData();
			if (RS != null) {
				int col = RSMD.getColumnCount();
				String[] rsFields = new String[col];
				for (int i=1;i<=col;i++) {
					rsFields[i-1] = RSMD.getColumnName(i).toString();
				}
				myJson.array();
				/**
				 * loop records
				 */
				while (already_at_next || RS.next()) {
					already_at_next = false;

					myJson.object();
					/**
					 * loop fields
					 */
					for (int iCnt = 0; iCnt < rsFields.length; iCnt++) {
						/**
						 * get value of iCntth field and add to json
						 */
						String rsValue = RS.getString(rsFields[iCnt]);
						myJson.key(rsFields[iCnt]);
						myJson.value(rsValue);
					}
					myJson.endObject();
				}
				myJson.endArray();
			}
		} catch (Exception E) {
		}
		return myJson.toString();
	}
	
	public static com.glimworm.common.database.xsd.DataSet getArray (ResultSet RS, boolean already_at_next) {
		/**
		 * Returns the ResultSet as a JSON string
		 */
		com.glimworm.common.database.xsd.DataSet ds = new com.glimworm.common.database.xsd.DataSet();
		try {
			ResultSetMetaData RSMD = RS.getMetaData();
			if (RS != null) {
				int col = RSMD.getColumnCount();
				ds.fields = new String[col];
				for (int i=1;i<=col;i++) {
					ds.fields[i-1] = RSMD.getColumnName(i).toString();
				}
				
				/**
				 * loop records
				 */
				while (already_at_next || RS.next()) {
					already_at_next = false;

					String[] row = new String[col];
					/**
					 * loop fields
					 */
					for (int iCnt = 0; iCnt < ds.fields.length; iCnt++) {
						/**
						 * get value of iCntth field and add to json
						 */
						String rsValue = RS.getString(ds.fields[iCnt]);
						row[iCnt] = rsValue;
					}
					ds.row.add(row);
				}
			}
		} catch (Exception E) {
			E.printStackTrace(System.out);
		}
		return ds;
	}	

	public static String getXML (ResultSet RS, boolean already_at_next) {
		/**
		 * Returns the ResultSet as a XML string
		 */
		// first create an empty string for the xml
		String rsXml = new String("");
		try {
			//get info about the ResultSet
			ResultSetMetaData RSMD = RS.getMetaData();
			//for now we assume the first column is from the main table
			String tableName = RSMD.getTableName(1);
			Document xmlRSStructure = null;
			if (RS != null) {
				//create the xml + the root and init the data definitions
				xmlRSStructure = DocumentHelper.createDocument();
				Element xmlRSStructRoot = xmlRSStructure.addElement("resultSetXml");
				Element xmlRSDataDef = xmlRSStructRoot.addElement("dataDefinitions");
				xmlRSDataDef.addElement("tableName")
						.addText(tableName);
				Element xmlRSFieldsDef = xmlRSDataDef.addElement( "fields" );
				int col = RSMD.getColumnCount();
				String[] rsFields = new String[col];
				//get all the field definitions
				for (int i=1;i<=col;i++) {
					rsFields[i-1] = RSMD.getColumnName(i);
					Element xmlRSFieldDef = xmlRSFieldsDef.addElement("field");
					xmlRSFieldDef.addElement("fieldName")
						.addText(rsFields[i-1]);
					xmlRSFieldDef.addElement("position")
						.addText(new Integer(i).toString());
					xmlRSFieldDef.addElement("dataType")
						.addText(RSMD.getColumnTypeName(i));
				}

				//now we start with the actual data
				Element xmlRSData = xmlRSStructRoot.addElement("data");

				//loop through the records and create a node with the tablename
				while (already_at_next || RS.next()) {
					already_at_next = false;

					Element xmlRecord = xmlRSData.addElement(tableName);
					//loop through the fields
					for (int iCnt = 0; iCnt < rsFields.length; iCnt++) {
						//now add the fields + values to the xmlRecord
						String rsValue = RS.getString(rsFields[iCnt]);
						xmlRecord.addElement(rsFields[iCnt])
							.addText(rsValue);
					}
				}
			}
			//fill the string we created to return the xml
			rsXml = (xmlRSStructure != null) ? xmlRSStructure.asXML() : "";
		} catch (Exception E) {
		}
		return rsXml;
	}
	/**
	 * The latest replace routine
	 *
	 * @param text
	 * @param FROM
	 * @param TO
	 * @return
	 */
	public static String replace(String text, String FROM, String TO) {
		if (TO == null) TO="";
		if (text != null && FROM !=null && TO != null) {
			int len = FROM.length();
			int len2a = TO.length();
			int len2 = TO.length()-1;
			int diff = TO.length() - FROM.length();
			if (text.trim().length() >= len) {
				StringBuilder sb = new StringBuilder(text);
				int diffcnt = 0;
				if (TO.length() ==0) {
					//for (int i = text.indexOf(FROM); i > -1; i = text.indexOf(FROM,i+len2+2) ) {
					//for (int i = text.indexOf(FROM); i > -1; i = text.indexOf(FROM,i+len2+1) ) {
					//	sb.delete(i+diffcnt,i+diffcnt+len);
					//	diffcnt +=diff;
					//}
					for (int i = sb.indexOf(FROM); i > -1; i = sb.indexOf(FROM,i) ) {
						sb.delete(i,i+len);
					}
				} else {
					//for (int i = text.indexOf(FROM); i > -1; i = text.indexOf(FROM,i+len2+2) ) {
					for (int i = sb.indexOf(FROM); i > -1; i = sb.indexOf(FROM,i) ) {
						sb.replace(i,i+len,TO);
						i+=len2a;
					}
				}
				text = sb.toString();
			}
		}
		return text;
	}	
	public static String getNewCreateStatement (String databaseOld, String tableName, String databaseNew, String type) {
		/**
		 * Returns a new create statement for the specified table
		 */
		// first create an empty string for the statement
		String oldCreate = new String("");
		String newCreate = new String("DROP TABLE IF EXISTS "+databaseNew+".`"+tableName+"`;\n");
		try {
			java.sql.ResultSet rs = GWDBBean.sqlStatic("show create table "+databaseOld+"."+tableName+";");
			while (rs.next()) {
				oldCreate = rs.getString(2);
			}
			String[] _oldCreate = oldCreate.split("\n");
			//Loop through the old statement to find the lines to be changed
			//change first line to include DBname, change text/longtext, change varchar(255)
			java.sql.ResultSet rs1 = null;
			for (int i = 0; i < _oldCreate.length; i++) {
				System.out.println(i+"]"+_oldCreate[i]);
				if (i == 0) {
					newCreate += replace(_oldCreate[i], "`"+tableName+"`", databaseNew+".`"+tableName+"`") + "\n";
				} else if (_oldCreate[i].indexOf(" longtext ") > -1 || _oldCreate[i].indexOf(" text ") > -1) {
					newCreate += replace(_oldCreate[i], ",", " default '',") + "\n";
				} else if (_oldCreate[i].indexOf(" varchar(255) ") > -1) {
					String columnName = _oldCreate[i].substring(3,_oldCreate[i].indexOf(" varchar(255) ")-1);
					rs1 = GWDBBean.sqlStatic("select max(length("+columnName+")) from "+databaseOld+"."+tableName+";");
					rs1.next();
					newCreate += replace(_oldCreate[i], "(255)", "("+rs1.getString(1)+")") + "\n";
				} else if (_oldCreate[i].indexOf(" varchar(100) ") > -1) {
					String columnName = _oldCreate[i].substring(3,_oldCreate[i].indexOf(" varchar(100) ")-1);
					rs1 = GWDBBean.sqlStatic("select max(length("+columnName+")) from "+databaseOld+"."+tableName+";");
					rs1.next();
					newCreate += replace(_oldCreate[i], "(100)", "("+rs1.getString(1)+")") + "\n";
				} else if (i == _oldCreate.length-1) {
					newCreate += replace(_oldCreate[i], "MyISAM", type) + "\n";
				} else newCreate += _oldCreate[i] + "\n";
			}

		} catch (Exception E) {
		}
		System.out.print(newCreate);
		return newCreate;
	}


	public static void main(String[] args) {

		gwDataUtils mainobj = new gwDataUtils();
		mainobj.getNewCreateStatement(args[0],args[1],args[2],args[3]);

	}

}
