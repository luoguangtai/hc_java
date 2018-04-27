package com.hc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hc.dao.IBaseDao;

/**
 * 基础服务类 实现基础的服务操作功能，包括对象获取、分页获取、新增、批量删除、修改等操作
 * 
 * @author luogt@hsit.com.cn
 * @date 2016-8-17
 */
public class BaseService implements IBaseService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	protected IBaseDao baseDao;

	public final Object getObject(long id) {
		return baseDao.getObject(id);
	}

	public final Object getObject(int id) {
		return baseDao.getObject(id);
	}

	public final Object getObject(String id) {
		return baseDao.getObject(id);
	}

	public void removeObject(long id) throws Exception {
		baseDao.removeObject(id);
	}

	public void removeObject(int id) throws Exception {
		baseDao.removeObject(id);
	}

	public void removeObject(String id) throws Exception {
		baseDao.removeObject(id);
	}

	public void delObject(long id) {
		baseDao.delObject(id);
	}

	public void delObject(int id) {
		baseDao.delObject(id);
	}

	public void delObject(String id) {
		baseDao.delObject(id);
	}

	public final void saveObject(Object modelObject) throws Exception {
		baseDao.saveObject(modelObject);
	}

	public final void updateObject(Object modelObject) throws Exception {
		baseDao.updateObject(modelObject);
	}
}
