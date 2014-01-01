package com.glimworm.opendata.parkshark.importdata.citySDK;

import java.util.Vector;

import org.json.JSONObject;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.PlaceParkingGarage;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.PlaceParkingGarageOpeningTimes;
import com.glimworm.opendata.parkshark.importdata.citySDK.xsd.geom;
import com.glimworm.opendata.parkshark.xsd.Meter;

public class Amsterdam {
	public static PlaceParkingGarage[] getGarages() {
		// TODO Auto-generated method stub
//		String S = "http://api.citysdk.waag.org/admr.nl.amsterdam/ptstops?name=Leidseplein";
//		String S = "http://api.citysdk.waag.org/nodes?layer=divv.parking.buildings&geom";
		String S = "http://test-api.citysdk.waag.org/nodes?layer=test.divv.parking.car&geom&per_page=1000";
		
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
		for (int i=0; i < ar.length(); i++) {
			System.out.println(ar.optJSONObject(i).optString("name"));
			System.out.println(ar.optJSONObject(i).optString("cdk_id"));
			System.out.println(ar.optJSONObject(i).optString("node_type"));

			
			geom coords = com.glimworm.opendata.parkshark.importdata.citySDK.utils.geomFromJson(ar.optJSONObject(i).optJSONObject("geom"));
			String cdk_id = ar.optJSONObject(i).optString("cdk_id");
			
			System.out.println(coords);
			
			JSONObject data = ar.optJSONObject(i).optJSONObject("layers").optJSONObject("test.divv.parking.car").optJSONObject("data");

			PlaceParkingGarage pl = com.glimworm.opendata.parkshark.importdata.citySDK.utils.garageFromJson(ar.optJSONObject(i),data, cdk_id, coords);
			
			/*
			{
				status: "success",
				url: "http://test-api.citysdk.waag.org/test.divv.parking.car.15?layer=test.divv.parking.car.price",
				results: [{
					cdk_id: "test.divv.parking.car.15",
					name: "Amsterdamse Poort (P21 t/m 24)",
					node_type: "node",
					layers: {
					test.divv.parking.car.price: {
						data: {
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
			String PriceURL = "http://test-api.citysdk.waag.org/"+cdk_id+"?layer=test.divv.parking.car.price";
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
					JSONObject data1 = ar1.optJSONObject(0).optJSONObject("layers").optJSONObject("test.divv.parking.car.price").optJSONObject("data");
					
					pl.csdkid = cdk_id;
					pl.csdkurl = "http://test-api.citysdk.waag.org/"+cdk_id;
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
