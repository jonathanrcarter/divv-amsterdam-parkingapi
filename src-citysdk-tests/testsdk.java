import java.util.ArrayList;
import java.util.List;

import org.json.*;

import com.glimworm.opendata.divvamsterdamapi.planning.xsd.Place;
import com.glimworm.opendata.divvamsterdamapi.planning.xsd.PlaceParkingGarage;
import com.glimworm.opendata.parkshark.importdata.citySDK.xsd.geom;

import citysdk.tourism.client.poi.lists.ListEvent;
import citysdk.tourism.client.poi.single.Event;
import citysdk.tourism.client.requests.Parameter;
import citysdk.tourism.client.requests.ParameterList;
import citysdk.tourism.client.requests.TourismClient;
import citysdk.tourism.client.requests.TourismClientFactory;
import citysdk.tourism.client.terms.ParameterTerms;


public class testsdk {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
