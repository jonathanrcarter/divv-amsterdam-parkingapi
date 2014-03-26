package com.glimworm.opendata.divvamsterdamapi.planning;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Place;

public class PlanRequest {
    public String type = "plan_request";
    public Place from, to;
    public Options options;
    public int timeout = 300;
    public String URL = null;
    public String PARAMS = null;
    public boolean urlonly = false;
    
    public PlanRequest() {
    	from = new Place();
    	to = new Place();
    	options = new Options();
    }
    

}
