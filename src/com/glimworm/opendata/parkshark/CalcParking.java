package com.glimworm.opendata.parkshark;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.glimworm.opendata.divvamsterdamapi.planning.ParallelPlanCallable;
import com.glimworm.opendata.divvamsterdamapi.planning.PlanResponse;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.PlaceParkingGarage;
import com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam;
import com.glimworm.opendata.parkshark.xsd.*;
import com.glimworm.opendata.utils.jsonUtils;
import com.glimworm.opendata.xsd.*;
import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javolution.util.FastMap;

public class CalcParking {
	

	
	public static String Q(String S) {
		return "\""+S+"\"";
	}
	public static double RadToDeg(double radians) {
		return radians * (180 / Math.PI);
	}
	public static double DegToRad (double degrees) {
		return degrees * (Math.PI / 180);
	}
	
	public static double distance_not_correct_maybe(location loc1, location loc2) {
		double lat1 = DegToRad(loc1.latitude);		
		double lon1 = DegToRad(loc1.longitude);
		double lat2 = DegToRad(loc2.latitude);		
		double lon2 = DegToRad(loc2.longitude);		
		

		double R = 6371000000.0; // m (change this constant to get miles)
		double dLat = (lat2-lat1) * Math.PI / 180;
		double dLon = (lon2-lon1) * Math.PI / 180;
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	    Math.cos(lat1 * Math.PI / 180 ) * Math.cos(lat2 * Math.PI / 180 ) *
	    Math.sin(dLon/2) * Math.sin(dLon/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double d = R * c;
		
//		System.out.println("dist ["+Math.round(d/2)+"] LAT ["+loc1.latitude+"/"+loc2.latitude+"] LON ["+loc1.longitude+"/"+loc2.longitude+"]");
		
	    return Math.round(d/20);
	};

	/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::                                                                         :*/
	/*::  This routine calculates the distance between two points (given the     :*/
	/*::  latitude/longitude of those points). It is being used to calculate     :*/
	/*::  the distance between two locations using GeoDataSource (TM) prodducts  :*/
	/*::                                                                         :*/
	/*::  Definitions:                                                           :*/
	/*::    South latitudes are negative, east longitudes are positive           :*/
	/*::                                                                         :*/
	/*::  Passed to function:                                                    :*/
	/*::    lat1, lon1 = Latitude and Longitude of point 1 (in decimal degrees)  :*/
	/*::    lat2, lon2 = Latitude and Longitude of point 2 (in decimal degrees)  :*/
	/*::    unit = the unit you desire for results                               :*/
	/*::           where: 'M' is statute miles                                   :*/
	/*::                  'K' is kilometers (default)                            :*/
	/*::                  'N' is nautical miles                                  :*/
	/*::  Worldwide cities and other features databases with latitude longitude  :*/
	/*::  are available at http://www.geodatasource.com                          :*/
	/*::                                                                         :*/
	/*::  For enquiries, please contact sales@geodatasource.com                  :*/
	/*::                                                                         :*/
	/*::  Official Web site: http://www.geodatasource.com                        :*/
	/*::                                                                         :*/
	/*::           GeoDataSource.com (C) All Rights Reserved 2014                :*/
	/*::                                                                         :*/
	/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

	private static double distance_from_geolocation_site(double lat1, double lon1, double lat2, double lon2) {
	  double theta = lon1 - lon2;
	  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
	  dist = Math.acos(dist);
	  dist = rad2deg(dist);
	  dist = dist * 60 * 1.1515;
	  dist = dist * 1.609344;	// kilometers
	  dist = dist * 1000; // meters
	  return Math.round(dist);
	}
	public static double distance(location loc1, location loc2) {
		return distance_from_geolocation_site(loc1.latitude, loc1.longitude, loc2.latitude, loc2.longitude);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts decimal degrees to radians             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double deg2rad(double deg) {
	  return (deg * Math.PI / 180.0);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts radians to decimal degrees             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double rad2deg(double rad) {
	  return (rad * 180 / Math.PI);
	}

	
	
	public static double getNorthLatFromLng(double lng) {
		double start_lat = 52.4208;
		double start_lng = 4.82815;	
		double lon_delta = 3.636122177; 	// each time the lon increases by 1 the lat decreases by this value

		double difference_in_lng = (lng - start_lng);
		double new_lat = start_lat - (difference_in_lng * lon_delta);		// new Longitude
		return new_lat;
	};

	public static boolean isInNorth(double lat, double lng) {
		double start_lat = 52.4208;
		double start_lng = 4.82815;	
		double lon_delta = 0.3636122177; 	// each time the lon increases by 1 the lat decreases by this value

		double difference_in_lng = (lng - start_lng);
		double new_lat = start_lat - (difference_in_lng * lon_delta);		// new Longitude
		
		if (lat > new_lat) return true;
		return false;
	};	
	
	public static boolean has(String S, String[] paymethods) {
		for (int i=0; i < paymethods.length; i++) {
			if (paymethods[i].equalsIgnoreCase(S)) return true;
		}
		return false;
	}

	
	public static Gebcode getgebcode(Meter meter) {
		/*
			gebcodes
			--------
			t_code :"a"
			geb_code : "G1"
			price_code : "TZ12"
			max : 60
			oms : ""
		*/
		try {
			java.sql.ResultSet rs = com.glimworm.common.database.GWDBBean.sqlStatic("select * from _site1493_dbsynch_gebiedcodes_xls where Tarief='"+meter.tariefcode+"'");
			if (rs.next()) {
				Gebcode gc = new Gebcode();
				gc.t_code = rs.getString("Tarief");
				gc.geb_code = rs.getString("GebiedCode");
				gc.price_code = rs.getString("Prijs");
				try {
					gc.max = rs.getInt("Max");
				} catch (Exception E) {
				}
				gc.oms = rs.getString("Omschrijving");
				return gc;
			}
		} catch (Exception E) {
		}
		return new Gebcode();
	}
	
	/**
	 * get geb codes
	 * @return
	 */
	public static Gebcode[] getgebcodes() {
		String sql = "select * from _site1493_dbsynch_gebiedcodes_xls";
		com.glimworm.common.database.xsd.DataSet data = com.glimworm.common.database.gwDataUtils.getArray(com.glimworm.common.database.GWDBBean.sqlStatic(sql),false);
		Gebcode[] gc = new Gebcode[data.rows()];
		for (int i=0; i < data.rows(); i++) {
			gc[i] = new Gebcode();
			gc[i].t_code = data.getString(i,"Tarief");
			gc[i].geb_code = data.getString(i,"GebiedCode");
			gc[i].price_code = data.getString(i,"Prijs");
			try {
				gc[i].max = data.getInt(i,"Max",0);
			} catch (Exception E) {
			}
			gc[i].oms = data.getString(i,"Omschrijving");
		}
		return gc;
	}
	public static String getGebCodes() {
		Gebcode[] gc = getgebcodes();
		String retval = Response.obj2json(gc);
		return retval;
//		return "{"+retval+"}";
	}
	
	
	public static Costs getcost(String price_code) {
		Costs retval = new Costs();
		
		String sql1 = "select * from _site1493_dbsynch_paytariff where code='"+price_code+"'";
		com.glimworm.common.database.xsd.DataSet ds = com.glimworm.common.database.gwDataUtils.getArray(com.glimworm.common.database.GWDBBean.sqlStatic(sql1),false);
		if (ds != null && ds.rows() > 0) {
			retval.t_code = ds.getString(0,"code");

			String price = ds.getString(0,"price");
			if (price.length() == 4) {
				retval.first.combination = "n";
				retval.first.hrs = 0;
				retval.first.price = 0.0;
				retval.cost = Double.parseDouble(price.trim());
			} else if (price.length() == 12 && price.indexOf(" en ") > -1) {
				retval.first.combination = "y";
				retval.first.hrs = 3;
				retval.first.price = Double.parseDouble(price.trim().substring(8));
				retval.cost = Double.parseDouble(price.substring(0,4));
			}
		}
		return retval;
	}
	public static Costs[] getcosts() {
		String sql = "select * from _site1493_dbsynch_paytariff";
		com.glimworm.common.database.xsd.DataSet data = com.glimworm.common.database.gwDataUtils.getArray(com.glimworm.common.database.GWDBBean.sqlStatic(sql),false);
		Costs[] c = new Costs[data.rows()];
		for (int i=0; i < data.rows(); i++) {
			c[i] = new Costs();
			c[i].t_code = data.getString(i,"code");
			String price = data.getString(i,"price");
			if (price.length() == 4) {
				c[i].first.combination = "n";
				c[i].first.hrs = 0;
				c[i].first.price = 0.0;
				c[i].cost = Double.parseDouble(price.trim());
			} else if (price.length() == 12 && price.indexOf(" en ") > -1) {
				c[i].first.combination = "y";
				c[i].first.hrs = 3;
				c[i].first.price = Double.parseDouble(price.trim().substring(8));
				c[i].cost = Double.parseDouble(price.substring(0,4));
			}
		}
		return c;
	}
	public static String getCosts() {
		Costs[] c = getcosts();
		String retval = Response.obj2json(c);
		return retval;
//		return "{"+retval+"}";
	}	
	
	
	
	
	public static PayTime decodeDate(String S) {
		String start = "0";
		String end = "0";
		if (S.trim().length() == 11) {
			if (S.substring(0,1) == "0") start = S.substring(1,2); else start = S.substring(0,2);
			if (S.substring(3,5) == "15") start += ".25";
			if (S.substring(3,5) == "30") start += ".5";
			if (S.substring(3,5) == "45") start += ".75";
			
			if (S.substring(6,7) == "0") end = S.substring(7,8); else end = S.substring(6,8);
			if (S.substring(9,11) == "15") end += ".25";
			if (S.substring(9,11) == "30") end += ".5";
			if (S.substring(9,11) == "45") end += ".75";
			 
		} 
		PayTime retval = new PayTime();
		retval.start = Integer.parseInt(start);
		retval.end = Integer.parseInt(end);
		return retval;
	}	


	
	public static PayTimes getpaytime(String geb_code) {
		PayTimes retval = new PayTimes();
		
		String sql1 = "select * from _site1493_dbsynch_paytimes where gebiedcode='"+geb_code+"'";
		com.glimworm.common.database.xsd.DataSet ds = com.glimworm.common.database.gwDataUtils.getArray(com.glimworm.common.database.GWDBBean.sqlStatic(sql1),false);
		if (ds != null && ds.rows() > 0) {
			retval.geb_code = ds.getString(0,"gebiedcode");
			retval.days[0] = decodeDate(ds.getString(0,"zo"));
			retval.days[1] = decodeDate(ds.getString(0,"ma"));
			retval.days[2] = decodeDate(ds.getString(0,"di"));
			retval.days[3] = decodeDate(ds.getString(0,"wo"));
			retval.days[4] = decodeDate(ds.getString(0,"do"));
			retval.days[5] = decodeDate(ds.getString(0,"vr"));
			retval.days[6] = decodeDate(ds.getString(0,"za"));
		}
		return retval;
	}
	
	
	public static PayTimes getcosts(Meter meter) {
		PayTimes retval = new PayTimes();
		
		Gebcode gebcode = getgebcode(meter);
		if (gebcode.geb_code.trim().length() == 0) {
			retval.cost = -1;
			return retval;	
		}

		String tcode = gebcode.t_code;
		String price_code = gebcode.price_code;
		String geb_code = gebcode.geb_code;

		Costs cost = getcost(price_code);
		if (cost.t_code.trim().length() == 0) {
			retval.cost = -2;
			return retval;	
		}

		PayTimes pt = getpaytime(geb_code);
		if (pt.geb_code.trim().length() == 0) {
			retval.cost = -3;
			return retval;	
		}	
		
		retval = pt;
		retval.t_code = cost.t_code;
		retval.first = cost.first;
		retval.cost = cost.cost;
		retval.max = gebcode.max;
		
		return retval;
	}
	
	public static boolean isInPaidParking(double lat, double lon) {
		
		return com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.isin(lat, lon, com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.inPaidParking.polys);	// garages
		
//		String S = "http://api.citysdk.waag.org/nodes?layer=divv.parking.zone&lat="+lat+"&lon="+lon+"&radius=1";
//		com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse res = com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(S, "", null, null, null, null);
//		System.out.println(res.text);
//		org.json.JSONObject jsob = com.glimworm.common.utils.jsonUtils.string2json(res.text);
//		org.json.JSONArray ar = jsob.optJSONArray("results");
//		if (ar != null && ar.length() > 0) return true;
//		return false;
	}
	
	public static String dp2(double d) {
		return Double.toString(d);
	}
	public static String dp2(int d) {
		return Integer.toString(d);
	}
	public static int asint(double d) {
		return new Double(d).intValue();
	}
	
	public static Meter[] smeters = null;
	public static PlaceParkingGarage[] sgarages = null;

	public static javolution.util.FastMap<String, String> chance_day = new javolution.util.FastMap<String,String>().setShared(true);
	public static javolution.util.FastMap<String, String> chance_sat = new javolution.util.FastMap<String,String>().setShared(true);
	public static javolution.util.FastMap<String, String> chance_sun = new javolution.util.FastMap<String,String>().setShared(true);
	public static boolean populate_meters_lock = false;

	public static void populate_meters() {
		if (populate_meters_lock == true) return;
		populate_meters_lock = true;
		
		String sql = "select a.*,p.cash,p.creditcard,p.pin,p.chip from _site1493_dbsynch_automats a left join _site1493_dbsynch_paymethods p on (a.typeautomaat = p.type) ";
		java.sql.ResultSet rs = com.glimworm.common.database.GWDBBean.sqlStatic(sql);
		com.glimworm.common.database.xsd.DataSet automats = com.glimworm.common.database.gwDataUtils.getArray(rs,false);

		Vector<Meter> vect = new Vector<Meter>();
		
//		smeters = new Meter[automats.rows()];
//		int cnt = 0;
//		for (int i=0; i < automats.rows(); i++) {
//			Meter _meter = new Meter();
//			_meter.i = i;
//			_meter.entityid = automats.getString(i, "entityid");
//			_meter.stadsdeel = automats.getString(i, "stadsdeel");
//			_meter.belnummer = automats.getString(i, "belnummer");
//			_meter.adres = automats.getString(i, "adres");
//			_meter.postcode = automats.getString(i, "postcode");
//			_meter.woonplaats = automats.getString(i, "woonplaats");
//			_meter.typeautomaat = automats.getString(i, "typeautomaat");
//			_meter.betaalwijze = automats.getString(i, "betaalwijze");
//			_meter.tariefcode = automats.getString(i, "tariefcode");
//			_meter.status = automats.getString(i, "status");
//			_meter.lat = automats.getDouble(i, "lon",0);
//			_meter.lon = automats.getDouble(i, "lat",0);
//			_meter.csdkzone = automats.getString(i, "csdkzone");
//			_meter.chance_weekday = automats.getString(i, "chance_day");
//			_meter.chance_sat = automats.getString(i, "chance_sat");
//			_meter.chance_sun = automats.getString(i, "chance_sun");
//
//			_meter.bw.cash = automats.getString(i, "cash").equalsIgnoreCase("Y");
//			_meter.bw.creditcard = automats.getString(i, "creditcard").equalsIgnoreCase("Y");
//			_meter.bw.pin = automats.getString(i, "pin").equalsIgnoreCase("Y");
//			_meter.bw.chip = automats.getString(i, "chip").equalsIgnoreCase("Y");
//			
//			_meter.costs = getcosts(_meter);
//			_meter.type = "on-street-meter";
//			try {
//				_meter.isInNorth = isInNorth(_meter.lat,_meter.lon);
//			} catch (Exception E) {
//				_meter.isInNorth = false;
//			}			
//			
//			vect.add(_meter);
//			
//			cnt++;
//		}

//		int cnt_meters = cnt;
		
		

//		PlaceParkingGarage[] areas = com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.getMeters();
//		for (int i=0; i < areas.length; i++) {
//			Meter _meter = new Meter();
//			_meter.i = cnt;
//			_meter.entityid = "";	//?
//			_meter.stadsdeel = "";	//?
//			_meter.belnummer = "";	//?
//			_meter.adres = automats.getString(i, "adres");
//			_meter.postcode = automats.getString(i, "postcode");
//			_meter.woonplaats = automats.getString(i, "woonplaats");
//			_meter.typeautomaat = automats.getString(i, "typeautomaat");
//			_meter.betaalwijze = automats.getString(i, "betaalwijze");
//			_meter.tariefcode = automats.getString(i, "tariefcode");
//			_meter.status = automats.getString(i, "status");
//			_meter.lat = automats.getDouble(i, "lon",0);
//			_meter.lon = automats.getDouble(i, "lat",0);
//			_meter.csdkzone = automats.getString(i, "csdkzone");
//			_meter.chance_weekday = automats.getString(i, "chance_day");
//			_meter.chance_sat = automats.getString(i, "chance_sat");
//			_meter.chance_sun = automats.getString(i, "chance_sun");

//			_meter.bw.cash = automats.getString(i, "cash").equalsIgnoreCase("Y");
//			_meter.bw.creditcard = automats.getString(i, "creditcard").equalsIgnoreCase("Y");
//			_meter.bw.pin = automats.getString(i, "pin").equalsIgnoreCase("Y");
//			_meter.bw.chip = automats.getString(i, "chip").equalsIgnoreCase("Y");
			
//			_meter.costs = getcosts(_meter);
//			_meter.type = "on-street-meter";
//			try {
//				_meter.isInNorth = isInNorth(_meter.lat,_meter.lon);
//			} catch (Exception E) {
//				_meter.isInNorth = false;
//			}			
//		}
		
		
		com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam._importlog.clear();
		PlaceParkingGarage[] garages = com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.getGarages();	// garages
		PlaceParkingGarage[] npr_areas = com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.getMeters();	// areas
		com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.downloadmeters(npr_areas);
		com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.loadgeojson();
		
		int cnt = 0;
		for (Meter _meter : com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.smeters) {
			_meter.i = cnt;
			_meter.displaytype = _meter.type;
			vect.add(_meter);
			cnt++;
		}
		int cnt_meters = cnt;

		
		
		for (int i=0; i < garages.length; i++) {
			Meter _meter = new Meter();
			_meter.i = cnt;
			_meter.entityid = garages[i].nprid;
			_meter.stadsdeel = "";
			_meter.belnummer = garages[i].nprid;
			_meter.adres = garages[i].street;
			_meter.postcode = garages[i].postcode;
			_meter.woonplaats = "";
			_meter.typeautomaat = "";
			_meter.betaalwijze = "";
			_meter.tariefcode = "";
			_meter.status = "";
			_meter.name = garages[i].name;
			_meter.lat = garages[i].lat;
			_meter.lon = garages[i].lon;
			_meter.csdkid = garages[i].csdkid;
			_meter.csdkurl = garages[i].csdkurl;
			_meter.nprid = garages[i].nprid;
			_meter.nprurl = garages[i].nprurl;

			_meter.bw.cash = true;
			_meter.bw.creditcard = true;
			_meter.bw.pin = true;
			_meter.bw.chip = true;
			
			_meter.costs = null;
			_meter.type = "garage";
			_meter.displaytype = (garages[i].displaytype != null && garages[i].displaytype.trim().length() > 0) ? garages[i].displaytype : _meter.type;
			try {
				_meter.isInNorth = isInNorth(_meter.lat,_meter.lon);
			} catch (Exception E) {
				_meter.isInNorth = false;
			}			
			_meter.garageid = i;
			
			vect.add(_meter);
			cnt++;
		}
		
		
		Object result[] = new Meter[vect.size()];
		vect.copyInto(result);
		smeters = (Meter[])result;
		sgarages = garages;

		
//		class add_zone implements Callable<Integer>{
//			private Meter meter;
//			
//			@Override
//			public Integer call() throws Exception {
//				String _URL = "http://test-api.citysdk.waag.org/nodes";
//				String _PARAMS = "lon="+this.meter.lon+"&lat="+this.meter.lat+"&layer=test.divv.parking.zone&radius=10&per_page=1";
//				com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse cr =  com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(_URL, _PARAMS, null, null, null, null, null);
//				try {
//					org.json.JSONObject jsob = jsonUtils.string2json(cr.text);
//					org.json.JSONArray results = jsob.optJSONArray("results");
//					org.json.JSONObject layers = results.getJSONObject(0).optJSONObject("layers");
//					String cdkid = results.getJSONObject(0).optString("cdk_id","");
//					this.meter.csdkzone = cdkid;
//					if (cdkid.length() > 0) {
//						if (chance_day.containsKey(cdkid)) {
//							this.meter.chance_weekday = chance_day.get(cdkid);
//							this.meter.chance_sat = chance_sat.get(cdkid);
//							this.meter.chance_sun = chance_sun.get(cdkid);
//						}
//						_URL = "http://test-api.citysdk.waag.org/"+cdkid;
//						_PARAMS = "layer=test.divv.parking.zone.chance";
//						com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse cr1 =  com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(_URL, _PARAMS, null, null, null, null, null);
//						org.json.JSONObject jsob1 = jsonUtils.string2json(cr1.text);
//						org.json.JSONArray results1 = jsob1.optJSONArray("results");
//						if (results1 != null) {
//							org.json.JSONObject layers1 = results1.getJSONObject(0).optJSONObject("layers");
//							org.json.JSONObject chances = layers1.optJSONObject("test.divv.parking.zone.chance");
//							
//							this.meter.chance_weekday = chances.optString("gemiddelde werkdag");
//							this.meter.chance_sat = chances.optString("gemiddelde zaterdag");
//							this.meter.chance_sun = chances.optString("gemiddelde zondag");
//							chance_day.put(cdkid, chances.optString("gemiddelde werkdag"));
//							chance_sat.put(cdkid, chances.optString("gemiddelde zaterdag"));
//							chance_sun.put(cdkid, chances.optString("gemiddelde zondag"));
//						} else {
//							System.out.println(this.meter.i+" ) not found for "+cdkid+"\n"+cr1.text+"\n"+cr.text+"\n\n");
//							chance_day.put(cdkid, "");
//							chance_sat.put(cdkid, "");
//							chance_sun.put(cdkid, "");
//						}
//					}
//					
//				} catch (Exception E) {
//					E.printStackTrace(System.out);
//				}				
//				return new Integer(0);
//			}
//			
//			public add_zone(Meter METER) {
//				this.meter = METER;
//			}
//		}
//
//		ExecutorService executor = Executors.newFixedThreadPool(50);
//	    List<Future<Integer>> list = new ArrayList<Future<Integer>>();
//	    for (int i=0 ; i < cnt_meters; i ++) {
//	    	Callable<Integer> worker = (Callable<Integer>) new add_zone(smeters[i]);
//	    	Future<Integer> submit = executor.submit(worker);
//	    	list.add(submit);
//		}
//	    executor.shutdown();
//	    
//	    try {
//	    	executor.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS);
//		    System.out.println("Finished all threads");
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//		    System.out.println("Interrupted");
//			e.printStackTrace();
//		}
		populate_meters_lock = false;
	    System.out.println("FINSHED!!!");


//	    for (int i=0 ; i < cnt_meters; i ++) {
//	    	System.out.println(smeters[i].belnummer + " / " + smeters[i].csdkzone + "/" + smeters[i].chance_weekday);
//	    }
		
	}
	
	public static String find_nearest(double lat, double lon, double rate) {
		
		
		if (smeters == null) {
			if (populate_meters_lock == true) return "";
			populate_meters();
		}

		Meter[] meters = smeters.clone();

		location loc = new location(lat, lon);

		int nearest = -1;
		double nearest_distance = -1;

		for (int i=0; i < meters.length; i++) {
			Meter meter = meters[i];
			PayTimes costs = getcosts(meter);

			if (rate > 0) {
				if (costs.cost < rate) continue;
			}

			double d = distance(loc, new location(meter.lat, meter.lon));
			if (nearest == -1 || d < nearest_distance) {
				nearest = i;
				nearest_distance = d;
			}
		}
		
		String retval = "{";
		retval += Q("id")+":"+nearest+",";
		retval += Q("meterlat")+":"+meters[nearest].lat+",";
		retval += Q("meterlon")+":"+meters[nearest].lon+",";
		retval += Q("meternum")+":"+meters[nearest].belnummer+",";
		retval += Q("dist")+":"+nearest_distance+"";
		retval += "}";
		
		return retval;
		
	}
	

	public static PlaceParkingGarage getGarageByGarageid (int id) {
		if (id < sgarages.length) return sgarages[id];
		return null;
	}
	public static Meter getMeterById(String meternumber) {
		if (smeters == null) {
			if (populate_meters_lock == true) return null;
			populate_meters();
		}
		for (int i=0; i < smeters.length; i++) {
			if (smeters[i].belnummer.equalsIgnoreCase(meternumber)) return smeters[i];
		}
		return null;
	}
	public static PlaceParkingGarage getGarageByNPRid (String id) {
		for (int i=0; i < sgarages.length; i++) {
			if (sgarages[i].nprid.equalsIgnoreCase(id)) return sgarages[i];
		}
		return null;
	}
	public static Meter getMeterByNPRid(String meternumber) {
		if (smeters == null) {
			if (populate_meters_lock == true) return null;
			populate_meters();
		}
		for (int i=0; i < smeters.length; i++) {
			if (smeters[i].nprid.equalsIgnoreCase(meternumber)) return smeters[i];
		}
		return null;
	}

	public static int getMeterIndexById(String meternumber) {
		if (smeters == null) {
			if (populate_meters_lock == true) return -1;
			populate_meters();
		}
		for (int i=0; i < smeters.length; i++) {
			if (smeters[i].belnummer.equalsIgnoreCase(meternumber)) return i;
		}
		return -1;
	}
	
	public static String getMeters(double north, double south, double east, double west) {
		if (smeters == null) {
			if (populate_meters_lock == true) return "";
			populate_meters();
		}

		class a {
			public double dist = 0;
			public double cost = 0;
			public String belnummer = "";
			public String address = "";
			public double lat = 0;
			public double lon = 0;
			public int i = 0;
		}
		ArrayList<a> al = new ArrayList<a>();
		
		
		for (int i=0; i < smeters.length; i++) {
			if (smeters[i].lon >= west && smeters[i].lon <= east && smeters[i].lat >= south && smeters[i].lat <= north) {
				a ameter = new a();
				ameter.lat = smeters[i].lat;
				ameter.lon = smeters[i].lon;
				ameter.address = smeters[i].adres;
				ameter.belnummer = smeters[i].belnummer;
				al.add(ameter);
			}
		}
		
		String retval2 = "";
		for (int j=0; j < al.size(); j++) {
			if (j > 0) retval2 += ",";
			retval2 += "["+Q(al.get(j).belnummer)+",0,0,"+Q(al.get(j).address)+","+al.get(j).lat+","+al.get(j).lon+","+j+","+al.get(j).i+"]";
		}
		
		String retval = "{"+Q("items")+":["+retval2+"]}";
		
		return retval;
		
	}
	public static String getMeters(String meternumber) {
		
		if (smeters == null) {
			if (populate_meters_lock == true) return "{}";
			populate_meters();
		}
		
		String[] meternumbers = meternumber.split("[,]");

		Meter[] meters = new Meter[meternumbers.length];
		
		// add distance	
		for (int i=0; i < meternumbers.length; i++) {
			meters[i] = getMeterById(meternumbers[i]);
		}
		
		String retval = Response.obj2json(meters);
		return retval;		
//		return "{"+retval+"}";
	}
	
	public static String calc(int _day, int hrs, int mins, double duration, double from_lat, double from_lon, String _paymethods) {
		return calc( _day,  hrs,  mins,  duration,  from_lat,  from_lon,  _paymethods,0);
	}
	
	public static String calc(int _day, int hrs, int mins, double duration, double from_lat, double from_lon, String _paymethods,int fmt) {
		ParkSharkCalcReturn obj = calcv2(_day, hrs, mins, duration, from_lat, from_lon,_paymethods, fmt);
		return obj.text;
	}
	
	public static javolution.util.FastMap<String, Integer> costsmap = new javolution.util.FastMap<String,Integer>().setShared(true);
	public static boolean costsmap_loaded = false;
	public static boolean populate_costmap_lock = false;

	public static String getSignature(Meter meter) {
		String rv = meter.costs.getSignature();
		if (meter.costsA != null && meter.costsA.length > 0) {
			System.out.println("WAS SIG ["+rv+"] len_("+meter.costsA.length+")");
			rv = "";
			for (int i=0; i < meter.costsA.length; i++) {
				String _sig = meter.costsA[i].getSignature();
				rv += "|c|"+_sig;
			}
			System.out.println("WILL BE SIG WITH COSTS ["+rv+"]");
		}
		if (meter.exclusions != null && meter.exclusions.length > 0) {
			for (int i=0; i < meter.exclusions.length; i++) {
				rv += "|e|"+meter.exclusions[i].getSignature();
			}
			System.out.println("WILL BE SIG WITH EXCLUSIONS ["+rv+"]");
		}
		return rv;
	}

		
	public static void populate_costmap() {

		if (populate_costmap_lock == true) return;
		populate_costmap_lock = true;

		for (int i=0; i < smeters.length; i++) {
			if (smeters[i].costs != null) {
//				String sig = smeters[i].costs.getSignature();
				String sig = getSignature(smeters[i]);
				if (costsmap.containsKey(sig) == false) {
					costsmap.put(sig, new Integer(i));
				}
			}
			if (smeters[i].garageid != -1) {
				String sig = "garage-"+smeters[i].garageid;
				if (costsmap.containsKey(sig) == false) {
					costsmap.put(sig, new Integer(i));
				}
			}

		}
		
		populate_costmap_lock = false;
		costsmap_loaded = true;
	}

	public static String getChance(String chance, int hr) {
		if (chance == null || chance.trim().length() == 0) return "";
		String[] ch = chance.split("[;]");
		if (hr < ch.length) return ch[hr].split("[:]")[1];
		return "";
	}
	
	public static ParkSharkCalcReturn calcv2(int _day, int hrs, int mins, double duration, double from_lat, double from_lon, String _paymethods,int fmt) {
		ParkSharkCalcRequest req = new ParkSharkCalcRequest();
		req._day = _day;
		req.hrs = hrs;
		req.mins = mins;
		req.duration = duration;
		req.from_lat = from_lat;
		req.from_lon = from_lon;
		req._paymethods = _paymethods;
		req.fmt = fmt;
		return calcv2(req);
		
	}
	
	public static int timevalue(int hrs, int mins) {
		return (hrs * 60)+mins;
	}
	public static int timevalue(String hrsmins) {
		if (hrsmins == null) return 0;
		if (hrsmins.length() == 2) return Integer.parseInt(hrsmins);
		if (hrsmins.length() == 3) return Integer.parseInt(hrsmins.substring(0,1)) * 60 + Integer.parseInt(hrsmins.substring(1));
		if (hrsmins.length() == 4) return Integer.parseInt(hrsmins.substring(0,2)) * 60 + Integer.parseInt(hrsmins.substring(2));
		return 0;
	}
	static int MINSINDAY = (24*60);
	public static int endday(int day, int timevalue, double duration) {
		long endtime = timevalue + Math.round(duration * 60);
		double ddays = Math.floor(endtime / MINSINDAY);
		int days = new Double(ddays).intValue();
		return (days % 7);
	}
	public static int endtime(int day, int timevalue, double duration) {
		long endtime = timevalue + Math.round(duration * 60);
		long endingtime = (endtime % MINSINDAY);
		return new Double(endingtime).intValue();
	}
	
	public static ParkSharkCalcReturn calcv2(ParkSharkCalcRequest req) {
		int _day = req._day;
		int hrs = req.hrs;
		int mins = req.mins;
		double duration = req.duration;
		double from_lat = req.from_lat;
		double from_lon = req.from_lon;
		String _paymethods = req._paymethods;
		int fmt = req.fmt;
		
		int starttime = timevalue(hrs,mins);
		int endtime = endtime(_day, starttime, duration);
		int endday = endday(_day, starttime, duration);
		
		boolean dbg = (req.dbg.equalsIgnoreCase("y"));
		boolean log = (req.log.equalsIgnoreCase("y"));
		
		
		
//		var day = $ef.currentdata.date_start.dt.getDay();		// 0 = sunday
//		var duration = $ef.currentdata.duration;
//		var hrs = $ef.currentdata.date_start.dt.getHours();
//		var mins = $ef.currentdata.date_start.dt.getMinutes();
		
		long _exdt = new Date().getTime();
		ParkSharkCalcReturn ret = new ParkSharkCalcReturn();
		
		
		// make a table in the format
		//
		//	[{dayofweek, starttime, endtime}]

		class calculate_cost implements Callable<Integer>{
//			private Meter meter;
			private int i = 0;
			private String sig = "";
			private ArrayList<Days> days;
			private boolean DBG;
			private javolution.util.FastMap<String, Double> costmap;
			private javolution.util.FastMap<String, String> dbgmap;
			private boolean isInAmsterdamCentrum = true;
			private int total_parking_minutes = 0;
			private int match = 1;
			
			@Override
			public Integer call() throws Exception {
				try {
					if (smeters[i].garageid > -1) {
						if (sgarages[smeters[i].garageid].time_unit_minutes > -1 && sgarages[smeters[i].garageid].price_per_time_unit > -1) {
							double val = 0;
							String dbg = "";
							for (int j=0; j < days.size(); j++) {
		
								// we have to clone the array
								Days day = new Days();
								day.day = days.get(j).day;
								day.start = days.get(j).start;
								day.end = days.get(j).end;
								double dayhours = (day.end - day.start);
								double daycost = Math.ceil((dayhours * 60) / sgarages[smeters[i].garageid].time_unit_minutes) * sgarages[smeters[i].garageid].price_per_time_unit;
								if (sgarages[smeters[i].garageid].price_day > -1 && sgarages[smeters[i].garageid].price_day < daycost) daycost = sgarages[smeters[i].garageid].price_day;
								
								val += daycost;
							}
							
							if (isInAmsterdamCentrum && sgarages[smeters[i].garageid].ams_pr_fare != null && sgarages[smeters[i].garageid].ams_pr_fare.trim().length() > 0) {
								foundprice:
								for (int k=0; k < sgarages[smeters[i].garageid].ams_pr_fares.length; k++) {
									if (days.get(0).day >= sgarages[smeters[i].garageid].ams_pr_fares[k].dayOfWeek_start && days.get(0).day <= sgarages[smeters[i].garageid].ams_pr_fares[k].dayOfWeek_end) {
										if (days.get(0).start >= sgarages[smeters[i].garageid].ams_pr_fares[k].entry_start && days.get(0).start <= sgarages[smeters[i].garageid].ams_pr_fares[k].entry_end) {
											val = sgarages[smeters[i].garageid].ams_pr_fares[k].price_day;
											break foundprice;
										}
									}
								}
							}
							
							costmap.put(sig, new Double(val));
							dbgmap.put(sig, dbg);
						} 
						return new Integer(0);
					}
	
					PayTimes costs = smeters[i].costs;
					PayTimes[] costsA = smeters[i].costsA;
					PayTimes[] exclusions = smeters[i].exclusions;
					
					if (exclusions != null) {
						for (int ii=0; ii < exclusions.length; ii++) {
							nextday:
							for (int j=0; j < days.size(); j++) {
								
								// we have to clone the array
								Days day = new Days();
								day.day = days.get(j).day;
								day.start = days.get(j).start;
								day.end = days.get(j).end;
								
								PayTime cday = exclusions[ii].days[day.day];
								
								if (cday.start == 0 && cday.end == 0) continue nextday;	// no restriction!
								if (day.start < cday.start) day.start = cday.start;
								if (day.end > cday.end) day.end = cday.end;
								double dayhours = (day.end - day.start);
								double daymins = dayhours*60;
								
								if (dayhours > 0) {
									if (exclusions[ii].parkingProhibited == true) {
										costmap.put(sig, new Double(-2));
										dbgmap.put(sig, "(EXCLUDED_BY_EXCLUSION_"+exclusions[ii].NPRtariffDescription+"_PROHIBITED_DAYNUM_"+day.day+"_CDAY_"+cday.start+"-"+cday.end+"_DAY_"+day.start+"-"+day.end+")");
										return -2;
									}
									if (exclusions[ii].durationRestriction == true && daymins > exclusions[ii].maximumDuration) {
										costmap.put(sig, new Double(-3));
										dbgmap.put(sig, "(EXCLUDED_BY_EXCLUSION_"+exclusions[ii].NPRtariffDescription+"_DAYHOURS_"+dayhours+"_GREATER_THAN_"+exclusions[ii].maximumDuration+")");
										return -3;
									}
								}
							}
						}
					}
					
					if (costsA == null) costsA = new PayTimes[] {costs};

					
					double val = 0;
					String dbg = "";

					for (int ii=0; ii < costsA.length; ii++) {
						costs = costsA[ii];
						
						if (costs.cost < 0) {
							continue;
							//return new Integer(0);
						}
		
						
						for (int j=0; j < days.size(); j++) {
		
							// we have to clone the array
							Days day = new Days();
							day.day = days.get(j).day;
							day.start = days.get(j).start;
							day.end = days.get(j).end;
							
							PayTime cday = costs.days[day.day];
							
							if (DBG) dbg += "(day_"+j+"_start_"+dp2(day.start)+"_end_"+dp2(day.end)+"_costs_"+costs.cost+"_dday_"+day.day+")\n\r";
							if (DBG) dbg += "(day_"+j+"_gebcode_"+costs.geb_code+"_firstcombination_"+costs.first.combination+"_firsthour_"+costs.first.hrs+"_firstprice_"+costs.first.price+")\n\r";
							if (DBG) dbg += "(day_"+j+"_cday_start_"+cday.start+"_cday_end_"+cday.end+")\n\r";
							if (cday.start == 0 && cday.end == 0) continue;
							if (DBG) dbg += "(cday_start_and_end_non_zero)";
							if (day.start > cday.end) continue;
							if (DBG) dbg += "(day_start_greater_than_cday_start)";
							if (day.end < cday.start) continue;
							if (DBG) dbg += "(day_end_less_than_cday_end)";
							if (day.start < cday.start) {
								day.start = cday.start;
								if (DBG) dbg += "(day_start_WAS_less_than_cday_start)";
							}
							if (day.end > cday.end) {
								day.end = cday.end;
								if (DBG) dbg += "(day_end_WAS_more_than_cday_end)";
							}
							double dayhours = (day.end - day.start);
							if (DBG) dbg += "(day_hours:"+dayhours+")";
				
							if (dayhours > 9) {
								// you never pay more than 9 hours
								dayhours = 9;
								if (DBG) dbg += "(dayhours_WAS_more_than_9)";
							} else if (cday.start == 9 && cday.end == 24 && day.start == 9 && day.end == 19) {
								// day card = 6h
								dayhours = 6;
								if (DBG) dbg += "(cday_9_to_24_and_day_WAS_9_to_19_NOW_6h_daycard)";
							} else if (cday.start == 9 && cday.end == 24 && day.start == 19 && day.end == 24) {
								// evening card = 4h
								dayhours = 4;
								if (DBG) dbg += "(cday_9_to_24_and_day_WAS_19_to_24_NOW_4h_eveningcard)";
							} else if (cday.start == 12 && cday.end == 24 && dayhours > 7.2) {
								// sundaycard = 7.2h
								dayhours = 7.2;
								if (DBG) dbg += "(sunday_and_cday_12_to_24_and_dayhours_WAS_greater_than_7.2_NOW_7.2_sunday_card)";
							}
				
							int el = 0;
							if (costs.first.combination.equalsIgnoreCase("y") && j == 0) {
								if (dayhours <= costs.first.hrs) {
									val += (dayhours * costs.first.price);
									if (DBG) dbg += "f\n";
									if (DBG) dbg += "(combination_"+(dayhours * costs.first.price)+"_added_to_val[yj:"+j+"/s:"+day.start+"/e:"+day.end+"/c:"+costs.cost+"/hrs:"+dayhours+"(all in first hours),val="+val+"])\n";
								} else {
									val += (costs.first.hrs * costs.first.price);
									val += ((dayhours - costs.first.hrs) * costs.cost);
				
									if (DBG) dbg += "f\n";
									if (DBG) dbg += "(combination_"+(costs.first.hrs * costs.first.price)+"_and_"+((dayhours - costs.first.hrs) * costs.cost)+"_added_to_val[yj:"+j+"/s:"+day.start+"/e:"+day.end+"/c:"+costs.cost+"/hrs:"+dayhours+"(some in first hours),val="+val+"])\n";
								}
							} else {
								val += (dayhours * costs.cost);
								if (DBG) dbg += "f\n";
								if (DBG) dbg += "(costs_"+(dayhours * costs.cost)+"_added_to_val[yj:"+j+"/s:"+day.start+"/e:"+day.end+"/c:"+costs.cost+"/hrs:"+dayhours+"(none in first hours),val="+val+"]\n";
							}
							
							if (costs.max > 0 && total_parking_minutes > costs.max) {
								if (DBG) dbg += "(COSTSMAX_"+costs.NPRtariffDescription+"_"+(costs.max)+"_GREATER_THAN_TOTALMINS_"+total_parking_minutes+"_THEREFORE_EXCLUDED)\n";
								match = 0;
							}
							
							
							
						}
					}
	
					if (DBG) dbg += ("(end_value_[val="+val+"])");
					if (DBG) dbg += ("[val(converted)="+new Double(val).toString()+"]");
					
					if (match == 0) val = -1;
					
					costmap.put(sig, new Double(val));
					dbgmap.put(sig, dbg);
				} catch (Exception E) {
					E.printStackTrace(System.out);
				}
				return new Integer(0);
			}
			
			
			public calculate_cost(int I, String SIG, boolean _DBG, javolution.util.FastMap<String, Double> COSTMAP, javolution.util.FastMap<String, String> DBGMAP, ArrayList<Days> DAYS, boolean isInAmsterdamCentrum, int total_parking_minutesP) {
				this.i = I;
				this.sig = SIG;
				this.DBG = _DBG;
				this.costmap = COSTMAP;
				this.dbgmap = DBGMAP;
				this.days = DAYS;
				this.isInAmsterdamCentrum = isInAmsterdamCentrum;
				this.total_parking_minutes = total_parking_minutesP;
			}
		}		
		
		
		
		ArrayList<Days> days = new ArrayList<Days>();

		// tim is decimal i.e 22.5 or 23.75
		int total_parking_minutes = new Double(duration*60).intValue();
		
		int tim = hrs;
		if (hrs > 0) tim += (mins/60);
	
		while (true) {	
			int remainder = (24 - tim);
			if (remainder > duration) {
				Days d = new Days(_day, tim, (tim+new Double(duration).intValue()));
				days.add(d);
				break;
			} else {
				Days d = new Days(_day, tim,24);
				days.add(d);
				duration = duration - (24 - tim);
				tim = 0;
				_day++;
				if (_day > 6) _day = 0;
			}
		}
		
		System.out.println("START CALC");
		if (log) System.out.println("DAYS");
		if (log) System.out.println(days);
		com.thoughtworks.xstream.XStream xstream = new com.thoughtworks.xstream.XStream(new com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver());
		xstream.setMode(com.thoughtworks.xstream.XStream.NO_REFERENCES);
		if (log) System.out.println(xstream.toXML(days));

		ret.timings.add("calc : after days " + new Long(new Date().getTime() - _exdt));
		
		/*
		   entityid: 101
		   stadsdeel: Centrum
		   belnummer: 10101
		       adres: Herengracht 607
		    postcode: 1017CE
		  woonplaats: Amsterdam
		typeautomaat: CWO
		 betaalwijze: 5
		      status: OP
		  tariefcode: 1
		         lat: 4.8991499
		         lon: 52.3655281
		*/

		if (smeters == null || _paymethods.indexOf("reload") > -1) {
			if (populate_meters_lock == true) {
				ret.text = "";
				return ret;
			}

			populate_meters();
			
		}
		if (costsmap_loaded == false || _paymethods.indexOf("reload") > -1) {
			if (populate_costmap_lock == true) {
				ret.text = "";
				return ret;
			}
			
			populate_costmap();
		}

		boolean isInAmsterdamCentrum =  com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.isin(req.from_lat, req.from_lon, com.glimworm.opendata.parkshark.importdata.NPR.Amsterdam.inCentrum.polys);
		
		
		Meter[] meters = new Meter[smeters.length];
		
		location loc = new location(from_lat, from_lon);
		
		long start = new Date().getTime();
		
		int cc = 0;
		// add distance	

		boolean DBG = false;
		if (fmt == 3) DBG = true;
		if (dbg) DBG = true;

		javolution.util.FastMap<String, Double> costmap = new javolution.util.FastMap<String,Double>().setShared(true);
		javolution.util.FastMap<String, String> dbgmap = new javolution.util.FastMap<String,String>().setShared(true);
		
		ExecutorService executor = Executors.newFixedThreadPool(10);
	    List<Future<Integer>> list = new ArrayList<Future<Integer>>();
		for (FastMap.Entry<String, Integer> e = costsmap.head(), end = costsmap.tail(); (e = e.getNext()) != end;) {

			int i = e.getValue().intValue();
			String sig = e.getKey();
			
	    	Callable<Integer> worker = (Callable<Integer>) new calculate_cost(i, sig, DBG, costmap, dbgmap, days, isInAmsterdamCentrum,total_parking_minutes);
	    	
	    	Future<Integer> submit = executor.submit(worker);
	    	list.add(submit);
		}
	    executor.shutdown();
	    
	    try {
	    	executor.awaitTermination(30, java.util.concurrent.TimeUnit.SECONDS);
		    System.out.println("ParkShark CalcParking Finished all threads");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		    System.out.println("Interrupted");
			e.printStackTrace();
		}
		if (log) System.out.println(xstream.toXML(costmap));

		ret.timings.add("calc : after costs " + new Long(new Date().getTime() - _exdt));

			
//		for (FastMap.Entry<String, Integer> e = costsmap.head(), end = costsmap.tail(); (e = e.getNext()) != end;) {
//
//			int i = e.getValue().intValue();
//			String sig = e.getKey();
//
//			PayTimes costs = smeters[i].costs;
//			if (costs.cost < 0) {
//				continue;
//			}
//			double val = 0;
//			String dbg = "";
//			
//			for (int j=0; j < days.size(); j++) {
//				// we have to clone the array
//				Days day = new Days();
//				day.day = days.get(j).day;
//				day.start = days.get(j).start;
//				day.end = days.get(j).end;
//				
//				PayTime cday = costs.days[day.day];
//				
//				if (DBG) dbg += "[xj:"+j+"/s:"+dp2(day.start)+"/e:"+dp2(day.end)+"/c:"+costs.cost+"/d.d:"+day.day+"]\n";
//				if (DBG) dbg += "[xj:"+j+"/gc:"+costs.geb_code+"/fc:"+costs.first.combination+"/fh:"+costs.first.hrs+"/fp:"+costs.first.price+"]\n";
//				if (DBG) dbg += "[xj:"+j+"/s:"+cday.start+"/e:"+cday.end+"]\n";
//				if (cday.start == 0 && cday.end == 0) continue;
//				if (DBG) dbg += "a";
//				if (day.start > cday.end) continue;
//				if (DBG) dbg += "b";
//				if (day.end < cday.start) continue;
//				if (DBG) dbg += "c";
//				if (day.start < cday.start) day.start = cday.start;
//				if (DBG) dbg += "d";
//				if (day.end > cday.end) day.end = cday.end;
//				if (DBG) dbg += "e";
//				double dayhours = (day.end - day.start);
//	
//				if (dayhours > 9) {
//					// you never pay more than 9 hours
//					dayhours = 9;
//				} else if (cday.start == 9 && cday.end == 24 && day.start == 9 && day.end == 19) {
//					// day card = 6h
//					dayhours = 6;
//				} else if (cday.start == 9 && cday.end == 24 && day.start == 19 && day.end == 24) {
//					// evening card = 4h
//					dayhours = 4;
//				} else if (cday.start == 12 && cday.end == 24 && dayhours > 7.2) {
//					// sundaycard = 7.2h
//					dayhours = 7.2;
//				}
//	
//				int el = 0;
//				if (costs.first.combination.equalsIgnoreCase("y") && j == 0) {
//					if (dayhours <= costs.first.hrs) {
//						val += (dayhours * costs.first.price);
//						if (DBG) dbg += "f\n";
//						if (DBG) dbg += "[yj:"+j+"/s:"+day.start+"/e:"+day.end+"/c:"+costs.cost+"/hrs:"+dayhours+"(all in first hours),val="+val+"]\n";
//					} else {
//						val += (costs.first.hrs * costs.first.price);
//						val += ((dayhours - costs.first.hrs) * costs.cost);
//	
//						if (DBG) dbg += "f\n";
//						if (DBG) dbg += "[yj:"+j+"/s:"+day.start+"/e:"+day.end+"/c:"+costs.cost+"/hrs:"+dayhours+"(some in first hours),val="+val+"]\n";
//					}
//				} else {
//					val += (dayhours * costs.cost);
//					if (DBG) dbg += "f\n";
//					if (DBG) dbg += "[yj:"+j+"/s:"+day.start+"/e:"+day.end+"/c:"+costs.cost+"/hrs:"+dayhours+"(none in first hours),val="+val+"]\n";
//				}
//				
//				
//			}
//			if (DBG) dbg += ("[val="+val+"]");
//			if (DBG) dbg += ("[val(converted)="+new Double(val).toString()+"]");
//			
//			costmap.put(sig, new Double(val));
//			dbgmap.put(sig, dbg);
//		}
//		System.out.println(xstream.toXML(costmap));
		
		// now we have calculated all of the costs per signature
		
		
		String[] paymethods = _paymethods.split("[,]");


		for (int i=0; i < meters.length; i++) {
			meters[i] = new Meter();
			meters[i].lat = smeters[i].lat;
			meters[i].lon = smeters[i].lon;
			meters[i].bw = smeters[i].bw;
			meters[i].i = i;
			meters[i].type = smeters[i].type;
			
			if (_day < 5) meters[i].expected_occupancy = getChance(smeters[i].chance_weekday,_day);
			if (_day == 5) meters[i].expected_occupancy = getChance(smeters[i].chance_sat,_day);
			if (_day == 6) meters[i].expected_occupancy = getChance(smeters[i].chance_sun,_day);
			
			Meter meter = meters[i];
//			if (meter.match == 0) continue;
			
			/* MATCH */
			int match = 0;
			if (meter.cost < 0) {
				match++;
			} else {
				if (has("cash",paymethods) && meter.bw.cash == true) match++;
				if (has("creditcard",paymethods) && meter.bw.creditcard == true) match++;
				if (has("pin",paymethods) && meter.bw.pin == true) match++;
				if (has("chipknip",paymethods) && meter.bw.chip == true) match++;
			}
			if (meter.max > 0 && total_parking_minutes > meter.max) match = 0;
			meters[i].match = match;
			
			if (DBG) meters[i].dbg += ("[match="+match+"]");
			
			
			
			double d = distance(loc, new location(meter.lat, meter.lon));
			meters[i].dist = d;

			if (smeters[i].garageid > -1 && meters[i].match > 0 && (sgarages[smeters[i].garageid].opening_times_raw == null || sgarages[smeters[i].garageid].opening_times_raw.trim().length() == 0)) {
				meters[i].match = 0;
			}

			// check if the opingin times match for for garages if the match if currently positive
			if (smeters[i].garageid > -1 && meters[i].match > 0 && sgarages[smeters[i].garageid].opening_times_raw != null && sgarages[smeters[i].garageid].opening_times_raw.trim().length() > 0) {
				System.out.println("--) "+i);
				System.out.println(smeters[i].garageid);
				System.out.println(sgarages.length);
				System.out.println(_day);
				System.out.println(sgarages[smeters[i].garageid].name);
				System.out.println(sgarages[smeters[i].garageid].opening_times_raw);
				System.out.println(sgarages[smeters[i].garageid].opening_times[_day].json());
				System.out.println(sgarages[smeters[i].garageid].opening_times[_day].open24());
				
				if (sgarages[smeters[i].garageid].opening_times[_day].closed24() == true) {
					meters[i].match = 0;
					if (DBG) meters[i].dbg += ("[match on closed24 failed d:"+_day+" ]");
				} else if (sgarages[smeters[i].garageid].opening_times[_day].open24() == false) {
					// if the garage is not open 24x7 and start parking time before open_in then fail the match
					if (starttime < timevalue(sgarages[smeters[i].garageid].opening_times[_day].open_in)) {
						meters[i].match = 0;
						if (DBG) meters[i].dbg += ("[match on opening in failed d:"+_day+", st:"+starttime+" openin : "+sgarages[smeters[i].garageid].opening_times[_day].open_in+" openin_tv "+timevalue(sgarages[smeters[i].garageid].opening_times[_day].open_in)+"]");
					}
					if (starttime > timevalue(sgarages[smeters[i].garageid].opening_times[_day].close_in)) {
						meters[i].match = 0;
						if (DBG) meters[i].dbg += ("[match on opening in failed d:"+_day+", st:"+starttime+" closein : "+sgarages[smeters[i].garageid].opening_times[_day].close_in+" close_in_tv "+timevalue(sgarages[smeters[i].garageid].opening_times[_day].close_in)+"]");
					}
				}
				if (sgarages[smeters[i].garageid].opening_times[endday].closed24() == true) {
					meters[i].match = 0;
					if (DBG) meters[i].dbg += ("[match on closed24 failed d:"+endday+" ]");
				} else if (sgarages[smeters[i].garageid].opening_times[endday].open24() == false) {
					// if the garage is not open 24x7 and start parking time before open_in then fail the match
					if (endtime > timevalue(sgarages[smeters[i].garageid].opening_times[endday].close_out)) {
						meters[i].match = 0;
						if (DBG) meters[i].dbg += ("[match on close out failed d:"+endday+", st:"+endtime+" closeout : "+sgarages[smeters[i].garageid].opening_times[endday].close_out+" closeout_tv "+timevalue(sgarages[smeters[i].garageid].opening_times[endday].close_out)+"]");
					}
					if (endtime < timevalue(sgarages[smeters[i].garageid].opening_times[endday].open_out)) {
						meters[i].match = 0;
						if (DBG) meters[i].dbg += ("[match on close out failed d:"+endday+", st:"+endtime+" openout : "+sgarages[smeters[i].garageid].opening_times[endday].open_out+" openout_tv "+timevalue(sgarages[smeters[i].garageid].opening_times[endday].open_out)+"]");
					}
				}
			}
			
			if (smeters[i].garageid > -1) {
				String sig = "garage-"+smeters[i].garageid;
				if (costsmap.containsKey(sig) && costmap.get(sig) != null) {
					double val = costmap.get(sig).doubleValue();
					String _dbg = dbgmap.get(sig);
					meters[i].cost = val;
					meters[i].totalcost = val;
					meters[i].dbg = "sig "+sig + _dbg;
				} else {
					meters[i].cost = -1;
					meters[i].totalcost = -1;
					meters[i].dbg = "sig "+sig + " not found in costmap";
				}
				continue;
			}
			
			
			if (smeters[i].costs == null) {
				meters[i].cost = -1;
				meters[i].totalcost = -1;
				continue;
			}
			
			PayTimes costs = smeters[i].costs;

			if (costs.cost < 0) {
				meters[i].cost = costs.cost;
				continue;
			}
			meters[i].max = costs.max;
			
//			String sig = costs.getSignature();
			String sig = getSignature(smeters[i]);
			
			meters[i].costsignature = sig;
			if (costsmap.containsKey(sig) && costmap.get(sig) != null) {
				double val = costmap.get(sig).doubleValue();
				String _dbg = dbgmap.get(sig);
				meters[i].cost = val;
				meters[i].totalcost = val;
				meters[i].dbg = "sig :"+sig + "\ndebug : "+ _dbg;
				if (val < -1) meters[i].match = 0;	// if -2 or -3 is the valuse from the calculation then exclude from results
			}
			
		}

		ret.timings.add("calc : after matching " + new Long(new Date().getTime() - _exdt));

		System.out.println("TS1 " + (new Date().getTime() - start));
		System.out.println("END CALC");
		System.out.println("TS2 " + (new Date().getTime() - start));
		
		System.out.println("START SORT");

		class DistanceSort implements Comparator<Meter> {
		    // Comparator interface requires defining compare method.
		    public int compare(Meter a, Meter b) {
		        if (a.dist < b.dist) {return -1;}
		        if (a.dist > b.dist) {return 1;}
		        return 0;
		    }
		}
		
		Comparator<Meter> MeterByDistance = new DistanceSort();
		Arrays.sort(meters, MeterByDistance);

		ret.timings.add("calc : after sort " + new Long(new Date().getTime() - _exdt));
		
		System.out.println("END SORT");
		System.out.println("TS3 " + (new Date().getTime() - start));
		
		String retval = "";

		ArrayList<ParkSharkCalcReturnReccommendation> al = new ArrayList<ParkSharkCalcReturnReccommendation>();
		ArrayList<String> found_signatures = new ArrayList<String>();
		
		StringBuffer retvalsb = new StringBuffer(); 
		
		int metersfound = 0;
		int garagesfound = 0;
		int prfound = 0;
		int totalfound = 0;

		int metersmax = -1;		// unlimited by default
		int garagesmax = -1;
		int prmax = -1;
		int totalmax = -1;
		
		if (req.opt_metercount != null && req.opt_metercount.trim().length() > 0) {
			try { metersmax = Integer.parseInt(req.opt_metercount); } catch (Exception E) { metersmax = -1; }
		}
		if (req.opt_garagecount != null && req.opt_garagecount.trim().length() > 0) {
			try { garagesmax = Integer.parseInt(req.opt_garagecount); } catch (Exception E) { garagesmax = -1; }
		}
		if (req.opt_prcount != null && req.opt_prcount.trim().length() > 0) {
			try { prmax = Integer.parseInt(req.opt_prcount); } catch (Exception E) { prmax = -1; }
		}
		
		if (req.opt_maxresults != null && req.opt_maxresults.trim().length() > 0) {
			try { totalmax = Integer.parseInt(req.opt_maxresults); } catch (Exception E) { totalmax = -1; }
		}
		
		System.out.println("req.opt_metercount ["+req.opt_metercount+"] ["+metersmax+"]");
		System.out.println("req.opt_garagecount ["+req.opt_garagecount+"] ["+garagesmax+"]");
		System.out.println("req.opt_prcount ["+req.opt_prcount+"] ["+prmax+"]");
		System.out.println("req.opt_maxresults ["+req.opt_maxresults+"] ["+totalmax+"]");
		
		meterloop:
		for (int i=0; i < meters.length; i++) {
			
			if (totalmax > -1 && totalfound > totalmax) break meterloop;
//			System.out.println("d:"+meters[i].dist + "c:"+meters[i].cost + "a:"+meters[i].adres+" \n"+meters[i].dbg+"\n");
			int I = meters[i].i;
			if (i > 0) retvalsb.append(",");
			if (fmt == 3) {
				retvalsb.append("["+Q(smeters[I].belnummer)+","+meters[i].cost+","+asint(meters[i].dist)+","+meters[i].match+","+meters[i].i+","+meters[i].lat+","+meters[i].lon+","+Q(smeters[I].adres)+","+Q(meters[I].dbg.replace('\n', '|'))+"]");
			} else if (fmt == 2) {
				retvalsb.append("["+Q(smeters[I].belnummer)+","+meters[i].cost+","+asint(meters[i].dist)+","+meters[i].match+","+meters[i].i+","+meters[i].lat+","+meters[i].lon+","+Q(smeters[I].adres)+"]");
			} else if (fmt == 1) {
				retvalsb.append("["+Q(smeters[I].belnummer)+","+meters[i].cost+","+asint(meters[i].dist)+","+meters[i].match+","+meters[i].i+","+meters[i].lat+","+meters[i].lon+"]");
			} else  {
				retvalsb.append("["+Q(smeters[I].belnummer)+","+meters[i].cost+","+asint(meters[i].dist)+","+meters[i].match+","+meters[i].i+"]");
			} 
			
			boolean fnd = false;
//			J:
//			for (int j=0; j < al.size(); j++) {
//				if (al.get(j).cost == meters[i].cost) {
//					fnd = true;
//					if (al.get(j).dist > meters[i].dist) {
//						al.get(j).belnummer = smeters[I].belnummer;
//						al.get(j).dist = meters[i].dist;
//						al.get(j).address = smeters[I].adres;
//						al.get(j).lat = smeters[I].lat;
//						al.get(j).lon = smeters[I].lon;
//						al.get(j).i = meters[i].i;
//						al.get(j).type = meters[i].type;
//						break J;
//					}
//				}
//			}
			
			if (meters[i].type.equalsIgnoreCase("on-street-meter")) {
				if (found_signatures.contains(meters[i].costsignature) == false) {
					if (req.opt_include_unmatched.equalsIgnoreCase("y") == true || meters[i].match > 0) {
						if (metersmax < 0 || metersfound < metersmax) {
							metersfound++;
							totalfound++;
							ParkSharkCalcReturnReccommendation newa = new ParkSharkCalcReturnReccommendation();
							newa.automat_number = smeters[I].belnummer;
							newa.dist_in_meters = meters[i].dist;
		//					newa.cost = meters[i].cost;
							newa.cost = (double)(Math.round(meters[i].cost*100))/100;
							newa.address = smeters[I].adres;
							newa.lat = smeters[I].lat;
							newa.lon = smeters[I].lon;
							newa.type = smeters[I].type;
							newa.displaytype = smeters[I].displaytype;
							newa.match = meters[i].match;
							newa.dbg = meters[i].dbg;
							newa.chance_weekday = smeters[I].chance_weekday;
							newa.chance_sat = smeters[I].chance_sat;
							newa.chance_sun = smeters[I].chance_sun;
							newa.name = smeters[I].name;
							newa.expected_occupancy = meters[i].expected_occupancy;
							al.add(newa);
							ret.timings.add("calc : reccommendations found : " +i+" ("+meters[i].type+") : "+ new Long(new Date().getTime() - _exdt));
							found_signatures.add(meters[i].costsignature);
						}
					}
				}
			} else {
				if (req.opt_include_unmatched.equalsIgnoreCase("y") == true || meters[i].match > 0) {
					boolean _local_add_to_resultset = false;
					boolean isPandR = (sgarages[smeters[I].garageid].type.equalsIgnoreCase("p-and-r") || sgarages[smeters[I].garageid].type.equalsIgnoreCase("park-and-ride"));
					
					if (isPandR) {
						// if is in centrum 
						if (isInAmsterdamCentrum == true) {
							if (prmax < 0 || prfound < prmax) {
								prfound++;
								totalfound++;
								_local_add_to_resultset = true;
							}
						}
					} else {
						if (garagesmax < 0 || garagesfound < garagesmax) {
							garagesfound++;
							totalfound++;
							_local_add_to_resultset = true;
						}
					}
					
					
					if (_local_add_to_resultset == true) {
						ParkSharkCalcReturnReccommendation newa = new ParkSharkCalcReturnReccommendation();
						newa.dist_in_meters = meters[i].dist;
	//					newa.cost = meters[i].cost;
						newa.cost = (double)(Math.round(meters[i].cost*100))/100;
						newa.address = smeters[I].adres;
						newa.lat = smeters[I].lat;
						newa.lon = smeters[I].lon;
						newa.type = smeters[I].type;
						newa.displaytype = smeters[I].displaytype;
						newa.dbg = meters[i].dbg;
						newa.match = meters[i].match;
						newa.chance_weekday = smeters[I].chance_weekday;
						newa.chance_sat = smeters[I].chance_sat;
						newa.chance_sun = smeters[I].chance_sun;
						newa.name = smeters[I].name;
						newa.csdkid = smeters[I].csdkid;
						newa.csdkurl = smeters[I].csdkurl;
						//npr
						newa.nprid = smeters[I].nprid;
						newa.nprurl = smeters[I].nprurl;
						
						newa.expected_occupancy = meters[i].expected_occupancy;
						newa.garage_type = sgarages[smeters[I].garageid].type;
						newa.garageid = Integer.toString(smeters[I].garageid);
						newa.notes = sgarages[smeters[I].garageid].remarks;
						newa.garage_opening_hours = sgarages[smeters[I].garageid].opening_times_raw;
						newa.garage_opening_hours_today = sgarages[smeters[I].garageid].opening_times[_day].raw();
						newa.garage_opening_hours_today_json = sgarages[smeters[I].garageid].opening_times[_day].json();
						newa.garage_includes_public_transport = sgarages[smeters[I].garageid].includes_public_transport;
						newa.garage_owner = sgarages[smeters[I].garageid].owner;
						newa.garage_infourl = sgarages[smeters[I].garageid].url;
						newa.ams_pr_fare = sgarages[smeters[I].garageid].ams_pr_fare;
						al.add(newa);
						ret.timings.add("calc : reccommendations found : " +i+" ("+meters[i].type+") : "+ new Long(new Date().getTime() - _exdt));
					}
				} else {
					ret.timings.add("calc : reccommendations excluded : " +i+" ("+meters[i].type+") : "+ new Long(new Date().getTime() - _exdt));
				}
				
			}
		}
		retval = retvalsb.toString();
		ret.timings.add("calc : after reccommendations " + new Long(new Date().getTime() - _exdt));

		
		StringBuffer retvalsb2 = new StringBuffer(); 
		for (int j=0; j < al.size(); j++) {
			if (j > 0) retvalsb2.append(",");
			retvalsb2.append("["+Q(al.get(j).automat_number)+","+al.get(j).cost+","+asint(al.get(j).dist_in_meters)+","+Q(al.get(j).address)+","+al.get(j).lat+","+al.get(j).lon+","+j+","+al.get(j).i+"]");
		}
		ret.timings.add("calc : after 2nd stringbuffer " + new Long(new Date().getTime() - _exdt));
		

		retval = "{"+Q("params")+":{"+Q("fmt")+":"+fmt+"},"+Q("results")+":["+retval+"],"+Q("advice")+":["+retvalsb2.toString()+"]}";

		ret.timings.add("calc : end  - after add together string " + new Long(new Date().getTime() - _exdt));
		
		System.out.println("START CHEAPEST");
		System.out.println("TS4 " + (new Date().getTime() - start));

		ret.timings.add("calc : end " + new Long(new Date().getTime() - _exdt));
		
		ret.text = retval;
		ret.meters = meters;
		ret.reccommendations = al;
		ret.costsmap = costmap;
		return ret;
		
				
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String rv = calc(5, 8, 20 ,  10.5, 51, 5, "cash,pin");
		System.out.println(rv);

	}

}
