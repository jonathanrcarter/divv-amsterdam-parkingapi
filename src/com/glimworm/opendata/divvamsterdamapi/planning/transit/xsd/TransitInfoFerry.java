package com.glimworm.opendata.divvamsterdamapi.planning.transit.xsd;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.TransitLineStop;

public class TransitInfoFerry extends TransitInfoBase {
	public TransitInfoFerry() {
		from = new TransitLineStop();
		to = new TransitLineStop();
	}

}
