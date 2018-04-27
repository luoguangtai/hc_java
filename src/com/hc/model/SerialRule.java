package com.hc.model;

import java.io.Serializable;

public class SerialRule implements Serializable{

	private static final long serialVersionUID = -6416016029528057231L;

	/**
	 * 截取key值数据末尾长度
	 * 与firstLen互斥
	 */
	private int lastLen;
	
	/**
	 * 截取key值数据前部分长度
	 * 与lastLen互斥
	 */
	private int firstLen;
	
	/**
	 * 流水号长度
	 */
	private int len;
	
	/**
	 * 对应数据Map中的key值
	 */
	private String key;
	
	/**
	 * 填充字符
	 */
	private String fill;
	
	/**
	 * @return the lastLen
	 */
	public int getLastLen() {
		return lastLen;
	}

	/**
	 * @param lastLen the lastLen to set
	 */
	public void setLastLen(int lastLen) {
		this.lastLen = lastLen;
	}

	/**
	 * @return the firstLen
	 */
	public int getFirstLen() {
		return firstLen;
	}

	/**
	 * @param firstLen the firstLen to set
	 */
	public void setFirstLen(int firstLen) {
		this.firstLen = firstLen;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the fill
	 */
	public String getFill() {
		return fill;
	}

	/**
	 * @param fill the fill to set
	 */
	public void setFill(String fill) {
		this.fill = fill;
	}

	/**
	 * @return the totalLen
	 */
	public int getLen() {
		return len;
	}

	/**
	 * @param totalLen the totalLen to set
	 */
	public void setLen(int len) {
		this.len = len;
	}

	
	
}