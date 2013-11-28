package com.glimworm.opendata.divvamsterdamapi.planning.xsd;

public class MMdatetime {

	public int day,month,year,hour,minute = 0;
	
	public String getTimeAsISO8601() {
		return "format(DateTime::ISO8601)";		
	}

	public MMdatetime addMinutes(int minutes) {
		return this;
	}
	
	public MMdatetime addSeconds(int minutes) {
		return this;
	}
	
	
}
