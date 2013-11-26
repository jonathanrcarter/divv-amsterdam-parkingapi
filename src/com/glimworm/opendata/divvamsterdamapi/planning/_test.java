package com.glimworm.opendata.divvamsterdamapi.planning;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Place;

public class _test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		PlanRequest req = new PlanRequest();
		PlanResponse res = PlanCarByMapQuest.plan(req);
		System.out.println(res);

		PlanRequest req1 = new PlanRequest();
		PlanResponse res1 = PlanOtp.plan(req1);
		System.out.println(res1);
		
		Place pl = GeoCodeByMapQuest.geocode("eerste weteringplantsoen 8 , Amsterdam, Netherlands");
		System.out.println(pl);

	}

}
