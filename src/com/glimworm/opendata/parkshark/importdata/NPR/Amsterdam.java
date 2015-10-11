package com.glimworm.opendata.parkshark.importdata.NPR;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import org.json.JSONObject;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.PlaceParkingGarage;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.PlaceParkingGarageAmsterdamPrVariation;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.PlaceParkingGarageOpeningTimes;
import com.glimworm.opendata.parkshark.importdata.NPR.xsd.AreaListItem;
import com.glimworm.opendata.parkshark.importdata.citySDK.utils;
import com.glimworm.opendata.parkshark.importdata.citySDK.xsd.geom;
import com.glimworm.opendata.parkshark.xsd.First;
import com.glimworm.opendata.parkshark.xsd.Meter;
import com.glimworm.opendata.parkshark.xsd.PayTime;
import com.glimworm.opendata.parkshark.xsd.PayTimes;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Amsterdam {
	
	public static int GARAGES = 1;
	public static int METERS = 2;
	
	public static PlaceParkingGarage[] getMeters() {
		return getGarages(METERS);

	}
	
	public static PlaceParkingGarage[] getGarages() {
		return getGarages(GARAGES);
	}

	public static boolean isInNorth(double lat, double lng) {
		double start_lat = 52.4208;
		double start_lng = 4.82815;	
		double lon_delta = 0.3636122177; 	// each time the lon increases by 1 the lat decreases by this value

		double difference_in_lng = (lng - start_lng);
		double new_lat = start_lat - (difference_in_lng * lon_delta);		// new Longitude
		
		if (lat > new_lat) return true;
		return false;
	};
	
	public static Polygon getPolyFromJsonArray(org.json.JSONArray coordset){

		Vector<Coordinate> vectc = new Vector<Coordinate>();
		Coordinate cor0 = null;
		
		for (int j=0; j < coordset.length(); j++) {
		
			org.json.JSONArray point = coordset.optJSONArray(j);
			
			if (point != null && point.length() > 1) {
				double lon = point.optDouble(0);
				double lat = point.optDouble(1);
				Coordinate cor1 = new Coordinate(lat,lon);
				vectc.add(cor1);
				if (cor0 == null) cor0 = cor1;
			}
		}

		if (cor0 != null) vectc.add(cor0);	// close the shape
		//
		Object resultc[] = new Coordinate[vectc.size()];
		vectc.copyInto(resultc);

		Coordinate[] cor = (Coordinate[])resultc;
		GeometryFactory fact = new GeometryFactory();
		Polygon poly1 = fact.createPolygon(cor);
		return poly1;
		
	}

	public static AreaListItem inAmsterdam = null;
	public static AreaListItem inPaidParking = null;
	public static AreaListItem inCentrum = null;
	
	public static void loadgeojson() {
		inAmsterdam = loadgeojson("gemeentegrens");
		// inPaidParking is loaded during  "downloadmeters";
		inCentrum = loadgeojson("centrumzone");
	}
	public static boolean isin(double lat, double lon, ArrayList<Polygon> polys) {

		GeometryFactory fact = new GeometryFactory();
		Coordinate cor1 = new Coordinate(lat,lon);
		final com.vividsolutions.jts.geom.Point point = fact.createPoint(cor1);

		for (int i=0; i < polys.size(); i++) {
			if (point.within(polys.get(i)) == true) return true;
		}
		return false;
	}
	
	public static AreaListItem loadgeojson(String filename) {
		return loadgeojson(filename, "n");
	}
	public static AreaListItem loadgeojson(String filename, String debug) {
		AreaListItem pl =new AreaListItem();
		
		String FN = "/opt/tmp/rdw/upload/"+filename+".geojson";
		System.out.println(FN);
		File f = new File(FN);
		if (!f.exists()) return null;

		String txt = com.glimworm.opendata.divvamsterdamapi.planning.net.FileUtils.readFile(FN);
		
		if (txt == null || txt.trim().length() == 0) {
			return null;
		}
		
		/**
		 * create a json obejct "jsob"
		 */
		
		
		org.json.JSONObject jsob = com.glimworm.common.utils.jsonUtils.string2json(txt);
		org.json.JSONObject jso_features = jsob.optJSONObject("features");
		org.json.JSONArray jso_featuresArray = jsob.optJSONArray("features");

		if (debug != null && debug.equalsIgnoreCase("y")) {
			pl.source = txt;
			pl.sourceJson = jsob;
		}

		for (int fi =0; fi < jso_featuresArray.length(); fi++) {
			jso_features = jso_featuresArray.optJSONObject(fi);
			if (jso_features != null) {
				org.json.JSONObject jso_geom = jso_features.optJSONObject("geometry");
				if (jso_geom != null) {
					String type = jso_geom.optString("type","");
					if (type.equalsIgnoreCase("Polygon")) {
						org.json.JSONArray coords = jso_geom.optJSONArray("coordinates");
						for (int i=0; i < coords.length(); i++) {
							org.json.JSONArray coordset = coords.optJSONArray(i);
							if (coordset!= null) {
								Polygon poly = getPolyFromJsonArray(coordset);
								pl.polys.add(poly);
							}
						}
					}
					if (type.equalsIgnoreCase("MultiPolygon")) {
						org.json.JSONArray coords = jso_geom.optJSONArray("coordinates");
						for (int i=0; i < coords.length(); i++) {
							System.out.println("POLY "+i);
							org.json.JSONArray multicoordset = coords.optJSONArray(i);
							System.out.println("POLY "+i+" multicoordset");
							if (multicoordset!= null) {
								for (int j=0; j < multicoordset.length(); j++) {
									org.json.JSONArray coordset = multicoordset.optJSONArray(j);
									System.out.println("POLY "+i+" coordset");
									if (coordset!= null) {
										Polygon poly = getPolyFromJsonArray(coordset);
										pl.polys.add(poly);
										System.out.println("POLY "+i+" Added");
									}
								}
							}
						}
					}
				}
			}
		}
		
		return pl;
	}
	
	
	public static Meter[] smeters = null;
	public static void downloadmeters(PlaceParkingGarage[] areas) {

		class VERKOOPPUNT {
			public String areamanagerid = "";
			public String sellingpointid = "";
			public String SellingPointDesc = "";
			public double lat = 0;
			public double lon = 0;
			public String area = "";
		}

		/*		
		mysql> select a.*,p.cash,p.creditcard,p.pin,p.chip from _site1493_dbsynch_automats a left join _site1493_dbsynch_paymethods p on (a.typeautomaat = p.type) limit 100,1\G;
		*************************** 1. row ***************************
		    entityid: 325
		   stadsdeel: Centrum
		   belnummer: 10325
		       adres: Keizersgracht 436
		    postcode: 1016 GD
		  woonplaats: Amsterdam
		typeautomaat: CWT
		 betaalwijze: 5
		      status: OP
		  tariefcode: 1
		         lat: 4.8842101
		         lon: 52.3679199
		      csdkid: 
		    csdkzone: test.divv.parking.zone.2
		  chance_day: 0:93%;1:91%;2:91%;3:91%;4:91%;5:91%;6:90%;7:89%;8:82%;9:87%;10:92%;11:95%;12:95%;13:96%;14:96%;15:94%;16:91%;17:91%;18:97%;19:98%;20:101%;21:99%;22:97%;23:96%;
		  chance_sat: 0:97%;1:95%;2:94%;3:94%;4:94%;5:94%;6:93%;7:92%;8:88%;9:84%;10:81%;11:75%;12:82%;13:85%;14:84%;15:88%;16:92%;17:97%;18:97%;19:98%;20:99%;21:100%;22:100%;23:100%;
		  chance_sun: 0:94%;1:92%;2:91%;3:91%;4:91%;5:91%;6:91%;7:90%;8:90%;9:90%;10:90%;11:88%;12:85%;13:84%;14:89%;15:89%;16:90%;17:95%;18:96%;19:97%;20:99%;21:99%;22:97%;23:95%;
		        cash: N
		  creditcard: Y
		         pin: Y
		        chip: Y
		1 row in set (0.00 sec)
		
		VERKOOPPUNT
		
		AreaManagerId	AreaId	UUID	AreaName
		344				3300	57d6f361-ffea-4186-a5a0-80a122c06fc3	
		796				20411	b6a5905f-c79f-4669-a43a-68eccef1f3c3	

		VERKOOPPUNT (METERS)
		AreaManagerId	SellingPointId	StartDateSellingPoint	EndDateSellingPoint	SellingPointDesc	Location
		473				236131			20120101														(52.378572755, 4.530698053)
		599				320				20140101									J.B. BAKEMAKADE		(51.9076118, 4.5018101)

		AreaManagerId	AreaId	StartDate	EndDate	Geodata
		363				1401	01/01/12			POLYGON((1 1,1 2))
		

		*
		*/		
		
		String parkinglocationsUrl = "https://opendata.rdw.nl/api/views/cgqw-pfbp/rows.csv?accessType=DOWNLOAD";
		com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse res = com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(parkinglocationsUrl, "", null, null, null, null,null,500);
		System.out.println(res.text);
		String[] lines = res.text.split("[\n]");

		
		GeometryFactory fact = new GeometryFactory();

		ArrayList<VERKOOPPUNT> VERKOOPPUNTs = new ArrayList<VERKOOPPUNT>();
		Vector<Meter> vect = new Vector<Meter>();

		for (int i=1; i < lines.length; i++) {	// skip first line
			String[] cols = lines[i].split("[,]");
			if (cols.length > 2) {
				VERKOOPPUNT item = new VERKOOPPUNT();
				item.areamanagerid = cols[0];
				item.sellingpointid = cols[1];
				item.SellingPointDesc = cols[4];
				try {
					/*
					 * get position frmo the string format (51,4.8)
					 */
					item.lat = Double.parseDouble(cols[5].replace('(',' ').replace('"',' ').trim());
					item.lon = Double.parseDouble(cols[6].replace(')',' ').replace('"',' ').trim());
					
					if (item.sellingpointid.equalsIgnoreCase("14086")) {
						System.err.println("14086!!");
//						return;
					}

					Coordinate cor1 = new Coordinate(item.lat,item.lon);
					final com.vividsolutions.jts.geom.Point point = fact.createPoint(cor1);
					
					
					/*
					 * match to an area
					 */
					
					boolean found1 = false;
					place:
					for (PlaceParkingGarage area : areas) {
						if (area.polys == null) continue place;
						
						boolean fnd = false;
						for (Polygon areapoly : area.polys) {
							if (point.within(areapoly) == true) fnd = true;
						}
						
						if (fnd) {
							item.area = area.name;
							System.out.println("L: "+i+" "+item.sellingpointid+" ["+item.lat+","+item.lon+" A:"+item.area+"]");				
							found1 = true;

							
							Meter _meter = new Meter();
							_meter.i = i;
//							_meter.name = item.area;
							_meter.name = (item.SellingPointDesc.trim().length() > 0) ? item.SellingPointDesc : item.area;
							_meter.entityid = item.sellingpointid;
							_meter.stadsdeel = "";								//automats.getString(i, "stadsdeel");
							_meter.belnummer = item.sellingpointid;;			//automats.getString(i, "belnummer");
							_meter.adres = "";									//automats.getString(i, "adres");
							_meter.postcode = "";								//automats.getString(i, "postcode");
							_meter.woonplaats = "";								//automats.getString(i, "woonplaats");
							_meter.typeautomaat = "CWT";						//automats.getString(i, "typeautomaat");
							_meter.betaalwijze = "OP";							//automats.getString(i, "betaalwijze");
							_meter.tariefcode = "";								//automats.getString(i, "tariefcode");
							_meter.status = "";									//automats.getString(i, "status");
							_meter.lat = item.lat;								//automats.getDouble(i, "lon",0);
							_meter.lon = item.lon;								//automats.getDouble(i, "lat",0);
							_meter.csdkzone = "";								//automats.getString(i, "csdkzone");
							_meter.chance_weekday = "";							//automats.getString(i, "chance_day");
							_meter.chance_sat = "";								//automats.getString(i, "chance_sat");
							_meter.chance_sun = "";								//automats.getString(i, "chance_sun");
							
							_meter.csdkurl = "https://www.google.nl/maps/place/"+_meter.lat+","+_meter.lon;

							_meter.bw.cash = false;						//automats.getString(i, "cash").equalsIgnoreCase("Y");
							_meter.bw.creditcard = true;				//automats.getString(i, "creditcard").equalsIgnoreCase("Y");
							_meter.bw.pin = true;						//automats.getString(i, "pin").equalsIgnoreCase("Y");
							_meter.bw.chip = false;						//automats.getString(i, "chip").equalsIgnoreCase("Y");
						
							_meter.nprid = area.nprid;
							_meter.nprurl = "https://npropendata.rdw.nl/parkingdata/v2/static/"+area.nprid;
							
							PayTimes retval = new PayTimes();
							
							retval.cost = (60/area.time_unit_minutes) * area.price_per_time_unit;

							
							/*
							 * text of Verkooppunt name is like "R116E ma-za 09-24"
							 */
							String[] parts = item.area.split("[ ]");
							boolean found = false;
							partloop:
							for (int d=0; d < (parts.length -1); d++) {
								String part = parts[d];		//	[ma-za] 09-24 zo 12-24
								String part2 = parts[d+1];	//	ma-za [09-24] zo 12-24
								
								PayTime pt = null;
								if (part2.length() == 5 && part2.indexOf("-") > -1) {
									if (part2.startsWith("0") || part2.startsWith("1") || part2.startsWith("2")) {
										pt = new PayTime();
										pt.start = Integer.parseInt(part2.split("[-]")[0]);
										pt.end = Integer.parseInt(part2.split("[-]")[1]);
									}
								}
								if (pt == null) continue partloop;
								
								int dc = 0;
								for (String day : "zo,ma,di,wo,di,vr,za".split("[,]")) {
									if (part.startsWith(day)) {
										
										if (part.length() == 2) {
											retval.days[dc] = new PayTime();
											retval.days[dc].start = pt.start;
											retval.days[dc].end = pt.end;
											retval.t_code = parts[0];
											found = true;
											
										} else if (part.length() == 5) {
											int dc1 = 0;
											for (String day1 : "zo,ma,di,wo,di,vr,za".split("[,]")) {
												if (part.endsWith(day1)) {
													for (int ddc = 0; ddc < dc1; ddc++) {
														retval.days[ddc] = new PayTime();
														retval.days[ddc].start = pt.start;
														retval.days[ddc].end = pt.end;
														retval.t_code = parts[0];
														found = true;
													}
												}
												dc1++;
											}
											
										}
										
									}
									dc++;
								}
							}
							
							if (found == false) {
								retval = (PayTimes)area.pt.clone();
							}
							if (parts.length > 1) {
								retval.t_code = parts[0];
								_meter.tariefcode = parts[0];
							}
							
							/**
							 * fill empty ones with 00
							 */
							for (int ddc = 0; ddc < retval.days.length ; ddc++) {
								if (retval.days[ddc] == null) {
									retval.days[ddc] = new PayTime();
									retval.days[ddc].start = 0;
									retval.days[ddc].end = 0;
								}
							}
							//retval.first = area.free_minutes;
							if (area.pt.first.combination.equalsIgnoreCase("y")) {
								retval.first = (First)area.pt.first.clone();
							} else {
								retval.first = new First();
								retval.first.combination = "n";		//jc todo
								retval.first.hrs = 0;				//jc todo
								retval.first.hrs2 = 0;				//jc todo
								retval.first.price = 0;				//jc todo
								retval.first.price2 = 0;			//jc todo
							}
							
							retval.geb_code = "";				//jc todo
							retval.max = 0;						//jc todo
							retval.maxdaycost = area.price_day;
							_meter.costs = retval;

							_meter.type = "on-street-meter";
							try {
								_meter.isInNorth = isInNorth(_meter.lat,_meter.lon);
							} catch (Exception E) {
								_meter.isInNorth = false;
							}			
							
							vect.add(_meter);							
							_importlog.add("ADD::(METER):ADDED:"+_meter.nprid+":"+_meter.name);
							
							
							break place;
							
						}
						
					}
					
					if (!found1) {
						System.out.println("X: "+i+" "+item.sellingpointid+" ["+item.lat+","+item.lon+" A:"+item.area+"]");				
					}

				
				} catch (Exception E) {
					System.out.println(cols[5]+","+cols[6]+"/"+cols[5].replace('(',' ').replace('"',' ').trim()+"/"+cols[6].replace(')',' ').replace('"',' ').trim());
					E.printStackTrace(System.out);
					item.lat = 0;
					item.lon = 0;
				}

				
				VERKOOPPUNTs.add(item);
			}
		}

		AreaListItem ali = new AreaListItem();
		ali.name = "AllPaidParkingAreas";
		
		System.out.println("START OUTPUT MAP INDEX for ["+areas.length+"] areas");
		StringBuffer coords = new StringBuffer("");
		for (PlaceParkingGarage a : areas) {
			System.out.println("START OUTPUT MAP INDEX area ["+a.name+"]");
			if (a.polys != null) {
				for (Polygon poly : a.polys) {
					ali.polys.add(poly);
					
					coords.append("/* "+a.name+"*/\n");
					coords.append("coords.push([");
					System.out.println("START OUTPUT MAP INDEX for ["+poly.getNumPoints()+"] points");
					for (int i=0; i < poly.getNumPoints(); i++) {
						coords.append("new google.maps.LatLng( "+poly.getCoordinates()[i].x+","+poly.getCoordinates()[i].y+"),");
					}
					coords.append("]);\n");
				}
			} else {
				if (a.name.length() > 20) {
					System.out.println(a.name.substring(0,20));
				} else {
					System.out.println(a.name);
				}
				System.out.println(a.nprurl);
			}
		}
		System.out.println("END OUTPUT MAP INDEX for ["+areas.length+"] areas");
		com.glimworm.opendata.divvamsterdamapi.planning.net.FileUtils.writeFile("/opt/tmp/rdw/map/index_coords.js", coords.toString());
		inPaidParking = ali;
		
		
		Object result[] = new Meter[vect.size()];
		vect.copyInto(result);
		smeters = (Meter[])result;
		
		System.out.println("END ROUTINE smeter count is  ["+smeters.length+"]");
		
		return;
				
	}

	public static org.w3c.dom.Node getnode(org.w3c.dom.Node node, String NAME) {
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			if (node.getChildNodes().item(i).getNodeName().equalsIgnoreCase(NAME)) {
				return node.getChildNodes().item(i);
			}
		}
		return null;
	}
	public static List<org.w3c.dom.Node> getnodes(org.w3c.dom.Node node, String NAME) {
		List<org.w3c.dom.Node> retval = new ArrayList<org.w3c.dom.Node>();
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			if (node.getChildNodes().item(i).getNodeName().equalsIgnoreCase(NAME)) {
				retval.add(node.getChildNodes().item(i));
			}
		}
		return retval;
	}	
	public static ArrayList<Exception>_importerrors = new ArrayList<Exception>();
	public static ArrayList<String>_importlog = new ArrayList<String>();
	
	public static PlaceParkingGarage[] getGarages(int GARAGESORMETERS) {
		// TODO Auto-generated method stub
		
		_importerrors.clear();
		
		String polygoneUrl = "https://opendata.rdw.nl/api/views/nsk3-v9n7/rows.csv?accessType=DOWNLOAD";
		com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse res0 = com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(polygoneUrl, "", null, null, null, null,null,500);
		System.out.println(res0.text);
		String[] polygon_lines = res0.text.split("[\n]");

		ArrayList<AreaListItem> alis = new ArrayList<AreaListItem>();
		
		
		nextline:
		for (int i=1; i < polygon_lines.length; i++) {	// skip first line
			String[] cols = polygon_lines[i].split("[,]",5);
			
			AreaListItem ali = new AreaListItem();
			ali.areamanagerid = cols[0];
			ali.areaId = cols[1];

			if (ali.areamanagerid.equalsIgnoreCase("363") == false && ali.areaId.startsWith("363_") == false) continue nextline;

			
			
			String geo = cols[4];

//			System.out.print(ali.areaId);
//			System.out.print(ali.areamanagerid);
//			System.out.print(geo);
//			System.out.println();
			
			if (geo.startsWith("\"POLYGON ((")) {
				geo = geo.substring(11).replace(')',' ').replace('"',' ').trim();
				

//				System.out.print(geo);
//				System.out.println();
				
				String[] geos = geo.split("[(]");
				for (String _geo : geos) {

					String[] points = _geo.split("[,]");
	
					Vector<Coordinate> vectc = new Vector<Coordinate>();
					Coordinate cor0 = null;
					
					nextpoint:
					for (String point : points) {
						
						System.out.println("pnt " + point);
						if (point == null || point.trim().length() == 0) continue nextpoint;
						
						String[] pnt = point.trim().split("[ ]");

						if (pnt == null || pnt.length != 2) continue nextpoint;

						System.out.println("pnt0 " + pnt[0]);
						System.out.println("pnt1 " + pnt[1]);
						
						try {
							double lon = Double.parseDouble(pnt[0]);
							double lat = Double.parseDouble(pnt[1]);
							Coordinate cor1 = new Coordinate(lat,lon);
							vectc.add(cor1);
							if (cor0 == null) cor0 = cor1;
						} catch (Exception E) {
							
						}
					}
					if (cor0 != null) vectc.add(cor0);	// close the shape
					//
					Object resultc[] = new Coordinate[vectc.size()];
					vectc.copyInto(resultc);
					Coordinate[] cor = (Coordinate[])resultc;
					GeometryFactory fact = new GeometryFactory();
					ali.polys.add(fact.createPolygon(cor));
				}
				alis.add(ali);
			}
		}

		if (alis.size() > 0) {
			for (int i=0; i < alis.size(); i++) {
				
				System.out.print("AI " + alis.get(i).areaId);
				System.out.print("\t" + alis.get(i).areamanagerid);
				System.out.print("\t" + alis.get(i).polys.get(0));
				System.out.println();
			}
			
		}
		/*
		 * Download the KML file
		 */
		
		String KMLurl = "https://opendata.rdw.nl/api/geospatial/7sqe-3mca?method=export&format=KML";
		com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse reskml = com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(KMLurl, "", null, null, null, null,null,500);
		System.out.println(reskml.text);

		String FNKML = "/opt/tmp/rdw/download.kml";
		com.glimworm.opendata.divvamsterdamapi.planning.net.FileUtils.writeFile(FNKML, reskml.text);

		try {
		
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			org.w3c.dom.Document document = builder.parse(new File(FNKML));		
			
			org.w3c.dom.NodeList nodeList = document.getDocumentElement().getChildNodes().item(1).getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				org.w3c.dom.Node node = nodeList.item(i);
				if (node.getNodeName().equalsIgnoreCase("Placemark")) {

					org.w3c.dom.NodeList placelist = node.getChildNodes();
					String AreaId = "";
					String AreaManId = "";
					List<String> poly = new ArrayList<String>();
					AreaListItem ali = new AreaListItem();

					nextline:
					for (int j = 0; j < placelist.getLength(); j++) {
						org.w3c.dom.Node nodep = placelist.item(j);

						if (nodep.getNodeName().equalsIgnoreCase("Description")) {
//							System.out.println(j+")"+nodep.getChildNodes().item(0).getNodeValue());
							String S1 = nodep.getChildNodes().item(0).getNodeValue();
							String[] SS = S1.split("[\n]");
							for (String SSS : SS) {
								if (SSS.indexOf(">AreaId<") > -1) {
									String[] SSSS = SSS.split("[<>]");
									if (SSSS.length > 12) {
										AreaId = SSSS[12];
									}
//									for (String SSSSS : SSSS) {
//										System.out.println("*"+SSSSS);
//									}
								}
								if (SSS.indexOf(">AreaManId<") > -1) {
									String[] SSSS = SSS.split("[<>]");
									if (SSSS.length > 12) {
										AreaManId = SSSS[12];
									}
//									for (String SSSSS : SSSS) {
//										System.out.println("*"+SSSSS);
//									}
								}
							}
							
						}

						ali.areamanagerid = AreaManId;
						ali.areaId = AreaId;

						if (ali.areamanagerid.equalsIgnoreCase("363") == false && ali.areaId.startsWith("363_") == false) continue nextline;
						
						
						if (nodep.getNodeName().equalsIgnoreCase("MultiGeometry")) {
						
							org.w3c.dom.Node multigeometry = nodep;	//getnode(nodep,"MultiGeometry");
							if (multigeometry != null) {
								
								List<org.w3c.dom.Node> polygons = getnodes(multigeometry,"Polygon");
								for (org.w3c.dom.Node polygon : polygons) {
									List<org.w3c.dom.Node> outerBoundaryIss = getnodes(polygon,"outerBoundaryIs");
									for (org.w3c.dom.Node outerBoundaryIs : outerBoundaryIss) {
										List<org.w3c.dom.Node> LinearRings = getnodes(outerBoundaryIs,"LinearRing");
										for (org.w3c.dom.Node LinearRing : LinearRings) {
											List<org.w3c.dom.Node> coordinatess = getnodes(LinearRing,"coordinates");
											for (org.w3c.dom.Node coordinates : coordinatess) {
												poly.add(coordinates.getChildNodes().item(0).getNodeValue());
											}
										}
									}
								}
							}
						}
						
						
					}
					System.out.print("["+AreaId+"]");
					System.out.print("\t");
					System.out.print("["+AreaManId+"]");
					System.out.print("\t");
					System.out.print("["+poly+"]");
					System.out.println("");
					// <coordinates>4.616,52.4593 4.6164,52.4593</coordinates>
					
					for (String geo : poly){
						String[] points = geo.split("[ ]");
		
						Vector<Coordinate> vectc = new Vector<Coordinate>();
						Coordinate cor0 = null;
						
						nextpoint:
						for (String point : points) {
							
							System.out.println("pnt " + point);
							if (point == null || point.trim().length() == 0) continue nextpoint;
							
							String[] pnt = point.trim().split("[,]");

							if (pnt == null || pnt.length != 2) continue nextpoint;

							System.out.println("pnt0 " + pnt[0]);
							System.out.println("pnt1 " + pnt[1]);
							
							try {
								double lon = Double.parseDouble(pnt[0]);
								double lat = Double.parseDouble(pnt[1]);
								Coordinate cor1 = new Coordinate(lat,lon);
								vectc.add(cor1);
								if (cor0 == null) cor0 = cor1;
							} catch (Exception E) {
								
							}
						}
						if (cor0 != null) vectc.add(cor0);	// close the shape
						//
						Object resultc[] = new Coordinate[vectc.size()];
						vectc.copyInto(resultc);
						Coordinate[] cor = (Coordinate[])resultc;
						GeometryFactory fact = new GeometryFactory();
						ali.polys.add(fact.createPolygon(cor));
					}
					alis.add(ali);
//					_importlog.add("AREAS:"+GARAGESORMETERS+":(alis loop):ADDED:"+ali.areaId+":"+ali.name);
				} else {
//					_importlog.add("AREAS:"+GARAGESORMETERS+":(alis loop):SKIPPED-NOT-PLACEMARK:"+node.getNodeName()+":"+node.toString());
				}
			}
			
			
		} catch (Exception E) {
			E.printStackTrace(System.out);
		}
		

		
		/* make a list of area items
		 * 
		 */

		String parkinglocationsUrl = "https://opendata.rdw.nl/api/views/mz4f-59fw/rows.csv?accessType=DOWNLOAD";
		com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse res = com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(parkinglocationsUrl, "", null, null, null, null,null,500);
		System.out.println(res.text);
		
		/*
		 * GET AREAS (areamanagerid , areaID, uuid)
		 * filters on 363 and 363_
		 * 
		 */
		
		
		
		String[] lines = res.text.split("[\n]");
		ArrayList<AreaListItem> areas = new ArrayList<AreaListItem>();
		for (int i=1; i < lines.length; i++) {	// skip first line
			String[] cols = lines[i].split("[,]");
			if (cols.length > 2) {
				AreaListItem item = new AreaListItem();
				item.areamanagerid = cols[0];
				item.areaId = cols[1];
				item.uuid = cols[2];
				if (item.areamanagerid.equalsIgnoreCase("363") || item.areaId.startsWith("363_")) {
					areas.add(item);
				} else {
//					_importlog.add("AREAS:"+GARAGESORMETERS+":(areas loop):SKIPPED-NOT-363:"+item.areamanagerid+":"+item.areaId);
				}
			} else {
				_importlog.add("AREAS:"+GARAGESORMETERS+":(areas loop):SKIPPED-NOT-2-COLUMNS:"+lines[i]);
			}
		}

		
		/*
		 * walks through all the AREAS
		 * gets  original data from NPR and populates "parkingPlaceGarage" array
		 * 
		 * ALSO
		 * 
		 * adds more rich data to the existing area array
		 * 
		 * 
		 * 
		 */
		
		
		Vector<PlaceParkingGarage> vect = new Vector<PlaceParkingGarage>();
		
		int readfrominternet = 0;
		nextarea:
		for (int i=0; i < areas.size(); i++) {
			try {
				String FN = "/opt/tmp/rdw/"+areas.get(i).uuid+".json";
				File f = new File(FN);
				String txt = "";
				String U = "https://npropendata.rdw.nl/parkingdata/v2/static/" + areas.get(i).uuid;
				
				/** if file exists do not download again - sort of cache **/
				
				if (f.exists()) {
//					System.out.println("READING FILE ["+areas.get(i).uuid+"]");
					txt = com.glimworm.opendata.divvamsterdamapi.planning.net.FileUtils.readFile(FN);
				}
				if (txt == null || txt.trim().length() == 0) {
//					System.out.println("SEARCHING FOR ["+areas.get(i).uuid+"]"+U);
					res = com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(U, "", null, null, null, null,null,500);
					com.glimworm.opendata.divvamsterdamapi.planning.net.FileUtils.writeFile(FN, res.text);
					txt = res.text;
					readfrominternet++;
				}
				
				/**
				 * create a json obejct "jsob"
				 */
				
				org.json.JSONObject jsob = com.glimworm.common.utils.jsonUtils.string2json(txt);
				
				org.json.JSONObject jsob1 = jsob.optJSONObject("parkingFacilityInformation");
				
				/**
				 * add detail to the area array item
				 */
				
				areas.get(i).detail_url = U;
				areas.get(i).description =  jsob1.optString("description");
				areas.get(i).name =  jsob1.optString("name");
				areas.get(i).operator =  jsob1.optJSONObject("operator");
				areas.get(i).validityStartOfPeriod =  jsob1.optLong("validityStartOfPeriod");
				areas.get(i).validityEndOfPeriod =  jsob1.optLong("validityEndOfPeriod");
				
				long now = (long) (System.currentTimeMillis() / 1000L);
//				long now = 1337126400+1000;
//				new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse("01/01/2013 01:00:00").getTime() / 1000;
				
				/**
				 * skip if not currently active
				 * 
				 * 
				 */
				
				if (now < areas.get(i).validityStartOfPeriod || (areas.get(i).validityEndOfPeriod > 0 && now > areas.get(i).validityEndOfPeriod)) {
					areas.get(i).valid =  false;
					System.out.println("DBG1 : NOW, START , END");
					System.out.println(now);
					System.out.println(areas.get(i).validityStartOfPeriod);
					System.out.println(areas.get(i).validityEndOfPeriod);
					System.out.println("INVALID");
					_importlog.add("ADD:"+GARAGESORMETERS+":(AREAS LOOP 1):SKIP-INVALID-DATE:"+i+":"+areas.get(i).areaId+":"+areas.get(i).validityStartOfPeriod+":"+areas.get(i).validityEndOfPeriod);
					continue nextarea;
				}

				org.json.JSONArray specifications = jsob1.optJSONArray("specifications");
				if (specifications != null) {
					for (int k = 0; k < specifications.length(); k++) {
						String usage = specifications.getJSONObject(k).getString("usage");
						areas.get(i).usage += usage+",";
						System.out.println(usage);
						/*
						 * skip if the wrong type
						 * 
						 * note :: there could be other text descriptions ??
						 */
						
						if (usage.startsWith("VergunningParkeren") == true) {
							continue nextarea;
						}
						if (GARAGESORMETERS == GARAGES && (usage.startsWith("Garage") == false && usage.startsWith("Park & Ride") == false)) {
							if (usage.startsWith("BetaaldParkeren") == false) {
								_importlog.add("ADD:"+GARAGESORMETERS+":(AREAS LOOP 1):SKIP-INVALID-USAGE:"+i+":"+areas.get(i).areaId+":"+areas.get(i).usage);
							}
							
							continue nextarea;
						}
						if (GARAGESORMETERS == METERS && usage.startsWith("BetaaldParkeren") == false) continue nextarea;
						
						/*
						 * create polygon
						 */
						
						org.json.JSONObject areaGeometry = specifications.getJSONObject(k).optJSONObject("areaGeometry");
						if (areaGeometry != null) {
							System.out.println("areaGeometry");
							String type = areaGeometry.optString("type");
							org.json.JSONArray coordinates = areaGeometry.optJSONArray("coordinates");
							if (coordinates != null) {
								System.out.println("coordinates");
								for (int k1=0; k1 < coordinates.length(); k1++) {
									System.out.println("coordinates "+k1);
									org.json.JSONArray coo = coordinates.optJSONArray(k1);
									if (coo != null) {

										Vector<Coordinate> vectc = new Vector<Coordinate>();
										Coordinate cor0 = null;
										for (int k2=0; k2 < coo.length(); k2++) {
											org.json.JSONArray co = coo.optJSONArray(k2);
											if (co != null) {
												System.out.println("co "+co.toString());
												double lon = co.optDouble(0);
												double lat = co.optDouble(1);
												System.out.println("["+lat+","+lon+"]");
												Coordinate cor1 = new Coordinate(lat,lon);
												vectc.add(cor1);
												if (cor0 == null) cor0 = cor1;
											}
										}
										if (cor0 != null) vectc.add(cor0);	// close the shape
										//
										Object resultc[] = new Coordinate[vectc.size()];
										vectc.copyInto(resultc);

										Coordinate[] cor = (Coordinate[])resultc;
										GeometryFactory fact = new GeometryFactory();
										Polygon poly1 = fact.createPolygon(cor);
										areas.get(i).polys.add(poly1);
//										if (areas.get(i).poly == null) {
//											areas.get(i).poly = poly1;
//											System.err.println("xxp1"+poly1);
//											System.err.println("xxp1.area="+poly1.getArea());
//										} else {
//											System.err.println(poly1);
//											System.err.println("xxxp1.area="+poly1.getArea());
//											System.err.println("xxxa1.area="+areas.get(i).poly.getArea());
//											if (poly1.getArea() > areas.get(i).poly.getArea()) {
//												System.err.println("xxxp1 replaced");
//												areas.get(i).poly = poly1;
//											}
//											System.err.println("xxxp1"+areas.get(i).poly);
//										}
										
										
										 
//										com.vividsolutions.jts.geom.Polygon poly1 = new com.vividsolutions.jts.geom.Polygon(pol);
									}
								}
							}
						}
					}
				}
				
				/* 
				 * if the area did not get a polygon at all for any reason then revert to the imported list above
				 */
//				areas.get(i).polys.clear();
				
//				if (areas.get(i).polys.size() == 0) {
					// we use the other array
					for (int ai=0; ai < alis.size(); ai++) {
						System.out.println(""+alis.get(ai).areaId+" =?= " + areas.get(i).areaId+"");
						if (alis.get(ai).areaId.equalsIgnoreCase(areas.get(i).areaId)) {
							System.out.println(""+alis.get(ai).areamanagerid+" =?= " + areas.get(i).areamanagerid+"");
							if (alis.get(ai).areamanagerid.equalsIgnoreCase(areas.get(i).areamanagerid)) {
								if (alis.get(ai).polys.size() > 0) {
									System.out.println(alis.get(ai).polys);
									for (int aii = 0; aii < alis.get(ai).polys.size(); aii++) {
										areas.get(i).polys.add(alis.get(ai).polys.get(aii));
									}
								}
							}
						}
					}
//				}

				
				if (GARAGESORMETERS == METERS) {
					// hmmm  did I want to do something here?
				}
				
				/*
				 * garages have access points which we use for the GEO
				 * 
				 * note : will take the last one
				 * 
				 */
				
				org.json.JSONArray accessPoints = jsob1.optJSONArray("accessPoints");
				if (accessPoints != null) {
					for (int k = 0; k < accessPoints.length(); k++) {
						org.json.JSONArray accessPointLocation = accessPoints.getJSONObject(k).optJSONArray("accessPointLocation");
						if (accessPointLocation != null && accessPointLocation.length() > 0) {
							double lat = accessPointLocation.optJSONObject(0).optDouble("latitude");
							double lon = accessPointLocation.optJSONObject(0).optDouble("longitude");
							areas.get(i).lat = lat;
							areas.get(i).lon = lon;

							areas.get(i).usage += "("+lat+","+lon+"),";
							
						}
						org.json.JSONObject accessPointAddress = accessPoints.getJSONObject(k).optJSONObject("accessPointAddress");
						if (accessPointAddress != null) {

							String streetName = accessPointAddress.optString("streetName");
							String houseNumber = accessPointAddress.optString("houseNumber");
							String zipcode = accessPointAddress.optString("zipcode");
							String city = accessPointAddress.optString("city");
							String province = accessPointAddress.optString("province");
							String country = accessPointAddress.optString("country");

							areas.get(i).streetName = streetName;
							areas.get(i).houseNumber = houseNumber;
							areas.get(i).zipcode = zipcode;
							areas.get(i).city = city;
							areas.get(i).province = province;
							areas.get(i).country = country;
							
						}
					}
				}


				String FN1 = "/opt/tmp/rdw/upload/additionaldata/"+areas.get(i).uuid+".json";
				File f1 = new File(FN1);
				if (f1.exists()) {
//					System.out.println("READING FILE ["+areas.get(i).uuid+"]");
					String txt1 = com.glimworm.opendata.divvamsterdamapi.planning.net.FileUtils.readFile(FN1);
					org.json.JSONObject jsob_additional = com.glimworm.common.utils.jsonUtils.string2json(txt1);
					if (jsob_additional.has("location")) {
						org.json.JSONArray coords = jsob_additional.optJSONArray("location");
						areas.get(i).lon = coords.getDouble(0);
						areas.get(i).lat = coords.getDouble(1);
					}
					areas.get(i).streetName = jsob_additional.optString("streetname",areas.get(i).streetName);
					areas.get(i).houseNumber = jsob_additional.optString("houseNumber",areas.get(i).streetName);
					areas.get(i).zipcode = jsob_additional.optString("zipcode",areas.get(i).streetName);
					areas.get(i).city = jsob_additional.optString("city",areas.get(i).streetName);
					areas.get(i).province = jsob_additional.optString("province",areas.get(i).streetName);
					areas.get(i).country = jsob_additional.optString("country",areas.get(i).streetName);
					
				}
				
				/*
				 * move the data into a "parkingplacegarage"
				 */

				PlaceParkingGarage pl = new PlaceParkingGarage();
				pl.name = areas.get(i).name;
				pl.url = areas.get(i).detail_url;
				pl.lat = areas.get(i).lat;
				pl.lon = areas.get(i).lon;
				
				if (areas.get(i).polys != null) {
					pl.polys = new Polygon[areas.get(i).polys.size()];
					areas.get(i).polys.toArray(pl.polys);
				} else {
					pl.polys = new Polygon[0];
				}
				pl.postcode = areas.get(i).zipcode;
				pl.street = areas.get(i).streetName + " " + areas.get(i).houseNumber;
				pl.nprurl = U;
				pl.nprid = areas.get(i).uuid;
				pl.cdk_id = "";
				pl.csdkurl = "";
				pl.places = 0;
				pl.type = "parking-garage"; // "park-and-ride"
				pl.opening_times_raw = "n/a";

				pl.capacity = 0;
				pl.free_minutes = 0;
				pl.time_unit_minutes = 0;

				pl.price_day = 0.0d;
				pl.price_per_time_unit = 0.0d;
				
				/*
				 * add opening times
				 * 
				 */
				
				for (int d=0; d < 7; d++) {
					pl.opening_times[d] = new PlaceParkingGarageOpeningTimes();
					pl.opening_times[d].dayOfWeek= d;
					pl.opening_times[d].open_in = "0000";
					pl.opening_times[d].open_out = "0000";
					pl.opening_times[d].close_in = "0000";
					pl.opening_times[d].close_out = "0000";
				}
				
				
				
				PayTimes pt = new PayTimes();
				pt.cost = 0;
				pt.first = new First();
				pt.geb_code = "";
				pt.max = 0;
				pt.maxdaycost = 0;
				pt.t_code = "";
//				pt.days[0] = new Py

				org.json.JSONArray tarrifs = jsob1.optJSONArray("tariffs");
				for (int j = 0; j < tarrifs.length(); j++) {
					org.json.JSONObject jsobjt = tarrifs.getJSONObject(j);

					String tariffDescription = jsobjt.optString("tariffDescription");
					long startOfPeriod = jsobjt.optLong("startOfPeriod");
					long endOfPeriod = jsobjt.optLong("endOfPeriod");

//					System.out.println(now);
//					System.out.println(startOfPeriod);
//					System.out.println(endOfPeriod);
					
					if (now > startOfPeriod && (endOfPeriod == 0 || now < endOfPeriod)) {
						
						org.json.JSONArray validitydays = jsobjt.optJSONArray("validityDays");
						if (validitydays != null) {
							for (int k1 = 0; k1 < validitydays.length(); k1++) {
								String day = validitydays.getString(k1);
								areas.get(i).usage += "\n"+tariffDescription+" / "+day+":";
								
								int daynum = 0;
								if (day.equalsIgnoreCase("mon")) daynum = 1;
								if (day.equalsIgnoreCase("tue")) daynum = 2;
								if (day.equalsIgnoreCase("wed")) daynum = 3;
								if (day.equalsIgnoreCase("thu")) daynum = 4;
								if (day.equalsIgnoreCase("fri")) daynum = 5;
								if (day.equalsIgnoreCase("sat")) daynum = 6;
								if (day.equalsIgnoreCase("sun")) daynum = 0;

								// this is a valid tarrif
								int validityFromTime_h = jsobjt.getJSONObject("validityFromTime").getInt("h");
								int validityFromTime_m = jsobjt.getJSONObject("validityFromTime").getInt("m");
								int validityFromTime_s = jsobjt.getJSONObject("validityFromTime").getInt("s");
		
								areas.get(i).usage += " "+validityFromTime_h+validityFromTime_m+" ";

								// this is a valid tarrif
								int validityUntilTime_h = jsobjt.getJSONObject("validityUntilTime").getInt("h");
								int validityUntilTime_m = jsobjt.getJSONObject("validityUntilTime").getInt("m");
								int validityUntilTime_s = jsobjt.getJSONObject("validityUntilTime").getInt("s");
								
								if (validityUntilTime_m > 45 && validityUntilTime_h < 24) validityUntilTime_h++;

								areas.get(i).usage += " "+validityUntilTime_h+validityUntilTime_m+" ";

								org.json.JSONArray intervalRates = jsobjt.optJSONArray("intervalRates");
								if (intervalRates != null) {
		
									for (int k = 0; k < intervalRates.length(); k++) {
									
										double charge = intervalRates.getJSONObject(k).optDouble("charge");
										// 0.1
										int chargePeriod = intervalRates.getJSONObject(k).optInt("chargePeriod");
										// 60
										int durationFrom = intervalRates.getJSONObject(k).optInt("durationFrom");
										// 0
										int durationUntil = intervalRates.getJSONObject(k).optInt("durationUntil");
										// -1
										String durationType = intervalRates.getJSONObject(k).optString("durationType");
										// "Minutes"
										
										

										if (charge > 0) {
											/*
											 * exclude DAY and EVENING CARDS
											 */
											if (chargePeriod < 61) {
												
												areas.get(i).usage += " "+charge+" ";
												areas.get(i).usage += " "+chargePeriod+" ";
												areas.get(i).usage += " "+durationType+" ";
												areas.get(i).usage += " "+durationFrom+" ";
												areas.get(i).usage += " "+durationUntil+" ";
												
												if (charge> 0) {
													if (pt.days[daynum] == null) {
														/*
														 * if this is the first encountered data for this day
														 */
														pt.days[daynum] = new PayTime();
														pt.days[daynum].start = validityFromTime_h;
														pt.days[daynum].end = validityUntilTime_h;
													} else {
														/*
														 * extend the time window to the max
														 * 
														 * can go wrong here if different rates for different times of the day
														 * 
														 */
														if (validityFromTime_h < pt.days[daynum].start) pt.days[daynum].start = validityFromTime_h;
														if (validityUntilTime_h > pt.days[daynum].end) pt.days[daynum].end = validityUntilTime_h;
													}
												}
												
												/*
												 * get the smallest time period
												 */
												
												if (pl.time_unit_minutes == 0 || chargePeriod < pl.time_unit_minutes) {
													pl.time_unit_minutes = chargePeriod;
													pl.price_per_time_unit = charge;
													/* 
													 * I want it in euro / hour for meters
													 */
													if (tariffDescription.indexOf("combi") > -1){
														if (intervalRates.length() > 1 && k == 0) {
															// this is the first of a combi zone
															pt.first.combination = "y";
															pt.first.hrs = (durationUntil/60);
															pt.first.price = (60/chargePeriod) * charge;
															pt.first.price2 = (60/chargePeriod) * charge;
														}
														if (intervalRates.length() > 1 && k == 1) {
															// this is the first of a combi zone
															pt.first.hrs2 = (durationUntil/60);
															pt.first.price2 = (60/chargePeriod) * charge;
															pt.cost = (60/chargePeriod) * charge;
														}
														
													} else {
														if (((60/chargePeriod) * charge) > pt.cost) {
															pt.cost = (60/chargePeriod) * charge;
														}
													}
													
												}
											
											}
											/*
											 * more than 1400 mins are  "day cards"
											 */
											if (chargePeriod > (1400)) {
												if (pl.price_day == 0 || charge > pl.price_day) {
													pl.price_day = charge;
													pt.maxdaycost = charge;
												}
											}
										}
									
									}
								}
							}
						}

					}
					
				}
				/*
				 * fill up remaing days with zero
				 */
				for (int pi=0; pi < pt.days.length; pi++) {
					if (pt.days[pi] == null) {
						pt.days[pi] = new PayTime();
						pt.days[pi].start = 0;
						pt.days[pi].end = 0;
					}
				}
				pl.pt = pt;
//				pl.name += "|"+areas.get(i).usage;
				
				org.json.JSONArray openingTimes = jsob1.optJSONArray("openingTimes");
				if (openingTimes != null) {
					for (int j1 = 0; j1 < openingTimes.length(); j1++) {
						/*
						 * nothing done with opening times for garages!
						 * 
						 */
						System.out.println(openingTimes.getJSONObject(j1).toString());
					}
				}
				vect.add(pl);
				_importlog.add("ADD:"+GARAGESORMETERS+":(AREAS LOOP 1):ADDED:"+pl.nprid+":"+pl.name);
				
//				// https://npropendata.rdw.nl/parkingdata/v2/static/a2ad1b55-29f8-4f7f-934b-50851bac6384
//				if (areas.get(i).uuid.equalsIgnoreCase("a2ad1b55-29f8-4f7f-934b-50851bac6384")) {
//					System.out.println(areas.get(i).areamanagerid);
//					System.out.println(areas.get(i).areaId);
//					System.out.println(areas.get(i).poly);
//					return null;
//				}
				
				
//				if (readfrominternet > 50) break;

			} catch (Exception E) {
//				E.printStackTrace(System.out);
				System.out.println(E.getMessage());
				_importlog.add("ADD:"+GARAGESORMETERS+":(AREAS LOOP 1):ERROR:"+i+":"+areas.get(i).name+":"+areas.get(i).uuid+":"+E.getMessage());
				
			}
			
		}
		/*

		   {
		        "cdk_id": "",
		        "name": "Transferium ArenA",
		        "lat": 52.3136379,
		        "lon": 4.940279,
		        "owner": "www.amsterdam.nl/parkeergebouwen",
		        "street": "",
		        "postcode": "1101EP",
		        "url": "http://www.amsterdam.nl/parkeergebouwen/onze-garages/amsterdam_arenapoort/p1-arena/",
		        "type": "parking-garage",
		        "calc_type": "garage",
		        "capacity": 0,
		        "places": 0,
		        "price_day": 24.5,
		        "remarks": "",
		        "opening_times_raw": "0-6 0000 0000 0000 0000",
		        "tariff_raw": "0000-9999  ",
		        "includes_public_transport": "n",
		        "ams_pr_fare": ""
		    },
		*/
		
		String FN1 = "/opt/tmp/rdw/upload/additionaldata/newgarages/garages.json";
		File f1 = new File(FN1);
		if (f1.exists()) {
			String txt1 = com.glimworm.opendata.divvamsterdamapi.planning.net.FileUtils.readFile(FN1);
			org.json.JSONObject jsob_additional = com.glimworm.common.utils.jsonUtils.string2json("{\"arr\" : " + txt1+ "}");
			org.json.JSONArray garages = jsob_additional.optJSONArray("arr");
			for (int i=0; i < garages.length(); i++) {
				org.json.JSONObject garage = garages.optJSONObject(i);
				if (GARAGESORMETERS == GARAGES && garage != null) {
					String nprid = garage.optString("nprid");
					if (nprid != null && nprid.length() > 0) {		// this is not an empty cdk
						
						for (int gi=0; gi < vect.size(); gi++) {
							if (vect.get(gi) != null && vect.get(gi).nprid != null &&  vect.get(gi).nprid.equalsIgnoreCase(nprid)){
								/* this garage matches */
								
								/* replace theopening tims */
								if (garage.optString("opening_times_raw",null) != null) {
									vect.get(gi).opening_times_raw = garage.optString("opening_times_raw","n/a");
									
									PlaceParkingGarage pl = vect.get(gi);
									
									try {
										// opening_times: "0 0930 0930 1930 1930|1-6 0730 0730 2130 2130",
										//				  "0 0930 0930 1930 1930|1-6 0730 0730 2130 2130"	

										
										if (pl.opening_times_raw != null && pl.opening_times_raw.trim().length() > 0) {
											System.out.println("openingtimes::"+pl.opening_times_raw);
											String[] days = pl.opening_times_raw.split("[|]");
											nextday:
											for (String day : days) {	// e.g. 0 0930 0930 1930 1930
												System.out.println("openingtimes::day::"+day);
												if (day == null || day.trim().length() == 0) continue nextday;
												String[] dayParts = day.split("[ ]");	// e.g. [0, 0930, 0930, 1930, 1930]
												System.out.println("openingtimes::day::len="+dayParts.length);
												if (dayParts.length != 5) continue nextday;

												System.out.println("openingtimes::day::[0]="+dayParts[0]);
												if (dayParts[0].indexOf("-") > -1) {	// e.g. 1-6
													System.out.println("openingtimes::day::[0]::option1");
													String[] start_and_end = dayParts[0].split("[-]");
													int start = Integer.parseInt(start_and_end[0]);
													int end = Integer.parseInt(start_and_end[1]);
													for (int tempday=start; tempday <= end; tempday++) {
														System.out.println("openingtimes::day::[0]::option1::tempday="+tempday);
														pl.opening_times[tempday].open_in = dayParts[1];
														pl.opening_times[tempday].open_out = dayParts[2];
														pl.opening_times[tempday].close_in = dayParts[3];
														pl.opening_times[tempday].close_out = dayParts[4];
													}
													
												} else if (dayParts[0].indexOf(",") > -1) {	// 2,3
													System.out.println("openingtimes::day::[0]::option2");
													String[] listOfDays = dayParts[0].split("[,]");
													for (String tempday_str : listOfDays) {
														int tempday = Integer.parseInt(tempday_str);
														System.out.println("openingtimes::day::[0]::option2::tempday="+tempday);
														pl.opening_times[tempday].open_in = dayParts[1];
														pl.opening_times[tempday].open_out = dayParts[2];
														pl.opening_times[tempday].close_in = dayParts[3];
														pl.opening_times[tempday].close_out = dayParts[4];
														
													}

												} else if (dayParts[0] != null && dayParts[0].trim().length() > 0) {	// 3
													System.out.println("openingtimes::day::[0]::option3");
													int tempday = Integer.parseInt(dayParts[0]);
													System.out.println("openingtimes::day::[0]::option3::tempday="+tempday);
													pl.opening_times[tempday].open_in = dayParts[1];
													pl.opening_times[tempday].open_out = dayParts[2];
													pl.opening_times[tempday].close_in = dayParts[3];
													pl.opening_times[tempday].close_out = dayParts[4];
												}
											}
										}
									} catch (Exception E) {
										E.printStackTrace(System.out);
									}
									
									
								}
								/* replace the remarks */
								if (garage.optString("remarks",null) != null) {
									vect.get(gi).remarks  = garage.optString("remarks",vect.get(gi).remarks);
								}
								/* replace the lat */
								if (garage.optString("lat",null) != null) {
									vect.get(gi).lat = garage.optDouble("lat", vect.get(gi).lat);
								}
								/* replace the lon */
								if (garage.optString("lon",null) != null) {
									vect.get(gi).lon = garage.optDouble("lon", vect.get(gi).lon);
								}
								/* replace the owner */
								if (garage.optString("owner",null) != null) {
									vect.get(gi).owner  = garage.optString("owner",vect.get(gi).owner);
								}
								/* replace the street */
								if (garage.optString("street",null) != null) {
									vect.get(gi).street  = garage.optString("street",vect.get(gi).street);
								}
								/* replace the postcode */
								if (garage.optString("postcode",null) != null) {
									vect.get(gi).postcode  = garage.optString("postcode",vect.get(gi).postcode);
								}
								/* replace the url */
								if (garage.optString("url",null) != null) {
									vect.get(gi).url  = garage.optString("url",vect.get(gi).url);
								}
								/* replace the type */
								if (garage.optString("type",null) != null) {
									vect.get(gi).type  = garage.optString("type",vect.get(gi).type);
									if (vect.get(gi).type.equalsIgnoreCase("p-and-r")) {
										vect.get(gi).type = "garage";
										vect.get(gi).displaytype = "park-and-ride";
									}
								}
								/* replace the calc_type */
								if (garage.optString("calc_type",null) != null) {
									vect.get(gi).calc_type  = garage.optString("calc_type",vect.get(gi).calc_type);
								}
								/* replace the capacity */
								if (garage.optString("capacity",null) != null) {
									vect.get(gi).capacity = garage.optInt("capacity", vect.get(gi).capacity);
								}
								/* replace the places */
								if (garage.optString("places",null) != null) {
									vect.get(gi).places = garage.optInt("places", vect.get(gi).places);
								}

								/* replace the price_day */
								if (garage.optString("price_day",null) != null) {
									vect.get(gi).price_day = garage.optDouble("price_day", vect.get(gi).price_day);
								}

								/* replace the includes_public_transport */
								if (garage.optString("includes_public_transport",null) != null) {
									vect.get(gi).includes_public_transport = garage.optString("includes_public_transport", vect.get(gi).includes_public_transport);
								}

								/* replace the includes_public_transport */
								if (garage.optString("tariff_raw",null) != null) {
									String tariff_raw = garage.optString("tariff_raw");
									if (tariff_raw != null) {
										// 0000-9999 2 21
										String[] tariff_raws = tariff_raw.split("[|]");
										if (tariff_raws.length == 1) {
											String[] tariff_rawsp = tariff_raws[0].split("[ ]");
											if (tariff_rawsp.length == 3) {
												try {
													vect.get(gi).price_per_time_unit = Double.parseDouble(tariff_rawsp[1]);
													vect.get(gi).time_unit_minutes = Integer.parseInt(tariff_rawsp[2]);
												} catch (Exception E) { E.printStackTrace(System.out);}
											}
										} else if (tariff_raws.length == 2) {
											vect.get(gi).pt = new PayTimes();
											vect.get(gi).pt.first.combination = "y";
			
											String[] tariff_rawsp = tariff_raws[1].split("[ ]");
											if (tariff_rawsp.length == 3) {
												try {
													vect.get(gi).pt.first.hrs = Integer.parseInt(tariff_rawsp[0].split("[-]")[1])/60;
													vect.get(gi).pt.first.price = Double.parseDouble(tariff_rawsp[1]);
												} catch (Exception E) { E.printStackTrace(System.out);}
											}
			
											tariff_rawsp = tariff_raws[1].split("[ ]");
											if (tariff_rawsp.length == 3) {
												try {
													vect.get(gi).price_per_time_unit = Double.parseDouble(tariff_rawsp[1]);
													vect.get(gi).pt.first.price2 = Double.parseDouble(tariff_rawsp[1]);
													vect.get(gi).time_unit_minutes = Integer.parseInt(tariff_rawsp[2]);
													vect.get(gi).pt.first.hrs = Integer.parseInt(tariff_rawsp[0].split("[-]")[1])/60;
												} catch (Exception E) { E.printStackTrace(System.out);}
											}
										}
									}
								}
								
								
								
								
								if (garage.optString("ams_pr_fare",null) != null) {
									vect.get(gi).ams_pr_fare = garage.optString("ams_pr_fare", vect.get(gi).ams_pr_fare);
								try {
								//	fare: "1-5 04:00 10:00 8 24 | 1-5 10:00 04:00 1 24 | 6-0 0:00 0:00 1 24",
									
								//	fare: "1-5 0400 1000 8 24 | 1-5 1000 0400 1 24 | 6-0 0000 0000 1 24",
								if (vect.get(gi).ams_pr_fare != null && vect.get(gi).ams_pr_fare.trim().length() > 0) {
									Vector<PlaceParkingGarageAmsterdamPrVariation> vect1 = new Vector<PlaceParkingGarageAmsterdamPrVariation>();
									String[] vects = vect.get(gi).ams_pr_fare.trim().split("[|]");
									for (int j = 0; j < vects.length; j++) {
										String[] parts = vects[j].trim().split("[ ]");
										if (parts.length == 5) {
											String[] dayparts = parts[0].trim().split("[-]");
											
											int day_low = Integer.parseInt(dayparts[0]);
											int day_high = (dayparts.length> 1) ? Integer.parseInt(dayparts[1]) : Integer.parseInt(dayparts[0]);
											int hour_start = Integer.parseInt(parts[1].substring(0,2));
											int hour_end = Integer.parseInt(parts[2].substring(0,2));
											double dayrate = Double.parseDouble(parts[3]);
											int maxstay = Integer.parseInt(parts[4]);
											if (day_low < day_high) {
												if (hour_start == 0 && hour_end == 0) {
													PlaceParkingGarageAmsterdamPrVariation pv = new PlaceParkingGarageAmsterdamPrVariation();
													pv.dayOfWeek_start = day_low;
													pv.dayOfWeek_end = day_high;
													pv.entry_start = 0;
													pv.entry_end = 24;
													pv.price_day = dayrate;
													vect1.add(pv);
												} else if (hour_start < hour_end) {
													PlaceParkingGarageAmsterdamPrVariation pv = new PlaceParkingGarageAmsterdamPrVariation();
													pv.dayOfWeek_start = day_low;
													pv.dayOfWeek_end = day_high;
													pv.entry_start = hour_start;
													pv.entry_end = hour_end;
													pv.price_day = dayrate;
													vect1.add(pv);
												} else {
													PlaceParkingGarageAmsterdamPrVariation pv = new PlaceParkingGarageAmsterdamPrVariation();
													pv.dayOfWeek_start = day_low;
													pv.dayOfWeek_end = day_high;
													pv.entry_start = 0;
													pv.entry_end = hour_start;
													pv.price_day = dayrate;
													vect1.add(pv);
		
													PlaceParkingGarageAmsterdamPrVariation pv1 = new PlaceParkingGarageAmsterdamPrVariation();
													pv1.dayOfWeek_start = day_low;
													pv1.dayOfWeek_end = day_high;
													pv1.entry_start = hour_end;
													pv1.entry_end = 24;
													pv1.price_day = dayrate;
													vect1.add(pv1);
													
												}
												
												
											} else {
												if (hour_start == 0 && hour_end == 0) {
													PlaceParkingGarageAmsterdamPrVariation pv = new PlaceParkingGarageAmsterdamPrVariation();
													pv.dayOfWeek_start = day_low;
													pv.dayOfWeek_end = day_low;
													pv.entry_start = 0;
													pv.entry_end = 24;
													pv.price_day = dayrate;
													vect1.add(pv);
													
													PlaceParkingGarageAmsterdamPrVariation pv1 = new PlaceParkingGarageAmsterdamPrVariation();
													pv1.dayOfWeek_start = day_high;
													pv1.dayOfWeek_end = day_high;
													pv1.entry_start = 0;
													pv1.entry_end = 24;
													pv1.price_day = dayrate;
													vect1.add(pv1);
												} else if (hour_start < hour_end) {
													PlaceParkingGarageAmsterdamPrVariation pv = new PlaceParkingGarageAmsterdamPrVariation();
													pv.dayOfWeek_start = day_low;
													pv.dayOfWeek_end = day_low;
													pv.entry_start = hour_start;
													pv.entry_end = hour_end;
													pv.price_day = dayrate;
													vect1.add(pv);
													
													PlaceParkingGarageAmsterdamPrVariation pv1 = new PlaceParkingGarageAmsterdamPrVariation();
													pv1.dayOfWeek_start = day_high;
													pv1.dayOfWeek_end = day_high;
													pv.entry_start = hour_start;
													pv.entry_end = hour_end;
													pv1.price_day = dayrate;
													vect1.add(pv1);
												} else {
													PlaceParkingGarageAmsterdamPrVariation pv = new PlaceParkingGarageAmsterdamPrVariation();
													pv.dayOfWeek_start = day_low;
													pv.dayOfWeek_end = day_low;
													pv.entry_start = 0;
													pv.entry_end = hour_start;
													pv.price_day = dayrate;
													vect1.add(pv);
													
													PlaceParkingGarageAmsterdamPrVariation pv1 = new PlaceParkingGarageAmsterdamPrVariation();
													pv1.dayOfWeek_start = day_high;
													pv1.dayOfWeek_end = day_high;
													pv1.entry_start = 0;
													pv1.entry_end = hour_start;
													pv1.price_day = dayrate;
													vect1.add(pv1);
		
													PlaceParkingGarageAmsterdamPrVariation pv2 = new PlaceParkingGarageAmsterdamPrVariation();
													pv2.dayOfWeek_start = day_low;
													pv2.dayOfWeek_end = day_low;
													pv2.entry_start = hour_end;
													pv2.entry_end = 24;
													pv2.price_day = dayrate;
													vect1.add(pv2);
													
													PlaceParkingGarageAmsterdamPrVariation pv3 = new PlaceParkingGarageAmsterdamPrVariation();
													pv3.dayOfWeek_start = day_high;
													pv3.dayOfWeek_end = day_high;
													pv3.entry_start = hour_end;
													pv3.entry_end = 24;
													pv3.price_day = dayrate;
													vect1.add(pv3);
												}
											}
											
											
											
										}
									}
		
									
									Object result[] = new PlaceParkingGarageAmsterdamPrVariation[vect1.size()];
									vect1.copyInto(result);
									vect.get(gi).ams_pr_fares = (PlaceParkingGarageAmsterdamPrVariation[])result;
								}
								} catch (Exception E) {
									_importerrors.add(E);
									_importlog.add("ERROR:(ADDITIONAL GARAGES):ERROR"+E.getMessage());
									System.out.println("EEEEEEEEEEEEEEE");
									E.printStackTrace(System.out);
								}
								}
								
								// , tariff_raw,  
							}
						}
					}
					if (nprid != null && nprid.length() == 0) {		// this is an empty cdk
						/*
						 * move the data into a "parkingplacegarage"
						 */
						try {
	
							PlaceParkingGarage pl = new PlaceParkingGarage();
							pl.name = garage.optString("name");
							pl.url = garage.optString("url");
							pl.lat = garage.optDouble("lat");
							pl.lon = garage.optDouble("lon");
							pl.postcode = garage.optString("postcode");
							pl.street = garage.optString("street");
							pl.nprurl = "";
							pl.nprid = "";
							pl.cdk_id = "";
							pl.csdkurl = "";
							pl.places = garage.optInt("places");
							pl.type = garage.optString("type","parking-garage"); // "park-and-ride"
							if (pl.type.equalsIgnoreCase("p-and-r")) pl.type = "park-and-ride";
							pl.opening_times_raw = garage.optString("opening_times_raw","n/a");
	
							pl.capacity = garage.optInt("capacity");
							pl.free_minutes = 0;
							pl.time_unit_minutes = 0;
							pl.remarks = garage.optString("remarks");
	
							pl.price_day = garage.optDouble("price_day");
							pl.price_per_time_unit = garage.optDouble("price_per_time_unit",-1);
							pl.calc_type = garage.optString("calc_type");
							pl.ams_pr_fare = garage.optString("ams_pr_fare");
							pl.includes_public_transport = garage.optString("includes_public_transport");
							
							String tariff_raw = garage.optString("tariff_raw");
							if (tariff_raw != null) {
								// 0000-9999 2 21
								String[] tariff_raws = tariff_raw.split("[|]");
								if (tariff_raws.length == 1) {
									String[] tariff_rawsp = tariff_raws[0].split("[ ]");
									if (tariff_rawsp.length == 3) {
										try {
											pl.price_per_time_unit = Double.parseDouble(tariff_rawsp[1]);
											pl.time_unit_minutes = Integer.parseInt(tariff_rawsp[2]);
										} catch (Exception E) { E.printStackTrace(System.out);}
									}
								} else if (tariff_raws.length == 2) {
									pl.pt = new PayTimes();
									pl.pt.first.combination = "y";
	
									String[] tariff_rawsp = tariff_raws[1].split("[ ]");
									if (tariff_rawsp.length == 3) {
										try {
											pl.pt.first.hrs = Integer.parseInt(tariff_rawsp[0].split("[-]")[1])/60;
											pl.pt.first.price = Double.parseDouble(tariff_rawsp[1]);
										} catch (Exception E) { E.printStackTrace(System.out);}
									}
	
									tariff_rawsp = tariff_raws[1].split("[ ]");
									if (tariff_rawsp.length == 3) {
										try {
											pl.price_per_time_unit = Double.parseDouble(tariff_rawsp[1]);
											pl.pt.first.price2 = Double.parseDouble(tariff_rawsp[1]);
											pl.time_unit_minutes = Integer.parseInt(tariff_rawsp[2]);
											pl.pt.first.hrs = Integer.parseInt(tariff_rawsp[0].split("[-]")[1])/60;
										} catch (Exception E) { E.printStackTrace(System.out);}
									}
									
								}
							}
							
							
							for (int d=0; d < 7; d++) {
								pl.opening_times[d] = new PlaceParkingGarageOpeningTimes();
								pl.opening_times[d].dayOfWeek= d;
							}
							try {
								// opening_times: "0 0930 0930 1930 1930|1-6 0730 0730 2130 2130",
								//				  "0 0930 0930 1930 1930|1-6 0730 0730 2130 2130"	
	
								
								if (pl.opening_times_raw != null && pl.opening_times_raw.trim().length() > 0) {
									System.out.println("openingtimes::"+pl.opening_times_raw);
									String[] days = pl.opening_times_raw.split("[|]");
									nextday:
									for (String day : days) {	// e.g. 0 0930 0930 1930 1930
										System.out.println("openingtimes::day::"+day);
										if (day == null || day.trim().length() == 0) continue nextday;
										String[] dayParts = day.split("[ ]");	// e.g. [0, 0930, 0930, 1930, 1930]
										System.out.println("openingtimes::day::len="+dayParts.length);
										if (dayParts.length != 5) continue nextday;
	
										System.out.println("openingtimes::day::[0]="+dayParts[0]);
										if (dayParts[0].indexOf("-") > -1) {	// e.g. 1-6
											System.out.println("openingtimes::day::[0]::option1");
											String[] start_and_end = dayParts[0].split("[-]");
											int start = Integer.parseInt(start_and_end[0]);
											int end = Integer.parseInt(start_and_end[1]);
											for (int tempday=start; tempday <= end; tempday++) {
												System.out.println("openingtimes::day::[0]::option1::tempday="+tempday);
												pl.opening_times[tempday].open_in = dayParts[1];
												pl.opening_times[tempday].open_out = dayParts[2];
												pl.opening_times[tempday].close_in = dayParts[3];
												pl.opening_times[tempday].close_out = dayParts[4];
											}
											
										} else if (dayParts[0].indexOf(",") > -1) {	// 2,3
											System.out.println("openingtimes::day::[0]::option2");
											String[] listOfDays = dayParts[0].split("[,]");
											for (String tempday_str : listOfDays) {
												int tempday = Integer.parseInt(tempday_str);
												System.out.println("openingtimes::day::[0]::option2::tempday="+tempday);
												pl.opening_times[tempday].open_in = dayParts[1];
												pl.opening_times[tempday].open_out = dayParts[2];
												pl.opening_times[tempday].close_in = dayParts[3];
												pl.opening_times[tempday].close_out = dayParts[4];
												
											}
	
										} else if (dayParts[0] != null && dayParts[0].trim().length() > 0) {	// 3
											System.out.println("openingtimes::day::[0]::option3");
											int tempday = Integer.parseInt(dayParts[0]);
											System.out.println("openingtimes::day::[0]::option3::tempday="+tempday);
											pl.opening_times[tempday].open_in = dayParts[1];
											pl.opening_times[tempday].open_out = dayParts[2];
											pl.opening_times[tempday].close_in = dayParts[3];
											pl.opening_times[tempday].close_out = dayParts[4];
										}
									}
								}
							} catch (Exception E) {
								E.printStackTrace(System.out);
							}
	
							//	fare: "1-5 04:00 10:00 8 24 | 1-5 10:00 04:00 1 24 | 6-0 0:00 0:00 1 24",
							if (pl.ams_pr_fare != null && pl.ams_pr_fare.trim().length() > 0) {
								Vector<PlaceParkingGarageAmsterdamPrVariation> vect1 = new Vector<PlaceParkingGarageAmsterdamPrVariation>();
								String[] vects = pl.ams_pr_fare.trim().split("[|]");
								for (int j = 0; j < vects.length; j++) {
									String[] parts = vects[j].trim().split("[ ]");
									if (parts.length == 5) {
										String[] dayparts = parts[0].trim().split("[-]");
										int day_low = Integer.parseInt(dayparts[0]);
										int day_high = (dayparts.length> 1) ? Integer.parseInt(dayparts[1]) : Integer.parseInt(dayparts[0]);
										int hour_start = Integer.parseInt(parts[1].substring(0,2));
										int hour_end = Integer.parseInt(parts[2].substring(0,2));
										double dayrate = Double.parseDouble(parts[3]);
										int maxstay = Integer.parseInt(parts[4]);
										if (day_low < day_high) {
											if (hour_start == 0 && hour_end == 0) {
												PlaceParkingGarageAmsterdamPrVariation pv = new PlaceParkingGarageAmsterdamPrVariation();
												pv.dayOfWeek_start = day_low;
												pv.dayOfWeek_end = day_high;
												pv.entry_start = 0;
												pv.entry_end = 24;
												pv.price_day = dayrate;
												vect1.add(pv);
											} else if (hour_start < hour_end) {
												PlaceParkingGarageAmsterdamPrVariation pv = new PlaceParkingGarageAmsterdamPrVariation();
												pv.dayOfWeek_start = day_low;
												pv.dayOfWeek_end = day_high;
												pv.entry_start = hour_start;
												pv.entry_end = hour_end;
												pv.price_day = dayrate;
												vect1.add(pv);
											} else {
												PlaceParkingGarageAmsterdamPrVariation pv = new PlaceParkingGarageAmsterdamPrVariation();
												pv.dayOfWeek_start = day_low;
												pv.dayOfWeek_end = day_high;
												pv.entry_start = 0;
												pv.entry_end = hour_start;
												pv.price_day = dayrate;
												vect1.add(pv);
	
												PlaceParkingGarageAmsterdamPrVariation pv1 = new PlaceParkingGarageAmsterdamPrVariation();
												pv1.dayOfWeek_start = day_low;
												pv1.dayOfWeek_end = day_high;
												pv1.entry_start = hour_end;
												pv1.entry_end = 24;
												pv1.price_day = dayrate;
												vect1.add(pv1);
												
											}
											
											
										} else {
											if (hour_start == 0 && hour_end == 0) {
												PlaceParkingGarageAmsterdamPrVariation pv = new PlaceParkingGarageAmsterdamPrVariation();
												pv.dayOfWeek_start = day_low;
												pv.dayOfWeek_end = day_low;
												pv.entry_start = 0;
												pv.entry_end = 24;
												pv.price_day = dayrate;
												vect1.add(pv);
												
												PlaceParkingGarageAmsterdamPrVariation pv1 = new PlaceParkingGarageAmsterdamPrVariation();
												pv1.dayOfWeek_start = day_high;
												pv1.dayOfWeek_end = day_high;
												pv1.entry_start = 0;
												pv1.entry_end = 24;
												pv1.price_day = dayrate;
												vect1.add(pv1);
											} else if (hour_start < hour_end) {
												PlaceParkingGarageAmsterdamPrVariation pv = new PlaceParkingGarageAmsterdamPrVariation();
												pv.dayOfWeek_start = day_low;
												pv.dayOfWeek_end = day_low;
												pv.entry_start = hour_start;
												pv.entry_end = hour_end;
												pv.price_day = dayrate;
												vect1.add(pv);
												
												PlaceParkingGarageAmsterdamPrVariation pv1 = new PlaceParkingGarageAmsterdamPrVariation();
												pv1.dayOfWeek_start = day_high;
												pv1.dayOfWeek_end = day_high;
												pv.entry_start = hour_start;
												pv.entry_end = hour_end;
												pv1.price_day = dayrate;
												vect1.add(pv1);
											} else {
												PlaceParkingGarageAmsterdamPrVariation pv = new PlaceParkingGarageAmsterdamPrVariation();
												pv.dayOfWeek_start = day_low;
												pv.dayOfWeek_end = day_low;
												pv.entry_start = 0;
												pv.entry_end = hour_start;
												pv.price_day = dayrate;
												vect1.add(pv);
												
												PlaceParkingGarageAmsterdamPrVariation pv1 = new PlaceParkingGarageAmsterdamPrVariation();
												pv1.dayOfWeek_start = day_high;
												pv1.dayOfWeek_end = day_high;
												pv1.entry_start = 0;
												pv1.entry_end = hour_start;
												pv1.price_day = dayrate;
												vect1.add(pv1);
	
												PlaceParkingGarageAmsterdamPrVariation pv2 = new PlaceParkingGarageAmsterdamPrVariation();
												pv2.dayOfWeek_start = day_low;
												pv2.dayOfWeek_end = day_low;
												pv2.entry_start = hour_end;
												pv2.entry_end = 24;
												pv2.price_day = dayrate;
												vect1.add(pv2);
												
												PlaceParkingGarageAmsterdamPrVariation pv3 = new PlaceParkingGarageAmsterdamPrVariation();
												pv3.dayOfWeek_start = day_high;
												pv3.dayOfWeek_end = day_high;
												pv3.entry_start = hour_end;
												pv3.entry_end = 24;
												pv3.price_day = dayrate;
												vect1.add(pv3);
											}
										}
										
										
										
									}
								}
	
								
								Object result[] = new PlaceParkingGarageAmsterdamPrVariation[vect1.size()];
								vect1.copyInto(result);
								pl.ams_pr_fares = (PlaceParkingGarageAmsterdamPrVariation[])result;
							}
							if (pl.type.equalsIgnoreCase("park-and-ride")) {
								pl.displaytype = "park-and-ride";
							}
							// vect.add(pl);
							if (pl.type.equalsIgnoreCase("park-and-ride")) {
								PlaceParkingGarage plcopy = (PlaceParkingGarage)pl.clone();
								plcopy.type = "garage";
								plcopy.displaytype = "park-and-ride";
								vect.add(plcopy);
								_importlog.add("ADD:"+GARAGESORMETERS+":(ADDITIONAL GARAGES):ADDED:"+pl.nprid+":"+pl.name);
							} else {
								_importlog.add("ADD:"+GARAGESORMETERS+":(ADDITIONAL GARAGES):ADDED-NOT-PR:"+pl.nprid+":"+pl.name);
								vect.add(pl);
							}
							
						} catch (Exception E) {
							_importerrors.add(E);
							_importlog.add("ADD:"+GARAGESORMETERS+":(ADDITIONAL GARAGES):ERROR"+E.getLocalizedMessage());
						}
					}
				}
			}
		}
		
		
		Object result[] = new PlaceParkingGarage[vect.size()];
		vect.copyInto(result);
		PlaceParkingGarage[] pg = (PlaceParkingGarage[])result;

		/*
		 * sort on area of polygon
		 */
		class AreaSort implements Comparator<PlaceParkingGarage> {
		    // Comparator interface requires defining compare method.
		    public int compare(PlaceParkingGarage a, PlaceParkingGarage b) {
		    	double apolyarea = 0;
		    	double bpolyarea = 0;
		    	if (a.polys != null) {
			    	for (Polygon apoly : a.polys) {
			    		if (apoly.getArea() > apolyarea) apolyarea = apoly.getArea();
			    	}
		    	}
		    	if (b.polys != null) {
			    	for (Polygon bpoly : b.polys) {
			    		if (bpoly.getArea() > bpolyarea) bpolyarea = bpoly.getArea();
			    	}
		    	}
		    	
		        if (apolyarea > 0 && apolyarea < bpolyarea) {return -1;}
		        if (bpolyarea > 0 && bpolyarea < apolyarea) {return 1;}
		        return 0;
		    }
		}
		
		System.out.println("BEGIN SORT - array length : "+ pg.length);
		Comparator<PlaceParkingGarage> MeterByArea = new AreaSort();
		Arrays.sort(pg, MeterByArea);
		System.out.println("END BEGIN SORT");
		
		
//		for (PlaceParkingGarage p : pg) {
//			System.out.print(p.name);
//			System.out.print(" ("+p.lat);
//			System.out.println(","+p.lon+")");
//		}
		
		/*
		 * return list of garages
		 * list of areas is actually in the class file and not retuened
		 */
		return pg;

	}

	public static PlaceParkingGarage[] getGarages_old_deprectiated() {
		// TODO Auto-generated method stub
//		String S = "http://api.citysdk.waag.org/admr.nl.amsterdam/ptstops?name=Leidseplein";
		String S = "http://api.citysdk.waag.org/nodes?layer=divv.parking.buildings&geom";
		
		/*
		cdk_id: "divv.parking.buidings.1124",
		name: "Willemspoort Garage",
		node_type: "node",
		geom: {
			type: "Point",
			coordinates: [
				4.8849266,
				52.3848016
			]
		},
		layers: {
			divv.parking.buildings: {
				data: {
					gid: "1124",
					url: "http://www.amsterdam.nl/parkeergebouwen/onze-garages/amsterdam_centrum/willemspoort/",
					adres: "Haarlemmer Houttuinen 549",
					title: "Willemspoort Garage",
					aantal: null,
					postcode: "1013GM",
					huisnummer: "549",
					woonplaats: "Amsterdam",
					opmerkingen: null
				}
			}
		},
		layer: "divv.parking.buildings"
		},
		*/
		
		Vector<PlaceParkingGarage> vect = new Vector<PlaceParkingGarage>();
		
		com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse res = com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(S, "", null, null, null, null);
		System.out.println(res.text);
		org.json.JSONObject jsob = com.glimworm.common.utils.jsonUtils.string2json(res.text);
		org.json.JSONArray ar = jsob.optJSONArray("results");
		for (int i=0; i < ar.length(); i++) {
			System.out.println(ar.optJSONObject(i).optString("name"));
			System.out.println(ar.optJSONObject(i).optString("cdk_id"));
			System.out.println(ar.optJSONObject(i).optString("node_type"));

			
			geom coords = com.glimworm.opendata.parkshark.importdata.citySDK.utils.geomFromJson(ar.optJSONObject(i).optJSONObject("geom"));
			String cdk_id = ar.optJSONObject(i).optString("cdk_id");
			
			System.out.println(coords);
			
			JSONObject data = ar.optJSONObject(i).optJSONObject("layers").optJSONObject("divv.parking.buildings").optJSONObject("data");

			PlaceParkingGarage pl = com.glimworm.opendata.parkshark.importdata.citySDK.utils.garageFromJson(ar.optJSONObject(i),data, cdk_id, coords);
			pl.places = data.optInt("aantal");
			
			vect.add(pl);
			
			System.out.println(pl);
			
			System.out.println(data.optString("gid"));
			System.out.println(data.optString("url"));
			System.out.println(data.optString("adres"));
			System.out.println(data.optString("title"));
			System.out.println(data.optString("aantal"));
			System.out.println(data.optString("postcode"));
			System.out.println(data.optString("huisnummer"));
			System.out.println(data.optString("woonplaats"));
			System.out.println(data.optString("opmerkingen"));
			System.out.println(ar.optJSONObject(i).toString());
						
		}

		Object result[] = new PlaceParkingGarage[vect.size()];
		vect.copyInto(result);
		return (PlaceParkingGarage[])result;
		
	}	
	
	public static ArrayList<AreaListItem> areas;
	
	public static void populate() {
		
		String parkinglocationsUrl = "https://opendata.rdw.nl/api/views/mz4f-59fw/rows.csv?accessType=DOWNLOAD";
		com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse res = com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(parkinglocationsUrl, "", null, null, null, null,null,180);
		System.out.println(res.text);
		
		String[] lines = res.text.split("[\n]");
		areas = new ArrayList<AreaListItem>();
		for (int i=1; i < lines.length; i++) {	// skip first line
			String[] cols = lines[i].split("[,]");
			if (cols.length > 2) {
				AreaListItem item = new AreaListItem();
				item.areamanagerid = cols[0];
				item.areaId = cols[1];
				item.uuid = cols[2];
				if (item.areamanagerid.equalsIgnoreCase("363") || item.areaId.startsWith("363_")) {
					areas.add(item);
				}
			}
		}

		for (int i=0; i < areas.size(); i++) {
			try {
				String U = "https://npropendata.rdw.nl/parkingdata/v2/static/" + areas.get(i).uuid;
				System.out.println("SEARCHING FOR ["+areas.get(i).uuid+"]"+U);
				res = com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(U, "", null, null, null, null,null,180);
				org.json.JSONObject jsob1 = com.glimworm.common.utils.jsonUtils.string2json(res.text);

				areas.get(i).description =  jsob1.optString("description");
				areas.get(i).name =  jsob1.optString("name");
				areas.get(i).operator =  jsob1.optJSONObject("operator");
				areas.get(i).validityStartOfPeriod =  jsob1.optLong("validityStartOfPeriod");
				areas.get(i).validityEndOfPeriod =  jsob1.optLong("validityEndOfPeriod");
				
				

				if (i > 20) break;

			} catch (Exception E) {
				E.printStackTrace(System.out);
			}
		}

		
		
	}
	
}
