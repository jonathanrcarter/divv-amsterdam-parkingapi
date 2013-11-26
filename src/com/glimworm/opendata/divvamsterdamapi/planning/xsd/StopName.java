package com.glimworm.opendata.divvamsterdamapi.planning.xsd;

public class StopName {
	
	public String stopname = "";
	public MMdatetime scheduled_time_at_stop = null;
	public String name = "";
	public double lat,lon = 0;
	
	public StopName() {
		scheduled_time_at_stop = new MMdatetime();
	}
    
    public String toString() {
            return "stopname (" + stopname + ") , datetime (" + scheduled_time_at_stop.getTimeAsISO8601() + ")";
    }
	
}
