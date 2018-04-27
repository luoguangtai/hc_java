package com.hc.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;

import com.hc.util.CastUtil;
import com.hc.util.DateUtil;

/**
 * Dao基础类 实现Dao的基础操作封装，包括增加、删除、修改、获取、查询等方法
 * 
 * @author luogt@hsit.com.cn
 * @date 2016-7-30
 * 
 */
public class BaseDao implements IBaseDao {

	public Class modelClass;

	@Autowired
	private SessionFactory sessionFactory;

	Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public final Object getObject(long id) {
		Serializable idTemp = new Long(id);
		return getObject(idTemp);
	}

	public final Object getObject(int id) {
		Serializable idTemp = new Long(id);
		return getObject(idTemp);
	}

	public final Object getObject(String id) {
		Serializable idTemp = new String(id);
		return getObject(idTemp);
	}

	public final Object getObject(Serializable id) {
		return getSession().get(modelClass, id);
	}

	public final void removeObject(long id) throws Exception {
		Serializable idTemp = new Long(id);
		removeObject(idTemp);
	}

	public final void removeObject(int id) throws Exception {
		Serializable idTemp = new Long(id);
		removeObject(idTemp);
	}

	public final void removeObject(String id) throws Exception {
		Serializable idTemp = new String(id);
		removeObject(idTemp);
	}

	public final void removeObject(Serializable id) throws Exception {
		Object modelObject = getObject(id);
		if (modelObject != null) {
			MethodInvokingFactoryBean method = new MethodInvokingFactoryBean();
			method.setTargetObject(modelObject);
			method.setTargetMethod("setDataState");
			method.setArguments(new Object[] { "0" });
			method.afterPropertiesSet();
			method.setTargetMethod("setModifyTime");
			method.setArguments(new Object[] { DateUtil.getNowAsStr1() });
			method.afterPropertiesSet();
			getSession().update(modelObject);
		}
	}

	public final void delObject(long id) {
		Serializable idTemp = new Long(id);
		delObject(idTemp);
	}

	public final void delObject(int id) {
		Serializable idTemp = new Long(id);
		delObject(idTemp);
	}

	public final void delObject(String id) {
		Serializable idTemp = new String(id);
		delObject(idTemp);
	}

	public final void delObject(Serializable id) {
		Object modelObject = getObject(id);
		if (modelObject != null) {
			getSession().delete(modelObject);
		}
	}

	public final void saveObject(Object modelObject) throws Exception {
		if (modelObject != null) {
			String nowAsStr = DateUtil.getNowAsStr1();
			MethodInvokingFactoryBean method = new MethodInvokingFactoryBean();
			method.setTargetObject(modelObject);
			method.setTargetMethod("setDataState");
			method.setArguments(new Object[] { "1" });
			method.afterPropertiesSet();
			method.setTargetMethod("setInTime");
			method.setArguments(new Object[] { nowAsStr });
			method.afterPropertiesSet();
			method.setTargetMethod("setModifyTime");
			method.setArguments(new Object[] { nowAsStr });
			method.afterPropertiesSet();
			getSession().save(modelObject);
		}
	}

	public final void updateObject(Object modelObject) throws Exception {
		if (modelObject != null) {
			String nowAsStr = DateUtil.getNowAsStr1();
			MethodInvokingFactoryBean method = new MethodInvokingFactoryBean();
			method.setTargetObject(modelObject);
			method.setTargetMethod("setDataState");
			method.setArguments(new Object[] { "1" });
			method.afterPropertiesSet();
			method.setTargetMethod("setModifyTime");
			method.setArguments(new Object[] { nowAsStr });
			method.afterPropertiesSet();
			getSession().update(modelObject);
		}
	}

	public final Map<String, Object> findObjectBySql(String sql) {
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List list = sqlQuery.list();
		if (list.size() <= 0) {
			return null;
		}
		Map<String, Object> reMap = CastUtil.castMap(list.get(0));
		return reMap;
	}

	public final Object findObjectByHql(String hql) {
		List list = getSession().createQuery(hql).list();
		if (list.size() <= 0) {
			return null;
		}
		return list.get(0);
	}

	public final List findListBySql(String sql) {
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List list = CastUtil.castList(sqlQuery.list());
		return list;
	}

	public final List findListByHql(String hql) {
		return getSession().createQuery(hql).list();
	}

	public final int executeSql(String sql) {
		return getSession().createSQLQuery(sql).executeUpdate();
	}

	public final int executeHql(String hql) {
		return getSession().createQuery(hql).executeUpdate();
	}

	public final Page findPageBySql(String sql, int currPage, int pageSize) {
		int totalCount = this.getCountBySql(sql);
		return this.findPageBySql(sql, currPage, pageSize, totalCount);
	}

	public final Page findPageBySql(String sql, int currPage, int pageSize, int totalCount) {
		int startRow = (currPage - 1) * pageSize;
		if (startRow < 0)
			startRow = 0;
		Query query = getSession().createSQLQuery(sql).setFirstResult(startRow).setMaxResults(pageSize);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List dataList = CastUtil.castList(query.list());
		return new Page(dataList, totalCount, currPage, pageSize);
	}

	public final Page findPageByHql(String hql, int currPage, int pageSize, int totalCount) {
		int startRow = (currPage - 1) * pageSize;
		if (startRow < 0)
			startRow = 0;
		Query query = getSession().createQuery(hql).setFirstResult(startRow).setMaxResults(pageSize);
		List dataList = query.list();
		return new Page(dataList, totalCount, currPage, pageSize);
	}

	public final int getCountBySql(String sql) {
		int totalCount = 0;
		sql = sql.toUpperCase();
		if (sql.indexOf("GROUP BY") > 0) {
			sql = " SELECT COUNT(1) AS COUNT_NUM FROM (" + sql + ") AS CT";
		} else {
			sql = " SELECT COUNT(1) AS COUNT_NUM " + sql.substring(sql.indexOf("FROM"), sql.length());
		}
		Object value = getSession().createSQLQuery(sql).addScalar("COUNT_NUM", StandardBasicTypes.LONG).uniqueResult();
		if (value != null) {
			totalCount = ((Long) value).intValue();
		}
		return totalCount;
	}

	public final int getCountByHql(String hql) {
		int totalCount = 0;
		Object value = getSession().createQuery(hql).uniqueResult();
		if (value != null) {
			totalCount = ((Long) value).intValue();
		}
		return totalCount;
	}
}