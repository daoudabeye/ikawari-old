package org.mobibank.payme.backend;

public class StringUtils {

	public static boolean isEmpty(String value) {
		if(value == null)
			return true;
		if(value.isEmpty())
			return true;
		return false;
	}
}
