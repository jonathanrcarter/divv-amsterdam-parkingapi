package com.glimworm.opendata.divvamsterdamapi.planning.xsd;

public class PlaceParkingGarage  extends Place {
	

	public int places = 0;
	
	public String owner = "";	// Q-park
	public String remarks = "";
	public int capacity = 0;
	public double price_day = 0;
	public int free_minutes = 0;
	public String service_open_in = "0:00";
	public String service_close_in = "0:00";
	public String service_open_out = "0:00";
	public String service_close_out = "0:00";
	public int time_unit_minutes = 0;
	public double price_per_time_unit = 0;
	public String includes_public_transport = "n";
	public String calc_type = "garage";
	
	
	public PlaceParkingGarage() {
	}
    
    public String toString() {
    	return super.toString() + " places : "+ places;
    }
	
}
