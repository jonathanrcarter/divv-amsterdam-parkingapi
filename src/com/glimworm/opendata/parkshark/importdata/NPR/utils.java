package com.glimworm.opendata.parkshark.importdata.NPR;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Place;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.PlaceParkingGarage;
import com.glimworm.opendata.parkshark.importdata.citySDK.xsd.*;

public class utils {

	public static geom geomFromJson(org.json.JSONObject ob) {
		/* a geom is in the format
		geom : {
			type: "Point",
			coordinates: [
				4.8849266,
				52.3848016
			]
		}
		*/
		
		geom retval = new geom();
		try {
			retval.type = ob.optString("type","");
			org.json.JSONArray coordinates = ob.optJSONArray("coordinates");
			if (coordinates != null) {
				retval.lat = coordinates.getDouble(1);
				retval.lon = coordinates.getDouble(0);
			}
		} catch (Exception E) {
			retval.status = 9;
		}
		return retval;
	}
	
	public static PlaceParkingGarage garageFromJson(org.json.JSONObject nameob,org.json.JSONObject ob, String cdk_id, geom coordinates) {
		
		PlaceParkingGarage retval = new PlaceParkingGarage();
		try {
			retval.name = nameob.optString("name");
			retval.postcode = ob.optString("postcode");
			retval.street = ob.optString("adres");
			retval.url = ob.optString("url");
			retval.cdk_id = cdk_id;
			
			retval.places = ob.optInt("antaal", 0);
			if (coordinates != null) {
				retval.lat = coordinates.lat;
				retval.lon = coordinates.lon;
			}
			if (ob.optString("type","").equalsIgnoreCase("Parkeergarage")) retval.type = "parking-garage";
			else if (ob.optString("type","").equalsIgnoreCase("P+R")) retval.type = "park-and-ride";
			else retval.type = "parking-garage";
			
			retval.url = ob.optString("info_url","");

		
		} catch (Exception E) {
		}
		
		return retval;

	}
	
	
	
	public static void placeFromJson(org.json.JSONObject ob, Place pl, geom coordinates) {
		/*
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
		*/

		try {
			pl.name = ob.optString("title");
			pl.postcode = ob.optString("postcode");
			pl.street = ob.optString("adres");
			pl.url = ob.optString("url");
			if (coordinates != null) {
				pl.lat = coordinates.lat;
				pl.lon = coordinates.lon;
			}
			
		} catch (Exception E) {
		}
		
	}
	
}
