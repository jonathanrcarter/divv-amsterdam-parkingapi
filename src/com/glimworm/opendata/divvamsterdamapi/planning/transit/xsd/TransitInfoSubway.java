package com.glimworm.opendata.divvamsterdamapi.planning.transit.xsd;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.TransitLineStop;

public class TransitInfoSubway extends TransitInfoBase {
	
	public TransitInfoSubway() {
		from = new TransitLineStop();
		to = new TransitLineStop();
	}

}
