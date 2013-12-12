package com.glimworm.opendata.parkshark.xsd;

public class ParkSharkCalcReturnReccommendation {
	public double dist = 0;
	public double cost = 0;
	public String belnummer = "";
	public String address = "";
	public double lat = 0;
	public double lon = 0;
	public int i = 0;
	public String type = "";
	public String dbg = "";
	public String parking_chance = "";
	public String chance_weekday = "";
	public String chance_sat = "";
	public String chance_sun = "";
	

	public ParkSharkCalcReturnReccommendation_ptroute reccommended_pt_route = new ParkSharkCalcReturnReccommendation_ptroute();
	public ParkSharkCalcReturnReccommendation_ptroute reccommended_pt_route_return = new ParkSharkCalcReturnReccommendation_ptroute();

}
