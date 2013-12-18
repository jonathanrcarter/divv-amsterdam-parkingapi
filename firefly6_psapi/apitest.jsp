<%@ page import="java.util.*, gwdb.*, java.io.*, java.text.*, com.glimworm.common.utils.*, javax.mail.*, javax.mail.internet.*, com.oroinc.net.*,com.glimworm.opendata.divvamsterdamapi.planning.*, com.glimworm.opendata.divvamsterdamapi.planning.xsd.*"
	session="true"
	buffer="128kb"
	autoFlush="true"
	isThreadSafe="true"
	contentType="text/html;charset=UTF-8"%><%

	long _exdt = new Date().getTime();
	com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse ar = new com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse();
	
	com.thoughtworks.xstream.XStream xstream = new com.thoughtworks.xstream.XStream(new com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver());
	xstream.setMode(com.thoughtworks.xstream.XStream.NO_REFERENCES);
	xstream.omitField(com.glimworm.opendata.divvamsterdamapi.planning.xsd.MMdatetime.class,"dt");
	xstream.alias("result",com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse.class);

	xstream.omitField(com.glimworm.opendata.parkshark.xsd.Meter.class,"chance_weekday");
	xstream.omitField(com.glimworm.opendata.parkshark.xsd.Meter.class,"chance_sat");
	xstream.omitField(com.glimworm.opendata.parkshark.xsd.Meter.class,"chance_sun");
	xstream.omitField(com.glimworm.opendata.parkshark.xsd.Meter.class,"dbg");
	
	xstream.omitField(com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturnReccommendation.class,"chance_weekday");
	xstream.omitField(com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturnReccommendation.class,"chance_sat");
	xstream.omitField(com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturnReccommendation.class,"chance_sun");
	xstream.omitField(com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturnReccommendation.class,"dbg");
	
	
	String action = gwdb.gwUtils.get(request,"action","");
	String to_lat = gwdb.gwUtils.get(request,"to_lat","52.368104267594056");
	String to_lon = gwdb.gwUtils.get(request,"to_lon","4.856208655327167");
	String dd = gwdb.gwUtils.get(request,"dd","28");
	String mm = gwdb.gwUtils.get(request,"mm","12");
	String yy = gwdb.gwUtils.get(request,"yy","2013");
	String h = gwdb.gwUtils.get(request,"h","12");
	String m = gwdb.gwUtils.get(request,"m","50");
	String dur = gwdb.gwUtils.get(request,"dur","2");
	String opt_am = gwdb.gwUtils.get(request,"opt_am","n");
	String opt_routes = gwdb.gwUtils.get(request,"opt_routes","y");
	String opt_routes_ret = gwdb.gwUtils.get(request,"opt_routes_ret","n");
	String opt_rec = gwdb.gwUtils.get(request,"opt_rec","y");
	String tim = gwdb.gwUtils.get(request,"tim","n");
	String plan_radius = gwdb.gwUtils.get(request,"plan_radius","2000");
	
	
	
	if (action.equalsIgnoreCase("")) {
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel='stylesheet' href='apitest.css' />");
		out.println("</head>");
		out.println("<body>");
		out.println("<form>");
		out.println("<input type='hidden' name='action' value='plan'>");
		out.println("LAT <input name='to_lat' value='"+to_lat+"'>");
		out.println("LON <input name='to_lon' value='"+to_lon+"'>");
		out.println("<br>");
		out.println("DD <input name='dd' value='"+dd+"'>");
		out.println("MM <input name='mm' value='"+mm+"'>");
		out.println("YY <input name='yy' value='"+yy+"'>");
		out.println("<br>");
		out.println("HH <input name='h' value='"+h+"'>");
		out.println("MM <input name='m' value='"+m+"'>");
		out.println("<br>");
		out.println("PARKING IN HRS <input name='dur' value='"+dur+"'>");
		out.println("<br>");
		out.println("plan routes <input name='opt_routes' value='"+opt_routes+"'>");
		out.println("<br>");
		out.println("plan routes return also <input name='opt_routes_ret' value='"+opt_routes_ret+"'>");
		out.println("<br>");
		out.println("include meters <input name='opt_am' value='"+opt_am+"'>");
		out.println("<br>");
		out.println("include reccommendations <input name='opt_rec' value='"+opt_rec+"'>");
		out.println("<br>");
		out.println("plan routes if distance less than <input name='plan_radius' value='"+plan_radius+"'>");
		out.println("<br>");
		out.println("<input type='submit' value='plan'>");
		out.println("Notes");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
		return;
	}
	
//	ArrayList<Place> pl_home = GeoCodeByMapQuest.geocode("eerste weteringplantsoen 8 , Amsterdam, Netherlands");
//	ArrayList<Place> pl_destination = GeoCodeByMapQuest.geocode("Marco Polostraat 107 , Amsterdam, Netherlands");
//	out.println(pl_home);
//	out.println(pl_destination);
//	ar.timings.add("after lookup : " + new Long(new Date().getTime() - _exdt));

	org.joda.time.LocalDate __dt = new org.joda.time.LocalDate(Integer.parseInt(yy),Integer.parseInt(mm),Integer.parseInt(dd));
	int day = __dt.dayOfWeek().get();


	Place destination = new Place();
	destination.lat = Double.parseDouble(to_lat);
	destination.lon = Double.parseDouble(to_lon);

	String ymd = yy+"-"+mm+"-"+dd;
	String hm = h+":"+m;
	int hr = Integer.parseInt(h);
	int min = Integer.parseInt(m);
	int duration = Integer.parseInt(dur);
	double lat = destination.lat;
	double lon = destination.lon;
	String methods = "pin";
	int fmt = 2;

	String ret_ymd = yy+"-"+mm+"-"+dd;
	String ret_hm = (hr+duration)+":"+m;
	
	double plan_rad = Double.parseDouble(plan_radius);

	

//	req.options._date = "2013-12-28";
//	req.options._time = "18:00";
	com.glimworm.opendata.parkshark.xsd.ParkSharkCalcRequest req = new com.glimworm.opendata.parkshark.xsd.ParkSharkCalcRequest();
	req._day = day;
	req.hrs = hr;
	req.mins = min;
	req.duration = duration;
	req.from_lat = lat;
	req.from_lon = lon;
	req._paymethods = methods;
	req.fmt = fmt;
	
	
	com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturn prv = com.glimworm.opendata.parkshark.CalcParking.calcv2(req);

	if (tim.equalsIgnoreCase("y")) {
		for (int i=0; i < prv.timings.size(); i++) {
			ar.timings.add(prv.timings.get(i));
		}
	}
	ar.timings.add("after prv : " + new Long(new Date().getTime() - _exdt));


	/*
	out.println("start");
	for (int i=0; i < pl_destination.size(); i++) {
		out.println(xstream.toXML(pl_destination.get(i)));
	}
	out.println("end ");
	*/

	if (opt_routes.equalsIgnoreCase("y")) {
		System.out.println("NOW IN PARALLEL");
		ParallelPlanRequest ppr = new ParallelPlanRequest();
		ppr.pl_destination = destination;
		ppr.ymd = ymd;
		ppr.hm = hm;
		ppr.plan_return_also = (opt_routes_ret.equalsIgnoreCase("y"));
		ppr.ret_ymd = ret_ymd;
		ppr.ret_hm = ret_hm;
		ppr.plan_rad = plan_rad;
	
		com.glimworm.opendata.divvamsterdamapi.planning.ParallelPlan.plan(ppr, prv.reccommendations);
		System.out.println("NOW IN PARALLEL - DONE");

		ar.timings.add("after plan : " + new Long(new Date().getTime() - _exdt));
	} else {
		xstream.omitField(com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturnReccommendation.class,"reccommended_pt_route");

	}
	
	if (opt_rec.equalsIgnoreCase("y")) {
		ar.reccommendations = prv.reccommendations;
	}
	
	
	if (opt_am.equalsIgnoreCase("y")) {
		ar.meters = prv.meters;
	}

	ar._executiontime = new Date().getTime() - _exdt;

	out.println(xstream.toXML(ar));
	
	
	
//	String URL = "http://api.parkshark.nl/psapi/api.jsp";
//	String PARAMS = "day=5&hr=8&min=30&duration=3&lat=52.377&lon=4.9104&methods=cash,pin=";
//	com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse cr =  com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(URL, PARAMS, null, null, null, null, null);
	
//	System.out.println(cr.text);

//	String rv = cr.text;
//	System.out.println(rv);
//	System.out.println("parkshark::done all");
	
%>
