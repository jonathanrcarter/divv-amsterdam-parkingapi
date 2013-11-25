package com.glimworm.opendata.parkshark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.glimworm.opendata.parkshark.xsd.*;
import com.glimworm.opendata.xsd.*;
import java.util.Comparator;

public class CalcParking {
	

	
	public static String Q(String S) {
		return "\""+S+"\"";
	}
	public static double RadToDeg(double radians) {
		return radians * (180 / Math.PI);
	}
	public static double DegToRad (double degrees) {
		return degrees * (Math.PI / 180);
	}
	
	public static double distance(location loc1, location loc2) {
		double lat1 = DegToRad(loc1.latitude);
		double lon1 = DegToRad(loc1.longitude);
		double lat2 = DegToRad(loc2.latitude);
		double lon2 = DegToRad(loc2.longitude);
		

		double R = 6371000000.0; // m (change this constant to get miles)
		double dLat = (lat2-lat1) * Math.PI / 180;
		double dLon = (lon2-lon1) * Math.PI / 180;
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	    Math.cos(lat1 * Math.PI / 180 ) * Math.cos(lat2 * Math.PI / 180 ) *
	    Math.sin(dLon/2) * Math.sin(dLon/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double d = R * c;
		
//		System.out.println("dist ["+Math.round(d/2)+"] LAT ["+loc1.latitude+"/"+loc2.latitude+"] LON ["+loc1.longitude+"/"+loc2.longitude+"]");
		
	    return Math.round(d/2);
	};
	
	public static double getNorthLatFromLng(double lng) {
		double start_lat = 52.4208;
		double start_lng = 4.82815;	
		double lon_delta = 3.636122177; 	// each time the lon increases by 1 the lat decreases by this value

		double difference_in_lng = (lng - start_lng);
		double new_lat = start_lat - (difference_in_lng * lon_delta);		// new Longitude
		return new_lat;
	};

	public static boolean isInNorth(double lat, double lng) {
		double start_lat = 52.4208;
		double start_lng = 4.82815;	
		double lon_delta = 0.3636122177; 	// each time the lon increases by 1 the lat decreases by this value

		double difference_in_lng = (lng - start_lng);
		double new_lat = start_lat - (difference_in_lng * lon_delta);		// new Longitude
		
		if (lat > new_lat) return true;
		return false;
	};	
	
	public static boolean has(String S, String[] paymethods) {
		for (int i=0; i < paymethods.length; i++) {
			if (paymethods[i].equalsIgnoreCase(S)) return true;
		}
		return false;
	}

	
	public static Gebcode getgebcode(Meter meter) {
		/*
			gebcodes
			--------
			t_code :"a"
			geb_code : "G1"
			price_code : "TZ12"
			max : 60
			oms : ""
		*/
		try {
			java.sql.ResultSet rs = com.glimworm.common.database.GWDBBean.sqlStatic("select * from _site1493_dbsynch_gebiedcodes_xls where Tarief='"+meter.tariefcode+"'");
			if (rs.next()) {
				Gebcode gc = new Gebcode();
				gc.t_code = rs.getString("Tarief");
				gc.geb_code = rs.getString("GebiedCode");
				gc.price_code = rs.getString("Prijs");
				try {
					gc.max = rs.getInt("Max");
				} catch (Exception E) {
				}
				gc.oms = rs.getString("Omschrijving");
				return gc;
			}
		} catch (Exception E) {
		}
		return new Gebcode();
	}
	
	/**
	 * get geb codes
	 * @return
	 */
	public static Gebcode[] getgebcodes() {
		String sql = "select * from _site1493_dbsynch_gebiedcodes_xls";
		com.glimworm.common.database.xsd.DataSet data = com.glimworm.common.database.gwDataUtils.getArray(com.glimworm.common.database.GWDBBean.sqlStatic(sql),false);
		Gebcode[] gc = new Gebcode[data.rows()];
		for (int i=0; i < data.rows(); i++) {
			gc[i] = new Gebcode();
			gc[i].t_code = data.getString(i,"Tarief");
			gc[i].geb_code = data.getString(i,"GebiedCode");
			gc[i].price_code = data.getString(i,"Prijs");
			try {
				gc[i].max = data.getInt(i,"Max",0);
			} catch (Exception E) {
			}
			gc[i].oms = data.getString(i,"Omschrijving");
		}
		return gc;
	}
	public static String getGebCodes() {
		Gebcode[] gc = getgebcodes();
		String retval = Response.obj2json(gc);
		return retval;
//		return "{"+retval+"}";
	}
	
	
	public static Costs getcost(String price_code) {
		Costs retval = new Costs();
		
		String sql1 = "select * from _site1493_dbsynch_paytariff where code='"+price_code+"'";
		com.glimworm.common.database.xsd.DataSet ds = com.glimworm.common.database.gwDataUtils.getArray(com.glimworm.common.database.GWDBBean.sqlStatic(sql1),false);
		if (ds != null && ds.rows() > 0) {
			retval.t_code = ds.getString(0,"code");

			String price = ds.getString(0,"price");
			if (price.length() == 4) {
				retval.first.combination = "n";
				retval.first.hrs = 0;
				retval.first.price = 0.0;
				retval.cost = Double.parseDouble(price.trim());
			} else if (price.length() == 12 && price.indexOf(" en ") > -1) {
				retval.first.combination = "y";
				retval.first.hrs = 3;
				retval.first.price = Double.parseDouble(price.trim().substring(8));
				retval.cost = Double.parseDouble(price.substring(0,4));
			}
		}
		return retval;
	}
	public static Costs[] getcosts() {
		String sql = "select * from _site1493_dbsynch_paytariff";
		com.glimworm.common.database.xsd.DataSet data = com.glimworm.common.database.gwDataUtils.getArray(com.glimworm.common.database.GWDBBean.sqlStatic(sql),false);
		Costs[] c = new Costs[data.rows()];
		for (int i=0; i < data.rows(); i++) {
			c[i] = new Costs();
			c[i].t_code = data.getString(i,"code");
			String price = data.getString(i,"price");
			if (price.length() == 4) {
				c[i].first.combination = "n";
				c[i].first.hrs = 0;
				c[i].first.price = 0.0;
				c[i].cost = Double.parseDouble(price.trim());
			} else if (price.length() == 12 && price.indexOf(" en ") > -1) {
				c[i].first.combination = "y";
				c[i].first.hrs = 3;
				c[i].first.price = Double.parseDouble(price.trim().substring(8));
				c[i].cost = Double.parseDouble(price.substring(0,4));
			}
		}
		return c;
	}
	public static String getCosts() {
		Costs[] c = getcosts();
		String retval = Response.obj2json(c);
		return retval;
//		return "{"+retval+"}";
	}	
	
	
	
	
	public static PayTime decodeDate(String S) {
		String start = "0";
		String end = "0";
		if (S.trim().length() == 11) {
			if (S.substring(0,1) == "0") start = S.substring(1,2); else start = S.substring(0,2);
			if (S.substring(3,5) == "15") start += ".25";
			if (S.substring(3,5) == "30") start += ".5";
			if (S.substring(3,5) == "45") start += ".75";
			
			if (S.substring(6,7) == "0") end = S.substring(7,8); else end = S.substring(6,8);
			if (S.substring(9,11) == "15") end += ".25";
			if (S.substring(9,11) == "30") end += ".5";
			if (S.substring(9,11) == "45") end += ".75";
			 
		} 
		PayTime retval = new PayTime();
		retval.start = Integer.parseInt(start);
		retval.end = Integer.parseInt(end);
		return retval;
	}	


	
	public static PayTimes getpaytime(String geb_code) {
		PayTimes retval = new PayTimes();
		
		String sql1 = "select * from _site1493_dbsynch_paytimes where gebiedcode='"+geb_code+"'";
		com.glimworm.common.database.xsd.DataSet ds = com.glimworm.common.database.gwDataUtils.getArray(com.glimworm.common.database.GWDBBean.sqlStatic(sql1),false);
		if (ds != null && ds.rows() > 0) {
			retval.geb_code = ds.getString(0,"gebiedcode");
			retval.days[0] = decodeDate(ds.getString(0,"zo"));
			retval.days[1] = decodeDate(ds.getString(0,"ma"));
			retval.days[2] = decodeDate(ds.getString(0,"di"));
			retval.days[3] = decodeDate(ds.getString(0,"wo"));
			retval.days[4] = decodeDate(ds.getString(0,"do"));
			retval.days[5] = decodeDate(ds.getString(0,"vr"));
			retval.days[6] = decodeDate(ds.getString(0,"za"));
		}
		return retval;
	}
	
	
	public static PayTimes getcosts(Meter meter) {
		PayTimes retval = new PayTimes();
		
		Gebcode gebcode = getgebcode(meter);
		if (gebcode.geb_code.trim().length() == 0) {
			retval.cost = -1;
			return retval;	
		}

		String tcode = gebcode.t_code;
		String price_code = gebcode.price_code;
		String geb_code = gebcode.geb_code;

		Costs cost = getcost(price_code);
		if (cost.t_code.trim().length() == 0) {
			retval.cost = -2;
			return retval;	
		}

		PayTimes pt = getpaytime(geb_code);
		if (pt.geb_code.trim().length() == 0) {
			retval.cost = -3;
			return retval;	
		}	
		
		retval = pt;
		retval.t_code = cost.t_code;
		retval.first = cost.first;
		retval.cost = cost.cost;
		retval.max = gebcode.max;
		
		return retval;
	}
	
	public static String dp2(double d) {
		return Double.toString(d);
	}
	public static String dp2(int d) {
		return Integer.toString(d);
	}
	public static int asint(double d) {
		return new Double(d).intValue();
	}
	
	public static Meter[] smeters = null;
	public static void populate_meters() {
		String sql = "select a.*,p.cash,p.creditcard,p.pin,p.chip from _site1493_dbsynch_automats a left join _site1493_dbsynch_paymethods p on (a.typeautomaat = p.type) ";
		com.glimworm.common.database.xsd.DataSet automats = com.glimworm.common.database.gwDataUtils.getArray(com.glimworm.common.database.GWDBBean.sqlStatic(sql),false);
		smeters = new Meter[automats.rows()];
		for (int i=0; i < automats.rows(); i++) {
			smeters[i] = new Meter();
			smeters[i].i = i;
			smeters[i].entityid = automats.getString(i, "entityid");
			smeters[i].stadsdeel = automats.getString(i, "stadsdeel");
			smeters[i].belnummer = automats.getString(i, "belnummer");
			smeters[i].adres = automats.getString(i, "adres");
			smeters[i].postcode = automats.getString(i, "postcode");
			smeters[i].woonplaats = automats.getString(i, "woonplaats");
			smeters[i].typeautomaat = automats.getString(i, "typeautomaat");
			smeters[i].betaalwijze = automats.getString(i, "betaalwijze");
			smeters[i].tariefcode = automats.getString(i, "tariefcode");
			smeters[i].status = automats.getString(i, "status");
			smeters[i].lat = automats.getDouble(i, "lon",0);
			smeters[i].lon = automats.getDouble(i, "lat",0);

			smeters[i].bw.cash = automats.getString(i, "cash").equalsIgnoreCase("Y");
			smeters[i].bw.creditcard = automats.getString(i, "creditcard").equalsIgnoreCase("Y");
			smeters[i].bw.pin = automats.getString(i, "pin").equalsIgnoreCase("Y");
			smeters[i].bw.chip = automats.getString(i, "chip").equalsIgnoreCase("Y");
			
			smeters[i].costs = getcosts(smeters[i]);
			try {
				smeters[i].isInNorth = isInNorth(smeters[i].lat,smeters[i].lon);
			} catch (Exception E) {
				smeters[i].isInNorth = false;
			}

			
			
//			String sql2 = "select * from _site1493_dbsynch_paymethods where type='"+meters[i].typeautomaat+"'";
//			com.glimworm.common.database.xsd.DataSet paymethods = com.glimworm.common.database.gwDataUtils.getArray(com.glimworm.common.database.GWDBBean.sqlStatic(sql2),false);
//			if (paymethods.rows() > 0) {
//				meters[i].bw.cash = paymethods.getString(0, "cash").equalsIgnoreCase("Y");
//				meters[i].bw.creditcard = paymethods.getString(0, "creditcard").equalsIgnoreCase("Y");
//				meters[i].bw.pin = paymethods.getString(0, "pin").equalsIgnoreCase("Y");
//				meters[i].bw.chip = paymethods.getString(0, "chip").equalsIgnoreCase("Y");
//			}
			System.out.println("meter"+i+": "+smeters[i].belnummer + " ::" + smeters[i].adres);
			
		}		
	}
	
	public static String find_nearest(double lat, double lon, double rate) {
		
		
		if (smeters == null) {
			populate_meters();
		}

		Meter[] meters = smeters.clone();

		location loc = new location(lat, lon);

		int nearest = -1;
		double nearest_distance = -1;

		for (int i=0; i < meters.length; i++) {
			Meter meter = meters[i];
			PayTimes costs = getcosts(meter);

			if (rate > 0) {
				if (costs.cost < rate) continue;
			}

			double d = distance(loc, new location(meter.lat, meter.lon));
			if (nearest == -1 || d < nearest_distance) {
				nearest = i;
				nearest_distance = d;
			}
		}
		
		String retval = "{";
		retval += Q("id")+":"+nearest+",";
		retval += Q("meterlat")+":"+meters[nearest].lat+",";
		retval += Q("meterlon")+":"+meters[nearest].lon+",";
		retval += Q("meternum")+":"+meters[nearest].belnummer+",";
		retval += Q("dist")+":"+nearest_distance+"";
		retval += "}";
		
		return retval;
		
	}
	
	public static Meter getMeterById(String meternumber) {
		if (smeters == null) {
			populate_meters();
		}
		for (int i=0; i < smeters.length; i++) {
			if (smeters[i].belnummer.equalsIgnoreCase(meternumber)) return smeters[i];
		}
		return null;
	}

	public static int getMeterIndexById(String meternumber) {
		if (smeters == null) {
			populate_meters();
		}
		for (int i=0; i < smeters.length; i++) {
			if (smeters[i].belnummer.equalsIgnoreCase(meternumber)) return i;
		}
		return -1;
	}
	
	public static String getMeters(double north, double south, double east, double west) {
		if (smeters == null) {
			populate_meters();
		}

		class a {
			public double dist = 0;
			public double cost = 0;
			public String belnummer = "";
			public String address = "";
			public double lat = 0;
			public double lon = 0;
			public int i = 0;
		}
		ArrayList<a> al = new ArrayList<a>();
		
		
		for (int i=0; i < smeters.length; i++) {
			if (smeters[i].lon >= west && smeters[i].lon <= east && smeters[i].lat >= south && smeters[i].lat <= north) {
				a ameter = new a();
				ameter.lat = smeters[i].lat;
				ameter.lon = smeters[i].lon;
				ameter.address = smeters[i].adres;
				ameter.belnummer = smeters[i].belnummer;
				al.add(ameter);
			}
		}
		
		String retval2 = "";
		for (int j=0; j < al.size(); j++) {
			if (j > 0) retval2 += ",";
			retval2 += "["+Q(al.get(j).belnummer)+",0,0,"+Q(al.get(j).address)+","+al.get(j).lat+","+al.get(j).lon+","+j+","+al.get(j).i+"]";
		}
		
		String retval = "{"+Q("items")+":["+retval2+"]}";
		
		return retval;
		
	}
	public static String getMeters(String meternumber) {
		
		if (smeters == null) {
			populate_meters();
		}
		
		String[] meternumbers = meternumber.split("[,]");

		Meter[] meters = new Meter[meternumbers.length];
		
		// add distance	
		for (int i=0; i < meternumbers.length; i++) {
			meters[i] = getMeterById(meternumbers[i]);
		}
		
		String retval = Response.obj2json(meters);
		return retval;		
//		return "{"+retval+"}";
	}
	
	public static String calc(int _day, int hrs, int mins, double duration, double from_lat, double from_lon, String _paymethods) {
		return calc( _day,  hrs,  mins,  duration,  from_lat,  from_lon,  _paymethods,0);
	}
	
	public static String calc(int _day, int hrs, int mins, double duration, double from_lat, double from_lon, String _paymethods,int fmt) {
//		var day = $ef.currentdata.date_start.dt.getDay();		// 0 = sunday
//		var duration = $ef.currentdata.duration;
//		var hrs = $ef.currentdata.date_start.dt.getHours();
//		var mins = $ef.currentdata.date_start.dt.getMinutes();
		
		
		// make a table in the format
		//
		//	[{dayofweek, starttime, endtime}]
		class Days {
			public int day = 0;
			public int start = 0;
			public int end = 0;
			public Days () {
				
			}
			public Days (int Day, int Start, int End) {
				day = Day;
				start = Start;
				end = End;
			}
		}
		
		ArrayList<Days> days = new ArrayList<Days>();

		// tim is decimal i.e 22.5 or 23.75
		int total_parking_minutes = new Double(duration*60).intValue();
		
		int tim = hrs;
		if (hrs > 0) tim += (mins/60);
	
		while (true) {	
			int remainder = (24 - tim);
			if (remainder > duration) {
				Days d = new Days(_day, tim, (tim+new Double(duration).intValue()));
				days.add(d);
				break;
			} else {
				Days d = new Days(_day, tim,24);
				days.add(d);
				duration = duration - (24 - tim);
				tim = 0;
				_day++;
				if (_day > 6) _day = 0;
			}
		}
		
		System.out.println("START CALC");
		System.out.println("DAYS");
		System.out.println(days);

		/*
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

		if (smeters == null || _paymethods.indexOf("reload") > -1) {
			populate_meters();
		}
		
		
		Meter[] meters = new Meter[smeters.length];
		
		location loc = new location(from_lat, from_lon);
		
		long start = new Date().getTime();
		
		int cc = 0;
		// add distance	

		boolean DBG = false;
		if (fmt == 3) DBG = true;
		
		
		for (int i=0; i < meters.length; i++) {
			
			meters[i] = new Meter();
			meters[i].lat = smeters[i].lat;
			meters[i].lon = smeters[i].lon;
			meters[i].bw = smeters[i].bw;
			meters[i].i = i;
			
			Meter meter = meters[i];
//			if (meter.match == 0) continue;
			
			double d = distance(loc, new location(meter.lat, meter.lon));
			meters[i].dist = d;
			meters[i].dist2 = d;
			
			PayTimes costs = smeters[i].costs;

			if (costs.cost < 0) {
				meters[i].cost = costs.cost;
				continue;
			}
			meters[i].max = costs.max;

			double val = 0;
			String dbg = "";
			
			for (int j=0; j < days.size(); j++) {
				// we have to clone the array
				Days day = new Days();
				day.day = days.get(j).day;
				day.start = days.get(j).start;
				day.end = days.get(j).end;
				
				PayTime cday = costs.days[day.day];
				
				if (DBG) dbg += "[xj:"+j+"/s:"+dp2(day.start)+"/e:"+dp2(day.end)+"/c:"+costs.cost+"/d.d:"+day.day+"]\n";
				if (DBG) dbg += "[xj:"+j+"/gc:"+costs.geb_code+"/fc:"+costs.first.combination+"/fh:"+costs.first.hrs+"/fp:"+costs.first.price+"]\n";
				if (DBG) dbg += "[xj:"+j+"/s:"+cday.start+"/e:"+cday.end+"]\n";
				if (cday.start == 0 && cday.end == 0) continue;
				if (DBG) dbg += "a";
				if (day.start > cday.end) continue;
				if (DBG) dbg += "b";
				if (day.end < cday.start) continue;
				if (DBG) dbg += "c";
				if (day.start < cday.start) day.start = cday.start;
				if (DBG) dbg += "d";
				if (day.end > cday.end) day.end = cday.end;
				if (DBG) dbg += "e";
				double dayhours = (day.end - day.start);
	
				if (dayhours > 9) {
					// you never pay more than 9 hours
					dayhours = 9;
				} else if (cday.start == 9 && cday.end == 24 && day.start == 9 && day.end == 19) {
					// day card = 6h
					dayhours = 6;
				} else if (cday.start == 9 && cday.end == 24 && day.start == 19 && day.end == 24) {
					// evening card = 4h
					dayhours = 4;
				} else if (cday.start == 12 && cday.end == 24 && dayhours > 7.2) {
					// sundaycard = 7.2h
					dayhours = 7.2;
				}
	
				int el = 0;
				if (costs.first.combination.equalsIgnoreCase("y") && j == 0) {
					if (dayhours <= costs.first.hrs) {
						val += (dayhours * costs.first.price);
						if (DBG) dbg += "f\n";
						if (DBG) dbg += "[yj:"+j+"/s:"+day.start+"/e:"+day.end+"/c:"+costs.cost+"/hrs:"+dayhours+"(all in first hours),val="+val+"]\n";
					} else {
						val += (costs.first.hrs * costs.first.price);
						val += ((dayhours - costs.first.hrs) * costs.cost);
	
						if (DBG) dbg += "f\n";
						if (DBG) dbg += "[yj:"+j+"/s:"+day.start+"/e:"+day.end+"/c:"+costs.cost+"/hrs:"+dayhours+"(some in first hours),val="+val+"]\n";
					}
				} else {
					val += (dayhours * costs.cost);
					if (DBG) dbg += "f\n";
					if (DBG) dbg += "[yj:"+j+"/s:"+day.start+"/e:"+day.end+"/c:"+costs.cost+"/hrs:"+dayhours+"(none in first hours),val="+val+"]\n";
				}
				
				
			}
			
	//		dbg += "max["+meter.max+"] tpm["+total_parking_minutes+"]\n";
//			if (DBG) dbg += "TOTAL["+val+"]["+dp2(val)+"]\n";
//			try{
//				meters[i].isInNorth = isInNorth(meters[i].lat,meters[i].lon);
//				if (DBG) dbg += "NORTH ["+isInNorth(meters[i].lat,meters[i].lon)+"]\n";
//				if (DBG) dbg += "N-LAT ["+meters[i].lat+"]\n";
//				if (DBG) dbg += "N-LON ["+meters[i].lon+"]\n";
//				if (DBG) dbg += "NN-LNG ["+getNorthLatFromLng(meters[i].lon)+"]\n";
//			} catch (Exception E) {
//				meters[i].isInNorth = false;
//				if (DBG) dbg += "NORTH ["+E.getMessage()+"]\n";
//			}

			// if (val == 0 && cc++ < 3) {
				// System.out.println("days");
				// System.out.println(days);
				// System.out.println("costs.days");
				// System.out.println(costs.days);
				// System.out.println("costs.cost");
				// System.out.println(costs.cost);
			// }

			if (DBG) dbg += ("[val="+val+"]");
			if (DBG) dbg += ("[val(converted)="+new Double(val).toString()+"]");
			
			meters[i].cost = val;
			meters[i].totalcost = val;
			meters[i].dbg = dbg;
			

//			String sql2 = "select * from _site1493_dbsynch_paymethods where type='"+meters[i].typeautomaat+"'";
//			com.glimworm.common.database.xsd.DataSet paymethods = com.glimworm.common.database.gwDataUtils.getArray(com.glimworm.common.database.GWDBBean.sqlStatic(sql2),false);
//			if (paymethods.rows() > 0) {
//				meters[i].bw.cash = paymethods.getString(0, "cash").equalsIgnoreCase("Y");
//				meters[i].bw.creditcard = paymethods.getString(0, "creditcard").equalsIgnoreCase("Y");
//				meters[i].bw.pin = paymethods.getString(0, "pin").equalsIgnoreCase("Y");
//				meters[i].bw.chip = paymethods.getString(0, "chip").equalsIgnoreCase("Y");
//			}
			
		}

		System.out.println("TS1 " + (new Date().getTime() - start));
		
		String[] paymethods = _paymethods.split("[,]");
		
		// add match
		for (int i=0; i < meters.length; i++) {
			Meter meter = meters[i];
			int match = 0;
			if (meter.cost < 0) {
				match++;
			} else {
				if (has("cash",paymethods) && meter.bw.cash == true) match++;
				if (has("creditcard",paymethods) && meter.bw.creditcard == true) match++;
				if (has("pin",paymethods) && meter.bw.pin == true) match++;
				if (has("chipknip",paymethods) && meter.bw.chip == true) match++;
			}
			// if there is a maximum and the desired parking minutes is more than the max then the match is not made
			if (meter.max > 0 && total_parking_minutes > meter.max) match = 0;
			// System.out.println("meter.bw");
			// System.out.println(meter.bw);
			
			meters[i].match = match;
			
			if (DBG) meters[i].dbg += ("[match="+match+"]");
			
		}
		
		System.out.println("END CALC");
		System.out.println("TS2 " + (new Date().getTime() - start));
		
		System.out.println("START SORT");

		class DistanceSort implements Comparator<Meter> {
		    // Comparator interface requires defining compare method.
		    public int compare(Meter a, Meter b) {
		        if (a.dist < b.dist) {return -1;}
		        if (a.dist > b.dist) {return 1;}
		        return 0;
		    }
		}
		
		Comparator<Meter> MeterByDistance = new DistanceSort();
		Arrays.sort(meters, MeterByDistance);

		System.out.println("END SORT");
		System.out.println("TS3 " + (new Date().getTime() - start));
		
		String retval = "";

		class a {
			public double dist = 0;
			public double cost = 0;
			public String belnummer = "";
			public String address = "";
			public double lat = 0;
			public double lon = 0;
			public int i = 0;
		}
		ArrayList<a> al = new ArrayList<a>();
		
		
		for (int i=0; i < meters.length; i++) {
//			System.out.println("d:"+meters[i].dist + "c:"+meters[i].cost + "a:"+meters[i].adres+" \n"+meters[i].dbg+"\n");
			int I = meters[i].i;
			if (i > 0) retval += ",";
			if (fmt == 3) {
				retval += "["+Q(smeters[I].belnummer)+","+meters[i].cost+","+asint(meters[i].dist)+","+meters[i].match+","+meters[i].i+","+meters[i].lat+","+meters[i].lon+","+Q(smeters[I].adres)+","+Q(meters[I].dbg.replace('\n', '|'))+"]";
			} else if (fmt == 2) {
				retval += "["+Q(smeters[I].belnummer)+","+meters[i].cost+","+asint(meters[i].dist)+","+meters[i].match+","+meters[i].i+","+meters[i].lat+","+meters[i].lon+","+Q(smeters[I].adres)+"]";
			} else if (fmt == 1) {
				retval += "["+Q(smeters[I].belnummer)+","+meters[i].cost+","+asint(meters[i].dist)+","+meters[i].match+","+meters[i].i+","+meters[i].lat+","+meters[i].lon+"]";
			} else  {
				retval += "["+Q(smeters[I].belnummer)+","+meters[i].cost+","+asint(meters[i].dist)+","+meters[i].match+","+meters[i].i+"]";
			} 
			
			boolean fnd = false;
			J:
			for (int j=0; j < al.size(); j++) {
				if (al.get(j).cost == meters[i].cost) {
					fnd = true;
					if (al.get(j).dist > meters[i].dist) {
						al.get(j).belnummer = smeters[I].belnummer;
						al.get(j).dist = meters[i].dist;
						al.get(j).address = smeters[I].adres;
						al.get(j).lat = smeters[I].lat;
						al.get(j).lon = smeters[I].lon;
						al.get(j).i = meters[i].i;
						break J;
					}
				}
			}
			if (fnd == false) {
				a newa = new a();
				newa.belnummer = smeters[I].belnummer;
				newa.dist = meters[i].dist;
				newa.cost = meters[i].cost;
				newa.address = smeters[I].adres;
				newa.lat = smeters[I].lat;
				newa.lon = smeters[I].lon;
				al.add(newa);
			}
		}
		
		String retval2 = "";
		for (int j=0; j < al.size(); j++) {
			if (j > 0) retval2 += ",";
			retval2 += "["+Q(al.get(j).belnummer)+","+al.get(j).cost+","+asint(al.get(j).dist)+","+Q(al.get(j).address)+","+al.get(j).lat+","+al.get(j).lon+","+j+","+al.get(j).i+"]";
		}
		
		retval = "{"+Q("params")+":{"+Q("fmt")+":"+fmt+"},"+Q("results")+":["+retval+"],"+Q("advice")+":["+retval2+"]}";
		
		System.out.println("START CHEAPEST");
		System.out.println("TS4 " + (new Date().getTime() - start));
		
		
		


		/*
		int cheapest = 0;
		int cheapestid = -1;
		var cheapestarray = [];
		for (int i=0; i < meters.length; i++) {
			Meter meter = meters[i];
			if (meter.match == 0) continue;
			if (meter.cost < 0) continue;
			if (meter.max && meter.max > 0) continue;
			if (meter.isInNorth == true) continue;
			
			
			int jfnd = -1;
			for (int j=0; j < cheapestarray.length; j++) {
				if (cheapestarray[j].cost == meter.cost) {
					jfnd = j;
					break;
				}
			}
			if (jfnd == -1) {
				cheapestarray.push({cost:meter.cost,dist:meter.dist,id:i,note:''});
			} else {
				if (meter.dist < cheapestarray[jfnd].dist) {
					cheapestarray[jfnd].dist = meter.dist;
					cheapestarray[jfnd].id = i;
				}	
			}
			
			if (cheapestid == -1 || (meter.cost < cheapest)) {
				cheapest = meter.cost;
				cheapestid = i;
			}
		}
		cheapestarray.sort(function(a,b) {
	        if (a.dist < b.dist) {return -1;}
	        if (a.dist > b.dist) {return 1;}
	        return 0;
	    });	
	    Meter nearest = cheapestarray[0];
	    cheapestarray[0].note += "N";
	
	    cheapestarray.sort(function(a,b) {
	        if (a.cost < b.cost) {return -1;}
	        if (a.cost > b.cost) {return 1;}
	        return 0;
	    });	
		System.out.println("END CHEAPEST");
		
		var nearest_walking = nearest;
		for (var j=0; j < cheapestarray.length; j++) {
			if (cheapestarray[j].dist < 15000) {
				nearest_walking = cheapestarray[j];
				cheapestarray[j].note += "W";
				break;
			}
		}
		
		/*
		var top = 10;
		for (var j=0; j < cheapestarray.length; j++) {
			var cheapestbut = $gw.btn("["+formatCurrency(cheapestarray[j].cost)+"]/["+formatDistance(cheapestarray[j].dist)+"]["+cheapestarray[j].note+"]", function(e) {
				var loc = {
					longitude : meters[e.source.customid].lon,
					latitude : meters[e.source.customid].lat,
					latitudeDelta : 0.005,
					longitudeDelta : 0.005
				}
				map.setLocation(loc);	
				
			});
			cheapestbut.top=10+(20*j);
			cheapestbut.width='44%';
			cheapestbut.height=20;
			cheapestbut.borderWidth=0;
			cheapestbut.zIndex=9;
			cheapestbut.font = fnt12;
			cheapestbut.customid = cheapestarray[j].id;
			//w.win.add(cheapestbut);
		}
		*/
		
		/*
		
		$gw.popwin(w);
		w.x.sethead("Park Shark's Advice");
		var info = $gw.btn("close", function() {
			w.x.close();
		});
		w.x.view2.add(info);
	
		if ($ef.isInAmsterdam($ef.currentdata.location_to.latitude,$ef.currentdata.location_to.longitude) == false) {
			w.x.view.add($gw.lbl("Park Shark AMSTERDAM only works in the city limits of amsterdam, if you are planning your trip in advance you can return to the options screen and select a location in the city that you want to park at and a date and time in the future.",{left:'10%', top:10,right:'10%',font:fnt15a,color:BLACK}));
			
		} else {
			w.x.view.add($gw.lbl("Prices based on "+$ef.currentdata.duration+"hr parking at "+$ef.currentdata.location_to.text+" starting at "+formatDate($ef.currentdata.date_start.dt,"MMM dd, yyyy HH:mm"),{left:'10%', top:10,right:'10%',font:fnt15a,color:BLACK}));
		
		
			var lntab = $gw.newTable({width:"100%",height:250,borderWidth:0,top:10,separatorColor:"#ffffff"},false);
			var rows = [];
			
			var ROWHEIGHT = 50;
		
			// var row = $gw.row({height:ROWHEIGHT,borderWidth:0});
			// row.add($gw.lbl($ef.currentdata.duration+"hr parking at "+$ef.currentdata.location_to.text,{left:'20%', width:200,font:fntAdvice,color:BLACK}));
			// rows.push(row);
		
		
			var row = $gw.row({height:ROWHEIGHT,borderWidth:0});
			var img = $gw.img("/images/icon_park.png",{top:4,width:40,height:40,left:8});
			row.add(img);
			//row.add($gw.lbl("Closest Parking:\n"+"€"+formatCurrency(nearest.cost)+" ",{left:'20%', width:200,font:fntAdvice,color:BLACK}));
			row.add($gw.lbl(""+formatCurrency2(nearest.cost),{top:10,textAlign:'right',left:'20%', width:80,font:fntAdvice,color:BLACK}));
			row.add($gw.lbl(formatDistance(nearest.dist),{top:10,left:'20%', textAlign:'right', width:180,font:fntAdvice,color:BLACK}));
			row.add($gw.lbl("",{left:0, right:-20,font:fntAdvice,color:BLACK,height:1,borderColor:"#cccccc",borderWidth:1}));
			
			rows.push(row);
		
			var row = $gw.row({height:ROWHEIGHT,borderWidth:0});
			var img = $gw.img("/images/icon_park_star_empty.png",{top:4,width:40,height:40,left:8});
			row.add(img);
		
			if (nearest.id != nearest_walking.id) {
				//row.add($gw.lbl("Walking distance\n"+"€"+formatCurrency(nearest_walking.cost)+" and is "+formatDistance(nearest_walking.dist)+" away",{left:'20%',width:200,font:fntAdvice,color:BLACK}));
				//row.add($gw.lbl("Walking distance : "+"€"+formatCurrency(nearest_walking.cost),{left:'20%',width:200,font:fntAdvice,color:BLACK}));
				//row.add(""$gw.lbl(formatDistance(nearest_walking.dist)+" : "+"€"+formatCurrency(nearest_walking.cost),{left:'20%',width:200,font:fntAdvice,color:BLACK}));
				//row.add($gw.lbl("Walking distance :\n"+"€"+formatCurrency(nearest_walking.cost),{left:'20%',width:200,font:fntAdvice,color:BLACK}));
				
				//row.add($gw.lbl("€"+formatCurrency(nearest_walking.cost)+" "+formatDistance(nearest_walking.dist),{top:10,left:'20%', width:200,font:fntAdvice,color:BLACK}));
				row.add($gw.lbl(""+formatCurrency2(nearest_walking.cost),{top:10,textAlign:'right',left:'20%', width:80,font:fntAdvice,color:BLACK}));
				row.add($gw.lbl(formatDistance(nearest_walking.dist),{top:10,left:'20%', textAlign:'right', width:180,font:fntAdvice,color:BLACK}));
		
				row.add($gw.lbl("",{left:0, right:-20,font:fntAdvice,color:BLACK,height:1,borderColor:"#cccccc",borderWidth:1}));
				rows.push(row);
			} else {
		//		row.add($gw.lbl("Within Walking distance there is no cheaper place to park",{left:'20%',width:200,font:fnt19a,color:BLACK}));
				row.add($gw.lbl("Walking distance : Not Cheaper",{top:10,left:'20%',width:200,font:fntAdvice_small,color:BLACK}));
				row.add($gw.lbl("",{left:0, right:-20,font:fntAdvice,color:BLACK,height:1,borderColor:"#cccccc",borderWidth:1}));
			}
		
			var row = $gw.row({height:ROWHEIGHT,borderWidth:0});
			var img = $gw.img("/images/icon_park_star.png",{top:4,width:40,height:40,left:8});
			row.add(img);
			if (nearest.id != cheapestarray[0].id) {
				//row.add($gw.lbl("€"+formatCurrency(cheapestarray[0].cost)+" "+formatDistance(cheapestarray[0].dist)+"",{top:10,left:'20%',width:200,font:fntAdvice,color:BLACK}));
				row.add($gw.lbl(""+formatCurrency2(cheapestarray[0].cost),{top:10,textAlign:'right',left:'20%', width:80,font:fntAdvice,color:BLACK}));
				row.add($gw.lbl(formatDistance(cheapestarray[0].dist),{top:10,left:'20%', textAlign:'right', width:180,font:fntAdvice,color:BLACK}));
		
			 	row.add($gw.lbl("",{left:0, right:-20,font:fntAdvice,color:BLACK,height:1,borderColor:"#cccccc",borderWidth:1}));
				row.add($gw.lbl("",{left:0, right:-20,font:fntAdvice,color:BLACK,height:1,borderColor:"#cccccc",borderWidth:1,top:ROWHEIGHT}));
				rows.push(row);
		 	} else {
				//row.add($gw.lbl("There is no chaper place to park in the city",{left:'20%',width:200,font:fnt19a,color:BLACK}));
				row.add($gw.lbl("Cheapest \n"+"Not Cheaper",{top:10,left:'20%',width:200,font:fntAdvice,color:BLACK}));
				row.add($gw.lbl("",{left:0, right:-20,font:fntAdvice,color:BLACK,height:1,borderColor:"#cccccc",borderWidth:1}));
				row.add($gw.lbl("",{left:0, right:-20,font:fntAdvice,color:BLACK,height:1,borderColor:"#cccccc",borderWidth:1,top:ROWHEIGHT}));
			}
			
		
			lntab.view.setData(rows);
			lntab.view.addEventListener('click', function(e) {
				if (e.index == 0) {
					w.x.close(function() {
						$ef.gotometer(nearest);
					});
				} else if (e.index == 1) {
					w.x.close(function() {
						$ef.gotometer(nearest_walking);
					});
				} else if (e.index == 2) {
					w.x.close(function() {
						$ef.gotometer(cheapestarray[0]);
					});
				}
			});
			
			$ef.gotometer = function(obj) {
				var loc = {
					longitude : meters[obj.id].lon,
					latitude : meters[obj.id].lat,
					latitudeDelta : 0.005,
					longitudeDelta : 0.005
				}
				Ti.API.debug(obj);
				map.setLocation(loc);	
			};
			
			w.x.view.add(lntab.view);
		}
		
		$ef.showadvice = function() {
			if ($ef.lock.selectpoi > 0) clearTimeout($ef.lock.selectpoi_timer);
			w.x.open();
		};
		var advice_but = $gw.btn("advice",function(e) {
			$ef.showadvice();
		});
		w.win.rightNavButton = advice_but;
		
		*/
		
		return retval;
		
				
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String rv = calc(5, 8, 20 ,  10.5, 51, 5, "cash,pin");
		System.out.println(rv);

	}

}
