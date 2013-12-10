package com.glimworm.opendata.parkshark.importdata.citySDK.xsd;

public class geom {
	public String type = "";
	public double lat = 0;
	public double lon = 0;
	public int status = 0;	// 0 is OK
	
	public String toString() {
		return type +","+ lat +"," +lon +","+ status;
	}
}
