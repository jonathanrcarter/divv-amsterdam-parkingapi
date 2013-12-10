package com.glimworm.opendata.parkshark.importdata.citySDK;

import java.util.Vector;

import org.json.JSONObject;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.PlaceParkingGarage;
import com.glimworm.opendata.parkshark.importdata.citySDK.xsd.geom;
import com.glimworm.opendata.parkshark.xsd.Meter;

public class Amsterdam {
	public static PlaceParkingGarage[] getGarages() {
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

			PlaceParkingGarage pl = com.glimworm.opendata.parkshark.importdata.citySDK.utils.garageFromJson(data, cdk_id, coords);
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
