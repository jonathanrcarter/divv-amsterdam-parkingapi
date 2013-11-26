package com.glimworm.opendata.divvamsterdamapi.planning.transit.xsd;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Place;

public class TransitInfoBase {
	
	

	public String agency, line, lineId, headsign = "";
	public Place from, to = null;
	
	public TransitInfoBase() {
		from = new Place();
		to = new Place();
	}
	
	public String toString() {
		return "" + from.toString() + "\n-->" + to.toString()  + "\n\t(ag: " + agency + " /li: " +line+ " /li(id): " +lineId+ " /hs: " +headsign+ ")";
		
	}
	
}
