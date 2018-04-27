package com.hc.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页类
 */
public class Page implements Serializable {

	public final static int PAGESIZE = 10;
	// 每页显示条数
	private int pageSize = PAGESIZE;
	// 总记录数
	private int totalCount = 0;
	// 总页数
	private int totalPageNum = 0;
	// 当前页号
	private int currentPageNum = 1;
	// 每页数据
	private List dataList = new ArrayList();

	// 上一页号
	private int previousPageNum;
	// 下一页号
	private int nextPageNum;
	// 第一页号
	private int firstPageNum;
	// 最后一页号
	private int lastPageNum;

	// 是否是第一页
	private boolean isFirstPage;
	// 是否是最后一页
	private boolean isLastPage;
	// 是否有下一页
	private boolean hasNextPage;
	// 是否有上一页
	private boolean hasPreviousPage;

	public Page(List dataList, int totalCount, int currentPageNum, int pageSize) {
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		this.dataList = dataList;
		this.currentPageNum = currentPageNum;
		if (this.pageSize == 0) {
			this.totalPageNum = 0;
		} else {
			this.totalPageNum = totalCount % this.pageSize == 0 ? totalCount / this.pageSize : totalCount / this.pageSize + 1;
		}
		this.previousPageNum = getPreviousPageNum();
		this.nextPageNum = getNextPageNum();
		this.firstPageNum = getFirstPageNum();
		this.lastPageNum = getLastPageNum();
		this.isFirstPage = isFirstPage();
		this.isLastPage = isLastPage();
		this.hasNextPage = hasNextPage();
		this.hasPreviousPage = hasPreviousPage();
	}

	/**
	 * 获取每页显示条数
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 获取总记录数
	 * @return
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * 获取总页数
	 * @return
	 */
	public int getTotalPageNum() {
		return totalPageNum;
	}

	/**
	 * 获取当前页数
	 * @return
	 */
	public int getCurrentPageNum() {
		return currentPageNum;
	}

	/**
	 * 获取查询结果集合
	 * @return 查询结果集合
	 */
	public List getDataList() {
		return dataList;
	}

	/**
	 * 获取下一页面号码
	 * @return
	 */
	public int getNextPageNum() {
		return getCurrentPageNum() + 1 > totalPageNum ? totalPageNum : getCurrentPageNum() + 1;
	}

	/**
	 * 获取上一页面号码
	 * @return
	 */
	public int getPreviousPageNum() {
		return getCurrentPageNum() - 1 < 1 ? 1 : getCurrentPageNum() - 1;
	}

	/**
	 * 是否为第一页
	 * @return
	 */
	public boolean isFirstPage() {
		return getCurrentPageNum() == 1;
	}

	/**
	 * 是否最后一页
	 * @return
	 */
	public boolean isLastPage() {
		return getCurrentPageNum() == getTotalPageNum();
	}

	/**
	 * 是否有下一页
	 * @return
	 */
	public boolean hasNextPage() {
		return getTotalPageNum() > getCurrentPageNum();
	}

	/**
	 * 是否有上一页
	 * @return
	 */
	public boolean hasPreviousPage() {
		return getCurrentPageNum() > 1;
	}

	/**
	 * 获取第一页号码
	 * @return
	 */
	public int getFirstPageNum() {
		return 1;
	}

	/**
	 * 获取最后一页号码
	 * @return
	 */
	public int getLastPageNum() {
		return totalPageNum;
	}
}
