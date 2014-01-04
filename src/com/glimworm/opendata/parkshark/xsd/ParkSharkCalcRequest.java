package com.glimworm.opendata.parkshark.xsd;

public class ParkSharkCalcRequest {
	public int _day = 0;
	public int hrs = 0;
	public int mins = 0;
	public double duration = 0;
	public double from_lat = 0;
	public double from_lon = 0;
	public String _paymethods = "";
	public int fmt = 0;
	public String log = "n";	// log to sysylog for server debugging
	public String dbg = "n";	// return debug information
}
