package com.glimworm.opendata.divvamsterdamapi.planning.xsd;

public class PlaceParkingGarage  extends Place {
	

	public int places = 0;
	
	public PlaceParkingGarage() {
	}
    
    public String toString() {
    	return super.toString() + " places : "+ places;
    }
	
}
