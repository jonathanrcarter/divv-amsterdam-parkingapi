package com.glimworm.opendata.divvamsterdamapi.planning;

public class PlanOtp extends Plan {

	public static PlanResponse plan(PlanRequest request) {

		/*
		 * code here
		 */
		
		
		PlanResponse response = new PlanResponse();
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
