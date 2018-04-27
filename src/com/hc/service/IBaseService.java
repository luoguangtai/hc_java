package com.hc.service;

/**
 * 基础服务类
 * 实现基础的服务操作功能，包括对象获取、分页获取、新增、批量删除、修改等操作
 *@author luogt@hsit.com.cn
 *@date 2016-8-17
 */
public interface IBaseService {

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
}