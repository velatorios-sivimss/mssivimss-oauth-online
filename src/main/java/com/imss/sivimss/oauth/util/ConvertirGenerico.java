package com.imss.sivimss.oauth.util;

public class ConvertirGenerico {

	private ConvertirGenerico() {
	    throw new IllegalStateException("ConvertirGenerico class");
	  }
	
	@SuppressWarnings("unchecked")
	public static <T> T convertInstanceOfObject(Object o) {
	    try {
	       return (T) o;
	    } catch (ClassCastException e) {
	        return null;
	    }
	}
}
