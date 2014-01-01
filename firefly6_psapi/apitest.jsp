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
	
	String id = gwdb.gwUtils.get(request,"id","");
	String idg = gwdb.gwUtils.get(request,"idg","5");
	String idm = gwdb.gwUtils.get(request,"idm","11328");
	String addr = gwdb.gwUtils.get(request,"addr","Marco Polostraat 107 , Amsterdam, Netherlands");
	
	
	if (action.equalsIgnoreCase("")) {
		
		out.println("<html>");
		out.println("<head>");
		out.println("<link href='bootstrap/css/bootstrap.css' rel='stylesheet'>");
		out.println("<link rel='stylesheet' href='apitest.css' />");
		out.println("</head>");
		out.println("<body>");
		out.println(readFileCR(application.getRealPath("/")+"/apitest.top.html"));
		out.println("<h2>Basic API</h2>");
		out.println("<form class='form-horizontal'>");
		out.println("<input type='hidden' name='action' value='plan'>");
		out.println("<div class='form-inline'>");
		out.println("<label class='long'>LAT</label><input class='long' name='to_lat' value='"+to_lat+"'>");
		out.println("<label class='short'>LON</label><input class='long' name='to_lon' value='"+to_lon+"'>");
		out.println("</div>");
		out.println("<div class='form-inline'>");
		out.println("<label class='long'>DD</label><input name='dd' value='"+dd+"'>");
		out.println("<label class='short'>MM</label><input name='mm' value='"+mm+"'>");
		out.println("<label class='short'>YY</label><input name='yy' value='"+yy+"'>");
		out.println("</div>");
		out.println("<div class='form-inline'>");
		out.println("<label class='long'>HH</label><input name='h' value='"+h+"'>");
		out.println("<label class='short'>MM</label><input name='m' value='"+m+"'>");
		out.println("</div>");
		out.println("<div class='form-inline'>");
		out.println("<label class='long'>PARKING IN HRS</label><input name='dur' value='"+dur+"'>");
		out.println("</div>");
		out.println("<div class='form-inline'>");
		out.println("<label class='long'>plan routes</label><input name='opt_routes' value='"+opt_routes+"'>");
		out.println("</div>");
		out.println("<div class='form-inline'>");
		out.println("<label class='long'>plan routes return also</label><input name='opt_routes_ret' value='"+opt_routes_ret+"'>");
		out.println("</div>");
		out.println("<div class='form-inline'>");
		out.println("<label class='long'>include meters</label><input name='opt_am' value='"+opt_am+"'>");
		out.println("</div>");
		out.println("<div class='form-inline'>");
		out.println("<label class='long'>include reccommendations</label><input name='opt_rec' value='"+opt_rec+"'>");
		out.println("</div>");
		out.println("<div class='form-inline'>");
		out.println("<label class='long'>plan routes if distance less than</label><input name='plan_radius' value='"+plan_radius+"'>");
		out.println("<input type='submit' value='plan'>");
		out.println("</div>");
		out.println("</form>");
		out.println("<h2>Alternative calls</h2>");

		out.println("<form class='form-inline'>");
		out.println("<input type='hidden' name='action' value='get-meter-by-automat-number'>");
		out.println("<label class='url'>get-meter-by-automat-number&id=</label><input name='id' value='"+idm+"'>");
		out.println("<input type='submit' value='get details'>");
		out.println("</form>");

		out.println("<form class='form-inline'>");
		out.println("<input type='hidden' name='action' value='get-garage-by-id'>");
		out.println("<label class='url'>get-garage-by-id&id=</label><input name='id' value='"+idg+"'>");
		out.println("<input type='submit' value='get details'>");
		out.println("</form>");

		out.println("<form class='form-inline'>");
		out.println("<input type='hidden' name='action' value='geocode'>");
		out.println("<label class='url'>geocode&addr=</label><input class='vlong' name='addr' value='"+addr+"'>");
		out.println("<input type='submit' value='geocode'>");
		out.println("</form>");
		
		out.println("");
		out.println(readFileCR(application.getRealPath("/")+"/apitest.bot.html"));
		out.println("</body>");
		out.println("</html>");
		return;
	}
	if (action.equalsIgnoreCase("get-meter-by-automat-number")) {
		ar.meter = com.glimworm.opendata.parkshark.CalcParking.getMeterById(id);
		ar.reccommendations = null;
		ar._executiontime = new Date().getTime() - _exdt;
		out.println(xstream.toXML(ar));
		return;
	}
	if (action.equalsIgnoreCase("get-garage-by-id")) {
		int _id = Integer.parseInt(id);
		ar.garage = com.glimworm.opendata.parkshark.CalcParking.getGarageByGarageid(_id);
		ar.reccommendations = null;
		ar._executiontime = new Date().getTime() - _exdt;
		out.println(xstream.toXML(ar));
		return;
		
	}

	if (action.equalsIgnoreCase("geocode")) {
		ar.places = GeoCodeByMapQuest.geocode(addr);
		ar._executiontime = new Date().getTime() - _exdt;
		out.println(xstream.toXML(ar));
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
	
	if (opt_routes_ret.equalsIgnoreCase("y") == false ) {
		xstream.omitField(com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturnReccommendation.class,"reccommended_pt_route_return");
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
	
%><%!

public static String readFileCR(String F) {

	StringBuffer sb = new StringBuffer();

	File f = new File(F);
	System.out.println("readFileCR" + F);
	try {
		System.out.println("readFileCR" + f.getCanonicalPath());
		System.out.println("readFileCR" + f.getCanonicalFile().getAbsolutePath());
		if (!f.exists()) return null;

		System.out.println("readFileCR exists" + F);


		FileReader fr = new FileReader(f.getCanonicalFile());
		BufferedReader in = new BufferedReader(fr);

		String line = null;

		while ((line = in.readLine()) != null) {
			sb.append(line+"\n");
		}

		in.close();
		fr.close();

	} catch (Exception E) {
		System.out.println(" gwExtras : readFile : " + E.getMessage());
		return null;
	}

	return sb.toString();

}
%>
