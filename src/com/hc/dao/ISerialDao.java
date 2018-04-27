package com.hc.dao;

import java.util.List;
import java.util.Map;

public interface ISerialDao {
	public List<Map> getSerialRuleList(String serialEk, String orgCd);

	public long getSerialNum(final String serialEk, final String serialPrefix, final int cdNum, final String orgCd);
}
