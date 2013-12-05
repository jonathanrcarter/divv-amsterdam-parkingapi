package com.glimworm.opendata.divvamsterdamapi.planning;

import java.util.concurrent.Callable;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.ParallelPlanRequest;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Place;
import com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturnReccommendation;

public class ParallelPlanCallable implements Callable<PlanResponse>{

	private PlanRequest req = null;
	private ParkSharkCalcReturnReccommendation rec = null;
	private ParallelPlanRequest ppr = null;
	
	@Override
	public PlanResponse call() throws Exception {
		if (req != null) {
			PlanResponse pr = PlanOtp.plan(req);
			return pr;
		} else {
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
			
			rec.reccommended_pt_route = pr.toString();
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
