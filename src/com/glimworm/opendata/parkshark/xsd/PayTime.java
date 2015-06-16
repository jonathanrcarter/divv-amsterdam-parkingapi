package com.glimworm.opendata.parkshark.xsd;

public class PayTime implements Cloneable {
	public int start = 0;
	public int end = 0;
	public String getSignature() {
		return start+"|"+end;
	}
	public Object clone() {
		try {
			PayTime clone = (PayTime) super.clone();
			return clone;
		} catch( CloneNotSupportedException e ) {
		    return null;
		}
    }	

}
