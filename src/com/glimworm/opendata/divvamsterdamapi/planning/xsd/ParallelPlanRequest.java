package com.glimworm.opendata.divvamsterdamapi.planning.xsd;

public class ParallelPlanRequest {
	public Place pl_destination = null;
	public String ymd = "";
	public String hm = "";
	public boolean plan_return_also = false;
	public String ret_ymd = "";
	public String ret_hm = "";
	public double plan_rad = 10000;	// 10km
	public int opt_otp_tim = 3000;	// open trip planner timeout
	public double opt_otp_walkspeed = 0;
	public int opt_otp_server = 0;
}
