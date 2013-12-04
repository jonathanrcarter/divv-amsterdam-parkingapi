package com.glimworm.opendata.divvamsterdamapi.planning;

import java.util.concurrent.Callable;

public class ParallelPlanCallable implements Callable<PlanResponse>{

	private PlanRequest req = null;
	@Override
	public PlanResponse call() throws Exception {
		PlanResponse pr = PlanOtp.plan(req);
		return pr;
	}
	public ParallelPlanCallable() {
		
	}
	public ParallelPlanCallable(PlanRequest REQ) {
		req = REQ;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
