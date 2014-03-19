package com.example.notes;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptUtil {
	
	public static String hashMd5(String string){
		if(string != null){
			String digest = null;
			try{
				MessageDigest messageDigest = MessageDigest.getInstance("MD5");
				byte[] hash = messageDigest.digest(string.getBytes("UTF-8"));
				
				StringBuilder stringBuilder = new StringBuilder(2*hash.length);
				
				for(byte b : hash){
					stringBuilder.append(String.format("%02x", b&0xff));
				}
				
				digest = stringBuilder.toString();
			} catch (UnsupportedEncodingException e0){
				System.out.println("EncodingException (md5Hash)" + e0);
			} catch (NoSuchAlgorithmException e1) {
				System.out.println("NoSuchAlgorithmException (md5Hash)" + e1);
			}
			return digest;
		}
		return string;
	}
	
	public static String cryptContentByPassword(String content, String password) {
		String res = "";
		char[] aContent = content.toCharArray();
		char[] aPassword = password.toCharArray();
		for(int i = 0; i < aContent.length; i++) {
			res += (char)(aContent[i] + aPassword[i%aPassword.length]);
		}
		return res;
	}
	
	public static String decryptContentByPassword(String content, String password) {
		String res = "";
		char[] aContent = content.toCharArray();
		char[] aPassword = password.toCharArray();
		for(int i = 0; i < aContent.length; i++) {
			res += (char)(aContent[i] - aPassword[i%aPassword.length]);
		}
		return res;
	}
}
