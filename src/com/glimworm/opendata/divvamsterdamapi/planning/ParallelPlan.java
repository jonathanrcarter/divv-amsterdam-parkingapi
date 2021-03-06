package com.glimworm.opendata.divvamsterdamapi.planning;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.ParallelPlanRequest;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Place;
import com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturnReccommendation;

public class ParallelPlan {

	  private static final int NTHREDS = 20;
	  
	  public static void plan (ParallelPlanRequest ppr,ArrayList<ParkSharkCalcReturnReccommendation> reccommendations) {
		  ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);

		    List<Future<PlanResponse>> list = new ArrayList<Future<PlanResponse>>();
			for (int i=0; i < reccommendations.size(); i++) {
				boolean onlyurl = (i > 5);
		    	Callable<PlanResponse> worker = (Callable<PlanResponse>) new ParallelPlanCallable(ppr, reccommendations.get(i), onlyurl);
		    	Future<PlanResponse> submit = executor.submit(worker);
		    	list.add(submit);
			}
		    executor.shutdown();
		    
		    try {
		    	executor.awaitTermination(30, java.util.concurrent.TimeUnit.SECONDS);
			    System.out.println("ParallelPlannin Finished all threads");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			    System.out.println("Interrupted");
				e.printStackTrace();
			}
		    
	  }

}
