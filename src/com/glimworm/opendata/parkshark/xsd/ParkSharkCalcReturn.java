package com.glimworm.opendata.parkshark.xsd;

import java.util.ArrayList;

public class ParkSharkCalcReturn {
	public String text = "{}";
	public Meter[] meters = null;
	public ArrayList<ParkSharkCalcReturnReccommendation> reccommendations = null;
	public javolution.util.FastMap<String, Double> costsmap = null;
	public ArrayList<String> timings = new ArrayList<String>();
}
