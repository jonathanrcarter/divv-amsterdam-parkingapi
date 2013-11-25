package com.glimworm.opendata.parkshark.xsd;

public class Costs {

	/*
		costs
		--------
		t_code : "TZ12"
		first : {
			combination Y/n
			hrs : 3
			price : 0,10
		}
		cost : 5
	*/	
	
	public String t_code = "";
	public First first = new First();
	public double cost = 0;
	public double max = 0;
//	public Costs(String T_code, First pFirst, double Cost, double Max) {
//		t_code = T_code;
//		first = pFirst;
//		cost = Cost;
//		max = Max;
//	}
}


