package com.glimworm.opendata.divvamsterdamapi.planning.xsd;

public class Place {

	public double lat,lon = 0;
	public String name = "";
	
	public Place(double Lat, double Lon, String Name) {
		lat = Lat;
		lon = Lon;
		name = Name;
	}
	
	public String toString() {
		return lat+","+lon+"/"+name; 
	}
}
