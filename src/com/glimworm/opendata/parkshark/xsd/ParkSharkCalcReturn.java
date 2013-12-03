package com.glimworm.opendata.parkshark.xsd;

import java.util.ArrayList;

public class ParkSharkCalcReturn {
	public String text = "{}";
	public Meter[] meters = null;
	public ArrayList<ParkSharkCalcReturnShortMeterData> reccommendations = null;
	public javolution.util.FastMap<String, Double> costsmap = null;
}
