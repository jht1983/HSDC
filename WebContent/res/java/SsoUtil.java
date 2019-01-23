package com.extop.sip.sso;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Random;

public class SsoUtil {
	
	private static HashMap<String, String>  ssoCheck = new HashMap<String, String>();
	private static Random randomCl = new Random();
	private String MD5(String s) {
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] bytes = md.digest(s.getBytes("utf-8"));
	        return toHex(bytes);
	    }
	    catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	private static String toHex(byte[] bytes) {
	    final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
	    StringBuilder ret = new StringBuilder(bytes.length * 2);
	    for (int i=0; i<bytes.length; i++) {
	        ret.append(HEX_DIGITS[(bytes[i] >> 5) & 0x0f]);
	        ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
	    }
	    return ret.toString();
	}

	public String DataEncryption(String SsoKey,String _userCond){
	    if(SsoKey==null){
	        SsoUtil ssoUtil = new SsoUtil();
    		String onlyNum = System.currentTimeMillis()+"";
    		onlyNum = onlyNum + randomCl.nextInt();
    		SsoKey = ssoUtil.MD5(onlyNum+"");
	    }
		
		ssoCheck.put(SsoKey,_userCond);
		return SsoKey;
	}
	
	public int CheckUserRegisteredInfor(String _Encryption,String _userCond){
		if(ssoCheck.containsKey(_Encryption)){
			String getUser = ssoCheck.get(_Encryption);
			if(getUser.equals(_userCond)){
				ssoCheck.remove(_Encryption, getUser);
				return 0;
			}
		}else{
			return -1;
		}
		return -2;
	}
}
