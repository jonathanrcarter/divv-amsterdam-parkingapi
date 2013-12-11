package com.glimworm.opendata.parkshark.xsd;

public class Meter implements Cloneable {
	public int i = 0;
	public String poi = null;
	public int match = 0;
	public double dist = 0;
	public double dist2 = 0;
	public boolean onmap = false;
	public double lat = 0;
	public double lon = 0;
	public double cost = 0;
	public double totalcost = 0;
	public double max = 0;
	public boolean isInNorth = false;
	public String dbg = "";
	
	public BetWijze bw = new BetWijze();	// payment types
	public PayTimes costs = new PayTimes();	
	
	public String csdkid = "";
	public String csdkzone = "";
	public String chance_weekday = "";
	public String chance_sat = "";
	public String chance_sun = "";
	public String parking_chance = "";
	
	
	/*
	 * DATABASE
	   entityid: 101
	   stadsdeel: Centrum
	   belnummer: 10101
	       adres: Herengracht 607
	    postcode: 1017CE
	  woonplaats: Amsterdam
	typeautomaat: CWO
	 betaalwijze: 5
	      status: OP
	  tariefcode: 1
	         lat: 4.8991499
	         lon: 52.3655281
	*/
	
	public String entityid = "";
	public String stadsdeel = "";
	public String belnummer = "";
	public String adres = "";
	public String postcode = "";
	public String woonplaats = "";
	public String typeautomaat = "";
	public String betaalwijze = "";
	public String status = "";
	public String tariefcode = "";
	
	public String type = "meter";
	public String costsignature = "";
	

    public Object clone() {
        try {
            return super.clone();
        } catch( CloneNotSupportedException e ) {
            return null;
        }
    } 
	
}
