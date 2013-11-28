package com.glimworm.opendata.divvamsterdamapi.planning;

import java.util.ArrayList;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Place;

public class _test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ArrayList<Place> pl = GeoCodeByMapQuest.geocode("eerste weteringplantsoen 8 , Amsterdam, Netherlands");
		ArrayList<Place> pl2 = GeoCodeByMapQuest.geocode("Marco Polostraat 107 , Amsterdam, Netherlands");
		System.out.println(pl);
		System.out.println(pl2);
		
		PlanRequest req = new PlanRequest();
		req.from = pl.get(0);
		req.to = pl2.get(0);
		req.options._date = "2013-11-28";
		req.options._time = "18:00";
		PlanResponse res = PlanCarByMapQuest.plan(req);
		System.out.println(res.toString());

		PlanRequest req1 = new PlanRequest();
		PlanResponse res1 = PlanOtp.plan(req);
		System.out.println(res1);
		


	}

}
