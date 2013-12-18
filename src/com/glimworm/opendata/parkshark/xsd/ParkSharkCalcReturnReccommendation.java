package com.glimworm.opendata.parkshark.xsd;

public class ParkSharkCalcReturnReccommendation {
	public double dist_in_meters = 0;
	public double cost = 0;
	public String name = "";
	public String automat_number = "";
	public String address = "";
	public double lat = 0;
	public double lon = 0;
	public int i = 0;
	public String type = "";
	public String dbg = "";
	public String expected_occupancy = "";
	public String chance_weekday = "";
	public String chance_sat = "";
	public String chance_sun = "";
	public String garage_type = "";
	public String notes = "";
	public String garage_opening_hours = "";
	public String garage_owner = "";
	public String garage_infourl = "";
	public String garage_includes_public_transport = "";
	

	public ParkSharkCalcReturnReccommendation_ptroute reccommended_pt_route = new ParkSharkCalcReturnReccommendation_ptroute();
	public ParkSharkCalcReturnReccommendation_ptroute reccommended_pt_route_return = new ParkSharkCalcReturnReccommendation_ptroute();

}
