package com.glimworm.opendata.divvamsterdamapi.planning;

import java.util.ArrayList;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Place;
import com.glimworm.opendata.utils.jsonUtils;

public class GeoCodeByMapQuest extends GeoCode {

	public static ArrayList<Place> geocode(String address) {
		
		String URL = "http://open.mapquestapi.com/geocoding/v1/address";
		String PARAMS = "key=Fmjtd%7Cluubn16829%2Cbw%3Do5-90asgw&location=" + java.net.URLEncoder.encode(address);
		com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse cr =  com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(URL, PARAMS, null, null, null, null, null);
		
		//System.out.println(cr.text);
		
		org.json.JSONObject jsob = jsonUtils.string2json(cr.text);
		org.json.JSONArray results = jsob.optJSONArray("results");
		
		ArrayList<Place> retval = new ArrayList<Place>();
		
		
		for (int i=0; i<results.length(); i++) {
			org.json.JSONArray locations = results.optJSONObject(i).optJSONArray("locations");
			
			for (int j=0; j<locations.length(); j++) {
				org.json.JSONObject item = locations.optJSONObject(j);
			
				Place location = new Place();
				location.name = "";
				location.lat = item.optJSONObject("latLng").optDouble("lat");
				location.lon = item.optJSONObject("latLng").optDouble("lng");
				location.street = item.optString("street");
				location.postcode = item.optString("postalCode");
				location.url = item.optString("mapUrl");
				location.type = item.optString("type");
				location.rawdata = item.toString();
				location.data = item;
				retval.add(location);
			}
		}
		
		
		return retval;
	}
	
	/*
    public function geolookup_mapquest($address) {
        //        http://open.mapquestapi.com/geocoding/v1/address?location=glimworm    
        // (see http://open.mapquestapi.com/geocoding/#locations )
        
        $url="http://open.mapquestapi.com/geocoding/v1/address?location=".$address;
        $ch1 = curl_init();
        curl_setopt($ch1, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch1, CURLOPT_URL, $url);
        $result1 = curl_exec($ch1);
        $response = json_decode($result1);
        $retval = array();
        
        foreach ($response->results[0]->locations as $loc) {
                $ad = new obj();
                $ad->name = "";
                $ad->lat = $loc->latLng->lat;
                $ad->lng = $loc->latLng->lng;
                $ad->street = $loc->street;
                $ad->postcode = $loc->postalCode;
                $ad->url = $loc->mapUrl;
                $ad->type = $loc->type;
                $ad->data = $loc; //textstring? json2string
                array_push($retval , $ad);
        }
        
        return $retval;
    }
    */
	
}
