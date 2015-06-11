package com.glimworm.opendata.parkshark.importdata.NPR;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

import org.json.JSONObject;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.PlaceParkingGarage;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.PlaceParkingGarageAmsterdamPrVariation;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.PlaceParkingGarageOpeningTimes;
import com.glimworm.opendata.parkshark.importdata.NPR.xsd.AreaListItem;
import com.glimworm.opendata.parkshark.importdata.citySDK.utils;
import com.glimworm.opendata.parkshark.importdata.citySDK.xsd.geom;
import com.glimworm.opendata.parkshark.xsd.First;
import com.glimworm.opendata.parkshark.xsd.Meter;
import com.glimworm.opendata.parkshark.xsd.PayTime;
import com.glimworm.opendata.parkshark.xsd.PayTimes;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

public class Amsterdam {
	
	public static int GARAGES = 1;
	public static int METERS = 2;
	
	public static PlaceParkingGarage[] getMeters() {
		return getGarages(METERS);

	}
	
	public static PlaceParkingGarage[] getGarages() {
		return getGarages(GARAGES);
	}

	public static boolean isInNorth(double lat, double lng) {
		double start_lat = 52.4208;
		double start_lng = 4.82815;	
		double lon_delta = 0.3636122177; 	// each time the lon increases by 1 the lat decreases by this value

		double difference_in_lng = (lng - start_lng);
		double new_lat = start_lat - (difference_in_lng * lon_delta);		// new Longitude
		
		if (lat > new_lat) return true;
		return false;
	};	
	
	public static Meter[] smeters = null;
	public static void downloadmeters(PlaceParkingGarage[] areas) {

		class VERKOOPPUNT {
			public String areamanagerid = "";
			public String sellingpointid = "";
			public String SellingPointDesc = "";
			public double lat = 0;
			public double lon = 0;
			public String area = "";
		}

		/*		
		mysql> select a.*,p.cash,p.creditcard,p.pin,p.chip from _site1493_dbsynch_automats a left join _site1493_dbsynch_paymethods p on (a.typeautomaat = p.type) limit 100,1\G;
		*************************** 1. row ***************************
		    entityid: 325
		   stadsdeel: Centrum
		   belnummer: 10325
		       adres: Keizersgracht 436
		    postcode: 1016 GD
		  woonplaats: Amsterdam
		typeautomaat: CWT
		 betaalwijze: 5
		      status: OP
		  tariefcode: 1
		         lat: 4.8842101
		         lon: 52.3679199
		      csdkid: 
		    csdkzone: test.divv.parking.zone.2
		  chance_day: 0:93%;1:91%;2:91%;3:91%;4:91%;5:91%;6:90%;7:89%;8:82%;9:87%;10:92%;11:95%;12:95%;13:96%;14:96%;15:94%;16:91%;17:91%;18:97%;19:98%;20:101%;21:99%;22:97%;23:96%;
		  chance_sat: 0:97%;1:95%;2:94%;3:94%;4:94%;5:94%;6:93%;7:92%;8:88%;9:84%;10:81%;11:75%;12:82%;13:85%;14:84%;15:88%;16:92%;17:97%;18:97%;19:98%;20:99%;21:100%;22:100%;23:100%;
		  chance_sun: 0:94%;1:92%;2:91%;3:91%;4:91%;5:91%;6:91%;7:90%;8:90%;9:90%;10:90%;11:88%;12:85%;13:84%;14:89%;15:89%;16:90%;17:95%;18:96%;19:97%;20:99%;21:99%;22:97%;23:95%;
		        cash: N
		  creditcard: Y
		         pin: Y
		        chip: Y
		1 row in set (0.00 sec)
		
		VERKOOPPUNT
		
		AreaManagerId	AreaId	UUID	AreaName
		344				3300	57d6f361-ffea-4186-a5a0-80a122c06fc3	
		796				20411	b6a5905f-c79f-4669-a43a-68eccef1f3c3	

		VERKOOPPUNT (METERS)
		AreaManagerId	SellingPointId	StartDateSellingPoint	EndDateSellingPoint	SellingPointDesc	Location
		473				236131			20120101														(52.378572755, 4.530698053)
		599				320				20140101									J.B. BAKEMAKADE		(51.9076118, 4.5018101)

		*
		*/		
		
		String parkinglocationsUrl = "https://opendata.rdw.nl/api/views/cgqw-pfbp/rows.csv?accessType=DOWNLOAD";
		com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse res = com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(parkinglocationsUrl, "", null, null, null, null,null,500);
		System.out.println(res.text);
		String[] lines = res.text.split("[\n]");

		
		GeometryFactory fact = new GeometryFactory();

		ArrayList<VERKOOPPUNT> VERKOOPPUNTs = new ArrayList<VERKOOPPUNT>();
		Vector<Meter> vect = new Vector<Meter>();

		for (int i=1; i < lines.length; i++) {	// skip first line
			String[] cols = lines[i].split("[,]");
			if (cols.length > 2) {
				VERKOOPPUNT item = new VERKOOPPUNT();
				item.areamanagerid = cols[0];
				item.sellingpointid = cols[1];
				item.SellingPointDesc = cols[4];
				try {
					/*
					 * get position frmo the string format (51,4.8)
					 */
					item.lat = Double.parseDouble(cols[5].replace('(',' ').replace('"',' ').trim());
					item.lon = Double.parseDouble(cols[6].replace(')',' ').replace('"',' ').trim());

					Coordinate cor1 = new Coordinate(item.lat,item.lon);
					final com.vividsolutions.jts.geom.Point point = fact.createPoint(cor1);
					
					
					/*
					 * match to an area
					 */
					
					place:
					for (PlaceParkingGarage area : areas) {
						if (area.poly != null && point.within(area.poly) == true) {
							item.area = area.name;
							System.out.println("L: "+i+" "+item.sellingpointid+" ["+item.lat+","+item.lon+" A:"+item.area+"]");				

							
							Meter _meter = new Meter();
							_meter.i = i;
							_meter.name = item.area;
							_meter.entityid = item.sellingpointid;
							_meter.stadsdeel = "";								//automats.getString(i, "stadsdeel");
							_meter.belnummer = item.sellingpointid;;			//automats.getString(i, "belnummer");
							_meter.adres = "";									//automats.getString(i, "adres");
							_meter.postcode = "";								//automats.getString(i, "postcode");
							_meter.woonplaats = "";								//automats.getString(i, "woonplaats");
							_meter.typeautomaat = "CWT";						//automats.getString(i, "typeautomaat");
							_meter.betaalwijze = "OP";							//automats.getString(i, "betaalwijze");
							_meter.tariefcode = "";						//automats.getString(i, "tariefcode");
							_meter.status = "";									//automats.getString(i, "status");
							_meter.lat = item.lat;								//automats.getDouble(i, "lon",0);
							_meter.lon = item.lon;								//automats.getDouble(i, "lat",0);
							_meter.csdkzone = "";								//automats.getString(i, "csdkzone");
							_meter.chance_weekday = "";							//automats.getString(i, "chance_day");
							_meter.chance_sat = "";								//automats.getString(i, "chance_sat");
							_meter.chance_sun = "";								//automats.getString(i, "chance_sun");
							
							_meter.csdkurl = "https://www.google.nl/maps/place/"+_meter.lat+","+_meter.lon;

							_meter.bw.cash = false;						//automats.getString(i, "cash").equalsIgnoreCase("Y");
							_meter.bw.creditcard = true;				//automats.getString(i, "creditcard").equalsIgnoreCase("Y");
							_meter.bw.pin = true;						//automats.getString(i, "pin").equalsIgnoreCase("Y");
							_meter.bw.chip = false;						//automats.getString(i, "chip").equalsIgnoreCase("Y");
						
							_meter.nprid = area.nprid;
							_meter.nprurl = "https://npropendata.rdw.nl/parkingdata/v2/static/"+area.nprid;
							
							PayTimes retval = new PayTimes();
							
							retval.cost = (60/area.time_unit_minutes) * area.price_per_time_unit;

							
							/*
							 * text of Verkooppunt name is like "R116E ma-za 09-24"
							 */
							String[] parts = item.area.split("[ ]");
							boolean found = false;
							partloop:
							for (int d=0; d < (parts.length -1); d++) {
								String part = parts[d];		//	[ma-za] 09-24 zo 12-24
								String part2 = parts[d+1];	//	ma-za [09-24] zo 12-24
								
								PayTime pt = null;
								if (part2.length() == 5 && part2.indexOf("-") > -1) {
									if (part2.startsWith("0") || part2.startsWith("1") || part2.startsWith("2")) {
										pt = new PayTime();
										pt.start = Integer.parseInt(part2.split("[-]")[0]);
										pt.end = Integer.parseInt(part2.split("[-]")[1]);
									}
								}
								if (pt == null) continue partloop;
								
								int dc = 0;
								for (String day : "zo,ma,di,wo,di,vr,za".split("[,]")) {
									if (part.startsWith(day)) {
										
										if (part.length() == 2) {
											retval.days[dc] = new PayTime();
											retval.days[dc].start = pt.start;
											retval.days[dc].end = pt.end;
											found = true;
											
										} else if (part.length() == 5) {
											int dc1 = 0;
											for (String day1 : "zo,ma,di,wo,di,vr,za".split("[,]")) {
												if (part.endsWith(day1)) {
													for (int ddc = 0; ddc < dc1; ddc++) {
														retval.days[ddc] = new PayTime();
														retval.days[ddc].start = pt.start;
														retval.days[ddc].end = pt.end;
														found = true;
													}
												}
												dc1++;
											}
											
										}
										
									}
									dc++;
								}
							}
							
							if (found == false) {
								retval = area.pt;
							}
							
							/**
							 * fill empty ones with 00
							 */
							for (int ddc = 0; ddc < retval.days.length ; ddc++) {
								if (retval.days[ddc] == null) {
									retval.days[ddc] = new PayTime();
									retval.days[ddc].start = 0;
									retval.days[ddc].end = 0;
								}
							}
							//retval.first = area.free_minutes;
							retval.first = new First();
							retval.first.combination = "n";		//jc todo
							retval.first.hrs = 0;				//jc todo
							retval.first.hrs2 = 0;				//jc todo
							retval.first.price = 0;				//jc todo
							retval.first.price2 = 0;			//jc todo
							
							retval.geb_code = "";				//jc todo
							retval.max = 0;						//jc todo
							retval.maxdaycost = area.price_day;
							retval.t_code = "";					//jc todo
							_meter.costs = retval;

							_meter.type = "on-street-meter";
							try {
								_meter.isInNorth = isInNorth(_meter.lat,_meter.lon);
							} catch (Exception E) {
								_meter.isInNorth = false;
							}			
							
							vect.add(_meter);							
							
							
							break place;
							
						}
						
					}

				
				} catch (Exception E) {
					System.out.println(cols[5]+","+cols[6]+"/"+cols[5].replace('(',' ').replace('"',' ').trim()+"/"+cols[6].replace(')',' ').replace('"',' ').trim());
					E.printStackTrace(System.out);
					item.lat = 0;
					item.lon = 0;
				}

				
				VERKOOPPUNTs.add(item);
			}
		}
		Object result[] = new Meter[vect.size()];
		vect.copyInto(result);
		smeters = (Meter[])result;
		return;
				
	}
	public static PlaceParkingGarage[] getGarages(int GARAGESORMETERS) {
		// TODO Auto-generated method stub
		/* make a list of area items
		 * 
		 */

		String parkinglocationsUrl = "https://opendata.rdw.nl/api/views/mz4f-59fw/rows.csv?accessType=DOWNLOAD";
		com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse res = com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(parkinglocationsUrl, "", null, null, null, null,null,500);
		System.out.println(res.text);
		
		/*
		 * GET AREAS (areamanagerid , areaID, uuid)
		 * filters on 363 and 363_
		 * 
		 */
		
		
		
		String[] lines = res.text.split("[\n]");
		ArrayList<AreaListItem> areas = new ArrayList<AreaListItem>();
		for (int i=1; i < lines.length; i++) {	// skip first line
			String[] cols = lines[i].split("[,]");
			if (cols.length > 2) {
				AreaListItem item = new AreaListItem();
				item.areamanagerid = cols[0];
				item.areaId = cols[1];
				item.uuid = cols[2];
				if (item.areamanagerid.equalsIgnoreCase("363") || item.areaId.startsWith("363_")) {
					areas.add(item);
				}
			}
		}

		
		/*
		 * walks through all the AREAS
		 * gets  original data from NPR and populates "parkingPlaceGarage" array
		 * 
		 * ALSO
		 * 
		 * adds more rich data to the existing area array
		 * 
		 * 
		 * 
		 */
		
		
		Vector<PlaceParkingGarage> vect = new Vector<PlaceParkingGarage>();
		
		int readfrominternet = 0;
		nextarea:
		for (int i=0; i < areas.size(); i++) {
			try {
				String FN = "/opt/tmp/rdw/"+areas.get(i).uuid+".json";
				File f = new File(FN);
				String txt = "";
				String U = "https://npropendata.rdw.nl/parkingdata/v2/static/" + areas.get(i).uuid;
				
				/** if file exists do not download again - sort of cache **/
				
				if (f.exists()) {
					System.out.println("READING FILE ["+areas.get(i).uuid+"]");
					txt = com.glimworm.opendata.divvamsterdamapi.planning.net.FileUtils.readFile(FN);
				}
				if (txt == null || txt.trim().length() == 0) {
					System.out.println("SEARCHING FOR ["+areas.get(i).uuid+"]"+U);
					res = com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(U, "", null, null, null, null,null,500);
					com.glimworm.opendata.divvamsterdamapi.planning.net.FileUtils.writeFile(FN, res.text);
					txt = res.text;
					readfrominternet++;
				}
				
				/**
				 * create a json obejct "jsob"
				 */
				
				org.json.JSONObject jsob = com.glimworm.common.utils.jsonUtils.string2json(txt);
				
				org.json.JSONObject jsob1 = jsob.optJSONObject("parkingFacilityInformation");
				
				/**
				 * add detail to the area array item
				 */
				
				areas.get(i).detail_url = U;
				areas.get(i).description =  jsob1.optString("description");
				areas.get(i).name =  jsob1.optString("name");
				areas.get(i).operator =  jsob1.optJSONObject("operator");
				areas.get(i).validityStartOfPeriod =  jsob1.optLong("validityStartOfPeriod");
				areas.get(i).validityEndOfPeriod =  jsob1.optLong("validityEndOfPeriod");
				
				long now = (long) (System.currentTimeMillis() / 1000L);
//				long now = 1337126400+1000;
//				new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse("01/01/2013 01:00:00").getTime() / 1000;
				
				System.out.println(now);
				System.out.println(areas.get(i).validityStartOfPeriod);
				System.out.println(areas.get(i).validityEndOfPeriod);
				
				/**
				 * skip if not currently active
				 * 
				 * 
				 */
				
				if (now < areas.get(i).validityStartOfPeriod || (areas.get(i).validityEndOfPeriod > 0 && now > areas.get(i).validityEndOfPeriod)) {
					areas.get(i).valid =  false;
					continue nextarea;
				}
				System.out.println("VALID");

				org.json.JSONArray specifications = jsob1.optJSONArray("specifications");
				if (specifications != null) {
					for (int k = 0; k < specifications.length(); k++) {
						String usage = specifications.getJSONObject(k).getString("usage");
						areas.get(i).usage += usage+",";
						System.out.println(usage);
						/*
						 * skip if the wrong type
						 * 
						 * note :: there could be other text descriptions ??
						 */
						if (GARAGESORMETERS == GARAGES && usage.startsWith("Garage") == false) continue nextarea;
						if (GARAGESORMETERS == METERS && usage.startsWith("BetaaldParkeren") == false) continue nextarea;
						
						/*
						 * create polygon
						 */
						
						org.json.JSONObject areaGeometry = specifications.getJSONObject(k).optJSONObject("areaGeometry");
						if (areaGeometry != null) {
							System.out.println("areaGeometry");
							String type = areaGeometry.optString("type");
							org.json.JSONArray coordinates = areaGeometry.optJSONArray("coordinates");
							if (coordinates != null) {
								System.out.println("coordinates");
								for (int k1=0; k1 < coordinates.length(); k1++) {
									System.out.println("coordinates "+k1);
									org.json.JSONArray coo = coordinates.optJSONArray(k1);
									if (coo != null) {

										Vector<Coordinate> vectc = new Vector<Coordinate>();
										Coordinate cor0 = null;
										for (int k2=0; k2 < coo.length(); k2++) {
											org.json.JSONArray co = coo.optJSONArray(k2);
											if (co != null) {
												System.out.println("co "+co.toString());
												double lon = co.optDouble(0);
												double lat = co.optDouble(1);
												System.out.println("["+lat+","+lon+"]");
												Coordinate cor1 = new Coordinate(lat,lon);
												vectc.add(cor1);
												if (cor0 == null) cor0 = cor1;
											}
										}
										if (cor0 != null) vectc.add(cor0);	// close the shape
										//
										Object resultc[] = new Coordinate[vectc.size()];
										vectc.copyInto(resultc);

										Coordinate[] cor = (Coordinate[])resultc;
										GeometryFactory fact = new GeometryFactory();
										Polygon poly1 = fact.createPolygon(cor);
										areas.get(i).poly = poly1;
										
										System.err.println(poly1);
										
										 
//										com.vividsolutions.jts.geom.Polygon poly1 = new com.vividsolutions.jts.geom.Polygon(pol);
									}
								}
							}
						}
						
					}
				}
				
				if (GARAGESORMETERS == METERS) {
					// hmmm  did I want to do something here?
				}
				
				/*
				 * garages have access points which we use for the GEO
				 * 
				 * note : will take the last one
				 * 
				 */
				
				org.json.JSONArray accessPoints = jsob1.optJSONArray("accessPoints");
				if (accessPoints != null) {
					for (int k = 0; k < accessPoints.length(); k++) {
						org.json.JSONArray accessPointLocation = accessPoints.getJSONObject(k).optJSONArray("accessPointLocation");
						if (accessPointLocation != null && accessPointLocation.length() > 0) {
							double lat = accessPointLocation.optJSONObject(0).optDouble("latitude");
							double lon = accessPointLocation.optJSONObject(0).optDouble("longitude");
							areas.get(i).lat = lat;
							areas.get(i).lon = lon;

							areas.get(i).usage += "("+lat+","+lon+"),";
							
						}
						org.json.JSONObject accessPointAddress = accessPoints.getJSONObject(k).optJSONObject("accessPointAddress");
						if (accessPointAddress != null) {

							String streetName = accessPointAddress.optString("streetName");
							String houseNumber = accessPointAddress.optString("houseNumber");
							String zipcode = accessPointAddress.optString("zipcode");
							String city = accessPointAddress.optString("city");
							String province = accessPointAddress.optString("province");
							String country = accessPointAddress.optString("country");

							areas.get(i).streetName = streetName;
							areas.get(i).houseNumber = houseNumber;
							areas.get(i).zipcode = zipcode;
							areas.get(i).city = city;
							areas.get(i).province = province;
							areas.get(i).country = country;
							
						}
					}
				}
				
				/*
				 * move the data into a "parkingplacegarage"
				 */

				PlaceParkingGarage pl = new PlaceParkingGarage();
				pl.name = areas.get(i).name;
				pl.url = areas.get(i).detail_url;
				pl.lat = areas.get(i).lat;
				pl.lon = areas.get(i).lon;
				pl.poly = areas.get(i).poly;
				pl.postcode = areas.get(i).zipcode;
				pl.street = areas.get(i).streetName + " " + areas.get(i).houseNumber;
				pl.nprurl = U;
				pl.nprid = areas.get(i).uuid;
				pl.cdk_id = "";
				pl.csdkurl = "";
				pl.places = 0;
				pl.type = "parking-garage"; // "park-and-ride"
				pl.opening_times_raw = "n/a";

				pl.capacity = 0;
				pl.free_minutes = 0;
				pl.time_unit_minutes = 0;

				pl.price_day = 0.0d;
				pl.price_per_time_unit = 0.0d;
				
				/*
				 * add opening times
				 * 
				 */
				
				for (int d=0; d < 7; d++) {
					pl.opening_times[d] = new PlaceParkingGarageOpeningTimes();
					pl.opening_times[d].dayOfWeek= d;
					pl.opening_times[d].open_in = "0000";
					pl.opening_times[d].open_out = "0000";
					pl.opening_times[d].close_in = "0000";
					pl.opening_times[d].close_out = "0000";
				}
				
				
				
				PayTimes pt = new PayTimes();
				pt.cost = 0;
				pt.first = new First();
				pt.geb_code = "";
				pt.max = 0;
				pt.maxdaycost = 0;
				pt.t_code = "";
//				pt.days[0] = new Py

				org.json.JSONArray tarrifs = jsob1.optJSONArray("tariffs");
				for (int j = 0; j < tarrifs.length(); j++) {
					org.json.JSONObject jsobjt = tarrifs.getJSONObject(j);

					String tariffDescription = jsobjt.optString("tariffDescription");
					long startOfPeriod = jsobjt.optLong("startOfPeriod");
					long endOfPeriod = jsobjt.optLong("endOfPeriod");

//					System.out.println(now);
//					System.out.println(startOfPeriod);
//					System.out.println(endOfPeriod);
					
					if (now > startOfPeriod && (endOfPeriod == 0 || now < endOfPeriod)) {
						
						org.json.JSONArray validitydays = jsobjt.optJSONArray("validityDays");
						if (validitydays != null) {
							for (int k1 = 0; k1 < validitydays.length(); k1++) {
								String day = validitydays.getString(k1);
								areas.get(i).usage += "\n"+tariffDescription+" / "+day+":";
								
								int daynum = 0;
								if (day.equalsIgnoreCase("mon")) daynum = 1;
								if (day.equalsIgnoreCase("tue")) daynum = 2;
								if (day.equalsIgnoreCase("wed")) daynum = 3;
								if (day.equalsIgnoreCase("thu")) daynum = 4;
								if (day.equalsIgnoreCase("fri")) daynum = 5;
								if (day.equalsIgnoreCase("sat")) daynum = 6;
								if (day.equalsIgnoreCase("sun")) daynum = 0;

								// this is a valid tarrif
								int validityFromTime_h = jsobjt.getJSONObject("validityFromTime").getInt("h");
								int validityFromTime_m = jsobjt.getJSONObject("validityFromTime").getInt("m");
								int validityFromTime_s = jsobjt.getJSONObject("validityFromTime").getInt("s");
		
								areas.get(i).usage += " "+validityFromTime_h+validityFromTime_m+" ";

								// this is a valid tarrif
								int validityUntilTime_h = jsobjt.getJSONObject("validityUntilTime").getInt("h");
								int validityUntilTime_m = jsobjt.getJSONObject("validityUntilTime").getInt("m");
								int validityUntilTime_s = jsobjt.getJSONObject("validityUntilTime").getInt("s");

								areas.get(i).usage += " "+validityUntilTime_h+validityUntilTime_m+" ";

								org.json.JSONArray intervalRates = jsobjt.optJSONArray("intervalRates");
								if (intervalRates != null) {
		
									for (int k = 0; k < intervalRates.length(); k++) {
									
										double charge = intervalRates.getJSONObject(k).optDouble("charge");
										// 0.1
										int chargePeriod = intervalRates.getJSONObject(k).optInt("chargePeriod");
										// 60
										int durationFrom = intervalRates.getJSONObject(k).optInt("durationFrom");
										// 0
										int durationUntil = intervalRates.getJSONObject(k).optInt("durationUntil");
										// -1
										String durationType = intervalRates.getJSONObject(k).optString("durationType");
										// "Minutes"
										
										

										if (charge > 0) {
											/*
											 * exclude DAY and EVENING CARDS
											 */
											if (chargePeriod < 61) {
												
												areas.get(i).usage += " "+charge+" ";
												areas.get(i).usage += " "+chargePeriod+" ";
												areas.get(i).usage += " "+durationType+" ";
												areas.get(i).usage += " "+durationFrom+" ";
												areas.get(i).usage += " "+durationUntil+" ";
												
												if (charge> 0) {
													if (pt.days[daynum] == null) {
														/*
														 * if this is the first encountered data for this day
														 */
														pt.days[daynum] = new PayTime();
														pt.days[daynum].start = validityFromTime_h;
														pt.days[daynum].end = validityUntilTime_h;
													} else {
														/*
														 * extend the time window to the max
														 * 
														 * can go wrong here if different rates for different times of the day
														 * 
														 */
														if (validityFromTime_h < pt.days[daynum].start) pt.days[daynum].start = validityFromTime_h;
														if (validityUntilTime_h > pt.days[daynum].end) pt.days[daynum].end = validityUntilTime_h;
													}
												}
												
												/*
												 * get the smallest time period
												 */
												
												if (pl.time_unit_minutes == 0 || chargePeriod < pl.time_unit_minutes) {
													pl.time_unit_minutes = chargePeriod;
													pl.price_per_time_unit = charge;
													/* 
													 * I want it in euro / hour for meters
													 */
													if (((60/chargePeriod) * charge) > pt.cost) {
														pt.cost = (60/chargePeriod) * charge;
													}
												}
											
											}
											/*
											 * more than 1400 mins are  "day cards"
											 */
											if (chargePeriod > (1400)) {
												if (pl.price_day == 0 || charge > pl.price_day) {
													pl.price_day = charge;
													pt.maxdaycost = charge;
												}
											}
										}
									
									}
								}
							}
						}

					}
					
				}
				/*
				 * fill up remaing days with zero
				 */
				for (int pi=0; pi < pt.days.length; pi++) {
					if (pt.days[pi] == null) {
						pt.days[pi] = new PayTime();
						pt.days[pi].start = 0;
						pt.days[pi].end = 0;
					}
				}
				pl.pt = pt;
				pl.name += "|"+areas.get(i).usage;
				
				org.json.JSONArray openingTimes = jsob1.optJSONArray("openingTimes");
				if (openingTimes != null) {
					for (int j1 = 0; j1 < openingTimes.length(); j1++) {
						/*
						 * nothing done with opening times for garages!
						 * 
						 */
						System.out.println(openingTimes.getJSONObject(j1).toString());
					}
				}
				vect.add(pl);

//				if (readfrominternet > 50) break;

			} catch (Exception E) {
//				E.printStackTrace(System.out);
				System.out.println(E.getLocalizedMessage());
			}
			
		}
		
		Object result[] = new PlaceParkingGarage[vect.size()];
		vect.copyInto(result);
		PlaceParkingGarage[] pg = (PlaceParkingGarage[])result;

		/*
		 * sort on area of polygon
		 */
		class AreaSort implements Comparator<PlaceParkingGarage> {
		    // Comparator interface requires defining compare method.
		    public int compare(PlaceParkingGarage a, PlaceParkingGarage b) {
		        if (a.poly != null && b.poly != null && a.poly.getArea() < b.poly.getArea()) {return -1;}
		        if (a.poly != null && b.poly != null && a.poly.getArea() > b.poly.getArea()) {return 1;}
		        return 0;
		    }
		}
		
		Comparator<PlaceParkingGarage> MeterByArea = new AreaSort();
		Arrays.sort(pg, MeterByArea);
		
		
//		for (PlaceParkingGarage p : pg) {
//			System.out.print(p.name);
//			System.out.print(" ("+p.lat);
//			System.out.println(","+p.lon+")");
//		}
		
		/*
		 * return list of garages
		 * list of areas is actually in the class file and not retuened
		 */
		return pg;

	}

	public static PlaceParkingGarage[] getGarages_old_deprectiated() {
		// TODO Auto-generated method stub
//		String S = "http://api.citysdk.waag.org/admr.nl.amsterdam/ptstops?name=Leidseplein";
		String S = "http://api.citysdk.waag.org/nodes?layer=divv.parking.buildings&geom";
		
		/*
		cdk_id: "divv.parking.buidings.1124",
		name: "Willemspoort Garage",
		node_type: "node",
		geom: {
			type: "Point",
			coordinates: [
				4.8849266,
				52.3848016
			]
		},
		layers: {
			divv.parking.buildings: {
				data: {
					gid: "1124",
					url: "http://www.amsterdam.nl/parkeergebouwen/onze-garages/amsterdam_centrum/willemspoort/",
					adres: "Haarlemmer Houttuinen 549",
					title: "Willemspoort Garage",
					aantal: null,
					postcode: "1013GM",
					huisnummer: "549",
					woonplaats: "Amsterdam",
					opmerkingen: null
				}
			}
		},
		layer: "divv.parking.buildings"
		},
		*/
		
		Vector<PlaceParkingGarage> vect = new Vector<PlaceParkingGarage>();
		
		com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse res = com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(S, "", null, null, null, null);
		System.out.println(res.text);
		org.json.JSONObject jsob = com.glimworm.common.utils.jsonUtils.string2json(res.text);
		org.json.JSONArray ar = jsob.optJSONArray("results");
		for (int i=0; i < ar.length(); i++) {
			System.out.println(ar.optJSONObject(i).optString("name"));
			System.out.println(ar.optJSONObject(i).optString("cdk_id"));
			System.out.println(ar.optJSONObject(i).optString("node_type"));

			
			geom coords = com.glimworm.opendata.parkshark.importdata.citySDK.utils.geomFromJson(ar.optJSONObject(i).optJSONObject("geom"));
			String cdk_id = ar.optJSONObject(i).optString("cdk_id");
			
			System.out.println(coords);
			
			JSONObject data = ar.optJSONObject(i).optJSONObject("layers").optJSONObject("divv.parking.buildings").optJSONObject("data");

			PlaceParkingGarage pl = com.glimworm.opendata.parkshark.importdata.citySDK.utils.garageFromJson(ar.optJSONObject(i),data, cdk_id, coords);
			pl.places = data.optInt("aantal");
			
			vect.add(pl);
			
			System.out.println(pl);
			
			System.out.println(data.optString("gid"));
			System.out.println(data.optString("url"));
			System.out.println(data.optString("adres"));
			System.out.println(data.optString("title"));
			System.out.println(data.optString("aantal"));
			System.out.println(data.optString("postcode"));
			System.out.println(data.optString("huisnummer"));
			System.out.println(data.optString("woonplaats"));
			System.out.println(data.optString("opmerkingen"));
			System.out.println(ar.optJSONObject(i).toString());
						
		}

		Object result[] = new PlaceParkingGarage[vect.size()];
		vect.copyInto(result);
		return (PlaceParkingGarage[])result;
		
	}	
	
	public static ArrayList<AreaListItem> areas;
	
	public static void populate() {
		
		String parkinglocationsUrl = "https://opendata.rdw.nl/api/views/mz4f-59fw/rows.csv?accessType=DOWNLOAD";
		com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse res = com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(parkinglocationsUrl, "", null, null, null, null,null,180);
		System.out.println(res.text);
		
		String[] lines = res.text.split("[\n]");
		areas = new ArrayList<AreaListItem>();
		for (int i=1; i < lines.length; i++) {	// skip first line
			String[] cols = lines[i].split("[,]");
			if (cols.length > 2) {
				AreaListItem item = new AreaListItem();
				item.areamanagerid = cols[0];
				item.areaId = cols[1];
				item.uuid = cols[2];
				if (item.areamanagerid.equalsIgnoreCase("363") || item.areaId.startsWith("363_")) {
					areas.add(item);
				}
			}
		}

		for (int i=0; i < areas.size(); i++) {
			try {
				String U = "https://npropendata.rdw.nl/parkingdata/v2/static/" + areas.get(i).uuid;
				System.out.println("SEARCHING FOR ["+areas.get(i).uuid+"]"+U);
				res = com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(U, "", null, null, null, null,null,180);
				org.json.JSONObject jsob1 = com.glimworm.common.utils.jsonUtils.string2json(res.text);

				areas.get(i).description =  jsob1.optString("description");
				areas.get(i).name =  jsob1.optString("name");
				areas.get(i).operator =  jsob1.optJSONObject("operator");
				areas.get(i).validityStartOfPeriod =  jsob1.optLong("validityStartOfPeriod");
				areas.get(i).validityEndOfPeriod =  jsob1.optLong("validityEndOfPeriod");
				
				

				if (i > 20) break;

			} catch (Exception E) {
				E.printStackTrace(System.out);
			}
		}

		
		
	}
	
}
