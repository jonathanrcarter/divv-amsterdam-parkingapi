package com.glimworm.opendata.parkshark.rest;

import java.util.Date;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

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
	@Path("/plan")
	public Response getPlan(
			@QueryParam("to_lat") String to_lat) {

		if (to_lat == null || to_lat.trim().length() == 0) to_lat = "52.368104267594056";
		
		String output = "Jersey say : plan to " + to_lat;
 
		return Response.status(200).entity(output).build();
 
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
		
		

		long _exdt = new Date().getTime();

		com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse ar = new com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse();

		org.joda.time.LocalDate ___dt = new org.joda.time.LocalDate();
		
		ar.meter = com.glimworm.opendata.parkshark.CalcParking.getMeterById(id);
		ar.reccommendations = null;
		ar._executiontime = new Date().getTime() - _exdt;
		return Response.status(200).entity(""+callback+"("+getStandardXstream(debug).toXML(ar)+");").build();
		
	}
	

	
	
	private static com.thoughtworks.xstream.XStream getStandardXstream (String debug) {
		
		com.thoughtworks.xstream.XStream xstream = new com.thoughtworks.xstream.XStream(new com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver());
		xstream.setMode(com.thoughtworks.xstream.XStream.NO_REFERENCES);
		xstream.omitField(com.glimworm.opendata.divvamsterdamapi.planning.xsd.MMdatetime.class,"dt");
		xstream.alias("result",com.glimworm.opendata.divvamsterdamapi.planning.xsd.apiResponse.class);

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
	
	 
}
