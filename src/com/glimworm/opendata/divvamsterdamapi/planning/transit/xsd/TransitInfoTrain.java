package com.glimworm.opendata.divvamsterdamapi.planning.transit.xsd;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.StopName;

public class TransitInfoTrain extends TransitInfoBase {
	public TransitInfoTrain() {
		from = new StopName();
		to = new StopName();
	}

}
