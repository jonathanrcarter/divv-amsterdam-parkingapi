package com.glimworm.opendata.divvamsterdamapi.planning.transit.xsd;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.TransitLineStop;

public class TransitInfoBus extends TransitInfoBase {

	public TransitInfoBus() {
		from = new TransitLineStop();
		to = new TransitLineStop();
	}

}
