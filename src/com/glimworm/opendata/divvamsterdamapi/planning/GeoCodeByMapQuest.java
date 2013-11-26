package com.glimworm.opendata.divvamsterdamapi.planning;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Place;

public class GeoCodeByMapQuest extends GeoCode {

	public static Place geocode(String S) {
		
		
		Place retval = new Place();
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
                $ad->data = $loc;
                array_push($retval , $ad);
        }
        
        return $retval;
    }
    */
	
}
