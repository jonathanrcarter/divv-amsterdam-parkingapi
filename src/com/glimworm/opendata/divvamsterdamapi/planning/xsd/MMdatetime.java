package com.glimworm.opendata.divvamsterdamapi.planning.xsd;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.DateTimeZone;
public class MMdatetime {

	public int day = 0,month = 0,year = 0,hour = 0,minute = 0;
	private DateTime dt = new DateTime();
	
	public MMdatetime setTimeFromISO8601(long ISOtime) {
		
		//DateTimeZone zone = new DateTimeZone("Europe/Amsterdam");
		DateTimeZone zone = DateTimeZone.forID("Europe/Amsterdam");
		DateTime dt = new DateTime(ISOtime);
		this.dt =dt;
		this.year = this.dt.getYear();
		this.month = this.dt.getMonthOfYear();
		this.day = this.dt.getDayOfMonth();
		this.hour = this.dt.getHourOfDay();
		this.minute = this.dt.getMinuteOfHour();
		return this;
	}
	
	public MMdatetime setDateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour) {
		this.dt = new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, 0, 0);
		this.year = this.dt.getYear();
		this.month = this.dt.getMonthOfYear();
		this.day = this.dt.getDayOfMonth();
		this.hour = this.dt.getHourOfDay();
		this.minute = this.dt.getMinuteOfHour();
		return this;
	}
	
	public String getTimeAsISO8601() {
		
		return "format(DateTime::ISO8601)";		
	}

	
	public String getDate() {
		return this.dt.toString("yyyy-MM-dd");
	}
	
	public String getTime() {
		return this.dt.toString("HH:mm");
	}
	
	public MMdatetime addMinutes(int minutes) {
		DateTime result = this.dt.plusMinutes(minutes);
		this.dt = result;
		this.year = this.dt.getYear();
		this.month = this.dt.getMonthOfYear();
		this.day = this.dt.getDayOfMonth();
		this.hour = this.dt.getHourOfDay();
		this.minute = this.dt.getMinuteOfHour();
		return this;
	}
	
	public MMdatetime addSeconds(int seconds) {
		DateTime result = this.dt.plusSeconds(seconds);
		this.dt = result;
		this.year = this.dt.getYear();
		this.month = this.dt.getMonthOfYear();
		this.day = this.dt.getDayOfMonth();
		this.hour = this.dt.getHourOfDay();
		this.minute = this.dt.getMinuteOfHour();
		return this;
	}
	
	public String toString() {
		return this.dt.toString();
	}
	
	
}
