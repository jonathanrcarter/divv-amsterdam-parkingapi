package com.glimworm.opendata.divvamsterdamapi.planning.xsd;

import java.util.ArrayList;
import java.util.Map;

import com.glimworm.opendata.parkshark.xsd.Meter;
import com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturnReccommendation;

public class apiResponse {
	public long _executiontime = 0;
	public ArrayList<String> timings = new java.util.ArrayList<String>();
	public ArrayList<ParkSharkCalcReturnReccommendation> reccommendations = null;
	public Meter[] meters = null;
	public Meter meter = null;
	public PlaceParkingGarage garage = null;
	public ArrayList<Place> places = null;
}
