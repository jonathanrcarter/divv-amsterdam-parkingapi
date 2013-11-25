<%@ page import="java.util.*, java.io.*, java.text.*, com.glimworm.common.utils.*, javax.mail.*, javax.mail.internet.*, com.oroinc.net.*"
	session="true"
	buffer="128kb"
	autoFlush="true"
	isThreadSafe="true"
	contentType="text/html;charset=UTF-8"%><%
	
	String action = gwUtils.get(request,"action","");
	String sid = gwUtils.get(request,"sid","");
	String type = gwUtils.get(request,"typ","");
	String svr = gwUtils.get(request,"svr","");
	String uid = gwUtils.get(request,"uid","");
	String sessionid = gwUtils.get(request,"ses","");
	String event = gwUtils.get(request,"evt","");
	String object = gwUtils.get(request,"obj","");
	String jsonpvar = gwUtils.get(request,"jsonpvar","");
	String city = gwUtils.get(request,"city","");
	
	System.out.println("log log ["+type+"]");
	
	

//	String lat = gwUtils.get(request,"lat","0");

//	System.out.println("parkshark::lat"+lat);

	//Packages.gwdb.v6.logging.warn("A");	

	String rv = "{}";
	if (action.equalsIgnoreCase("find_nearest")) {
		
		double lat = com.glimworm.common.TypeConversions.Math.Double(gwUtils.get(request,"lat","51.1"),52);
		double lon = com.glimworm.common.TypeConversions.Math.Double(gwUtils.get(request,"lon","5.1"),6);
		double rate = com.glimworm.common.TypeConversions.Math.Double(gwUtils.get(request,"rate","0.0"),0);
		
		if (city.equalsIgnoreCase("antwerp")) {
			rv = com.glimworm.opendata.parkshark.CalcParkingAntwerp.find_nearest(lat,lon,rate);
		} else {
			rv = com.glimworm.opendata.parkshark.CalcParking.find_nearest(lat,lon,rate);
		}
		
	} else if (action.equalsIgnoreCase("get_gebcodes")) {
		
		if (city.equalsIgnoreCase("antwerp")) {
			rv = com.glimworm.opendata.parkshark.CalcParkingAntwerp.getGebCodes();
		} else {
			rv = com.glimworm.opendata.parkshark.CalcParking.getGebCodes();
		}
		
	} else if (action.equalsIgnoreCase("get_costs")) {
		
		if (city.equalsIgnoreCase("antwerp")) {
			rv = com.glimworm.opendata.parkshark.CalcParkingAntwerp.getCosts();
		} else {
			rv = com.glimworm.opendata.parkshark.CalcParking.getCosts();
		}
		
	} else if (action.equalsIgnoreCase("get_meters")) {
		
		String meternumbers = gwUtils.get(request,"meternumbers",""); 
		if (city.equalsIgnoreCase("antwerp")) {
			rv = com.glimworm.opendata.parkshark.CalcParkingAntwerp.getMeters(meternumbers);
		} else {
			rv = com.glimworm.opendata.parkshark.CalcParking.getMeters(meternumbers);
		}
		
	} else if (action.equalsIgnoreCase("get_meters_in_box")) {
		double north = com.glimworm.common.TypeConversions.Math.Double(gwUtils.get(request,"north","51.1"),51.1);
		double south = com.glimworm.common.TypeConversions.Math.Double(gwUtils.get(request,"south","51.0"),51.0);
		double east = com.glimworm.common.TypeConversions.Math.Double(gwUtils.get(request,"east","5.1"),5.1);
		double west = com.glimworm.common.TypeConversions.Math.Double(gwUtils.get(request,"west","5.0"),5.0);
		if (city.equalsIgnoreCase("antwerp")) {
			rv = com.glimworm.opendata.parkshark.CalcParkingAntwerp.getMeters(north,south,east,west);
		} else {
			rv = com.glimworm.opendata.parkshark.CalcParking.getMeters(north,south,east,west);
		}
		
		
	} else if (action.trim().length() == 0) {
		int day = com.glimworm.common.TypeConversions.Math.Int(gwUtils.get(request,"day","0"),0);
		int hr = com.glimworm.common.TypeConversions.Math.Int(gwUtils.get(request,"hr","0"),0);
		int min = com.glimworm.common.TypeConversions.Math.Int(gwUtils.get(request,"min","0"),0);
		double duration = com.glimworm.common.TypeConversions.Math.Double(gwUtils.get(request,"duration","2"),2);
		double lat = com.glimworm.common.TypeConversions.Math.Double(gwUtils.get(request,"lat","51.1"),52);
		double lon = com.glimworm.common.TypeConversions.Math.Double(gwUtils.get(request,"lon","5.1"),6);
		String methods = gwUtils.get(request,"methods","cash,pin");
		int fmt = com.glimworm.common.TypeConversions.Math.Int(gwUtils.get(request,"fmt","0"),0);
		
		if (city.equalsIgnoreCase("antwerp")) {
			rv = com.glimworm.opendata.parkshark.CalcParkingAntwerp.calc(day, hr, min ,  duration, lat, lon, methods,fmt);
		} else {
			rv = com.glimworm.opendata.parkshark.CalcParking.calc(day, hr, min ,  duration, lat, lon, methods,fmt);
		}
	} else {
		rv = "{}";
	}
	if (jsonpvar != null && jsonpvar.trim().length() > 0) {
		out.println("var "+jsonpvar+"="+rv+";");
	} else {
		out.println(rv);
		
	}

	System.out.println("parkshark::done all");
	
%>
