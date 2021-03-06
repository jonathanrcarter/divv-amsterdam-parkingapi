package com.glimworm.opendata.divvamsterdamapi.planning.xsd;

import java.util.ArrayList;

import com.glimworm.opendata.parkshark.xsd.PayTimes;
import com.vividsolutions.jts.geom.Polygon;

public class PlaceParkingGarage  extends Place implements Cloneable {
	

	public int places = 0;
	
	public String owner = "";	// Q-park
	public String remarks = "";
	public int capacity = 0;
	public double price_day = 0;
	public int free_minutes = 0;
	public PlaceParkingGarageOpeningTimes[] opening_times = new PlaceParkingGarageOpeningTimes[7];
//	public String opening_times_raw = "0 0930 0930 1930 1930|1-6 0730 0730 2130 2130";
	public String opening_times_raw = "";
	public int time_unit_minutes = 0;
	public double price_per_time_unit = 0;
	public String includes_public_transport = "n";
	public String calc_type = "garage";
	public String csdkid = "";
	public String csdkurl = "";
	public String csdkstatus = "";
	public String nprid = "";
	public String nprurl = "";
	public String nprstatus = "";
	public String ams_pr_fare = "";		// amsterdam park and ride fare string 
										// e.g. "1-5 04:00 10:00 8 24 | 1-5 10:00 04:00 1 24 | 6-0 0:00 0:00 1 24"
	public PlaceParkingGarageAmsterdamPrVariation[] ams_pr_fares = null;
	public Polygon[] polys = null;
	public PayTimes pt = null;
	public ArrayList<PayTimes> ptA = null;
	public ArrayList<PayTimes> ptEexclusions = null;
	
	public PlaceParkingGarage() {
	}
	public Object clone() {
		try {
		    return super.clone();
		} catch( CloneNotSupportedException e ) {
		    return null;
		}
    } 
    
    public String toString() {
    	return super.toString() + " places : "+ places;
    }
	
}
