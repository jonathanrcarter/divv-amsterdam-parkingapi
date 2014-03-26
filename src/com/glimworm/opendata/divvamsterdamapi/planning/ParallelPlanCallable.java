package com.glimworm.opendata.divvamsterdamapi.planning;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Callable;

import com.glimworm.opendata.divvamsterdamapi.planning.transit.xsd.TransitInfo;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Leg;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.MMdatetime;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.ParallelPlanRequest;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Place;
import com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturnReccommendation;
import com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturnReccommendation_ptroute;

public class ParallelPlanCallable implements Callable<PlanResponse>{

	private PlanRequest req = null;
	private ParkSharkCalcReturnReccommendation rec = null;
	private ParallelPlanRequest ppr = null;
	private boolean onlyurl = false;
	private boolean addJSON = false;
	private static com.thoughtworks.xstream.XStream xstream = new com.thoughtworks.xstream.XStream(new com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver());
	
	public static void convertOtpToReccommendation(ParkSharkCalcReturnReccommendation_ptroute reccommended_pt_route, PlanResponse pr, boolean addJSON) {
		/* here is the population of the route */
		reccommended_pt_route.error_msg = pr.error_text;
		reccommended_pt_route.otp_url = pr.otp_url;
		reccommended_pt_route.proxy_url = pr.proxy_url;
		
		
		reccommended_pt_route.summaryasstring = pr.toString();

		if (addJSON == true) {
			xstream.setMode(com.thoughtworks.xstream.XStream.NO_REFERENCES);
			reccommended_pt_route.summaryasjson = xstream.toXML(pr);
		}
		
		reccommended_pt_route.distance = pr.distance;
		reccommended_pt_route.duration = pr.duration;
		reccommended_pt_route.src = pr.src;
		reccommended_pt_route.url = pr.url;
		reccommended_pt_route.legs = pr.legs;
		reccommended_pt_route.type = pr.type;
		reccommended_pt_route.startTime = pr.startTime.toString();
		reccommended_pt_route.endTime = pr.endTime.toString();
		int mms = 0;
		PRICE1:
		for (int i=0; i < pr.legs.size(); i++) {
			if (pr.legs.get(i).mode.equalsIgnoreCase(TransitInfo.LEG_TYPE_TRAIN)) {
				reccommended_pt_route.cost = -1;
				break PRICE1;
			} else if (pr.legs.get(i).mode.equalsIgnoreCase(TransitInfo.LEG_TYPE_TRAM)) {
				// add distance
				mms += pr.legs.get(i).distance;
			} else if (pr.legs.get(i).mode.equalsIgnoreCase(TransitInfo.LEG_TYPE_BUS)) {
				// add distance
				mms += pr.legs.get(i).distance;
			} else if (pr.legs.get(i).mode.equalsIgnoreCase(TransitInfo.LEG_TYPE_SUBWAY)) {
				// add distance
				mms += pr.legs.get(i).distance;
			}
		}
		if (reccommended_pt_route.cost == 0 && mms > 0) {
			reccommended_pt_route.cost = ((0.148 * mms) / 1000) + 0.87;
			reccommended_pt_route.ovdistance = mms;
			reccommended_pt_route.cost = (double)(Math.round(reccommended_pt_route.cost*100))/100;
		}		
	}
	
	@Override
	public PlanResponse call() throws Exception {
		if (onlyurl == true ||  rec.dist_in_meters > ppr.plan_rad){
			Place parkingLocation = new Place();
			parkingLocation.lat = rec.lat;
			parkingLocation.lon = rec.lon;
			parkingLocation.name = rec.automat_number;
			parkingLocation.type = rec.type;

			PlanRequest req = new PlanRequest();
			req.from = parkingLocation;
			req.to = ppr.pl_destination;
			req.options._date = ppr.ymd;
			req.options._time = ppr.hm;
			req.timeout = ppr.opt_otp_tim;
			req.urlonly = true;
			PlanResponse pr = PlanOtp.plan(req);
			rec.reccommended_pt_route.error_msg = pr.error_text;
			rec.reccommended_pt_route.otp_url = pr.otp_url;
			rec.reccommended_pt_route.proxy_url = pr.proxy_url;

			if (ppr.plan_return_also == true) {

				req = new PlanRequest();
				req.to = parkingLocation;
				req.from = ppr.pl_destination;
				req.options._date = ppr.ret_ymd;
				req.options._time = ppr.ret_hm;
				req.urlonly = true;
				pr = PlanOtp.plan(req);

				rec.reccommended_pt_route_return.error_msg = pr.error_text;
				rec.reccommended_pt_route_return.otp_url = pr.otp_url;
				rec.reccommended_pt_route_return.proxy_url = pr.proxy_url;
			}
			
			return pr;
		}
		if (req != null) {
			PlanResponse pr = PlanOtp.plan(req);
			return pr;
		} else {
			long _exdt = new Date().getTime();
			
			Place parkingLocation = new Place();
			parkingLocation.lat = rec.lat;
			parkingLocation.lon = rec.lon;
			parkingLocation.name = rec.automat_number;
			parkingLocation.type = rec.type;

			PlanRequest req = new PlanRequest();
			req.from = parkingLocation;
			req.to = ppr.pl_destination;
			req.options._date = ppr.ymd;
			req.options._time = ppr.hm;
			req.timeout = ppr.opt_otp_tim;
			PlanResponse pr = PlanOtp.plan(req);
			
			/* here is the population of the route */
			rec.reccommended_pt_route.error_msg = pr.error_text;
			rec.reccommended_pt_route.otp_url = pr.otp_url;
			rec.reccommended_pt_route.proxy_url = pr.proxy_url;
			
			
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
			int mms = 0;
			PRICE1:
			for (int i=0; i < pr.legs.size(); i++) {
				if (pr.legs.get(i).mode.equalsIgnoreCase(TransitInfo.LEG_TYPE_TRAIN)) {
					rec.reccommended_pt_route.cost = -1;
					break PRICE1;
				} else if (pr.legs.get(i).mode.equalsIgnoreCase(TransitInfo.LEG_TYPE_TRAM)) {
					// add distance
					mms += pr.legs.get(i).distance;
				} else if (pr.legs.get(i).mode.equalsIgnoreCase(TransitInfo.LEG_TYPE_BUS)) {
					// add distance
					mms += pr.legs.get(i).distance;
				} else if (pr.legs.get(i).mode.equalsIgnoreCase(TransitInfo.LEG_TYPE_SUBWAY)) {
					// add distance
					mms += pr.legs.get(i).distance;
				}
			}
			if (rec.reccommended_pt_route.cost == 0 && mms > 0) {
				rec.reccommended_pt_route.cost = ((0.148 * mms) / 1000) + 0.87;
				rec.reccommended_pt_route.ovdistance = mms;
				rec.reccommended_pt_route.cost = (double)(Math.round(rec.reccommended_pt_route.cost*100))/100;
			}
			
			
			if (ppr.plan_return_also == false) {
				rec.reccommended_pt_route_return.summaryasstring = null;
			} else {

				req = new PlanRequest();
				req.to = parkingLocation;
				req.from = ppr.pl_destination;
				req.options._date = ppr.ret_ymd;
				req.options._time = ppr.ret_hm;
				pr = PlanOtp.plan(req);

				rec.reccommended_pt_route_return.error_msg = pr.error_text;
				rec.reccommended_pt_route_return.otp_url = pr.otp_url;
				rec.reccommended_pt_route_return.proxy_url = pr.proxy_url;
				
				
				/* here is the population of the route */
				rec.reccommended_pt_route_return.summaryasstring = pr.toString();
				
				rec.reccommended_pt_route_return.distance = pr.distance;
				rec.reccommended_pt_route_return.duration = pr.duration;
				rec.reccommended_pt_route_return.src = pr.src;
				rec.reccommended_pt_route_return.url = pr.url;
				rec.reccommended_pt_route_return.legs = pr.legs;
				rec.reccommended_pt_route_return.type = pr.type;
				rec.reccommended_pt_route_return.startTime = pr.startTime.toString();
				rec.reccommended_pt_route_return.endTime = pr.endTime.toString();
				rec.reccommended_pt_route_return.cost = 0;
				mms = 0;
				PRICE:
				for (int i=0; i < pr.legs.size(); i++) {
					if (pr.legs.get(i).mode.equalsIgnoreCase(TransitInfo.LEG_TYPE_TRAIN)) {
						rec.reccommended_pt_route_return.cost = -1;
						break PRICE;
					} else if (pr.legs.get(i).mode.equalsIgnoreCase(TransitInfo.LEG_TYPE_TRAM)) {
						// add distance
						mms += pr.legs.get(i).distance;
					} else if (pr.legs.get(i).mode.equalsIgnoreCase(TransitInfo.LEG_TYPE_BUS)) {
						// add distance
						mms += pr.legs.get(i).distance;
					} else if (pr.legs.get(i).mode.equalsIgnoreCase(TransitInfo.LEG_TYPE_SUBWAY)) {
						// add distance
						mms += pr.legs.get(i).distance;
					}
				}
				if (rec.reccommended_pt_route_return.cost == 0 && mms > 0) {
					rec.reccommended_pt_route_return.cost = ((0.148 * mms) / 1000) + 0.87;
					rec.reccommended_pt_route_return.ovdistance = mms;
					rec.reccommended_pt_route_return.cost = (double)(Math.round(rec.reccommended_pt_route_return.cost*100))/100;
				}
				
				
			}
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
		onlyurl = false;
		
	}
	public ParallelPlanCallable(ParallelPlanRequest PPR, ParkSharkCalcReturnReccommendation REC, boolean ONLYURL) {
		rec = REC;
		ppr = PPR;
		onlyurl = ONLYURL;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
