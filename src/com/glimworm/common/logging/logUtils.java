package com.glimworm.common.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;


public class logUtils {

	public static void log(String svr, String type, String object, String sid, String uid, String event, String sessionid) {
		com.glimworm.common.database.GWDBBean.sqlStaticWrite("insert ignore into GWDB01_STATS.events (id,server,type,object,sid,uid,event,sessionid,ts) values (0,'"+svr+"','"+type+"','"+object+"','"+sid+"','"+uid+"','"+event+"','"+sessionid+"',now())");
	}

	public static String getTrace(Exception E) {
		String trace = "[trace]";
		try {
			StringWriter sw = new StringWriter();
			E.printStackTrace(new PrintWriter(sw));
			trace = sw.toString();
			sw.close();
		} catch (Exception E1) {
		}
		return trace;
	}	
	
	
	public static void error(String CLASS,String METHOD,String NOTE,Exception E) {
		error("no site",CLASS,METHOD,NOTE,E,false);
	}
	public static void error(String SID,String CLASS,String METHOD,String NOTE,Exception E, boolean SENDTOJC) {
		String firefly = "";
		System.err.println("---------ERROR----------------------------------------------------");
		System.err.println(new Date().toString());
		System.err.println("SID=["+SID+"]");
		System.err.println(CLASS+" "+METHOD+" "+NOTE+" "+E.getMessage());
		E.printStackTrace(System.err);
		System.err.println("firefly");
		System.err.println("=======");
		System.err.println(firefly);
		System.out.println("------------------------------------------------------------------");
		if (SENDTOJC){
			String body = new Date().toString()+":"+CLASS+" "+METHOD+" "+NOTE+" "+E.getMessage()+"\n\n\nFirefly::"+firefly+"\n\n"+getTrace(E);
			com.glimworm.common.utils.SendMail.sendMail("error@glimworm.com","jc@glimworm.com","info@glimworm.com","Error "+SID+" "+CLASS,body,"text/plain");
		}
	}
	public static void warning(String SID,String CLASS,String METHOD,String NOTE,boolean SENDTOJC) {
		System.err.println("---------WARNING----------------------------------------------------");
		//System.out.println(Calendar.getInstance().toString());
		System.err.println(new Date().toString());
		System.err.println("SID=["+SID+"]");
		System.err.println(CLASS+" "+METHOD+" "+NOTE+" ");
		System.err.println("------------------------------------------------------------------");
		if (SENDTOJC){
			String body = new Date().toString()+":"+CLASS+" "+METHOD+" "+NOTE;
			com.glimworm.common.utils.SendMail.sendMail("warning@glimworm.com","jc@glimworm.com","info@glimworm.com","Warning "+SID+" "+CLASS,body,"text/plain");
		}
	}	
}
