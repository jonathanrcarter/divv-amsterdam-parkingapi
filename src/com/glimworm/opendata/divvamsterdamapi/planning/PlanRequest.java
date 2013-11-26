package com.glimworm.opendata.divvamsterdamapi.planning;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Place;

public class PlanRequest {
    public String type = "plan_request";
    public Place from, to;
    public Options options;
    
    public PlanRequest() {
    	from = new Place();
    	to = new Place();
    	options = new Options();
    }
    

}
