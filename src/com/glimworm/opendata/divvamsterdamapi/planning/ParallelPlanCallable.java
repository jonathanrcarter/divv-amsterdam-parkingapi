package com.glimworm.opendata.divvamsterdamapi.planning;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Callable;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Leg;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.MMdatetime;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.ParallelPlanRequest;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Place;
import com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturnReccommendation;

public class ParallelPlanCallable implements Callable<PlanResponse>{

	private PlanRequest req = null;
	private ParkSharkCalcReturnReccommendation rec = null;
	private ParallelPlanRequest ppr = null;
	private boolean addJSON = false;
	private static com.thoughtworks.xstream.XStream xstream = new com.thoughtworks.xstream.XStream(new com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver());
	
	@Override
	public PlanResponse call() throws Exception {
		if (req != null) {
			PlanResponse pr = PlanOtp.plan(req);
			return pr;
		} else {
			long _exdt = new Date().getTime();
			
			Place parkingLocation = new Place();
			parkingLocation.lat = rec.lat;
			parkingLocation.lon = rec.lon;
			parkingLocation.name = rec.belnummer;
			parkingLocation.type = rec.type;

			PlanRequest req = new PlanRequest();
			req.from = parkingLocation;
			req.to = ppr.pl_destination;
			req.options._date = ppr.ymd;
			req.options._time = ppr.hm;
			PlanResponse pr = PlanOtp.plan(req);
			
			/* here is the population of the route */
			rec.reccommended_pt_route.summaryasstring = pr.toString();

			if (addJSON == true) {
				xstream.setMode(com.thoughtworks.xstream.XStream.NO_REFERENCES);
				rec.reccommended_pt_route.summaryasjson = xstream.toXML(pr);
			}
			
			rec.reccommended_pt_route.distance = pr.distance;
			rec.reccommended_pt_route.duration = pr.duration;
			rec.reccommended_pt_route.src = pr.src;
			rec.reccommended_pt_route.url = pr.url;
			rec.reccommended_pt_route.legs = pr.legs;
			rec.reccommended_pt_route.type = pr.type;
			rec.reccommended_pt_route.startTime = pr.startTime.toString();
			rec.reccommended_pt_route.endTime = pr.endTime.toString();
			
			rec.reccommended_pt_route._executiontime = new Date().getTime() - _exdt;
			
			return pr;
			
		}
	}
	public ParallelPlanCallable() {
		
	}
	public ParallelPlanCallable(PlanRequest REQ) {
		req = REQ;
	}
	public ParallelPlanCallable(ParallelPlanRequest PPR, ParkSharkCalcReturnReccommendation REC) {
		rec = REC;
		ppr = PPR;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
