<%@ page import="java.util.*, gwdb.*, java.io.*, java.text.*, com.glimworm.common.utils.*, javax.mail.*, javax.mail.internet.*, com.oroinc.net.*,com.glimworm.opendata.divvamsterdamapi.planning.*, com.glimworm.opendata.divvamsterdamapi.planning.xsd.*"
	session="true"
	buffer="128kb"
	autoFlush="true"
	isThreadSafe="true"
	contentType="text/html;charset=UTF-8"%><%
	
	String action = gwdb.gwUtils.get(request,"action","");
	String sid = gwdb.gwUtils.get(request,"sid","");
	String type = gwdb.gwUtils.get(request,"typ","");
	String svr = gwdb.gwUtils.get(request,"svr","");
	String uid = gwdb.gwUtils.get(request,"uid","");
	String sessionid = gwdb.gwUtils.get(request,"ses","");
	String event = gwdb.gwUtils.get(request,"evt","");
	String object = gwdb.gwUtils.get(request,"obj","");
	String jsonpvar = gwdb.gwUtils.get(request,"jsonpvar","");
	String city = gwdb.gwUtils.get(request,"city","");
	
	
	System.out.println("log log ["+type+"]");
	
	ArrayList<Place> pl_home = GeoCodeByMapQuest.geocode("eerste weteringplantsoen 8 , Amsterdam, Netherlands");
	ArrayList<Place> pl_destination = GeoCodeByMapQuest.geocode("Marco Polostraat 107 , Amsterdam, Netherlands");
	out.println(pl_home);
	out.println(pl_destination);
	
	String dd = "28";
	String mm = "12";
	String yy = "2013";
	String ymd = yy+"-"+mm+"-"+dd;
	String h = "12";
	String m = "15";
	String hm = h+":"+m;
	int day = 5;
	int hr = 12;
	int min = 15;
	int duration = 180;
	double lat = pl_destination.get(0).lat;
	double lon = pl_destination.get(0).lon;
	String methods = "pin";
	int fmt = 2;

//	req.options._date = "2013-12-28";
//	req.options._time = "18:00";
	
	
	com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturn prv = com.glimworm.opendata.parkshark.CalcParking.calcv2(day, hr, min ,  duration, lat, lon, methods,fmt);

	com.thoughtworks.xstream.XStream xstream = new com.thoughtworks.xstream.XStream(new com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver());
	xstream.setMode(com.thoughtworks.xstream.XStream.NO_REFERENCES);

	out.println("start");
	for (int i=0; i < pl_destination.size(); i++) {
		out.println(xstream.toXML(pl_destination.get(i)));
	}
	out.println("end ");

	/*
	for (int i=0; i < prv.reccommendations.size(); i++) {
		System.out.print(prv.reccommendations.get(i).cost);
		System.out.print("\t");
		System.out.print(prv.reccommendations.get(i).type);
		System.out.print("\t");
		System.out.print(prv.reccommendations.get(i).address);
		System.out.print("\t");
		System.out.println("");
		
		
		Place parkingLocation = new Place();
		parkingLocation.lat = prv.reccommendations.get(i).lat;
		parkingLocation.lon = prv.reccommendations.get(i).lon;
		parkingLocation.name = prv.reccommendations.get(i).belnummer;
		parkingLocation.type = prv.reccommendations.get(i).type;

		PlanRequest req = new PlanRequest();
		req.from = parkingLocation;
		req.to = pl_destination.get(0);
		req.options._date = ymd;
		req.options._time = hm;
		PlanResponse res1 = PlanOtp.plan(req);
		System.out.println(res1); 
		System.out.println(xstream.toXML(res1));
		
		out.println(xstream.toXML(prv.reccommendations.get(i)));
		out.println(xstream.toXML(res1.toString()));
		
	}
	*/

	System.out.println("NOW IN PARALLEL");
	ParallelPlanRequest ppr = new ParallelPlanRequest();
	ppr.pl_destination = pl_destination.get(0);
	ppr.ymd = ymd;
	ppr.hm = hm;
	
	com.glimworm.opendata.divvamsterdamapi.planning.ParallelPlan.plan(ppr, prv.reccommendations);
	System.out.println("NOW IN PARALLEL - DONE");
	
	out.println(xstream.toXML(prv.reccommendations));
	
	
	
	String URL = "http://api.parkshark.nl/psapi/api.jsp";
	String PARAMS = "day=5&hr=8&min=30&duration=3&lat=52.377&lon=4.9104&methods=cash,pin=";
	com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse cr =  com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(URL, PARAMS, null, null, null, null, null);
	
//	System.out.println(cr.text);

	String rv = cr.text;
	System.out.println(rv);
	System.out.println("parkshark::done all");
	
%>
