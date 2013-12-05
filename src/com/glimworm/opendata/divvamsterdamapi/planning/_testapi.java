package com.glimworm.opendata.divvamsterdamapi.planning;

import java.util.ArrayList;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.ParallelPlanRequest;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Place;

public class _testapi {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ArrayList<Place> pl_home = GeoCodeByMapQuest.geocode("eerste weteringplantsoen 8 , Amsterdam, Netherlands");
		ArrayList<Place> pl_destination = GeoCodeByMapQuest.geocode("Marco Polostraat 107 , Amsterdam, Netherlands");
		System.out.println(pl_home);
		System.out.println(pl_destination);
		
		String dd = "28";
		String mm = "12";
		String yy = "2013";
		String ymd = yy+"-"+mm+"-"+dd;
		String h = "12";
		String m = "15";
		String hm = h+":"+m;
		int day = 5;
		int hr = 12;
		int min = 15;
		int duration = 180;
		double lat = pl_destination.get(0).lat;
		double lon = pl_destination.get(0).lon;
		String methods = "pin";
		int fmt = 2;

//		req.options._date = "2013-12-28";
//		req.options._time = "18:00";
		
		
		com.glimworm.opendata.parkshark.xsd.ParkSharkCalcReturn prv = com.glimworm.opendata.parkshark.CalcParking.calcv2(day, hr, min ,  duration, lat, lon, methods,fmt);

		com.thoughtworks.xstream.XStream xstream = new com.thoughtworks.xstream.XStream(new com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver());
		xstream.setMode(com.thoughtworks.xstream.XStream.NO_REFERENCES);

		
		
//		
//		for (int i=0; i < prv.reccommendations.size(); i++) {
//			System.out.print(prv.reccommendations.get(i).cost);
//			System.out.print("\t");
//			System.out.print(prv.reccommendations.get(i).type);
//			System.out.print("\t");
//			System.out.print(prv.reccommendations.get(i).address);
//			System.out.print("\t");
//			System.out.println("");
//			
//			Place parkingLocation = new Place();
//			parkingLocation.lat = prv.reccommendations.get(i).lat;
//			parkingLocation.lon = prv.reccommendations.get(i).lon;
//			parkingLocation.name = prv.reccommendations.get(i).belnummer;
//			parkingLocation.type = prv.reccommendations.get(i).type;
//
//			PlanRequest req = new PlanRequest();
//			req.from = parkingLocation;
//			req.to = pl_destination.get(0);
//			req.options._date = ymd;
//			req.options._time = hm;
//			PlanResponse res1 = PlanOtp.plan(req);
//			System.out.println(res1.toString());
//			System.out.println(xstream.toXML(res1.toString()));
//			
//			
//		}
		System.out.println("NOW IN PARALLEL");
		ParallelPlanRequest ppr = new ParallelPlanRequest();
		ppr.pl_destination = pl_destination.get(0);
		ppr.ymd = ymd;
		ppr.hm = hm;
		
		com.glimworm.opendata.divvamsterdamapi.planning.ParallelPlan.plan(ppr, prv.reccommendations);
		System.out.println("NOW IN PARALLEL - DONE");

		for (int i=0; i < prv.reccommendations.size(); i++) {
			System.out.print("** ");
			System.out.print(i);
			System.out.print(" : ");
			System.out.print(prv.reccommendations.get(i).belnummer);
			System.out.print(" : ");
			System.out.print(prv.reccommendations.get(i).reccommended_pt_route);
			System.out.println("");
		}
		

		
		String URL = "http://api.parkshark.nl/psapi/api.jsp";
		String PARAMS = "day=5&hr=8&min=30&duration=3&lat=52.377&lon=4.9104&methods=cash,pin=";
		com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse cr =  com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(URL, PARAMS, null, null, null, null, null);
		
//		System.out.println(cr.text);

		String rv = cr.text;
		System.out.println(rv);
		
		
		
		

	}

}
