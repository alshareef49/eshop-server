package com.eshop.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingUtility {

	public static String getHashValue(String data) throws NoSuchAlgorithmException {

		MessageDigest md = MessageDigest.getInstance("SHA-256");

		md.update(data.getBytes());

		byte[] byteData = md.digest();

		StringBuilder hexString = new StringBuilder();
        for (byte byteDatum : byteData) {

            String hex = Integer.toHexString(0xff & byteDatum);

            if (hex.length() == 1)
                hexString.append('0');

            hexString.append(hex);
        }

		return hexString.toString();
	}
}