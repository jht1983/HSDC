/**
 * 
 */
package com.timing.util;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * 
 * @author tianshisheng
 *
 */
public final class AES256Util {
	/**
	 * 
	 * @param key
	 * @param dataBytes
	 * @param isEncrypt
	 * @return
	 */
	public static byte[] cryptData(String key, byte[] dataBytes, boolean isEncrypt) {
		byte[] ret = null;
		try {
			byte[] keyBytes = key.getBytes();
			
			Security.addProvider(new BouncyCastleProvider());
			SecretKeySpec k = new SecretKeySpec(keyBytes, "AES");
			Cipher c = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
//			c.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, k, new IvParameterSpec(new byte[16]));
//			ret = c.doFinal(dataBytes);

			c.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, k);
			ret = c.doFinal(dataBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		String content = "taiji636988676669826955{\"StartTime\":\"2019-01-01\",\"EndTime\":\"2019-05-31\",\"MineNo\":\"K0001\"}";
		String password = "B889900BD08147B79C0A889D05C74FAF";
		// 加密
		System.out.println("加密前：" + content);
		byte[] dataBytes = content.getBytes("utf-8");
		byte[] encode = cryptData(password, dataBytes, true);

		// 传输过程,不转成16进制的字符串，就等着程序崩溃掉吧
		String code = parseByte2HexStr(encode);
		System.out.println("密文字符串：" + code);
		byte[] decode = parseHexStr2Byte(code);
		// 解密
		byte[] decryptResult = cryptData(password, decode, false);
		System.out.println("解密后：" + new String(decryptResult, "UTF-8"));
	}
}
