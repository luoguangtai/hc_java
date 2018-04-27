package com.hc.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IBaseDao {

	/**
	 * 通过主键获取数据
	 * @param id 主键值
	 * @return Object
	 * @author luogt@hsit.com.cn
	 * @date 2016-8-18
	 */
	public Object getObject(long id);

	/**
	 * 通过主键获取数据
	 * @param id 主键值
	 * @return Object
	 * @author luogt@hsit.com.cn
	 * @date 2016-8-18
	 */
	public Object getObject(int id);

	/**
	 * 通过主键获取数据
	 * @param id 主键值
	 * @return Object
	 * @author luogt@hsit.com.cn
	 * @date 2016-8-18
	 */
	public Object getObject(String id);

	/**
	 * 通过主键获取数据
	 * @param id 主键值
	 * @return Object
	 * @author luogt@hsit.com.cn
	 * @date 2016-8-18
	 */
	public Object getObject(Serializable id);

	/**
	 * 逻辑删除 通过主键逻辑删除数据
	 * @param id 主键值
	 * @author luogt@hsit.com.cn
	 * @date 2016-8-18
	 */
	public void removeObject(long id) throws Exception;

	/**
	 * 逻辑删除 通过主键逻辑删除数据
	 * @param id 主键值
	 * @author luogt@hsit.com.cn
	 * @date 2016-8-18
	 */
	public void removeObject(int id) throws Exception;

	/**
	 * 逻辑删除 通过主键逻辑删除数据
	 * @param id 主键值
	 * @author luogt@hsit.com.cn
	 * @date 2016-8-18
	 */
	public void removeObject(String id) throws Exception;

	/**
	 * 逻辑删除 通过主键逻辑删除数据
	 * @param id 主键值
	 * @author luogt@hsit.com.cn
	 * @date 2016-8-18
	 */
	public void removeObject(Serializable id) throws Exception;

	/**
	 * 物理删除 通过主键逻辑删除数据
	 * @param id 主键值
	 * @author luogt@hsit.com.cn
	 * @date 2016-8-18
	 */
	public void delObject(long id);

	/**
	 * 物理删除 通过主键逻辑删除数据
	 * @param id 主键值
	 * @author luogt@hsit.com.cn
	 * @date 2016-8-18
	 */
	public void delObject(int id);

	/**
	 * 物理删除 通过主键逻辑删除数据
	 * @param id 主键值
	 * @author luogt@hsit.com.cn
	 * @date 2016-8-18
	 */
	public void delObject(String id);

	/**
	 * 物理删除 通过主键逻辑删除数据
	 * @param id 主键值
	 * @author luogt@hsit.com.cn
	 * @date 2016-8-18
	 */
	public void delObject(Serializable id);

	/**
	 * 保存
	 * @param modelObject 实体类
	 * @author luogt@hsit.com.cn
	 * @date 2016-8-18
	 */
	public void saveObject(Object modelObject) throws Exception;
	/**
	 * 修改
	 * @param modelObject 实体类
	 * @author luogt@hsit.com.cn
	 * @date 2016-8-18
	 */
	public void updateObject(Object modelObject) throws Exception;

	/**
	 * 通过sql获取单条记录
	 * @param sql sql语句
	 * @return Map
	 * @author luogt@hsit.com.cn
	 * @date 2016-9-3
	 */
	public Map<String, Object> findObjectBySql(String sql);

	/**
	 * 通过hql获取单条记录
	 * @param hql hql语句
	 * @return Object
	 * @author luogt@hsit.com.cn
	 * @date 2016-9-3
	 */
	public Object findObjectByHql(String hql);

	/**
	 * 获取list数据
	 * @param sql sql语句
	 * @return list数据
	 * @author luogt@hsit.com.cn
	 * @date 2016-8-19
	 */
	public List findListBySql(String sql);

	/**
	 * 获取list数据
	 * @param hql hql语句
	 * @return list数据
	 * @author luogt@hsit.com.cn
	 * @date 2016-8-19
	 */
	public List findListByHql(String hql);

	/**
	 * 分页查询(使用sql)
	 * @param sql sql语句
	 * @param currPage 当前页
	 * @param pageSize 每页显示的记录条数
	 * @return page类
	 * @author luogt@hsit.com.cn
	 * @date 2016-8-18
	 */
	public Page findPageBySql(String sql, int currPage, int pageSize);
	
	/**
	 * 分页查询(使用sql)
	 * @param sql sql语句
	 * @param currPage 当前页
	 * @param pageSize 每页显示的记录条数
	 * @param totalCount 总记录数
	 * @return page类
	 * @author luogt@hsit.com.cn
	 * @date 2016-8-18
	 */
	public Page findPageBySql(String sql, int currPage, int pageSize, int totalCount);

	/**
	 * 执行SQL语句返回受影响的记录数
	 * @param sql sql语句
	 * @return 受影响的记录数
	 * @author luogt@hsit.com.cn
	 * @date 2016-9-3
	 */
	public int executeSql(String sql);

	/**
	 * 执行HQL语句返回受影响的记录数
	 * @param hql hql语句
	 * @return 受影响的记录数
	 * @author luogt@hsit.com.cn
	 * @date 2016-9-3
	 */
	public int executeHql(String hql);

	/**
	 * 分页查询(使用hql)
	 * @param hql hql语句
	 * @param currPage 当前页
	 * @param pageSize 每页显示的记录条数
	 * @param totalCount 总记录数
	 * @return page类
	 * @author luogt@hsit.com.cn
	 * @date 2016-8-18
	 */
	public Page findPageByHql(String hql, int currPage, int pageSize, int totalCount);

	/**
	 * 获取总记录数
	 * @param sql sql语句
	 * @return totalCount
	 * @author luogt@hsit.com.cn
	 * @date 2016-8-19
	 */
	public int getCountBySql(String sql);

	/**
	 * 获取总记录数
	 * @param hql hql语句
	 * @return totalCount
	 * @author luogt@hsit.com.cn
	 * @date 2016-8-19
	 */
	public int getCountByHql(String hql);

}