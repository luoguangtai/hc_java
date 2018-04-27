package com.hc.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author Administrator
 */
public class AESPlus {
	private Cipher encryptCipher = null;
	private Cipher decryptCipher = null;

	/**
	 * 指定密钥构造方法
	 * 
	 * @param strKey
	 *            指定的密钥
	 * @throws Exception
	 */
	public AESPlus(String password) {
		try{
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
	
			// Cipher对象实际完成加密操作
			encryptCipher = Cipher.getInstance("AES");
			encryptCipher.init(Cipher.ENCRYPT_MODE, key);
	
			decryptCipher = Cipher.getInstance("AES");
			decryptCipher.init(Cipher.DECRYPT_MODE, key);
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 加密字符串
	 * @param strIn 需加密的字符串
	 * @return 加密后的字符串
	 * @throws Exception
	 */
	public String encrypt(String strIn){
		try {
			return ByteUtil.byteArr2HexStr(encryptCipher.doFinal(strIn.getBytes()));
//			return Base64.encodeBase64String(encryptCipher.doFinal(strIn.getBytes()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 解密字符串
	 * @param strIn 需解密的字符串
	 * @return 解密后的字符串
	 * @throws Exception
	 */
	public String decrypt(String strIn) {
		try {
			return new String(decryptCipher.doFinal(ByteUtil.hexStr2ByteArr(strIn)));
//			return new String(decryptCipher.doFinal(Base64.decodeBase64(strIn)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			String test = "山口大家flask的叫法是asjdflasd测试一下kkkkkkkkkzzzzzz!";
			String password = "123456";
			String to = null;
			String to2 = null;
			long ss = System.nanoTime();
			AESPlus des = new AESPlus(password);// 自定义密钥
			for (int i = 0; i < 10000; i++) {
				to = des.encrypt(test);
				to2 = des.decrypt(to);
			}
			System.out.println((System.nanoTime() - ss) / 1000000);
			System.out.println("加密前的字符：" + test);
			System.out.println("密码：" + password);
			System.out.println("加密后的字符：" + to);
			System.out.println("加密后的字符长度：" + to.length());
			System.out.println("解密后的字符：" + to2);
			
			System.out.println(des.decrypt("ffasdf"));
	}
}
