package com.glimworm.opendata.divvamsterdamapi.planning.xsd;


public class Place {

	public double lat,lon = 0;
	public String name = "";
	public String street = "", postcode = "", url = "", type = "", rawdata = "";
	public org.json.JSONObject data = null;
	
	
	public Place() {
		lat = 0;
		lon = 0;
		name = "";
	}
	public Place(double Lat, double Lon, String Name) {
		lat = Lat;
		lon = Lon;
		name = Name;
	}
	
	public String toString() {
		return lat+","+lon+"/"+name+" "+street+" "+postcode+" "+url+" "+type; 
	}
}
