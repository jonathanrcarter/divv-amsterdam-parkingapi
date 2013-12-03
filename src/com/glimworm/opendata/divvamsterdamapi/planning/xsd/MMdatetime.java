package com.glimworm.opendata.divvamsterdamapi.planning.xsd;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.DateTimeZone;
public class MMdatetime {

	public int day,month,year,hour,minute = 0;
	public DateTime dt = null;
	
	public MMdatetime setTimeFromISO8601(long ISOtime) {
		
		//DateTimeZone zone = new DateTimeZone("Europe/Amsterdam");
		DateTime dt = new DateTime(ISOtime);
		this.dt =dt;
		
		return this;
	}
	
	public String getTimeAsISO8601() {
		return "format(DateTime::ISO8601)";		
	}

	public MMdatetime addMinutes(int minutes) {
		DateTime result = this.dt.plusMinutes(minutes);
		this.dt = result;
		return this;
	}
	
	public MMdatetime addSeconds(int seconds) {
		DateTime result = this.dt.plusSeconds(seconds);
		this.dt = result;
		return this;
	}
	
	
}
