package com.glimworm.opendata.divvamsterdamapi.planning.xsd;

import java.util.ArrayList;

public class Journey {
	
	ArrayList<Leg> Legs = new ArrayList<Leg>();

	public void addLeg(Leg leg) {
		Legs.add(leg);
	}
	
	public String as_json() {
//		jsonUtils.  still to convery to json
		return "{}";
	}

	/*
	 * Still to convery from php
	 * 

    public function as_json() {
                return json_encode($this);
    }

    public function summary() {
            $retval = "Journey\n";
            foreach ($this->legs as $leg) {
                    if ($leg->type == "STATIC") {
                            $retval = $retval . "\t" . $leg->mode . "\n";
                    } else {
                            $retval = $retval . "\t" . $leg->type . " (et:".$leg->endTime.", dur:".$leg->duration.", cost:".$leg->cost.") \n";
                            $retval = $retval . "\t" . $leg->url ."\n" ;
//                            $retval = $retval . "\t" . $leg->rawdata ."\n" ;
                            foreach ($leg->legs as $leg2) {
                                    $retval = $retval . "\t\t". $leg2->summary()."\n";
                            }
                    }
                }
                return $retval;
            
    }    
    public function summary_in_html($N) {
        $CNT = 0;
        $retval = "<ul>";
        $endtime = "";
        foreach ($this->legs as $leg) {
                $endtime = $leg->endTime->asTime();
            }
        $retval = $retval . sprintf("<li class='mode-journey'><div class='mode-title'>Arrival:</div><div class='journey-endtime'>%s</div></li>",$endtime);

        foreach ($this->legs as $leg) {
                $CNT1 = 0;
                if ($leg->type == "STATIC") {
                        try {
                                $ad = $leg->from->name;
                        } catch (Exception $e) {
                                $ad = "";
                        }
                        $retval = $retval . sprintf("<li class='mode-%s'><div class='mode-title'>%s</div><div class='mmhub'></div>%s</li>", $leg->mode, $leg->mode,$ad);
                } else {
                        $details = "et:".$leg->endTime."<br>";
                        if ($leg->duration != "") $details = $details . "dur:".$leg->duration; 
                        $details = $details . "<br>";
                        if ($leg->cost != "") $details = $details . "cost:".$leg->cost."";
                        
                        $lnk = sprintf("<a href='javascript:replan(%s,%s)'>Details</a>",$N,$CNT);
                        
                        $retval = $retval . sprintf("<li class='mode-title-li'><div class='mode-title'>%s</div> <div>%s</div>%s</li>", $leg->type,$details,$lnk);
                        foreach ($leg->legs as $leg2) {
                                $retval = $retval . $leg2->summary_in_html($N,$CNT,$CNT1);
                                $CNT1++;
                        }
                }
                $CNT++;
            }
            $retval = $retval . "</ul>";
            return $retval;
        
    }
    */
	
}
