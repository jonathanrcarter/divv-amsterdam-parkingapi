package com.glimworm.opendata.divvamsterdamapi.planning.xsd;

import com.glimworm.opendata.divvamsterdamapi.planning.transit.xsd.TransitInfoBase;

public class Leg {

	public Place from, to = null;
	public long distance = 0; //In Meters
	public int duration = 0; 
	public String mode = "";
	public TransitInfoBase transitinfo = null;
	public MMdatetime startTime, endTime = null;
	public String type = "";
	
	public Leg() {
		type = "leg";
		transitinfo = new TransitInfoBase();
	}

	
	/*
	 * Still to convert from php
	 * 
	 public function __toString()
	    {
	            try {
	                return sprintf('%s --> %s --> (%s) (st:%s et:%s) (%s)',$this->from,$this->to,$this->mode,$this->startTime,$this->endTime,$this->transitinfo);
	            } catch(Exception $e) {
	                return $this->from . " --> " . $this->to ." (". $this->mode . ") (no transit info)";
	            }
	    }

	    public function summary()
	    {
	            try {
	                return sprintf('%s --> %s --> (%s) (st:%s et:%s)',$this->from,$this->to,$this->mode,$this->startTime,$this->endTime);
	            } catch(Exception $e) {
	                return $this->from . " --> " . $this->to ." (". $this->mode . ") (no transit info)";
	            }
	    }
	    public function summary_in_html($N,$CNT,$CNT1)
	    {
	            try {
	                    $dur = ($this->duration && $this->duration > 0) ? "<div>du:".$this->duration . "</div>" : "";
	                    try {
	                            $line = ($this->transitinfo && $this->transitinfo->line && $this->transitinfo->line != "") ? $this->transitinfo->line : "";
	                            $agency = ($this->transitinfo && $this->transitinfo->agency && $this->transitinfo->agency != "") ? $this->transitinfo->agency : "";
	                            if ($line.$agency != "") {
	                                    $ti = sprintf("<div>ti:%s %s</div>",$this->transitinfo->line,$this->transitinfo->agency);
	                            } else {
	                                    $ti = "<div>&nbsp;</div>";
	                            }
	                    } catch(Exception $e) {
	                            $ti = "<div>&nbsp;</div>";
	                    }
	                    $lnk = sprintf("<a class='btn btn-mini' href='javascript:track(%s,%s,%s)'>details</a>",$N,$CNT,$CNT1);
	                    
	                return sprintf("<li class='mode-%s'><div class='mode-title'>%s</div><div class='mode-times'>%s <i class='icon-arrow-right'></i> %s</div> %s %s %s</li>",$this->mode,$this->mode,$this->startTime->asTime(),$this->endTime->asTime(),$dur, $ti, $lnk, $this->from,$this->to);
	            } catch(Exception $e) {
	                return $this->from . " --> " . $this->to ." (". $this->mode . ") (no transit info)";
	            }
	    }	
	 */	
}
