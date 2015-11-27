package com.epam.tester.shared;

public class FieldVerifier {

	public static boolean isValidName(String name) {
		if (!name.matches("[A-Za-z0-9]{3,20}"))
			return false;
		return name.length() > 3;
	}
}
