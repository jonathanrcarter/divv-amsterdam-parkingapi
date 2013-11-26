package com.glimworm.opendata.divvamsterdamapi.planning.net;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import com.glimworm.opendata.divvamsterdamapi.planning.net.xsd.curlResponse;



public class CurlUtils {
	
	public static String getURL(String S,String PARAMS, String DOM, String LOGIN, String PASS,String CONTENT_TYPE) {
		return getCURL(S, PARAMS, DOM, LOGIN, PASS, null).text;
		
	}
	public static curlResponse getCURL(String S,String PARAMS, String DOM, String LOGIN, String PASS,String CONTENT_TYPE) {
		return getCURL( S, PARAMS,  DOM,  LOGIN,  PASS, CONTENT_TYPE,null);
	}
	public static curlResponse getCURL(String S,String PARAMS, String DOM, String LOGIN, String PASS,String CONTENT_TYPE, String[] HEADERS) {
		
		
		curlResponse retval = new curlResponse();
		try{
			HttpClient client = new HttpClient();
			
			if (PARAMS != null && PARAMS.trim().length() > 0) {
				S += (S.indexOf("?") > -1) ? "&" : "?";
				S += PARAMS;
			}
			GetMethod method = new GetMethod(S);

			if (DOM != null) {
				//	new AuthScope("www.cmtportal.nl", 80, AuthScope.ANY_REALM),
				client.getState().setCredentials(
					new AuthScope(DOM, 80, AuthScope.ANY_REALM),
					new UsernamePasswordCredentials(LOGIN, PASS)
				);
				method.setDoAuthentication( true ); 
			}

			// e.g. "text/xml; charset=utf-8"
			if (CONTENT_TYPE != null) method.setRequestHeader("Content-Type", CONTENT_TYPE);
			if (HEADERS != null) {
				for (int i = 0; i < HEADERS.length; i++) {
					if (HEADERS[i] != null && HEADERS[i].trim().length() > 0 && HEADERS[i].indexOf(":") > -1) {
						String[] H = HEADERS[i].split("[:]",2);
						if (H.length == 2) method.setRequestHeader(H[0], H[1]);
					}
				}
			}
			
			retval.status = client.executeMethod(method);
	
			byte[] responseBody = method.getResponseBody();
			String rb = new String(responseBody);
			method.releaseConnection();
			
			System.out.println("statusCode ["+retval.status+"]");
			retval.text = rb;
			
		} catch (Exception E) {
			E.printStackTrace(System.out);
			retval.err = E.getMessage();
		}
		return retval;
	}
	//			DeleteMethod dm = new DeleteMethod();

	
	public static curlResponse postCURL(String S,String[][] PARAMS, String DOM, String LOGIN, String PASS,String CONTENT_TYPE, String[] HEADERS, String DATA) {
		curlResponse retval = new curlResponse();
		try{
			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(S);
			if (PARAMS != null) {
				for (int i=0; i < PARAMS.length; i++) {
					method.addParameter(PARAMS[i][0], PARAMS[i][1]);
				}
			}

			if (DOM != null) {
				//	new AuthScope("www.cmtportal.nl", 80, AuthScope.ANY_REALM),
				client.getState().setCredentials(
					new AuthScope(DOM, 80, AuthScope.ANY_REALM),
					new UsernamePasswordCredentials(LOGIN, PASS)
				);
				method.setDoAuthentication( true ); 
			}

			// e.g. "text/xml; charset=utf-8"
			if (CONTENT_TYPE != null) method.setRequestHeader("Content-Type", CONTENT_TYPE);
			if (HEADERS != null) {
				for (int i = 0; i < HEADERS.length; i++) {
					if (HEADERS[i] != null && HEADERS[i].trim().length() > 0 && HEADERS[i].indexOf(":") > -1) {
						String[] H = HEADERS[i].split("[:]",2);
						if (H.length == 2) method.setRequestHeader(H[0], H[1]);
					}
				}
			}
			if (DATA != null) {
				method.setRequestBody(DATA);
			}
			
			retval.status = client.executeMethod(method);
	
			byte[] responseBody = method.getResponseBody();
			String rb = new String(responseBody);
			method.releaseConnection();
			
			System.out.println("statusCode ["+retval.status+"]");
			retval.text = rb;
			
		} catch (Exception E) {
			E.printStackTrace(System.out);
			retval.err = E.getMessage();
		}
		return retval;
	}	
}
