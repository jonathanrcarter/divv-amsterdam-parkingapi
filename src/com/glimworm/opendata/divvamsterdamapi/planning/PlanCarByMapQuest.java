package com.glimworm.opendata.divvamsterdamapi.planning;

import java.util.ArrayList;

import com.glimworm.opendata.divvamsterdamapi.planning.transit.xsd.TransitInfo;
import com.glimworm.opendata.divvamsterdamapi.planning.transit.xsd.TransitInfoBase;
import com.glimworm.opendata.divvamsterdamapi.planning.transit.xsd.TransitInfoCar;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Leg;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.MMdatetime;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Place;
import com.glimworm.opendata.utils.jsonUtils;

public class PlanCarByMapQuest extends PlanCar {

	public static PlanResponse plan(PlanRequest request) {

		/*
		 * code here
		 */
		Place from = request.from;
		Place to = request.to;
		
		String URL = "http://open.mapquestapi.com/directions/v1/route";
		String PARAMS = "outFormat=json&from="+from.lat+","+from.lon+"&to="+to.lat+","+to.lon+"&unit=k&routeType=fastest&shapeFormat=raw&narrativeType=text&generalize=200";
		com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse cr =  com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(URL, PARAMS, null, null, null, null, null);
		
//		System.out.println("--- start route api response ---");
//		System.out.println(cr.text);
//		System.out.println("--- end route api response ---");
		
		org.json.JSONObject jsob = jsonUtils.string2json(cr.text);
		org.json.JSONObject route = jsob.optJSONObject("route");
		
		PlanResponse response = new PlanResponse();
		response.distance = route.optLong("distance");
		response.duration = route.optInt("time");
		response.startAddress = from;
		response.endAddress = to;
		response.legs = new ArrayList<Leg>();
		response.startTime = request.options._datetime;
		response.endTime = request.options._datetime.addSeconds(route.optInt("duration"));
		response.rawdata = route.toString();
		response.data = route;
		
		org.json.JSONArray legs = route.optJSONArray("legs");
		for (int i=0; i<legs.length(); i++) {
			org.json.JSONObject responseleg = legs.optJSONObject(i);
			Leg leg = new Leg();
			leg.from = from;
			leg.to = to;
			leg.mode = TransitInfo.LEG_TYPE_DRIVING;
			leg.startTime = request.options._datetime;
			leg.endTime = request.options._datetime.addSeconds(route.optInt("duration"));
			leg.transitinfo = new TransitInfoCar();
			leg.transitinfo.from = from;
			leg.transitinfo.to = to;
			leg.type = "leg";
			
			response.legs.add(leg);
		}
		
	
		
		
		return response;
		
	}
	
	/*
    public function plan_car_mapquest($request) {
        
        $origin = $request->from->lat ."," . $request->from->lon;
        $destination = $request->to->lat ."," . $request->to->lon;
        
//                reference : http://open.mapquestapi.com/directions/
            
            $url="http://open.mapquestapi.com/directions/v1/route";
            
            $ch1 = curl_init();
//                    curl_setopt($ch1, CURLOPT_POST, 1);
            curl_setopt($ch1, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($ch1, CURLOPT_URL, $url."?outFormat=json&from=".$origin."&to=".$destination."&unit=k&routeType=fastest&shapeFormat=raw&narrativeType=text&generalize=200");
            
            //mustAvoidLinkIds
            //tryAvoidLinkIds
            //dateType=2 (mon, 3=tue)
            //&timeType=2&dateType=0&date=04/14/2011&localTime=12:05
            
            $result1 = curl_exec($ch1);
            $response = json_decode($result1);

//                    echo "<h1>dump response</h1>";
//                    var_dump($response);
            
            $dur = $response->route->time;

            $lg = new Leg();
            $lg->from = new Place($request->from->lat, $request->from->lon, $request->from->name);
            $lg->to = new Place($request->to->lat, $request->to->lon, $request->to->name);
            $lg->mode = "DRIVING";
            $lg->duration = $dur;
            $lg->rawlegdata = $response;
            $lg->startTime = $request->options->_datetime;
            $lg->endTime = new mm_datetime();
            $lg->endTime->setMMDateTime($request->options->_datetime);
            $lg->endTime->addMinutes(floor($dur/60));


            $retval = new obj();
            $retval->distance = floor($response->route->distance*1000);
            $retval->duration = $response->route->time;
            $retval->distancetxt = floor($response->route->distance*1000);
            $retval->durationtxt = $response->route->time;
            $retval->startaddress = $response->route->locations[0]->street . $response->route->locations[0]->postalCode;
            $retval->endaddress = $response->route->locations[1]->street . $response->route->locations[1]->postalCode;
            $retval->src = "curl";
            $retval->url = $url."?outFormat=json&from=".$origin."&to=".$destination."&unit=k&routeType=fastest&shapeFormat=raw&narrativeType=text&generalize=200";
            $retval->rawdata = $result1;
            $retval->data = json_decode($result1);
            $retval->legs = array($lg);
            $retval->type = "car/mapquest";
            $retval->startTime = $lg->startTime;
            $retval->endTime = $lg->endTime;
//            $retval->legs = $legs;
        return $retval;


    }	
    */	
}
