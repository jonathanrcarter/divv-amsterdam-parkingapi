package com.glimworm.opendata.parkshark.xsd;

import java.util.ArrayList;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Leg;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.MMdatetime;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Place;

public class ParkSharkCalcReturnReccommendation_ptroute {
	/** here will be the actuial route data */
	public String summaryasstring = "";
	public String summaryasjson = "";
	public int status = 0;
	public long distance = 0; //In Meters
	public int duration = 0; 
	public double cost = 0;
	public String src = "";
	public String url = "";
	public ArrayList<Leg> legs = null;
	public String type = "";
	public String startTime = "";
	public String endTime = "";
	public long _executiontime = 0;
}
