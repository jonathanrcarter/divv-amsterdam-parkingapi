package com.glimworm.opendata.divvamsterdamapi.planning;

import java.util.ArrayList;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Place;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Leg;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.MMdatetime;

public class PlanResponse {
	public int status = 0;
	public String realtime_journey_id = "";
	public long distance = 0; //In Meters
	public int duration = 0; 
	public Place startAddress = null;
	public Place endAddress = null;
	public String src = "";
	public String url = "";
	public ArrayList<Leg> legs = null;
	public String type = "";
	public MMdatetime startTime = null;
	public MMdatetime endTime = null;
	public String rawdata = "";
	public String otp_url = "";
	public String proxy_url = "";
	public String proxy_url_rest = "";
	public String error_text = "";
	public org.json.JSONObject data = null;
	
	
/*	$retval->distance = floor($response->route->distance*1000);
    $retval->duration = $response->route->time;
    $retval->distancetxt = floor($response->route->distance*1000);
    $retval->durationtxt = $response->route->time;
    place $retval->startaddress = $response->route->locations[0]->street . $response->route->locations[0]->postalCode;
    place $retval->endaddress = $response->route->locations[1]->street . $response->route->locations[1]->postalCode;
    $retval->src = "curl";
    $retval->url = $url."?outFormat=json&from=".$origin."&to=".$destination."&unit=k&routeType=fastest&shapeFormat=raw&narrativeType=text&generalize=200";
    $retval->rawdata = $result1;
    $retval->data = json_decode($result1);
    $retval->legs = array($lg);
    $retval->type = "car/mapquest";
    $retval->startTime = $lg->startTime;
    $retval->endTime = $lg->endTime;*/
//    $retval->legs = $legs;
	
	
	public String toString() {
		return this.distance+" dates: "+
		((this.startTime == null) ? "" : this.startTime.toString()) +" - "+
		((this.endTime == null) ? "" : this.endTime.toString()) +" // "+
		((this.startAddress == null) ? "" : this.startAddress.toString())+ " // "+
		((this.endAddress == null) ? "" : this.endAddress.toString());
	}
	
}
