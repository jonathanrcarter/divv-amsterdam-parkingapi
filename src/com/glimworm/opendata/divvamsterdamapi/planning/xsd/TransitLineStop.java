package com.glimworm.opendata.divvamsterdamapi.planning.xsd;

public class TransitLineStop  extends Place {
	

    // The ID of the stop, taken from stopId->id, e.g. CXX|57225182 - this maps to a TimingPointCode
	public String stopid = "";
	// Canonical name of the stop, e.g. "Centraal Station"
	public String name = "";
	public double lat,lon = 0;
	
	public MMdatetime target_arrival_time = null;
	public MMdatetime target_departure_time = null;
	
	
	public String toString() {
		return "\n\tstopid ("+stopid+") , \n\tname ("+name+"), \n\tdatetime ("+timeString()+")";
	}
	
	public String timeString() {
		String retval = "";
		if (target_arrival_time != null) {
			retval += target_arrival_time.toString();
		}
		if (target_departure_time != null) {
			retval += target_departure_time.toString();
		}
		return retval;
	}
	
	public String timingPointCodeFromStopId() {
		try {
			return stopid.split("[|]")[1];
		} catch (Exception E) {
			E.printStackTrace(System.out);
		}
		return "";
	}

}
