package com.glimworm.opendata.divvamsterdamapi.planning;

import java.util.ArrayList;

import com.glimworm.opendata.divvamsterdamapi.planning.transit.xsd.TransitInfo;
import com.glimworm.opendata.divvamsterdamapi.planning.transit.xsd.TransitInfoBase;
import com.glimworm.opendata.divvamsterdamapi.planning.transit.xsd.TransitInfoBus;
import com.glimworm.opendata.divvamsterdamapi.planning.transit.xsd.TransitInfoSubway;
import com.glimworm.opendata.divvamsterdamapi.planning.transit.xsd.TransitInfoTrain;
import com.glimworm.opendata.divvamsterdamapi.planning.transit.xsd.TransitInfoTram;
import com.glimworm.opendata.divvamsterdamapi.planning.transit.xsd.TransitInfoWalk;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Leg;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.MMdatetime;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Place;
import com.glimworm.opendata.utils.jsonUtils;

public class PlanOtp extends Plan {

	public static PlanResponse plan(PlanRequest request) {

		/*
		 * code here
		 */
		Place from = request.from;
		Place to = request.to;
		
		String URL = "http://opentripplanner.nl/opentripplanner-api-webapp/ws/plan";
		String PARAMS = "maxTransfers="+request.options.maxTransfers;
		PARAMS += "&_dc=1358423838102";
		PARAMS += "&from=";
		PARAMS += "&to=";
		PARAMS += "&arriveBy="+request.options.arriveBy;
		PARAMS += "&mode="+request.options.mode;
		PARAMS += "&optimize="+request.options.optimize;
		PARAMS += "&maxWalkDistance="+request.options.maxWalkDistance;
		PARAMS += "&walkSpeed="+request.options.walkSpeed;
		PARAMS += "&hst="+request.options.hst;
		PARAMS += "&date="+request.options._date;
		PARAMS += "&time="+request.options._time;
		PARAMS += "&toPlace="+to.lat+","+to.lon;
		PARAMS += "&fromPlace="+from.lat+","+from.lon;
		System.out.println("--- otp api call ---");
		System.out.println(URL+"?"+PARAMS);
		
		com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse cr =  com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(URL, PARAMS, null, null, null, null, null);
		
		//System.out.println("--- start otp api response ---");
		System.out.println(cr.text);
		//System.out.println("--- end otp api response ---");

		org.json.JSONObject jsob = jsonUtils.string2json(cr.text);
		org.json.JSONObject plan = jsob.optJSONObject("plan");
		org.json.JSONObject itinerary = plan.optJSONArray("itineraries").optJSONObject(0);

		PlanResponse response = new PlanResponse();
		response.distance = 0; //plan.optLong("distance");
		response.duration = itinerary.optInt("duration")/1000;
		response.startAddress = new Place();
		response.startAddress.lon = plan.optJSONObject("from").optDouble("lon");
		response.startAddress.lat = plan.optJSONObject("from").optDouble("lat");
		response.startAddress.name = plan.optJSONObject("from").optString("name");
		response.endAddress = new Place();
		response.endAddress.lon = plan.optJSONObject("to").optDouble("lon");
		response.endAddress.lat = plan.optJSONObject("to").optDouble("lat");
		response.endAddress.name = plan.optJSONObject("to").optString("name");
		response.legs = new ArrayList<Leg>();
		response.startTime = null; //itinerary.optString("startTime");
		response.endTime = null;//itinerary.optString("endTime");;
		response.rawdata = plan.toString();
		response.data = plan;
		
		
		org.json.JSONArray legs = itinerary.optJSONArray("legs");
		for (int i=0; i<legs.length(); i++) {
			org.json.JSONObject responseleg = legs.optJSONObject(i);
			Leg leg = new Leg();
			leg.from = new Place();
			leg.from.lon = responseleg.optJSONObject("from").optDouble("lon");
			leg.from.lat = responseleg.optJSONObject("from").optDouble("lat");
			leg.from.name = responseleg.optJSONObject("from").optString("name");
			leg.to = new Place();
			leg.mode = TransitInfo.LEG_TYPE_DRIVING;
			leg.startTime = null; //responseleg.optString("startTime");
			leg.endTime = null; //responseleg.optString("endTime");
			leg.type = "leg";

			String legMode = responseleg.optString("mode");
			
			if (legMode.equalsIgnoreCase("BUS")) {
                leg.transitinfo = new TransitInfoBus();
                leg.transitinfo.polyline = responseleg.optString("legGeometry");
                leg.transitinfo.agency = responseleg.optString("agencyName");
                leg.transitinfo.line = responseleg.optString("route");
                leg.transitinfo.lineId = responseleg.optString("routeId");
                leg.transitinfo.headsign = responseleg.optString("headsign");
                leg.transitinfo.from = leg.from;
                leg.transitinfo.to = leg.to;
                
			} else if (legMode.equalsIgnoreCase("TRAM")) {
                leg.transitinfo = new TransitInfoTram();
                leg.transitinfo.polyline = responseleg.optString("legGeometry");
                leg.transitinfo.agency = responseleg.optString("agencyName");
                leg.transitinfo.line = responseleg.optString("route");
                leg.transitinfo.lineId = responseleg.optString("routeId");
                leg.transitinfo.headsign = responseleg.optString("headsign");
                leg.transitinfo.from = leg.from;
                leg.transitinfo.to = leg.to;
                
			} else if (legMode.equalsIgnoreCase("SUBWAY")) {
                leg.transitinfo = new TransitInfoSubway();
                leg.transitinfo.polyline = responseleg.optString("legGeometry");
                leg.transitinfo.agency = responseleg.optString("agencyName");
                leg.transitinfo.line = responseleg.optString("route");
                leg.transitinfo.lineId = responseleg.optString("routeId");
                leg.transitinfo.headsign = responseleg.optString("headsign");
                leg.transitinfo.from = leg.from;
                leg.transitinfo.to = leg.to;
                
			} else if (legMode.equalsIgnoreCase("RAIL")) {
                leg.transitinfo = new TransitInfoTrain();
                leg.transitinfo.polyline = responseleg.optString("legGeometry");
                leg.transitinfo.agency = responseleg.optString("agencyName");
                leg.transitinfo.line = responseleg.optString("route");
                leg.transitinfo.lineId = responseleg.optString("routeId");
                leg.transitinfo.headsign = responseleg.optString("headsign");
                leg.transitinfo.from = leg.from;
                leg.transitinfo.to = leg.to;
                
			} else if (legMode.equalsIgnoreCase("WALK")) {
                leg.transitinfo = new TransitInfoWalk();
                leg.transitinfo.polyline = responseleg.optString("legGeometry");
                leg.transitinfo.agency = responseleg.optString("agencyName");
                leg.transitinfo.line = responseleg.optString("route");
                leg.transitinfo.lineId = responseleg.optString("routeId");
                leg.transitinfo.headsign = responseleg.optString("headsign");
                leg.transitinfo.from = leg.from;
                leg.transitinfo.to = leg.to;
                
			} else {
                leg.transitinfo = new TransitInfoBase();
                leg.transitinfo.polyline = responseleg.optString("legGeometry");
                
			}

			
			
			/*
			 if ($leg->mode == "BUS") {
                                        $lg->transitinfo = new TransitInfoBus();
                                        $lg->transitinfo->agency = $leg->agencyName;
                                        $lg->transitinfo->line = $leg->route;
                                        $lg->transitinfo->lineId = $leg->routeId;
                                        $lg->transitinfo->headsign = $leg->headsign;
                                        $lg->transitinfo->from->stopindex = $leg->from->stopIndex;
                                        $lg->transitinfo->from->stopid = $leg->from->stopId->id;
                                        $lg->transitinfo->from->name = $leg->from->name;
                                        $lg->transitinfo->from->lat = $leg->from->lat;
                                        $lg->transitinfo->from->lon = $leg->from->lon;
                                        $lg->transitinfo->from->scheduled_time_at_stop = $lg->startTime;
                                        $lg->transitinfo->to->stopindex = $leg->to->stopIndex;
                                        $lg->transitinfo->to->stopid = $leg->to->stopId->id;
                                        $lg->transitinfo->to->name = $leg->to->name;
                                        $lg->transitinfo->to->lat = $leg->to->lat;
                                        $lg->transitinfo->to->lon = $leg->to->lon;
                                        $lg->transitinfo->to->scheduled_time_at_stop = $lg->endTime;
                                        $lg->transitinfo->legGeometry = $leg->legGeometry;

                                        
                                } else if ($leg->mode == "TRAM") {
                                        $lg->transitinfo = new TransitInfoTram();
                                        $lg->transitinfo->agency = $leg->agencyName;
                                        $lg->transitinfo->line = $leg->route;
                                        $lg->transitinfo->lineId = $leg->routeId;
                                        $lg->transitinfo->headsign = $leg->headsign;
                                        $lg->transitinfo->from->stopindex = $leg->from->stopIndex;
                                        $lg->transitinfo->from->stopid = $leg->from->stopId->id;
                                        $lg->transitinfo->from->name = $leg->from->name;
                                        $lg->transitinfo->from->lat = $leg->from->lat;
                                        $lg->transitinfo->from->lon = $leg->from->lon;
                                        $lg->transitinfo->from->scheduled_time_at_stop = $lg->startTime;
                                        $lg->transitinfo->to->stopindex = $leg->to->stopIndex;
                                        $lg->transitinfo->to->stopid = $leg->to->stopId->id;
                                        $lg->transitinfo->to->name = $leg->to->name;
                                        $lg->transitinfo->to->lat = $leg->to->lat;
                                        $lg->transitinfo->to->lon = $leg->to->lon;
                                        $lg->transitinfo->to->scheduled_time_at_stop = $lg->endTime;
                                        $lg->transitinfo->legGeometry = $leg->legGeometry;

                                } else if ($leg->mode == "SUBWAY") {
                                        $lg->transitinfo = new TransitInfoSubway();
                                        $lg->transitinfo->agency = $leg->agencyName;
                                        $lg->transitinfo->line = $leg->route;
                                        $lg->transitinfo->lineId = $leg->routeId;
                                        $lg->transitinfo->headsign = $leg->headsign;
                                        $lg->transitinfo->from->stopindex = $leg->from->stopIndex;
                                        $lg->transitinfo->from->stopid = $leg->from->stopId->id;
                                        $lg->transitinfo->from->name = $leg->from->name;
                                        $lg->transitinfo->from->lat = $leg->from->lat;
                                        $lg->transitinfo->from->lon = $leg->from->lon;
                                        $lg->transitinfo->from->scheduled_time_at_stop = $lg->startTime;
                                        $lg->transitinfo->to->stopindex = $leg->to->stopIndex;
                                        $lg->transitinfo->to->stopid = $leg->to->stopId->id;
                                        $lg->transitinfo->to->name = $leg->to->name;
                                        $lg->transitinfo->to->lat = $leg->to->lat;
                                        $lg->transitinfo->to->lon = $leg->to->lon;
                                        $lg->transitinfo->to->scheduled_time_at_stop = $lg->endTime;
                                        $lg->transitinfo->legGeometry = $leg->legGeometry;
                                        
                                } else if ($leg->mode == "RAIL") {
                                        $lg->transitinfo = new TransitInfoBase();
                                        $lg->transitinfo->agency = $leg->agencyName;
                                        $lg->transitinfo->line = $leg->route;
                                        $lg->transitinfo->lineId = $leg->routeId;
                                        $lg->transitinfo->headsign = $leg->headsign;

                                        $lg->transitinfo->from->stopindex = $leg->from->stopIndex;
                                        $lg->transitinfo->from->stopid = $leg->from->stopId->id;

                                        $lg->transitinfo->to->stopindex = $leg->to->stopIndex;
                                        $lg->transitinfo->to->stopid = $leg->to->stopId->id;

                                        $lg->transitinfo->routeColor = $leg->routeColor;
                                        $lg->transitinfo->routeType = $leg->routeType;
                                        $lg->transitinfo->tripShortName = $leg->tripShortName;
                                        $lg->transitinfo->tripId = $leg->tripId;

                                        $lg->transitinfo->legGeometry = $leg->legGeometry;
                                        $lg->transitinfo->notes = $leg->notes;
                                        $lg->transitinfo->alerts = $leg->alerts;
                                        $lg->transitinfo->duration = $leg->duration;

                                        $lg->rawlegdata = $leg;
                                
                                } else {
                                        $lg->transitinfo = new TransitInfoBase();
                                        $lg->transitinfo->legGeometry = $leg->legGeometry;
                                        $lg->rawlegdata = $leg;
			  
			 */
			
			
			response.legs.add(leg);
			response.distance += responseleg.optDouble("distance");
		}
		
		return response;
		
	}
	
	/*
    public function plan_otp($request) {
    
            $h = "http://opentripplanner.nl/opentripplanner-api-webapp/ws/plan";
//            $h = "http://aws2.glimworm.com:8080/opentripplanner-api-webapp/ws/plan";
            
            http://aws2.glimworm.com:8080/
            $h = $h . "?maxTransfers=".$request->options->maxTransfers;
            $h = $h . "&_dc=1358423838102";
            $h = $h . "&from=";
            $h = $h . "&to=";
            $h = $h . "&arriveBy=".$request->options->arriveBy;
//            $h = $h . "&ui_date=".$request->options->ui_date;
            $h = $h . "&mode=".$request->options->mode;
            $h = $h . "&optimize=".$request->options->optimize;
            $h = $h . "&maxWalkDistance=".$request->options->maxWalkDistance;
            $h = $h . "&walkSpeed=".$request->options->walkSpeed;
            $h = $h . "&hst=".$request->options->hst;
            $h = $h . "&date=".$request->options->_date;
            $h = $h . "&time=".$request->options->_time;
            $h = $h . "&toPlace=".$request->to->toString();
            $h = $h . "&fromPlace=".$request->from->toString();
//            $h = $h . "&maxWalkDistance=".$request->options->maxWalkDistance;
//            $h = $h . "&maxWalkDistance=".$request->options->maxWalkDistance;

                $ch = curl_init();
                $timeout = 5;
                curl_setopt($ch,CURLOPT_URL,$h);
                curl_setopt($ch,CURLOPT_RETURNTRANSFER,1);
                curl_setopt($ch,CURLOPT_CONNECTTIMEOUT,$timeout);
                $data = curl_exec($ch);
                curl_close($ch);
                
                $legs = array();
                $last_endtime = null;
                                
                $trip = json_decode($data);
                if ($trip->plan) {
                        $itinerary = $trip->plan->itineraries[0];
                        
                        for ($i=0; $i < count($itinerary->legs); $i++) {
                                $leg = $itinerary->legs[$i];
//                                echo "\n Leg : $i \n";
//                                var_dump($leg);

                                $SUMMERTIME = (60*60);
                                
                                $lg = new Leg();
                                $lg->from = new Place($leg->from->lat, $leg->from->lon, $leg->from->name);
                                $lg->to = new Place($leg->to->lat, $leg->to->lon, $leg->to->name);
                                $lg->mode = $leg->mode;
                                $lg->startTime = mm_datetime::createFromDateTime(DateTime::createFromFormat('U',($leg->startTime/1000)+$SUMMERTIME+(60*60),new DateTimeZone('Europe/Amsterdam')));
                                $lg->endTime = mm_datetime::createFromDateTime(DateTime::createFromFormat('U',($leg->endTime/1000)+$SUMMERTIME+(60*60),new DateTimeZone('Europe/Amsterdam')));
                                $last_endtime = $lg->endTime;
                                
                                if ($leg->mode == "BUS") {
                                        $lg->transitinfo = new TransitInfoBus();
                                        $lg->transitinfo->agency = $leg->agencyName;
                                        $lg->transitinfo->line = $leg->route;
                                        $lg->transitinfo->lineId = $leg->routeId;
                                        $lg->transitinfo->headsign = $leg->headsign;
                                        $lg->transitinfo->from->stopindex = $leg->from->stopIndex;
                                        $lg->transitinfo->from->stopid = $leg->from->stopId->id;
                                        $lg->transitinfo->from->name = $leg->from->name;
                                        $lg->transitinfo->from->lat = $leg->from->lat;
                                        $lg->transitinfo->from->lon = $leg->from->lon;
                                        $lg->transitinfo->from->scheduled_time_at_stop = $lg->startTime;
                                        $lg->transitinfo->to->stopindex = $leg->to->stopIndex;
                                        $lg->transitinfo->to->stopid = $leg->to->stopId->id;
                                        $lg->transitinfo->to->name = $leg->to->name;
                                        $lg->transitinfo->to->lat = $leg->to->lat;
                                        $lg->transitinfo->to->lon = $leg->to->lon;
                                        $lg->transitinfo->to->scheduled_time_at_stop = $lg->endTime;
                                        $lg->transitinfo->legGeometry = $leg->legGeometry;

                                        
                                } else if ($leg->mode == "TRAM") {
                                        $lg->transitinfo = new TransitInfoTram();
                                        $lg->transitinfo->agency = $leg->agencyName;
                                        $lg->transitinfo->line = $leg->route;
                                        $lg->transitinfo->lineId = $leg->routeId;
                                        $lg->transitinfo->headsign = $leg->headsign;
                                        $lg->transitinfo->from->stopindex = $leg->from->stopIndex;
                                        $lg->transitinfo->from->stopid = $leg->from->stopId->id;
                                        $lg->transitinfo->from->name = $leg->from->name;
                                        $lg->transitinfo->from->lat = $leg->from->lat;
                                        $lg->transitinfo->from->lon = $leg->from->lon;
                                        $lg->transitinfo->from->scheduled_time_at_stop = $lg->startTime;
                                        $lg->transitinfo->to->stopindex = $leg->to->stopIndex;
                                        $lg->transitinfo->to->stopid = $leg->to->stopId->id;
                                        $lg->transitinfo->to->name = $leg->to->name;
                                        $lg->transitinfo->to->lat = $leg->to->lat;
                                        $lg->transitinfo->to->lon = $leg->to->lon;
                                        $lg->transitinfo->to->scheduled_time_at_stop = $lg->endTime;
                                        $lg->transitinfo->legGeometry = $leg->legGeometry;

                                } else if ($leg->mode == "SUBWAY") {
                                        $lg->transitinfo = new TransitInfoSubway();
                                        $lg->transitinfo->agency = $leg->agencyName;
                                        $lg->transitinfo->line = $leg->route;
                                        $lg->transitinfo->lineId = $leg->routeId;
                                        $lg->transitinfo->headsign = $leg->headsign;
                                        $lg->transitinfo->from->stopindex = $leg->from->stopIndex;
                                        $lg->transitinfo->from->stopid = $leg->from->stopId->id;
                                        $lg->transitinfo->from->name = $leg->from->name;
                                        $lg->transitinfo->from->lat = $leg->from->lat;
                                        $lg->transitinfo->from->lon = $leg->from->lon;
                                        $lg->transitinfo->from->scheduled_time_at_stop = $lg->startTime;
                                        $lg->transitinfo->to->stopindex = $leg->to->stopIndex;
                                        $lg->transitinfo->to->stopid = $leg->to->stopId->id;
                                        $lg->transitinfo->to->name = $leg->to->name;
                                        $lg->transitinfo->to->lat = $leg->to->lat;
                                        $lg->transitinfo->to->lon = $leg->to->lon;
                                        $lg->transitinfo->to->scheduled_time_at_stop = $lg->endTime;
                                        $lg->transitinfo->legGeometry = $leg->legGeometry;
                                        
                                } else if ($leg->mode == "RAIL") {
                                        $lg->transitinfo = new TransitInfoBase();
                                        $lg->transitinfo->agency = $leg->agencyName;
                                        $lg->transitinfo->line = $leg->route;
                                        $lg->transitinfo->lineId = $leg->routeId;
                                        $lg->transitinfo->headsign = $leg->headsign;

                                        $lg->transitinfo->from->stopindex = $leg->from->stopIndex;
                                        $lg->transitinfo->from->stopid = $leg->from->stopId->id;

                                        $lg->transitinfo->to->stopindex = $leg->to->stopIndex;
                                        $lg->transitinfo->to->stopid = $leg->to->stopId->id;

                                        $lg->transitinfo->routeColor = $leg->routeColor;
                                        $lg->transitinfo->routeType = $leg->routeType;
                                        $lg->transitinfo->tripShortName = $leg->tripShortName;
                                        $lg->transitinfo->tripId = $leg->tripId;

                                        $lg->transitinfo->legGeometry = $leg->legGeometry;
                                        $lg->transitinfo->notes = $leg->notes;
                                        $lg->transitinfo->alerts = $leg->alerts;
                                        $lg->transitinfo->duration = $leg->duration;

                                        $lg->rawlegdata = $leg;
                                
                                } else {
                                        $lg->transitinfo = new TransitInfoBase();
                                        $lg->transitinfo->legGeometry = $leg->legGeometry;
                                        $lg->rawlegdata = $leg;
                                        
                                        
                                }
                                
                                array_push($legs , $lg);
                                
                        }        
                        $retval = new obj();
                        $retval->url = $h;
                        $retval->rawdata = $data;
                        $retval->data = json_decode($data);
                        $retval->legs = $legs;
                        $retval->type = "transit/OTP";
                        $retval->endTime = $last_endtime;
                        $retval->duration = 0;        // we don't use the duration here we instead send the endtime
                        $retval->status = 0;        // ok
                } else {
                        // 

                        // error occurred, it is generally possible to get errors like:

                        // Trip is not possible.  Your start or end point might not be safely accessible (for instance, you might be starting on a residential street connected only to a highway).

                        //
                        $retval = new obj();
                        $retval->url = $h;
                        $retval->rawdata = $data;
                        $retval->data = json_decode($data);
                        $retval->legs = array();
                        $retval->type = "transit/OTP";
                        $retval->endTime = $request->options->_datetime;
                        $retval->duration = 0;
                        $retval->status = -1;        // error
                        
                }

        
//                $items = json_decode($data);
//                array_push($totalitems , $items->items);
//                $totalitems = array_merge($totalitems , $items->items);
//                $totalitems = $items->items;



            
            return $retval;

    }
    */	
}
