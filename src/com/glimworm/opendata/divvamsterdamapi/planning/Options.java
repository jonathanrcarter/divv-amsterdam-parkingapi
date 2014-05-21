package com.glimworm.opendata.divvamsterdamapi.planning;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.MMdatetime;

public class Options {
	
    public String mode = "TRANSIT,WALK";
    public int maxTransfers = 25;
    public String arriveBy = "false";
    public String ui_date = "2014-01-14";
    public String optimize = "QUICK";
    public int maxWalkDistance = 3000;
    public double walkSpeed = 1.3888;
    public int server = 0;
    public boolean hst = true;
    public String _date = "2014-01-14";
    public String _time = "15:02";
    public MMdatetime _datetime;
    
    public Options() {
    	_datetime = new MMdatetime();
    }

}
