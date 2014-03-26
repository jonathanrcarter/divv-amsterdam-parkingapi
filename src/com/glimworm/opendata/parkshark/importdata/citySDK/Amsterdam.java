package com.glimworm.opendata.parkshark.importdata.citySDK;

import java.util.Vector;

import org.json.JSONObject;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.PlaceParkingGarage;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.PlaceParkingGarageAmsterdamPrVariation;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.PlaceParkingGarageOpeningTimes;
import com.glimworm.opendata.parkshark.importdata.citySDK.xsd.geom;
import com.glimworm.opendata.parkshark.xsd.Meter;

public class Amsterdam {
	public static PlaceParkingGarage[] getGarages() {
		// TODO Auto-generated method stub
//		String S = "http://api.citysdk.waag.org/admr.nl.amsterdam/ptstops?name=Leidseplein";
//		String S = "http://api.citysdk.waag.org/nodes?layer=divv.parking.buildings&geom";
//		String S = "http://test-api.citysdk.waag.org/nodes?layer=test.divv.parking.car&geom&per_page=1000";
		String S = "http://api.citysdk.waag.org/nodes?layer=divv.parking.car&geom&per_page=1000";
		
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
			test.divv.parking.car: {
				data: {
					type: "Parkeergarage",
					address: "Haarlemmer Houttuinen 549",
					info_url: "http://www.amsterdam.nl/parkeergebouwen/onze-garages/amsterdam_centrum/willemspoort/",
					postcode: "1013GM",
					info_title: "Parkeergebouwen Willemspoort"
				},
				modalities: [
					"car"
				]
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
		nextGarage:
		for (int i=0; i < ar.length(); i++) {
			System.out.println(ar.optJSONObject(i).toString());
			System.out.println(ar.optJSONObject(i).optString("name"));
			System.out.println(ar.optJSONObject(i).optString("cdk_id"));
			System.out.println(ar.optJSONObject(i).optString("node_type"));
			
			if (ar.optJSONObject(i).optJSONObject("geom") == null) {
				System.out.println("GEOM IS EMPTY");
				continue nextGarage;
			}

			
			
			geom coords = com.glimworm.opendata.parkshark.importdata.citySDK.utils.geomFromJson(ar.optJSONObject(i).optJSONObject("geom"));
			String cdk_id = ar.optJSONObject(i).optString("cdk_id");
			
			System.out.println(coords);
			
//			JSONObject data = ar.optJSONObject(i).optJSONObject("layers").optJSONObject("test.divv.parking.car").optJSONObject("data");
			JSONObject data = ar.optJSONObject(i).optJSONObject("layers").optJSONObject("divv.parking.car").optJSONObject("data");
			
			PlaceParkingGarage pl = com.glimworm.opendata.parkshark.importdata.citySDK.utils.garageFromJson(ar.optJSONObject(i),data, cdk_id, coords);
			
			/*
			{
				status: "success",
				url: "http://api.citysdk.waag.org/divv.parking.car.15?layer=divv.parking.car.price",
				results: [{
					cdk_id: "test.divv.parking.car.15",
					name: "Amsterdamse Poort (P21 t/m 24)",
					node_type: "node",
					layers: {
					test.divv.parking.car.price: {
						data: {
							fare: "1-5 04:00 10:00 8 24 | 1-5 10:00 04:00 1 24 | 6-0 0:00 0:00 1 24", (* new optionsl for P+R *)
							owner: "www.Q-Park.nl",
							remarks: null,
							capacity: null,
							price_day: "13.50",
							free_minutes: "0",
							opening_times: "0 0930 0930 1930 1930|1-6 0730 0730 2130 2130",
							// removed
							service_open_in: "0:00",
							service_remarks: null,
							service_close_in: "0:00",
							service_open_out: "0:00",
							service_close_out: "0:00",
							// until here
							time_unit_minutes: "21",
							price_per_time_unit: "0.50",
							includes_public_transport: "Nee"
						}
					}
				},
				layer: "test.divv.parking.car"
				}
				]
			}
			*/
//			String PriceURL = "http://test-api.citysdk.waag.org/"+cdk_id+"?layer=test.divv.parking.car.price";
			String PriceURL = "http://api.citysdk.waag.org/"+cdk_id+"?layer=divv.parking.car.price";
			
			System.out.println(PriceURL);
			
			
			com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse res1 = com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(PriceURL, "", null, null, null, null);
			System.out.println(res1.text);
			org.json.JSONObject jsob1 = com.glimworm.common.utils.jsonUtils.string2json(res1.text);
			org.json.JSONArray ar1 = jsob1.optJSONArray("results");
			if (ar1 != null && ar1.length() > 0) {
				try {
					/*
					JSONObject data0;
					data0 = ar1.optJSONObject(0);
					data0 = ar1.optJSONObject(0).optJSONObject("layers");
					System.out.println("");
					System.out.println("STR:0:");
					System.out.println(data0.toString());
					System.out.println("");
					System.out.println("");

					data0 = ar1.optJSONObject(0).optJSONObject("layers").optJSONObject("test.divv.parking.car.price");
					System.out.println("");
					System.out.println("STR:q:");
					System.out.println(data0.toString());
					System.out.println("");
					System.out.println("");
					
					data0 = ar1.optJSONObject(0).optJSONObject("layers").optJSONObject("test.divv.parking.car.price").optJSONObject("data");
					*/
//					JSONObject data1 = ar1.optJSONObject(0).optJSONObject("layers").optJSONObject("test.divv.parking.car.price").optJSONObject("data");
					JSONObject data1 = ar1.optJSONObject(0).optJSONObject("layers").optJSONObject("divv.parking.car.price").optJSONObject("data");
					
					pl.csdkid = cdk_id;
//					pl.csdkurl = "http://test-api.citysdk.waag.org/"+cdk_id;
					pl.csdkurl = "http://api.citysdk.waag.org/"+cdk_id;
					pl.remarks = data1.optString("remarks");
					pl.owner = data1.optString("owner");
					pl.opening_times_raw = data1.optString("opening_times");
					pl.includes_public_transport = data1.optString("includes_public_transport","").equalsIgnoreCase("ja") ? "y" : "n";
	
					pl.capacity = data1.optInt("capacity",0);
					pl.free_minutes = data1.optInt("free_minutes",0);
					pl.time_unit_minutes = data1.optInt("time_unit_minutes",0);
	
					pl.price_day = data1.optDouble("price_day",-1);
					pl.price_per_time_unit = data1.optDouble("price_per_time_unit",-1);
					
					for (int d=0; d < 7; d++) {
						pl.opening_times[d] = new PlaceParkingGarageOpeningTimes();
						pl.opening_times[d].dayOfWeek= d;
					}
					try {
						// opening_times: "0 0930 0930 1930 1930|1-6 0730 0730 2130 2130",
						//				  "0 0930 0930 1930 1930|1-6 0730 0730 2130 2130"	

						
						if (pl.opening_times_raw != null && pl.opening_times_raw.trim().length() > 0) {
							System.out.println("openingtimes::"+pl.opening_times_raw);
							String[] days = pl.opening_times_raw.split("[|]");
							nextday:
							for (String day : days) {	// e.g. 0 0930 0930 1930 1930
								System.out.println("openingtimes::day::"+day);
								if (day == null || day.trim().length() == 0) continue nextday;
								String[] dayParts = day.split("[ ]");	// e.g. [0, 0930, 0930, 1930, 1930]
								System.out.println("openingtimes::day::len="+dayParts.length);
								if (dayParts.length != 5) continue nextday;

								System.out.println("openingtimes::day::[0]="+dayParts[0]);
								if (dayParts[0].indexOf("-") > -1) {	// e.g. 1-6
									System.out.println("openingtimes::day::[0]::option1");
									String[] start_and_end = dayParts[0].split("[-]");
									int start = Integer.parseInt(start_and_end[0]);
									int end = Integer.parseInt(start_and_end[1]);
									for (int tempday=start; tempday <= end; tempday++) {
										System.out.println("openingtimes::day::[0]::option1::tempday="+tempday);
										pl.opening_times[tempday].open_in = dayParts[1];
										pl.opening_times[tempday].open_out = dayParts[2];
										pl.opening_times[tempday].close_in = dayParts[3];
										pl.opening_times[tempday].close_out = dayParts[4];
									}
									
								} else if (dayParts[0].indexOf(",") > -1) {	// 2,3
									System.out.println("openingtimes::day::[0]::option2");
									String[] listOfDays = dayParts[0].split("[,]");
									for (String tempday_str : listOfDays) {
										int tempday = Integer.parseInt(tempday_str);
										System.out.println("openingtimes::day::[0]::option2::tempday="+tempday);
										pl.opening_times[tempday].open_in = dayParts[1];
										pl.opening_times[tempday].open_out = dayParts[2];
										pl.opening_times[tempday].close_in = dayParts[3];
										pl.opening_times[tempday].close_out = dayParts[4];
										
									}

								} else if (dayParts[0] != null && dayParts[0].trim().length() > 0) {	// 3
									System.out.println("openingtimes::day::[0]::option3");
									int tempday = Integer.parseInt(dayParts[0]);
									System.out.println("openingtimes::day::[0]::option3::tempday="+tempday);
									pl.opening_times[tempday].open_in = dayParts[1];
									pl.opening_times[tempday].open_out = dayParts[2];
									pl.opening_times[tempday].close_in = dayParts[3];
									pl.opening_times[tempday].close_out = dayParts[4];
								}
							}
						}
					} catch (Exception E) {
						E.printStackTrace(System.out);
					}
					
					pl.ams_pr_fare = data1.optString("fare","");
					//	fare: "1-5 04:00 10:00 8 24 | 1-5 10:00 04:00 1 24 | 6-0 0:00 0:00 1 24",
					if (pl.ams_pr_fare != null && pl.ams_pr_fare.trim().length() > 0) {
						Vector<PlaceParkingGarageAmsterdamPrVariation> vect1 = new Vector<PlaceParkingGarageAmsterdamPrVariation>();
						String[] vects = pl.ams_pr_fare.trim().split("[|]");
						for (int j = 0; j < vects.length; j++) {
							String[] parts = vects[j].trim().split("[ ]");
							if (parts.length == 5) {
								String[] dayparts = parts[0].trim().split("[-]");
								int day_low = Integer.parseInt(dayparts[0]);
								int day_high = Integer.parseInt(dayparts[1]);
								int hour_start = Integer.parseInt(parts[1].split("[:]")[0]);
								int hour_end = Integer.parseInt(parts[2].split("[:]")[0]);
								double dayrate = Double.parseDouble(parts[3]);
								int maxstay = Integer.parseInt(parts[4]);
								if (day_low < day_high) {
									if (hour_start == 0 && hour_end == 0) {
										PlaceParkingGarageAmsterdamPrVariation pv = new PlaceParkingGarageAmsterdamPrVariation();
										pv.dayOfWeek_start = day_low;
										pv.dayOfWeek_end = day_high;
										pv.entry_start = 0;
										pv.entry_end = 24;
										pv.price_day = dayrate;
										vect1.add(pv);
									} else if (hour_start < hour_end) {
										PlaceParkingGarageAmsterdamPrVariation pv = new PlaceParkingGarageAmsterdamPrVariation();
										pv.dayOfWeek_start = day_low;
										pv.dayOfWeek_end = day_high;
										pv.entry_start = hour_start;
										pv.entry_end = hour_end;
										pv.price_day = dayrate;
										vect1.add(pv);
									} else {
										PlaceParkingGarageAmsterdamPrVariation pv = new PlaceParkingGarageAmsterdamPrVariation();
										pv.dayOfWeek_start = day_low;
										pv.dayOfWeek_end = day_high;
										pv.entry_start = 0;
										pv.entry_end = hour_start;
										pv.price_day = dayrate;
										vect1.add(pv);

										PlaceParkingGarageAmsterdamPrVariation pv1 = new PlaceParkingGarageAmsterdamPrVariation();
										pv1.dayOfWeek_start = day_low;
										pv1.dayOfWeek_end = day_high;
										pv1.entry_start = hour_end;
										pv1.entry_end = 24;
										pv1.price_day = dayrate;
										vect1.add(pv1);
										
									}
									
									
								} else {
									if (hour_start == 0 && hour_end == 0) {
										PlaceParkingGarageAmsterdamPrVariation pv = new PlaceParkingGarageAmsterdamPrVariation();
										pv.dayOfWeek_start = day_low;
										pv.dayOfWeek_end = day_low;
										pv.entry_start = 0;
										pv.entry_end = 24;
										pv.price_day = dayrate;
										vect1.add(pv);
										
										PlaceParkingGarageAmsterdamPrVariation pv1 = new PlaceParkingGarageAmsterdamPrVariation();
										pv1.dayOfWeek_start = day_high;
										pv1.dayOfWeek_end = day_high;
										pv1.entry_start = 0;
										pv1.entry_end = 24;
										pv1.price_day = dayrate;
										vect1.add(pv1);
									} else if (hour_start < hour_end) {
										PlaceParkingGarageAmsterdamPrVariation pv = new PlaceParkingGarageAmsterdamPrVariation();
										pv.dayOfWeek_start = day_low;
										pv.dayOfWeek_end = day_low;
										pv.entry_start = hour_start;
										pv.entry_end = hour_end;
										pv.price_day = dayrate;
										vect1.add(pv);
										
										PlaceParkingGarageAmsterdamPrVariation pv1 = new PlaceParkingGarageAmsterdamPrVariation();
										pv1.dayOfWeek_start = day_high;
										pv1.dayOfWeek_end = day_high;
										pv.entry_start = hour_start;
										pv.entry_end = hour_end;
										pv1.price_day = dayrate;
										vect1.add(pv1);
									} else {
										PlaceParkingGarageAmsterdamPrVariation pv = new PlaceParkingGarageAmsterdamPrVariation();
										pv.dayOfWeek_start = day_low;
										pv.dayOfWeek_end = day_low;
										pv.entry_start = 0;
										pv.entry_end = hour_start;
										pv.price_day = dayrate;
										vect1.add(pv);
										
										PlaceParkingGarageAmsterdamPrVariation pv1 = new PlaceParkingGarageAmsterdamPrVariation();
										pv1.dayOfWeek_start = day_high;
										pv1.dayOfWeek_end = day_high;
										pv1.entry_start = 0;
										pv1.entry_end = hour_start;
										pv1.price_day = dayrate;
										vect1.add(pv1);

										PlaceParkingGarageAmsterdamPrVariation pv2 = new PlaceParkingGarageAmsterdamPrVariation();
										pv2.dayOfWeek_start = day_low;
										pv2.dayOfWeek_end = day_low;
										pv2.entry_start = hour_end;
										pv2.entry_end = 24;
										pv2.price_day = dayrate;
										vect1.add(pv2);
										
										PlaceParkingGarageAmsterdamPrVariation pv3 = new PlaceParkingGarageAmsterdamPrVariation();
										pv3.dayOfWeek_start = day_high;
										pv3.dayOfWeek_end = day_high;
										pv3.entry_start = hour_end;
										pv3.entry_end = 24;
										pv3.price_day = dayrate;
										vect1.add(pv3);
										
									
									}
								}
							}
						}

						
						Object result[] = new PlaceParkingGarageAmsterdamPrVariation[vect1.size()];
						vect1.copyInto(result);
						pl.ams_pr_fares = (PlaceParkingGarageAmsterdamPrVariation[])result;
					}
					
					
				} catch (Exception E) {
					E.printStackTrace(System.out);
				}
				
			}
			
			
			vect.add(pl);
			
			/*
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
			*/			
		}

		Object result[] = new PlaceParkingGarage[vect.size()];
		vect.copyInto(result);
		return (PlaceParkingGarage[])result;
		
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
	
}
