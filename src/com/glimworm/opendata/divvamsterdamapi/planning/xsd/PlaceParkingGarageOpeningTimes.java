package com.glimworm.opendata.divvamsterdamapi.planning.xsd;

public class PlaceParkingGarageOpeningTimes {
	public int dayOfWeek = 0;
	public String open_in = "0000";
	public String open_out = "0000";
	public String close_in = "0000";
	public String close_out = "0000";
	
	public String raw() {
		return open_in+" "+open_out+" "+close_in+" "+close_out;
	}
	public String json() {
		return "{\"open_in\":\""+open_in+"\",\"open_out\":\""+open_out+"\",\"close_in\":\""+close_in+"\",\"close_out\":\""+close_out+"\"}";
	}
	
	public boolean open24() {
		if (open_in.equals("0000") && open_out.equals("0000") && close_in.equals("0000") && close_out.equals("0000")) return true;
		return false;
	}
	public boolean closed24() {
		if (open_in.equals("9999") && open_out.equals("9999") && close_in.equals("9999") && close_out.equals("9999")) return true;
		return false;
	}

}
