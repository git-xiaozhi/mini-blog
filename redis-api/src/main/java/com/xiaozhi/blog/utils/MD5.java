package com.xiaozhi.blog.utils;

//程序名为MD5.java

import java.security.*;

public class MD5 {

	public static String calcMD5(String str) {
		try {
			MessageDigest alga = MessageDigest.getInstance("MD5");
			// MessageDigest alga=MessageDigest.getInstance("SHA-1");
			alga.update(str.getBytes());
			byte[] digesta = alga.digest();
			return byte2hex(digesta);
		} catch (NoSuchAlgorithmException ex) {
			System.out.println("error");
		}
		return "NULL";

	}

	public static String byte2hex(byte[] b) { // 二行制转字符串

		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + "";
		}
		// return hs.toUpperCase();
		return hs;
	}

	public static void main(String[] args) {
		String s = "19820311";
		System.out.println(MD5.calcMD5(s));
	}

}
