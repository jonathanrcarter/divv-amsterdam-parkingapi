package com.glimworm.opendata.parkshark.xsd;

public class PayTimes {
	public String geb_code = "";
	public PayTime[] days = {null,null,null,null,null,null,null};

	public String t_code = "";
	public First first = new First();
	public double cost = 0;
	public double max = 0;	
	public double maxdaycost = 0;
	public int error = 0;
}
