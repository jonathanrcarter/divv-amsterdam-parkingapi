package com.glimworm.opendata.divvamsterdamapi.planning.transit.xsd;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.TransitLineStop;

public class TransitInfoTram extends TransitInfoBase {

	public TransitInfoTram() {
		from = new TransitLineStop();
		to = new TransitLineStop();
	}
	
}
