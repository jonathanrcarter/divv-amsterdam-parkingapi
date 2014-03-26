package com.glimworm.opendata.divvamsterdamapi.planning.xsd;

public class PlaceParkingGarageAmsterdamPrVariation {
	public int dayOfWeek_start = 0;
	public int dayOfWeek_end = 0;
	public int entry_start = 0;
	public int entry_end = 0;
	public double price_day = 0;
	
	public String json() {
		return "{\"dayOfWeek_start\":\""+dayOfWeek_start+"\",\"dayOfWeek_end\":\""+dayOfWeek_end+"\",\"entry_start\":\""+entry_start+"\",\"entry_end\":\""+entry_end+"\",\"price_day\":\""+price_day+"\"}";
	}

}
