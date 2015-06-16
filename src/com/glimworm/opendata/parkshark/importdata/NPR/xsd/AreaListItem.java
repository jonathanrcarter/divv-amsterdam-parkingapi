package com.glimworm.opendata.parkshark.importdata.NPR.xsd;

import java.util.ArrayList;

import com.glimworm.opendata.parkshark.xsd.PayTimes;
import com.vividsolutions.jts.geom.Polygon;

public class AreaListItem {
	/*
		
	{
	  "areaid" : "510",
	  "areamanagerid" : "599",
	  "uuid" : "42589417-307e-4f96-9a09-325eb9718d14"
	}		
		
	*/	
	public String areamanagerid = "";
	public String areaId = "";
	public String uuid = "";
	
	// from the second call
	public boolean valid = true;
	public String usage = "";
	public long validityStartOfPeriod = 0;
	public long validityEndOfPeriod = 0;
	public String detail_url = "";
	public String description = "";
	public String name = "";
	public org.json.JSONObject operator = null;
	public double lat = 0;
	public double lon = 0;

	public String streetName = "";
	public String houseNumber = "";
	public String zipcode = "";
	public String city = "";
	public String province = "";
	public String country = "";
//	public Polygon poly = null;
	public ArrayList<Polygon> polys = new ArrayList<Polygon>();
	
	
	public PayTimes pt = null;
	
	
	
}
