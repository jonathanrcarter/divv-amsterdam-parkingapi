package com.glimworm.opendata.divvamsterdamapi.planning;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Place;
import com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturnShortMeterData;

public class ParallelPlan {

	  private static final int NTHREDS = 10;
	  
	  public static void plan (Place pl_destination, String ymd, String hm,ArrayList<ParkSharkCalcReturnShortMeterData> reccommendations) {
		  ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);

		    List<Future<PlanResponse>> list = new ArrayList<Future<PlanResponse>>();
			for (int i=0; i < reccommendations.size(); i++) {
				
				Place parkingLocation = new Place();
				parkingLocation.lat = reccommendations.get(i).lat;
				parkingLocation.lon = reccommendations.get(i).lon;
				parkingLocation.name = reccommendations.get(i).belnummer;
				parkingLocation.type = reccommendations.get(i).type;

				PlanRequest req = new PlanRequest();
				req.from = parkingLocation;
				req.to = pl_destination;
				req.options._date = ymd;
				req.options._time = hm;

		    	Callable<PlanResponse> worker = (Callable<PlanResponse>) new ParallelPlanCallable();
		    	Future<PlanResponse> submit = executor.submit(worker);
		    	list.add(submit);
			}
		    System.out.println(list.size());
		    for (Future<PlanResponse> future : list) {
		    	try {
		    		System.out.println("getting return");
		    		System.out.println(future.toString());
	    		} catch (Exception e) {
					e.printStackTrace();
    			}
		    }
		    executor.shutdown();
	  }

}
