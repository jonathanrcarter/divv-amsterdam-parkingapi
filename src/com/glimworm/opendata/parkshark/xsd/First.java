package com.glimworm.opendata.parkshark.xsd;

public class First  implements Cloneable{
	public String combination = "";
	public double price = 0;
	public int hrs = 0;
	public double price2 = 0;
	public int hrs2 = 0;
	public String getSignature() {
		return combination + "|" +
			price + "|" +
			hrs + "|" +
			price2 + "|" +
			hrs2;
		
	}
	public Object clone() {
		try {
			First clone = (First) super.clone();
			return clone;
		} catch( CloneNotSupportedException e ) {
		    return null;
		}
    }	

}
