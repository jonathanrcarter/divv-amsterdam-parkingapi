package com.glimworm.opendata.parkshark.importdata.NPR;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.json.*;

import com.glimworm.common.utils.gwUtils;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Place;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.PlaceParkingGarage;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.PlaceParkingGarageOpeningTimes;
import com.glimworm.opendata.parkshark.importdata.NPR.xsd.AreaListItem;
import com.glimworm.opendata.parkshark.importdata.citySDK.xsd.geom;
import com.vividsolutions.jts.awt.PointShapeFactory.Point;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import citysdk.tourism.client.poi.lists.ListEvent;
import citysdk.tourism.client.poi.single.Event;
import citysdk.tourism.client.requests.Parameter;
import citysdk.tourism.client.requests.ParameterList;
import citysdk.tourism.client.requests.TourismClient;
import citysdk.tourism.client.requests.TourismClientFactory;
import citysdk.tourism.client.terms.ParameterTerms;


public class testimport {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String S = "http://api.citysdk.waag.org/admr.nl.amsterdam/ptstops?name=Leidseplein";
//		String S = "http://api.citysdk.waag.org/nodes?layer=divv.parking.buildings&geom";
		String S = "https://opendata.rdw.nl/resource/mz4f-59fw.json?$limit=10000";
		
		/*
		 * 
		 * 
		 * 

		
{
  "areaid" : "510",
  "areamanagerid" : "599",
  "uuid" : "42589417-307e-4f96-9a09-325eb9718d14"
}		


{"parkingFacilityInformation":{"contactPersons":[],"tariffs":[{"intervalRates":[{"durationFrom":0,"charge":0.0425,"chargePeriod":1,"validityEndOfPeriod":1325376000,"validityStartOfPeriod":1321623199,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":0.04333333,"chargePeriod":1,"validityEndOfPeriod":1356998400,"validityStartOfPeriod":1325376000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":0.044,"chargePeriod":1,"validityEndOfPeriod":1364774400,"validityStartOfPeriod":1356998400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Tue"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1364774400,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1321623199,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.0425,"chargePeriod":1,"validityEndOfPeriod":1325376000,"validityStartOfPeriod":1321623199,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":0.04333333,"chargePeriod":1,"validityEndOfPeriod":1356998400,"validityStartOfPeriod":1325376000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":0.044,"chargePeriod":1,"validityEndOfPeriod":1364774400,"validityStartOfPeriod":1356998400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Thu"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1364774400,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1321623199,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.0425,"chargePeriod":1,"validityEndOfPeriod":1325376000,"validityStartOfPeriod":1321623199,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":0.04333333,"chargePeriod":1,"validityEndOfPeriod":1356998400,"validityStartOfPeriod":1325376000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":0.044,"chargePeriod":1,"validityEndOfPeriod":1364774400,"validityStartOfPeriod":1356998400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Mon"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1364774400,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1321623199,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.0425,"chargePeriod":1,"validityEndOfPeriod":1325376000,"validityStartOfPeriod":1321623199,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":0.04333333,"chargePeriod":1,"validityEndOfPeriod":1356998400,"validityStartOfPeriod":1325376000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":0.044,"chargePeriod":1,"validityEndOfPeriod":1364774400,"validityStartOfPeriod":1356998400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Fri"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1364774400,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1321623199,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.0425,"chargePeriod":1,"validityEndOfPeriod":1325376000,"validityStartOfPeriod":1321623199,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":0.04333333,"chargePeriod":1,"validityEndOfPeriod":1356998400,"validityStartOfPeriod":1325376000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":0.044,"chargePeriod":1,"validityEndOfPeriod":1364774400,"validityStartOfPeriod":1356998400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Wed"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1364774400,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1321623199,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.0425,"chargePeriod":1,"validityEndOfPeriod":1325376000,"validityStartOfPeriod":1321623199,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":0.04333333,"chargePeriod":1,"validityEndOfPeriod":1356998400,"validityStartOfPeriod":1325376000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":0.044,"chargePeriod":1,"validityEndOfPeriod":1364774400,"validityStartOfPeriod":1356998400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Sat"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1364774400,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1321623199,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.044,"chargePeriod":1,"validityEndOfPeriod":1366988279,"validityStartOfPeriod":1364774400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Tue"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1366988279,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1364774400,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.044,"chargePeriod":1,"validityEndOfPeriod":1366988279,"validityStartOfPeriod":1364774400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Thu"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1366988279,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1364774400,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.044,"chargePeriod":1,"validityEndOfPeriod":1366988279,"validityStartOfPeriod":1364774400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Mon"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1366988279,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1364774400,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.044,"chargePeriod":1,"validityEndOfPeriod":1366988279,"validityStartOfPeriod":1364774400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Fri"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1366988279,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1364774400,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.044,"chargePeriod":1,"validityEndOfPeriod":1366988279,"validityStartOfPeriod":1364774400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Wed"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1366988279,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1364774400,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.044,"chargePeriod":1,"validityEndOfPeriod":1366988279,"validityStartOfPeriod":1364774400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Sat"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1366988279,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1364774400,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":17.8,"chargePeriod":840,"validityEndOfPeriod":1325376000,"validityStartOfPeriod":1322049393,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":18.1,"chargePeriod":840,"validityEndOfPeriod":1356998400,"validityStartOfPeriod":1325376000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":18.396,"chargePeriod":840,"validityEndOfPeriod":1366988264,"validityStartOfPeriod":1356998400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Dagkaart gebied 2","validityDays":["Tue"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1366988264,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1322049393,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":17.8,"chargePeriod":840,"validityEndOfPeriod":1325376000,"validityStartOfPeriod":1322049393,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":18.1,"chargePeriod":840,"validityEndOfPeriod":1356998400,"validityStartOfPeriod":1325376000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":18.396,"chargePeriod":840,"validityEndOfPeriod":1366988264,"validityStartOfPeriod":1356998400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Dagkaart gebied 2","validityDays":["Thu"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1366988264,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1322049393,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":17.8,"chargePeriod":840,"validityEndOfPeriod":1325376000,"validityStartOfPeriod":1322049393,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":18.1,"chargePeriod":840,"validityEndOfPeriod":1356998400,"validityStartOfPeriod":1325376000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":18.396,"chargePeriod":840,"validityEndOfPeriod":1366988264,"validityStartOfPeriod":1356998400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Dagkaart gebied 2","validityDays":["Mon"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1366988264,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1322049393,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":17.8,"chargePeriod":840,"validityEndOfPeriod":1325376000,"validityStartOfPeriod":1322049393,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":18.1,"chargePeriod":840,"validityEndOfPeriod":1356998400,"validityStartOfPeriod":1325376000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":18.396,"chargePeriod":840,"validityEndOfPeriod":1366988264,"validityStartOfPeriod":1356998400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Dagkaart gebied 2","validityDays":["Fri"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1366988264,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1322049393,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":17.8,"chargePeriod":840,"validityEndOfPeriod":1325376000,"validityStartOfPeriod":1322049393,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":18.1,"chargePeriod":840,"validityEndOfPeriod":1356998400,"validityStartOfPeriod":1325376000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":18.396,"chargePeriod":840,"validityEndOfPeriod":1366988264,"validityStartOfPeriod":1356998400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Dagkaart gebied 2","validityDays":["Wed"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1366988264,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1322049393,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":17.8,"chargePeriod":840,"validityEndOfPeriod":1325376000,"validityStartOfPeriod":1322049393,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":18.1,"chargePeriod":840,"validityEndOfPeriod":1356998400,"validityStartOfPeriod":1325376000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":18.396,"chargePeriod":840,"validityEndOfPeriod":1366988264,"validityStartOfPeriod":1356998400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Dagkaart gebied 2","validityDays":["Sat"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1366988264,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1322049393,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":6.1,"chargePeriod":240,"validityEndOfPeriod":1325376000,"validityStartOfPeriod":1322049433,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":6.2,"chargePeriod":240,"validityEndOfPeriod":1356998400,"validityStartOfPeriod":1325376000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":6.3,"chargePeriod":240,"validityEndOfPeriod":1366988254,"validityStartOfPeriod":1356998400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Avondkaart gebied 2","validityDays":["Tue"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1366988254,"validityFromTime":{"s":0,"m":0,"h":19},"startOfPeriod":1322049433,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":6.1,"chargePeriod":240,"validityEndOfPeriod":1325376000,"validityStartOfPeriod":1322049433,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":6.2,"chargePeriod":240,"validityEndOfPeriod":1356998400,"validityStartOfPeriod":1325376000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":6.3,"chargePeriod":240,"validityEndOfPeriod":1366988254,"validityStartOfPeriod":1356998400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Avondkaart gebied 2","validityDays":["Thu"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1366988254,"validityFromTime":{"s":0,"m":0,"h":19},"startOfPeriod":1322049433,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":6.1,"chargePeriod":240,"validityEndOfPeriod":1325376000,"validityStartOfPeriod":1322049433,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":6.2,"chargePeriod":240,"validityEndOfPeriod":1356998400,"validityStartOfPeriod":1325376000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":6.3,"chargePeriod":240,"validityEndOfPeriod":1366988254,"validityStartOfPeriod":1356998400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Avondkaart gebied 2","validityDays":["Mon"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1366988254,"validityFromTime":{"s":0,"m":0,"h":19},"startOfPeriod":1322049433,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":6.1,"chargePeriod":240,"validityEndOfPeriod":1325376000,"validityStartOfPeriod":1322049433,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":6.2,"chargePeriod":240,"validityEndOfPeriod":1356998400,"validityStartOfPeriod":1325376000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":6.3,"chargePeriod":240,"validityEndOfPeriod":1366988254,"validityStartOfPeriod":1356998400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Avondkaart gebied 2","validityDays":["Fri"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1366988254,"validityFromTime":{"s":0,"m":0,"h":19},"startOfPeriod":1322049433,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":6.1,"chargePeriod":240,"validityEndOfPeriod":1325376000,"validityStartOfPeriod":1322049433,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":6.2,"chargePeriod":240,"validityEndOfPeriod":1356998400,"validityStartOfPeriod":1325376000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":6.3,"chargePeriod":240,"validityEndOfPeriod":1366988254,"validityStartOfPeriod":1356998400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Avondkaart gebied 2","validityDays":["Wed"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1366988254,"validityFromTime":{"s":0,"m":0,"h":19},"startOfPeriod":1322049433,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":6.1,"chargePeriod":240,"validityEndOfPeriod":1325376000,"validityStartOfPeriod":1322049433,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":6.2,"chargePeriod":240,"validityEndOfPeriod":1356998400,"validityStartOfPeriod":1325376000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":6.3,"chargePeriod":240,"validityEndOfPeriod":1366988254,"validityStartOfPeriod":1356998400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Avondkaart gebied 2","validityDays":["Sat"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1366988254,"validityFromTime":{"s":0,"m":0,"h":19},"startOfPeriod":1322049433,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.044,"chargePeriod":1,"validityEndOfPeriod":1388534400,"validityStartOfPeriod":1369221928,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1390230329,"validityStartOfPeriod":1388534400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Tue"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1390230329,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1369221928,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.044,"chargePeriod":1,"validityEndOfPeriod":1388534400,"validityStartOfPeriod":1369221928,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1390230329,"validityStartOfPeriod":1388534400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Thu"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1390230329,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1369221928,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.044,"chargePeriod":1,"validityEndOfPeriod":1388534400,"validityStartOfPeriod":1369221928,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1390230329,"validityStartOfPeriod":1388534400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Mon"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1390230329,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1369221928,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.044,"chargePeriod":1,"validityEndOfPeriod":1388534400,"validityStartOfPeriod":1369221928,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1390230329,"validityStartOfPeriod":1388534400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Fri"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1390230329,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1369221928,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.044,"chargePeriod":1,"validityEndOfPeriod":1388534400,"validityStartOfPeriod":1369221928,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1390230329,"validityStartOfPeriod":1388534400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Wed"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1390230329,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1369221928,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.044,"chargePeriod":1,"validityEndOfPeriod":1388534400,"validityStartOfPeriod":1369221928,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1390230329,"validityStartOfPeriod":1388534400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Sat"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1390230329,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1369221928,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1401711520,"validityStartOfPeriod":1390230329,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Tue"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1401711520,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1390230329,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1401711520,"validityStartOfPeriod":1390230329,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Thu"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1401711520,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1390230329,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1401711520,"validityStartOfPeriod":1390230329,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Mon"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1401711520,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1390230329,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1401711520,"validityStartOfPeriod":1390230329,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Fri"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1401711520,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1390230329,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1401711520,"validityStartOfPeriod":1390230329,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Wed"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1401711520,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1390230329,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1401711520,"validityStartOfPeriod":1390230329,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Sat"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1401711520,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1390230329,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1401722116,"validityStartOfPeriod":1401711520,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Tue"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1401722116,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1401711520,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1401722116,"validityStartOfPeriod":1401711520,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Thu"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1401722116,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1401711520,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1401722116,"validityStartOfPeriod":1401711520,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Mon"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1401722116,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1401711520,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1401722116,"validityStartOfPeriod":1401711520,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Fri"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1401722116,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1401711520,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1401722116,"validityStartOfPeriod":1401711520,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Wed"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1401722116,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1401711520,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1401722116,"validityStartOfPeriod":1401711520,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Sat"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1401722116,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1401711520,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1420070400,"validityStartOfPeriod":1401722116,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Tue"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1420070400,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1401722116,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1420070400,"validityStartOfPeriod":1401722116,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Thu"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1420070400,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1401722116,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1420070400,"validityStartOfPeriod":1401722116,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Mon"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1420070400,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1401722116,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1420070400,"validityStartOfPeriod":1401722116,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Fri"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1420070400,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1401722116,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1420070400,"validityStartOfPeriod":1401722116,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Wed"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1420070400,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1401722116,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05683,"chargePeriod":1,"validityEndOfPeriod":1420070400,"validityStartOfPeriod":1401722116,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Sat"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":1420070400,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1401722116,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05783333,"chargePeriod":1,"validityEndOfPeriod":null,"validityStartOfPeriod":1420070400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Tue"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":null,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1420070400,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05783333,"chargePeriod":1,"validityEndOfPeriod":null,"validityStartOfPeriod":1420070400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Thu"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":null,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1420070400,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05783333,"chargePeriod":1,"validityEndOfPeriod":null,"validityStartOfPeriod":1420070400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Mon"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":null,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1420070400,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05783333,"chargePeriod":1,"validityEndOfPeriod":null,"validityStartOfPeriod":1420070400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Fri"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":null,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1420070400,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05783333,"chargePeriod":1,"validityEndOfPeriod":null,"validityStartOfPeriod":1420070400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Wed"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":null,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1420070400,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":0.05783333,"chargePeriod":1,"validityEndOfPeriod":null,"validityStartOfPeriod":1420070400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Kortparkeertarief gebied 2","validityDays":["Sat"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":null,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1420070400,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":18.396,"chargePeriod":840,"validityEndOfPeriod":1388534400,"validityStartOfPeriod":1369221967,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":23.7804,"chargePeriod":840,"validityEndOfPeriod":1420070400,"validityStartOfPeriod":1388534400,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":24.16,"chargePeriod":840,"validityEndOfPeriod":null,"validityStartOfPeriod":1420070400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Dagkaart gebied 2","validityDays":["Tue"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":null,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1369221967,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":18.396,"chargePeriod":840,"validityEndOfPeriod":1388534400,"validityStartOfPeriod":1369221967,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":23.7804,"chargePeriod":840,"validityEndOfPeriod":1420070400,"validityStartOfPeriod":1388534400,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":24.16,"chargePeriod":840,"validityEndOfPeriod":null,"validityStartOfPeriod":1420070400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Dagkaart gebied 2","validityDays":["Thu"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":null,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1369221967,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":18.396,"chargePeriod":840,"validityEndOfPeriod":1388534400,"validityStartOfPeriod":1369221967,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":23.7804,"chargePeriod":840,"validityEndOfPeriod":1420070400,"validityStartOfPeriod":1388534400,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":24.16,"chargePeriod":840,"validityEndOfPeriod":null,"validityStartOfPeriod":1420070400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Dagkaart gebied 2","validityDays":["Mon"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":null,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1369221967,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":18.396,"chargePeriod":840,"validityEndOfPeriod":1388534400,"validityStartOfPeriod":1369221967,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":23.7804,"chargePeriod":840,"validityEndOfPeriod":1420070400,"validityStartOfPeriod":1388534400,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":24.16,"chargePeriod":840,"validityEndOfPeriod":null,"validityStartOfPeriod":1420070400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Dagkaart gebied 2","validityDays":["Fri"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":null,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1369221967,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":18.396,"chargePeriod":840,"validityEndOfPeriod":1388534400,"validityStartOfPeriod":1369221967,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":23.7804,"chargePeriod":840,"validityEndOfPeriod":1420070400,"validityStartOfPeriod":1388534400,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":24.16,"chargePeriod":840,"validityEndOfPeriod":null,"validityStartOfPeriod":1420070400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Dagkaart gebied 2","validityDays":["Wed"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":null,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1369221967,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":18.396,"chargePeriod":840,"validityEndOfPeriod":1388534400,"validityStartOfPeriod":1369221967,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":23.7804,"chargePeriod":840,"validityEndOfPeriod":1420070400,"validityStartOfPeriod":1388534400,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":24.16,"chargePeriod":840,"validityEndOfPeriod":null,"validityStartOfPeriod":1420070400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Dagkaart gebied 2","validityDays":["Sat"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":null,"validityFromTime":{"s":0,"m":0,"h":9},"startOfPeriod":1369221967,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":6.3,"chargePeriod":240,"validityEndOfPeriod":1385856000,"validityStartOfPeriod":1369221979,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":8.13,"chargePeriod":240,"validityEndOfPeriod":1420070400,"validityStartOfPeriod":1385856000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":8.26,"chargePeriod":240,"validityEndOfPeriod":null,"validityStartOfPeriod":1420070400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Avondkaart gebied 2","validityDays":["Tue"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":null,"validityFromTime":{"s":0,"m":0,"h":19},"startOfPeriod":1369221979,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":6.3,"chargePeriod":240,"validityEndOfPeriod":1385856000,"validityStartOfPeriod":1369221979,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":8.13,"chargePeriod":240,"validityEndOfPeriod":1420070400,"validityStartOfPeriod":1385856000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":8.26,"chargePeriod":240,"validityEndOfPeriod":null,"validityStartOfPeriod":1420070400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Avondkaart gebied 2","validityDays":["Thu"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":null,"validityFromTime":{"s":0,"m":0,"h":19},"startOfPeriod":1369221979,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":6.3,"chargePeriod":240,"validityEndOfPeriod":1385856000,"validityStartOfPeriod":1369221979,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":8.13,"chargePeriod":240,"validityEndOfPeriod":1420070400,"validityStartOfPeriod":1385856000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":8.26,"chargePeriod":240,"validityEndOfPeriod":null,"validityStartOfPeriod":1420070400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Avondkaart gebied 2","validityDays":["Mon"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":null,"validityFromTime":{"s":0,"m":0,"h":19},"startOfPeriod":1369221979,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":6.3,"chargePeriod":240,"validityEndOfPeriod":1385856000,"validityStartOfPeriod":1369221979,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":8.13,"chargePeriod":240,"validityEndOfPeriod":1420070400,"validityStartOfPeriod":1385856000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":8.26,"chargePeriod":240,"validityEndOfPeriod":null,"validityStartOfPeriod":1420070400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Avondkaart gebied 2","validityDays":["Fri"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":null,"validityFromTime":{"s":0,"m":0,"h":19},"startOfPeriod":1369221979,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":6.3,"chargePeriod":240,"validityEndOfPeriod":1385856000,"validityStartOfPeriod":1369221979,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":8.13,"chargePeriod":240,"validityEndOfPeriod":1420070400,"validityStartOfPeriod":1385856000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":8.26,"chargePeriod":240,"validityEndOfPeriod":null,"validityStartOfPeriod":1420070400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Avondkaart gebied 2","validityDays":["Wed"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":null,"validityFromTime":{"s":0,"m":0,"h":19},"startOfPeriod":1369221979,"periodName":""},{"intervalRates":[{"durationFrom":0,"charge":6.3,"chargePeriod":240,"validityEndOfPeriod":1385856000,"validityStartOfPeriod":1369221979,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":8.13,"chargePeriod":240,"validityEndOfPeriod":1420070400,"validityStartOfPeriod":1385856000,"durationType":"Minutes","durationUntil":-1},{"durationFrom":0,"charge":8.26,"chargePeriod":240,"validityEndOfPeriod":null,"validityStartOfPeriod":1420070400,"durationType":"Minutes","durationUntil":-1}],"tariffDescription":"Avondkaart gebied 2","validityDays":["Sat"],"validityUntilTime":{"s":59,"m":59,"h":22},"endOfPeriod":null,"validityFromTime":{"s":0,"m":0,"h":19},"startOfPeriod":1369221979,"periodName":""}],"validityEndOfPeriod":null,"limitedAccess":false,"specifications":[{"areaGeometry":{"type":"Polygon","coordinates":[[[5.115448157,52.080419681],[5.11527572,52.080612321],[5.115797819,52.080797797],[5.11602255,52.080713853],[5.116683629,52.080577172],[5.117518873,52.080479263],[5.1181434,52.08020428],[5.118906002,52.07975301],[5.118259053,52.079413197],[5.116904687,52.078883794],[5.115262067,52.078259326],[5.114928184,52.078614897],[5.114840674,52.078708198],[5.113250777,52.080398057],[5.113599572,52.080518876],[5.113733436,52.080530584],[5.114011328,52.080539982],[5.114381454,52.080550897],[5.114731592,52.08054289],[5.114910881,52.080530873],[5.115166477,52.080512499],[5.115448157,52.080419681]]]},"usage":"Betaald Parkeren"}],"operator":{"name":"Utrecht","validityEndOfPeriod":null,"administrativeAddresses":[null],"validityStartOfPeriod":1318550400,"url":""},"parkingRestrictions":[],"description":"Hendrik Tollensstraat e.o.","name":"Hendrik Tollensstraat e.o.","accessPoints":[],"paymentMethods":[],"validityStartOfPeriod":1321574400,"specialDays":[],"identifier":"57d6f361-ffea-4186-a5a0-80a122c06fc3"}}





		*/

//		Amsterdam.populate();
		PlaceParkingGarage[] ggs = Amsterdam.getGarages();	// garages
		PlaceParkingGarage[] ags = Amsterdam.getMeters();	// areas
		
		System.out.println("Areas size : " +ags.length);
		if (ags.length > 0) {
			for (PlaceParkingGarage p : ags) {
				System.out.print(p.name);
				if (p.poly != null) {
					GeometryFactory fact = new GeometryFactory();
					Coordinate cor1 = new Coordinate(52.3487293,4.8404326);
//					boolean isin = p.poly.contains(fact.createPoint(cor1));

					final com.vividsolutions.jts.geom.Point point = fact.createPoint(cor1);
					boolean isin = point.within(p.poly);
					
					System.out.print("\t");
					System.out.print(cor1);
					System.out.print("\tA:");
					System.out.print(p.poly.getArea());
					System.out.print("\t");
					System.out.print(isin);
					System.out.print("\t");
					System.out.print(p.poly);
					
				} else {
					System.out.print("NULL");
					
				}
				System.out.println("");
			}
			Amsterdam.downloadmeters(ags);
			return;
		}
		
		
		/* make a list of area items
		 * 
		 */

		String parkinglocationsUrl = "https://opendata.rdw.nl/api/views/mz4f-59fw/rows.csv?accessType=DOWNLOAD";
		com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse res = com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(parkinglocationsUrl, "", null, null, null, null,null,500);
		System.out.println(res.text);
		
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
				}
			}
		}

		Vector<PlaceParkingGarage> vect = new Vector<PlaceParkingGarage>();
		
		int readfrominternet = 0;
		nextarea:
		for (int i=0; i < areas.size(); i++) {
			try {
				String FN = "/opt/tmp/rdw/"+areas.get(i).uuid+".json";
				File f = new File(FN);
				String txt = "";
				String U = "https://npropendata.rdw.nl/parkingdata/v2/static/" + areas.get(i).uuid;
				
				if (f.exists()) {
					System.out.println("READING FILE ["+areas.get(i).uuid+"]");
					txt = com.glimworm.opendata.divvamsterdamapi.planning.net.FileUtils.readFile(FN);
				}
				if (txt == null || txt.trim().length() == 0) {
					System.out.println("SEARCHING FOR ["+areas.get(i).uuid+"]"+U);
					res = com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(U, "", null, null, null, null,null,500);
					com.glimworm.opendata.divvamsterdamapi.planning.net.FileUtils.writeFile(FN, res.text);
					txt = res.text;
					readfrominternet++;
				}
				
				org.json.JSONObject jsob = com.glimworm.common.utils.jsonUtils.string2json(txt);
				
				org.json.JSONObject jsob1 = jsob.optJSONObject("parkingFacilityInformation");
				
				areas.get(i).detail_url = U;
				areas.get(i).description =  jsob1.optString("description");
				areas.get(i).name =  jsob1.optString("name");
				areas.get(i).operator =  jsob1.optJSONObject("operator");
				areas.get(i).validityStartOfPeriod =  jsob1.optLong("validityStartOfPeriod");
				areas.get(i).validityEndOfPeriod =  jsob1.optLong("validityEndOfPeriod");
				
				long now = (long) (System.currentTimeMillis() / 1000L);
//				long now = 1337126400+1000;
//				new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse("01/01/2013 01:00:00").getTime() / 1000;
				
				System.out.println(now);
				System.out.println(areas.get(i).validityStartOfPeriod);
				System.out.println(areas.get(i).validityEndOfPeriod);
				
				if (now < areas.get(i).validityStartOfPeriod || (areas.get(i).validityEndOfPeriod > 0 && now > areas.get(i).validityEndOfPeriod)) {
					areas.get(i).valid =  false;
					continue nextarea;
				}
				System.out.println("VALID");

				org.json.JSONArray specifications = jsob1.optJSONArray("specifications");
				if (specifications != null) {
					for (int k = 0; k < specifications.length(); k++) {
						String usage = specifications.getJSONObject(k).getString("usage");
						areas.get(i).usage += usage+",";
						System.out.println(usage);
						if (usage.startsWith("Garage") == false) continue nextarea;
					}
				}
				
				
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

				PlaceParkingGarage pl = new PlaceParkingGarage();
				pl.name = areas.get(i).name;
				pl.url = areas.get(i).detail_url;
				pl.lat = areas.get(i).lat;
				pl.lon = areas.get(i).lon;
				pl.postcode = areas.get(i).zipcode;
				pl.street = areas.get(i).streetName + " " + areas.get(i).houseNumber;
				pl.cdk_id = "";
				pl.places = 0;
				pl.type = "parking-garage"; // "park-and-ride"

				pl.capacity = 0;
				pl.free_minutes = 0;
				pl.time_unit_minutes = 0;

				pl.price_day = 0.0d;
				pl.price_per_time_unit = 0.0d;
				
				for (int d=0; d < 7; d++) {
					pl.opening_times[d] = new PlaceParkingGarageOpeningTimes();
					pl.opening_times[d].dayOfWeek= d;
				}
				

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

								// this is a valid tarrif
								int validityFromTime_h = jsobjt.getJSONObject("validityFromTime").getInt("h");
								int validityFromTime_m = jsobjt.getJSONObject("validityFromTime").getInt("m");
								int validityFromTime_s = jsobjt.getJSONObject("validityFromTime").getInt("s");
		
								areas.get(i).usage += " "+validityFromTime_h+validityFromTime_m+" ";

								// this is a valid tarrif
								int validityUntilTime_h = jsobjt.getJSONObject("validityUntilTime").getInt("h");
								int validityUntilTime_m = jsobjt.getJSONObject("validityUntilTime").getInt("m");
								int validityUntilTime_s = jsobjt.getJSONObject("validityUntilTime").getInt("s");

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
										
										

										if (chargePeriod < 2) {
											areas.get(i).usage += " "+charge+" ";
											areas.get(i).usage += " "+chargePeriod+" ";
											areas.get(i).usage += " "+durationType+" ";
											areas.get(i).usage += " "+durationFrom+" ";
											areas.get(i).usage += " "+durationUntil+" ";

											pl.time_unit_minutes = chargePeriod;
											pl.price_per_time_unit = charge;
										
										}
										if (chargePeriod > (60*6)) {
											pl.price_day = charge;
										}
									
									}
								}
							}
						}

					}
					
				}
				
				org.json.JSONArray openingTimes = jsob1.optJSONArray("openingTimes");
				if (openingTimes != null) {
					for (int j = 0; j < openingTimes.length(); j++) {
						System.out.println(openingTimes.getJSONObject(j).toString());
					}
				}
				vect.add(pl);

				if (readfrominternet > 50) break;

			} catch (Exception E) {
//				E.printStackTrace(System.out);
				System.out.println(E.getLocalizedMessage());
			}
			
		}
		
		Object result[] = new PlaceParkingGarage[vect.size()];
		vect.copyInto(result);
		PlaceParkingGarage[] pg = (PlaceParkingGarage[])result;
		for (PlaceParkingGarage p : pg) {
			System.out.print(p.name);
			System.out.print(" ("+p.lat);
			System.out.println(","+p.lon+")");
		}
		

		for (int i=0; i < areas.size(); i++) {
//			System.out.println("**) "+ areas.get(i).uuid+" \t "+areas.get(i).valid+"\t"+areas.get(i).usage+ "\t"+areas.get(i).description);
		}
		
		
//		Amsterdam.populate();
		
//		System.out.println("Areas size : " +Amsterdam.areas.size());
		
		if (res.text != null) return;
		
		
		
		
		
		res = com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(S, "", null, null, null, null);
		System.out.println(res.text.substring(0,500));
		
		
		org.json.JSONObject jsob = com.glimworm.common.utils.jsonUtils.string2json("{\"results\":"+res.text+"}");
		org.json.JSONArray ar = jsob.optJSONArray("results");
		for (int i=0; i < ar.length(); i++) {
			System.out.println(ar.optJSONObject(i).optString("areaid"));
			System.out.println(ar.optJSONObject(i).optString("areamanagerid"));
			System.out.println(ar.optJSONObject(i).optString("uuid"));
			
			String areaid = ar.optJSONObject(i).optString("areaid");
			String areamanagerid = ar.optJSONObject(i).optString("areamanagerid");
			String uuid = ar.optJSONObject(i).optString("uuid");
			
			if (areamanagerid.equalsIgnoreCase("363") == false) continue;
			
			String S1 = "https://npropendata.rdw.nl/parkingdata/v2/static/"+uuid;
			System.out.println(S1);
			com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse res1 = com.glimworm.opendata.divvamsterdamapi.planning.net.CurlUtils.getCURL(S1, "", null, null, null, null);
//			System.out.println(res1.text);

			org.json.JSONObject jsob1 = com.glimworm.common.utils.jsonUtils.string2json(res1.text);
			System.out.println(jsob1.toString());

			break;
//			geom coords = com.glimworm.opendata.parkshark.importdata.citySDK.utils.geomFromJson(ar.optJSONObject(i).optJSONObject("geom"));
//			String cdk_id = ar.optJSONObject(i).optString("cdk_id");
//			
//			System.out.println(coords);
//			
//			JSONObject data = ar.optJSONObject(i).optJSONObject("layers").optJSONObject("divv.parking.buildings").optJSONObject("data");
//
//			PlaceParkingGarage pl = com.glimworm.opendata.parkshark.importdata.citySDK.utils.garageFromJson(ar.optJSONObject(i),data, cdk_id, coords);
//			pl.places = data.optInt("aantal");
//			
//			System.out.println(pl);
//			
//			System.out.println(data.optString("gid"));
//			System.out.println(data.optString("url"));
//			System.out.println(data.optString("adres"));
//			System.out.println(data.optString("title"));
//			System.out.println(data.optString("aantal"));
//			System.out.println(data.optString("postcode"));
//			System.out.println(data.optString("huisnummer"));
//			System.out.println(data.optString("woonplaats"));
//			System.out.println(data.optString("opmerkingen"));
//			System.out.println(ar.optJSONObject(i).toString());
						
		}
		
		
/*
		try {
			TourismClient client;
			String homeUrl = "http://polar-lowlands-9873.herokuapp.com/?list=backend";
			
			client = TourismClientFactory.getInstance().getClient(homeUrl);
			client.useVersion("1.0");
	
			Integer limit = 20, offset = 0;
			
			List<String> category = new ArrayList<String>();
			category.add("Music");
			category.add("Not�cias");
			category.add("Stuff from Stuff");
			
			ParameterList params = new ParameterList();
			params.add(new Parameter(ParameterTerms.CATEGORY, category));
			params.add(new Parameter(ParameterTerms.TAG, "rock"));
			params.add(new Parameter(ParameterTerms.LIMIT, limit));
			params.add(new Parameter(ParameterTerms.OFFSET, offset));
			ListEvent eventList = client.getEvents(params);
			List<Event> events = eventList.getEvents();
			String url = "http://polar-lowlands-9873.herokuapp.com/v1/event/";
			Integer id = 1;
			for(Event event : events) {
				System.out.println(event.getBase() + "" + event.getId() + " / "  + url + (id++));
			}
		} catch (Exception E) {
			E.printStackTrace(System.out);
		}
	*/	

	}

}
