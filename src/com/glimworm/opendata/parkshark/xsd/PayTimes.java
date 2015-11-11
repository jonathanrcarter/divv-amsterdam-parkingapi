package com.glimworm.opendata.parkshark.xsd;

public class PayTimes implements Cloneable {
	public String geb_code = "";
	public PayTime[] days = {null,null,null,null,null,null,null};

	public String t_code = "";
	public First first = new First();
	public double cost = 0;
	public double max = 0;	
	public double maxdaycost = 0;
	public int error = 0;
	public String NPRtariffDescription = "";
	public boolean parkingProhibited = false;
	public boolean durationRestriction = false;
	public int maximumDuration = 0;
	public int minimumParkingInterruption = 0;
	

	public String getSignature() {
		String rv = geb_code + "|" + 
			days[0].getSignature() + "|" +
			days[1].getSignature() + "|" +
			days[2].getSignature() + "|" +
			days[3].getSignature() + "|" +
			days[4].getSignature() + "|" +
			days[5].getSignature() + "|" +
			days[6].getSignature() + "|" +
			t_code + "|" +
			first.getSignature() + "|" +
			cost + "|" +
			max + "|" +
			maxdaycost + "|" +
			NPRtariffDescription + "|" +
			((parkingProhibited)?"parkingProhibited":"") + "|" +
			((durationRestriction)?"durationRestriction":"") + "|" +
			maximumDuration + "|" +
			minimumParkingInterruption;
		return rv;
		
	}
	public Object clone() {
		try {
			PayTimes clone = (PayTimes) super.clone();
			clone.days = (PayTime[]) days.clone();
			clone.first = (First) first.clone();
			return clone;
		} catch( CloneNotSupportedException e ) {
		    return null;
		}
    }	
}
