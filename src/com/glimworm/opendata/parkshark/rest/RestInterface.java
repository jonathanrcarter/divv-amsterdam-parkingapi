package com.glimworm.opendata.parkshark.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.glimworm.opendata.divvamsterdamapi.planning.GeoCodeByMapQuest;
import com.glimworm.opendata.divvamsterdamapi.planning.PlanOtp;
import com.glimworm.opendata.divvamsterdamapi.planning.PlanRequest;
import com.glimworm.opendata.divvamsterdamapi.planning.PlanResponse;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.ParallelPlanRequest;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Place;
import com.glimworm.opendata.parkshark.importdata.NPR.xsd.AreaListItem;
import com.vividsolutions.jts.geom.Polygon;

@Path("")
public class RestInterface {
	 
	@GET
	@Path("/{param}")
	public Response getMsg(
			@PathParam("param") String msg) {

		String output = "Jersey say : " + msg;
 
		return Response.status(200).entity(output).build();
 
	}

	@GET
	@Path("/plann")
	public Response getPlan(
			@QueryParam("to_lat") String to_lat) {

		if (to_lat == null || to_lat.trim().length() == 0) to_lat = "52.368104267594056";
		String output = "Jersey say : plan to " + to_lat;
		return Response.status(200).entity(output).build();
 
	}

	@GET
	@Path("/admin/reload")
	@Produces("application/json")
	public Response Plan(
			@QueryParam("key") @DefaultValue("") String key,
			@QueryParam("debug") @DefaultValue("n") String debug) {

		class resp {
			public int status = 0;
			public String statusMsg = "";
			public Object errors;
		}
		resp res = new resp();
		if (key == null || key.equalsIgnoreCase(get_from_file("key","")) == false) {
			res.status = 100;
			res.statusMsg = "Un Authorized";
			return Response.status(200).entity(getStandardXstream(debug,resp.class).toXML(res)).build();
		}
		com.glimworm.opendata.parkshark.CalcParking.populate_meters();
		res.status = 0;
		res.statusMsg = "Reloaded";
		res.errors = com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam._importerrors;
		return Response.status(200).entity(getStandardXstream(debug,resp.class).toXML(res)).build();
	}

	class file {
		public String filename = "";
		public String contents = "";
		file (String F, String C) {
			filename = F;
			contents = C;
		}
	}
	
	
	@GET
	@Path("/admin/getfiles")
	@Produces("application/json")
	public Response GetFiles(
			@QueryParam("key") @DefaultValue("") String key,
			@QueryParam("debug") @DefaultValue("n") String debug) {

		class resp {
			public int status = 0;
			public String statusMsg = "";
			public ArrayList<file> files = new ArrayList<file>();
			public ArrayList<Exception> errors;
			public ArrayList<String> imports;
		}
		resp res = new resp();
		if (key == null || key.equalsIgnoreCase(get_from_file("key","")) == false) {
			res.status = 100;
			res.statusMsg = "Un Authorized";
			return Response.status(200).entity(getStandardXstream(debug,resp.class).toXML(res)).build();
		}
		com.glimworm.opendata.parkshark.CalcParking.populate_meters();
		res.status = 0;
		res.statusMsg = "Reloaded";
		res.errors = com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam._importerrors;
		res.imports = com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam._importlog;
		String[] files = {"/opt/tmp/rdw/upload/additionaldata/newgarages/garages.json"};
		for (String _file : files) {
			res.files.add(new file(_file,com.glimworm.opendata.divvamsterdamapi.planning.net.FileUtils.readFile(_file)));
		}
		return Response.status(200).entity(getStandardXstream(debug,resp.class).toXML(res)).build();
	}
	
	
	

	@GET
	@Path("/plan")
	@Produces("application/json")
	public Response Plan(
			@QueryParam("opt_am") @DefaultValue("n") String opt_am,
			@QueryParam("opt_routes") @DefaultValue("y") String opt_routes,
			@QueryParam("opt_routes_ret") @DefaultValue("n") String opt_routes_ret,
			@QueryParam("opt_rec") @DefaultValue("y") String opt_rec,
			@QueryParam("tim") @DefaultValue("n") String tim,
			@QueryParam("plan_radius") @DefaultValue("2000") String plan_radius,
			@QueryParam("opt_otp_timout") @DefaultValue("3000") String opt_otp_timout,
			@QueryParam("opt_otp_server") @DefaultValue("0") String opt_otp_server,
			@QueryParam("opt_otp_walkspeed") @DefaultValue("1.3888") String opt_otp_walkspeed,
			@QueryParam("log") @DefaultValue("n") String log,
			@QueryParam("opt_metercount") @DefaultValue("") String opt_metercount,
			@QueryParam("opt_garagecount") @DefaultValue("") String opt_garagecount,
			@QueryParam("opt_prcount") @DefaultValue("") String opt_prcount,
			@QueryParam("opt_maxresults") @DefaultValue("") String opt_maxresults,
			@QueryParam("dur") @DefaultValue("2") String dur,
			@QueryParam("h") @DefaultValue("12") String h,
			@QueryParam("m") @DefaultValue("50") String m,
			@QueryParam("dd") @DefaultValue("1") String dd,
			@QueryParam("mm") @DefaultValue("12") String mm,
			@QueryParam("yy") @DefaultValue("2015") String yy,
			@QueryParam("to_lon") @DefaultValue("4.856208655327167") String to_lon,
			@QueryParam("to_lat") @DefaultValue("52.368104267594056") String to_lat,
			@QueryParam("opt_include_unmatched") @DefaultValue("n") String opt_include_unmatched,
			@QueryParam("debug") @DefaultValue("n") String debug) {

		
		
		/**
		 * standard block
		 */
		long _exdt = new Date().getTime();
		com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse ar = new com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse();
		org.joda.time.LocalDate ___dt = new org.joda.time.LocalDate();
		
		
		org.joda.time.LocalDate __dt = new org.joda.time.LocalDate(Integer.parseInt(yy),Integer.parseInt(mm),Integer.parseInt(dd));
		int day = __dt.dayOfWeek().get();
		
		System.out.println(dd+"/"+mm+"/"+yy+"DOW "+day);
		/* joda goes 1..7 and we need 0..6 with 0 being sunday */
		if (day == 7) day = 0;


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
		int opt_otp_tim = Integer.parseInt(opt_otp_timout);
		
		

//		req.options._date = "2013-12-28";
//		req.options._time = "18:00";
		com.glimworm.opendata.parkshark.xsd.ParkSharkCalcRequest req = new com.glimworm.opendata.parkshark.xsd.ParkSharkCalcRequest();
		req._day = day;
		req.hrs = hr;
		req.mins = min;
		req.duration = duration;
		req.from_lat = lat;
		req.from_lon = lon;
		req._paymethods = methods;
		req.fmt = fmt;
		req.log = log;
		req.dbg = debug;
		req.opt_garagecount = opt_garagecount;
		req.opt_metercount = opt_metercount;
		req.opt_prcount = opt_prcount;
		req.opt_maxresults = opt_maxresults;
		req.opt_include_unmatched = opt_include_unmatched;
		
		
		com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturn prv = com.glimworm.opendata.parkshark.CalcParking.calcv2(req);

		if (tim.equalsIgnoreCase("y")) {
			for (int i=0; i < prv.timings.size(); i++) {
				ar.timings.add(prv.timings.get(i));
			}
		}
		ar.timings.add("after prv : " + new Long(new Date().getTime() - _exdt));
		ar.isInPaidParkingAmsterdam = com.glimworm.opendata.parkshark.CalcParking.isInPaidParking(lat,lon) ? "y" : "n";
		ar.isInAmsterdamCentrum =  com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.isin(lat, lon, com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.inCentrum.polys) ? "y" : "n";
		ar.isInAmsterdamWiderArea =  com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.isin(lat, lon, com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.inAmsterdam.polys) ? "y" : "n";


		com.thoughtworks.xstream.XStream xstream = getStandardXstream(debug);
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
			ppr.opt_otp_tim = opt_otp_tim;
			ppr.opt_otp_server = Integer.parseInt(opt_otp_server);
			ppr.opt_otp_walkspeed = Double.parseDouble(opt_otp_walkspeed);
		
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
		
		return Response.status(200).entity(xstream.toXML(ar)).build();
		
	}
	
	
	
	@GET
	@Path("/plan/callback")
	@Produces("application/json")
	public Response PlanWithCallback (
			@QueryParam("opt_am") @DefaultValue("n") String opt_am,
			@QueryParam("opt_routes") @DefaultValue("y") String opt_routes,
			@QueryParam("opt_routes_ret") @DefaultValue("n") String opt_routes_ret,
			@QueryParam("opt_rec") @DefaultValue("y") String opt_rec,
			@QueryParam("tim") @DefaultValue("n") String tim,
			@QueryParam("plan_radius") @DefaultValue("2000") String plan_radius,
			@QueryParam("opt_otp_timout") @DefaultValue("3000") String opt_otp_timout,
			@QueryParam("opt_otp_server") @DefaultValue("0") String opt_otp_server,
			@QueryParam("opt_otp_walkspeed") @DefaultValue("1.3888") String opt_otp_walkspeed,
			@QueryParam("log") @DefaultValue("n") String log,
			@QueryParam("opt_metercount") @DefaultValue("") String opt_metercount,
			@QueryParam("opt_garagecount") @DefaultValue("") String opt_garagecount,
			@QueryParam("opt_prcount") @DefaultValue("") String opt_prcount,
			@QueryParam("opt_maxresults") @DefaultValue("") String opt_maxresults,
			@QueryParam("dur") @DefaultValue("2") String dur,
			@QueryParam("h") @DefaultValue("12") String h,
			@QueryParam("m") @DefaultValue("50") String m,
			@QueryParam("dd") @DefaultValue("1") String dd,
			@QueryParam("mm") @DefaultValue("12") String mm,
			@QueryParam("yy") @DefaultValue("2015") String yy,
			@QueryParam("to_lon") @DefaultValue("4.856208655327167") String to_lon,
			@QueryParam("to_lat") @DefaultValue("52.368104267594056") String to_lat,
			@QueryParam("callback") @DefaultValue("callback") String callback,
			@QueryParam("debug") @DefaultValue("n") String debug) {

		
		
		/**
		 * standard block
		 */
		long _exdt = new Date().getTime();
		com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse ar = new com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse();
		org.joda.time.LocalDate ___dt = new org.joda.time.LocalDate();
		
		
		org.joda.time.LocalDate __dt = new org.joda.time.LocalDate(Integer.parseInt(yy),Integer.parseInt(mm),Integer.parseInt(dd));
		int day = __dt.dayOfWeek().get();
		
		System.out.println(dd+"/"+mm+"/"+yy+"DOW "+day);
		/* joda goes 1..7 and we need 0..6 with 0 being sunday */
		if (day == 7) day = 0;


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
		int opt_otp_tim = Integer.parseInt(opt_otp_timout);
		
		

//		req.options._date = "2013-12-28";
//		req.options._time = "18:00";
		com.glimworm.opendata.parkshark.xsd.ParkSharkCalcRequest req = new com.glimworm.opendata.parkshark.xsd.ParkSharkCalcRequest();
		req._day = day;
		req.hrs = hr;
		req.mins = min;
		req.duration = duration;
		req.from_lat = lat;
		req.from_lon = lon;
		req._paymethods = methods;
		req.fmt = fmt;
		req.log = log;
		req.dbg = debug;
		req.opt_garagecount = opt_garagecount;
		req.opt_metercount = opt_metercount;
		req.opt_prcount = opt_prcount;
		req.opt_maxresults = opt_maxresults;
		
		
		com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturn prv = com.glimworm.opendata.parkshark.CalcParking.calcv2(req);

		if (tim.equalsIgnoreCase("y")) {
			for (int i=0; i < prv.timings.size(); i++) {
				ar.timings.add(prv.timings.get(i));
			}
		}
		ar.timings.add("after prv : " + new Long(new Date().getTime() - _exdt));
		ar.isInPaidParkingAmsterdam = com.glimworm.opendata.parkshark.CalcParking.isInPaidParking(lat,lon) ? "y" : "n";
		ar.isInAmsterdamCentrum =  com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.isin(lat, lon, com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.inCentrum.polys) ? "y" : "n";
		ar.isInAmsterdamWiderArea =  com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.isin(lat, lon, com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.inAmsterdam.polys) ? "y" : "n";


		com.thoughtworks.xstream.XStream xstream = getStandardXstream(debug);
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
			ppr.opt_otp_tim = opt_otp_tim;
			ppr.opt_otp_server = Integer.parseInt(opt_otp_server);
			ppr.opt_otp_walkspeed = Double.parseDouble(opt_otp_walkspeed);
		
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
		
		return Response.status(200).entity(""+callback+"("+xstream.toXML(ar)+");").build();
		
	}	
	

	@GET
	@Path("/get-meter-by-automat-number/{param}")
	@Produces("application/json")
	public Response getMeterByNumber(
			@PathParam("param") @DefaultValue("") String id,
			@QueryParam("debug") @DefaultValue("n") String debug) {

		long _exdt = new Date().getTime();

		com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse ar = new com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse();

		org.joda.time.LocalDate ___dt = new org.joda.time.LocalDate();
		
		ar.meter = com.glimworm.opendata.parkshark.CalcParking.getMeterById(id);
		ar.reccommendations = null;
		ar._executiontime = new Date().getTime() - _exdt;
		return Response.status(200).entity(getStandardXstream(debug).toXML(ar)).build();
		
	}

	
	@GET
	@Path("/get-meter-by-automat-number/{param}/callback")
	@Produces("application/javascript")
	public Response getMeterByNumberWithCallback(
			@PathParam("param") @DefaultValue("") String id,
			@QueryParam("callback") @DefaultValue("callback") String callback,
			@QueryParam("debug") @DefaultValue("n") String debug) {
		
		

		/**
		 * standard block
		 */
		long _exdt = new Date().getTime();
		com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse ar = new com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse();
		org.joda.time.LocalDate ___dt = new org.joda.time.LocalDate();
		
		ar.meter = com.glimworm.opendata.parkshark.CalcParking.getMeterById(id);
		ar.reccommendations = null;
		ar._executiontime = new Date().getTime() - _exdt;
		return Response.status(200).entity(""+callback+"("+getStandardXstream(debug).toXML(ar)+");").build();
		
	}
	
	@GET
	@Path("/get-meter-by-nprid/{param}")
	@Produces("application/json")
	public Response getMeterByNPRid(
			@PathParam("param") @DefaultValue("") String id,
			@QueryParam("debug") @DefaultValue("n") String debug) {

		long _exdt = new Date().getTime();

		com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse ar = new com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse();

		org.joda.time.LocalDate ___dt = new org.joda.time.LocalDate();
		
		ar.meter = com.glimworm.opendata.parkshark.CalcParking.getMeterByNPRid(id);
		ar.reccommendations = null;
		ar._executiontime = new Date().getTime() - _exdt;
		return Response.status(200).entity(getStandardXstream(debug).toXML(ar)).build();
		
	}

	
	@GET
	@Path("/get-meter-by-nprid/{param}/callback")
	@Produces("application/javascript")
	public Response getMeterByNPRidWithCallback(
			@PathParam("param") @DefaultValue("") String id,
			@QueryParam("callback") @DefaultValue("callback") String callback,
			@QueryParam("debug") @DefaultValue("n") String debug) {
		
		

		/**
		 * standard block
		 */
		long _exdt = new Date().getTime();
		com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse ar = new com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse();
		org.joda.time.LocalDate ___dt = new org.joda.time.LocalDate();
		
		ar.meter = com.glimworm.opendata.parkshark.CalcParking.getMeterByNPRid(id);
		ar.reccommendations = null;
		ar._executiontime = new Date().getTime() - _exdt;
		return Response.status(200).entity(""+callback+"("+getStandardXstream(debug).toXML(ar)+");").build();
		
	}	
	
	@GET
	@Path("/get-garage-by-id/{param}")
	@Produces("application/json")
	public Response getGarageById(
			@PathParam("param") @DefaultValue("") String id,
			@QueryParam("debug") @DefaultValue("n") String debug) {
	

		/**
		 * standard block
		 */
		long _exdt = new Date().getTime();
		com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse ar = new com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse();
		org.joda.time.LocalDate ___dt = new org.joda.time.LocalDate();

		
		int _id = Integer.parseInt(id);
		
		ar.garage = com.glimworm.opendata.parkshark.CalcParking.getGarageByGarageid(_id);
		ar.reccommendations = null;
		ar._executiontime = new Date().getTime() - _exdt;
		return Response.status(200).entity(getStandardXstream(debug).toXML(ar)).build();
	}

	@GET
	@Path("/get-garage-by-id/{param}/callback")
	@Produces("application/javascript")
	public Response getGarageByIdWithCallback(
			@PathParam("param") @DefaultValue("") String id,
			@QueryParam("callback") @DefaultValue("callback") String callback,
			@QueryParam("debug") @DefaultValue("n") String debug) {
	

		/**
		 * standard block
		 */
		long _exdt = new Date().getTime();
		com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse ar = new com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse();
		org.joda.time.LocalDate ___dt = new org.joda.time.LocalDate();

		
		int _id = Integer.parseInt(id);
		
		ar.garage = com.glimworm.opendata.parkshark.CalcParking.getGarageByGarageid(_id);
		ar.reccommendations = null;
		ar._executiontime = new Date().getTime() - _exdt;
		return Response.status(200).entity(""+callback+"("+getStandardXstream(debug).toXML(ar)+");").build();
	}	
	
	@GET
	@Path("/get-garage-by-nprid/{param}")
	@Produces("application/json")
	public Response getGarageByNPRid(
			@PathParam("param") @DefaultValue("") String id,
			@QueryParam("debug") @DefaultValue("n") String debug) {
	

		/**
		 * standard block
		 */
		long _exdt = new Date().getTime();
		com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse ar = new com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse();
		org.joda.time.LocalDate ___dt = new org.joda.time.LocalDate();

		
		ar.garage = com.glimworm.opendata.parkshark.CalcParking.getGarageByNPRid(id);
		ar.reccommendations = null;
		ar._executiontime = new Date().getTime() - _exdt;
		return Response.status(200).entity(getStandardXstream(debug).toXML(ar)).build();
	}

	@GET
	@Path("/get-garage-by-nprid/{param}/callback")
	@Produces("application/javascript")
	public Response getGarageByNPRidWithCallback(
			@PathParam("param") @DefaultValue("") String id,
			@QueryParam("callback") @DefaultValue("callback") String callback,
			@QueryParam("debug") @DefaultValue("n") String debug) {
	

		/**
		 * standard block
		 */
		long _exdt = new Date().getTime();
		com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse ar = new com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse();
		org.joda.time.LocalDate ___dt = new org.joda.time.LocalDate();
		
		ar.garage = com.glimworm.opendata.parkshark.CalcParking.getGarageByNPRid(id);
		ar.reccommendations = null;
		ar._executiontime = new Date().getTime() - _exdt;
		return Response.status(200).entity(""+callback+"("+getStandardXstream(debug).toXML(ar)+");").build();
	}		
	

	@GET
	@Path("/list-meters")
	@Produces("application/json")
	public Response listMeters(
			@QueryParam("debug") @DefaultValue("n") String debug) {

		return Response.status(200).entity(getStandardXstream(debug).toXML(com.glimworm.opendata.parkshark.CalcParking.smeters)).build();
	}

	@GET
	@Path("/list-meters-in-box/{lat1}/{lat2}/{lon1}/{lon2}")
	@Produces("application/json")
	public Response listMetersInBox(
			@PathParam("lat1") @DefaultValue("56") Double lat1,
			@PathParam("lat2") @DefaultValue("52") Double lat2,
			@PathParam("lon1") @DefaultValue("4") Double lon1,
			@PathParam("lon2") @DefaultValue("6") Double lon2,
			@QueryParam("debug") @DefaultValue("n") String debug) {

		return Response.status(200).entity(getStandardXstream(debug).toXML(com.glimworm.opendata.parkshark.CalcParking.getMeters(lat1,lat2,lon1,lon2))).build();
	}

	@GET
	@Path("/list-garages")
	@Produces("application/json")
	public Response listGarages(
			@QueryParam("debug") @DefaultValue("n") String debug) {

		return Response.status(200).entity(getStandardXstream(debug).toXML(com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.getGarages())).build();
	}

	@GET
	@Path("/list-loaded-garages")
	@Produces("application/json")
	public Response listLoadedGarages(
			@QueryParam("debug") @DefaultValue("n") String debug) {

		return Response.status(200).entity(getStandardXstream(debug).toXML(com.glimworm.opendata.parkshark.CalcParking.sgarages)).build();
	}


	@GET
	@Path("/geocode/{param}")
	@Produces("application/json")
	public Response listLoadedGarages(
			@PathParam("param") @DefaultValue("Marco Polostraat 107 , Amsterdam, Netherlands") String addr,
			@QueryParam("debug") @DefaultValue("n") String debug) {

		/**
		 * standard block
		 */
		long _exdt = new Date().getTime();
		com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse ar = new com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse();
		org.joda.time.LocalDate ___dt = new org.joda.time.LocalDate();

		ar.places = GeoCodeByMapQuest.geocode(addr);
		ar._executiontime = new Date().getTime() - _exdt;
		
		return Response.status(200).entity(getStandardXstream(debug).toXML(com.glimworm.opendata.parkshark.CalcParking.sgarages)).build();
	}

	
	@GET
	@Path("/otp-proxy")
	@Produces("application/json")
	public Response otpProxy(
			@QueryParam("params") @DefaultValue("") String params,
			@QueryParam("debug") @DefaultValue("n") String debug) {

		/**
		 * standard block
		 */
		long _exdt = new Date().getTime();
		com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse ar = new com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse();
		org.joda.time.LocalDate ___dt = new org.joda.time.LocalDate();

		
		PlanRequest req = new PlanRequest();
		req.PARAMS = params;
		PlanResponse pr = PlanOtp.plan(req);
		ar.ptroute = new com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturnReccommendation_ptroute();
		com.glimworm.opendata.divvamsterdamapi.planning.ParallelPlanCallable.convertOtpToReccommendation(ar.ptroute,pr,false);
		
		return Response.status(200).entity(getStandardXstream(debug).toXML(ar)).build();
	}	
	
	
	@GET
	@Path("/otp-proxy/callback")
	@Produces("application/javascript")
	public Response otpProxyWithCallback(
			@QueryParam("params") @DefaultValue("") String params,
			@QueryParam("callback") @DefaultValue("callback") String callback,
			@QueryParam("debug") @DefaultValue("n") String debug) {

		/**
		 * standard block
		 */
		long _exdt = new Date().getTime();
		com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse ar = new com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse();
		org.joda.time.LocalDate ___dt = new org.joda.time.LocalDate();

		
		PlanRequest req = new PlanRequest();
		req.PARAMS = params;
		PlanResponse pr = PlanOtp.plan(req);
		ar.ptroute = new com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturnReccommendation_ptroute();
		com.glimworm.opendata.divvamsterdamapi.planning.ParallelPlanCallable.convertOtpToReccommendation(ar.ptroute,pr,false);
		
		return Response.status(200).entity(""+callback+"("+getStandardXstream(debug).toXML(ar)+");").build();
	}		

	@GET
	@Path("/get-point/{params_lat}/{params_lng}")
	@Produces("application/javascript")
	public Response getPoint(
			@PathParam("params_lat") @DefaultValue("52.37") Double lat,
			@PathParam("params_lng") @DefaultValue("4.926") Double lng,
			@QueryParam("callback") @DefaultValue("callback") String callback,
			@QueryParam("debug") @DefaultValue("n") String debug) {
		
		class resp {
			public boolean isInNorth = false;
			public boolean isInPaidParking = false;
			public AreaListItem gemeentegrens = null;
			public AreaListItem centrumzone = null;
			
		}
		resp res = new resp();
		res.isInPaidParking = com.glimworm.opendata.parkshark.CalcParking.isInPaidParking(lat, lng);
		res.isInNorth = com.glimworm.opendata.parkshark.CalcParking.isInNorth(lat, lng);
		
		if (debug.equalsIgnoreCase("y")) {
			res.gemeentegrens = com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.loadgeojson("gemeentegrens","y");
			res.centrumzone = com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.loadgeojson("centrumzone","y");
		}


		return Response.status(200).entity(getStandardXstream(debug,resp.class).toXML(res)).build();
	}		
	
	@GET
	@Path("/data-polys-paidparking")
	@Produces("application/javascript")
	public Response getPolysPaidParking(
			@QueryParam("callback") @DefaultValue("callback") String callback,
			@QueryParam("debug") @DefaultValue("n") String debug) {
		
		class resp {
			public ArrayList<Polygon> polys = null;
		}
		resp res = new resp();
		res.polys = com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.inPaidParking.polys;

		return Response.status(200).entity(getStandardXstream(debug,resp.class).toXML(res)).build();
	}	

	@GET
	@Path("/data-areas")
	@Produces("application/javascript")
	public Response getAreas(
			@QueryParam("callback") @DefaultValue("callback") String callback,
			@QueryParam("debug") @DefaultValue("n") String debug) {
		
		class resp {
			public AreaListItem inAmsterdam = null;
			public AreaListItem inPaidParking = null;
			public AreaListItem inCentrum = null;
		}
		resp res = new resp();
		res.inAmsterdam = com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.inAmsterdam;
		res.inPaidParking = com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.inPaidParking;
		res.inCentrum = com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.inCentrum;

		return Response.status(200).entity(getStandardXstream(debug,resp.class).toXML(res)).build();
	}		
	
	private static com.thoughtworks.xstream.XStream getStandardXstream (String debug) {
		return getStandardXstream(debug,null);
	}
	private static com.thoughtworks.xstream.XStream getStandardXstream (String debug, Class resultClass) {
		
		com.thoughtworks.xstream.XStream xstream = new com.thoughtworks.xstream.XStream(new com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver());
		xstream.setMode(com.thoughtworks.xstream.XStream.NO_REFERENCES);
		xstream.omitField(com.glimworm.opendata.divvamsterdamapi.planning.xsd.MMdatetime.class,"dt");
		xstream.alias("result",com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse.class);
		
		if (resultClass != null) {
			xstream.alias("result",resultClass);
		}
		
		

		xstream.omitField(com.glimworm.opendata.parkshark.xsd.Meter.class,"chance_weekday");
		xstream.omitField(com.glimworm.opendata.parkshark.xsd.Meter.class,"chance_sat");
		xstream.omitField(com.glimworm.opendata.parkshark.xsd.Meter.class,"chance_sun");
		if (debug != null && debug.equalsIgnoreCase("y") == false) {
			xstream.omitField(com.glimworm.opendata.parkshark.xsd.Meter.class,"dbg");
		}
		
		xstream.omitField(com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturnReccommendation.class,"chance_weekday");
		xstream.omitField(com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturnReccommendation.class,"chance_sat");
		xstream.omitField(com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturnReccommendation.class,"chance_sun");
		if (debug != null && debug.equalsIgnoreCase("y") == false) {
			xstream.omitField(com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturnReccommendation.class,"dbg");
		}
		return xstream;
	}

	public static String get_from_file(String S, String DEF) {
		System.out.println("Application::returnung from file Key["+S+"]");

		String cfile = "/opt/tmp/rdw/upload/settings.txt";
		
		String SS = com.glimworm.opendata.divvamsterdamapi.planning.net.FileUtils.readFileCR(cfile);
		if (SS == null) return DEF;
		for (String SP : SS.split("[\n]")) {
			if (SP.startsWith(S+"=")) {
				try {
					return (SP.split("[=]",2)[1]);
				} catch (Exception E) {
					E.printStackTrace(System.out);
					return DEF;
				}
			}
		}
		return DEF;		
	}

	 
}
