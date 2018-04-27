package com.hc.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 
 * @author Administrator
 */
public class DESPlus {
	private Cipher encryptCipher = null;
	private Cipher decryptCipher = null;

	/**
	 * 指定密钥构造方法
	 * @param strKey 指定的密钥
	 * @throws Exception
	 */
	public DESPlus(String password) {
		try{
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(password.getBytes());
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成加密操作
			encryptCipher = Cipher.getInstance("DES");
			encryptCipher.init(Cipher.ENCRYPT_MODE, securekey, random);
	
			decryptCipher = Cipher.getInstance("DES");
			decryptCipher.init(Cipher.DECRYPT_MODE, securekey, random);
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
	public String encrypt(String strIn) {
		try {
			return ByteUtil.byteArr2HexStr(encryptCipher.doFinal(strIn.getBytes()));
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
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			String test = "Hellojwoei加哦爱ueiqerlkdvmjfasf阿列克山大金佛iuw Word爱的色放可垃圾啊色的发生的dsfjlkdfjlsk阿拉山口大家flask的叫法是asjdflasd测试一下kkkkkkkkkzzzzzz!";
			String password = "1的路口附近的烧录卡";
			String to = null;
			String to2 = null;
			long ss = System.nanoTime();
			DESPlus des = new DESPlus(password);// 自定义密钥
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
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
