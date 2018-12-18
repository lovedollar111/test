package cn.dogplanet.app.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Arithmetic {

	public static String getMD5Str(String input) {
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(input.getBytes("UTF-8"));
			byte[] byteArray = messageDigest.digest();
			StringBuilder md5StrBuff = new StringBuilder();
			for (byte aByteArray : byteArray) {
				if (Integer.toHexString(0xFF & aByteArray).length() == 1) {
					md5StrBuff.append("0").append(
							Integer.toHexString(0xFF & aByteArray));
				} else {
					md5StrBuff.append(Integer.toHexString(0xFF & aByteArray));
				}
			}
			return md5StrBuff.toString().trim().toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
//	public static boolean checkMoblie(String phoneNumb){
//		if(phoneNumb != null){
//			Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//			Matcher m = p.matcher(phoneNumb);
//			return m.matches();
//		}
//		return false;
//	}

}
