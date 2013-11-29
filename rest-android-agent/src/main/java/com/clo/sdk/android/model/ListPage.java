package com.clo.sdk.android.model;

import java.io.Serializable;
import java.util.List;

/**
 * 分页的数据模型类
 * @author xiaozhi
 *
 */
public class ListPage<T> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2163218830368884853L;

	/**
	 * 当前页的记录列表
	 */
	private List<T> list;

	/**
	 * 当前页的第一条记录在全部记录中的索引数，以0为起始索引
	 */
	private int firstResult;

	/**
	 * 当前页的最后一条记录在全部记录中的索引数，以0为起始索引
	 */
	private int lastResult;

	/**
	 * 每一页的记录数
	 */
	private int maxResults;

	/**
	 * 全部记录的数量
	 */
	private int allResults;

	/**
	 * 上一页的第一条记录在全部记录中的索引数，以0为起始索引
	 */
	private int prevPage;

	/**
	 * 下一页的第一条记录在全部记录中的索引数，以0为起始索引
	 */
	private int nextPage;

	/**
	 * 最后一页的第一条记录在全部记录中的索引数，以0为起始索引
	 */
	private int lastPage;

	/**
	 * 当前页是第几页
	 */
	private int currPage;

	/**
	 * 共有多少页
	 */
	private int pages;

	public ListPage(){}

	public ListPage(List<T> list, int firstResult, int lastResult, int allResults) {
		if (firstResult >= lastResult) {
			throw new IllegalArgumentException("firstResult can not be greater than or equal lastResult");
		}
		this.list = list;
		this.firstResult = firstResult;
		this.lastResult = lastResult;
		maxResults = lastResult - firstResult+1;
		this.allResults = allResults;

		this.prevPage = firstResult - maxResults;
		if (this.prevPage < 0) {
			this.prevPage = 0;
		}
		if (allResults <= maxResults) {
			this.lastPage = 0;
		} else {
			this.lastPage = allResults % maxResults;
			this.lastPage = (this.lastPage == 0) ? (allResults - maxResults) : (allResults - this.lastPage);
		}
		this.nextPage = firstResult + maxResults;
		if (this.nextPage > this.lastPage) {
			this.nextPage = this.lastPage;
		}

		pages = allResults % maxResults;
		if (pages == 0)
			pages = allResults / maxResults;
		else
			pages = allResults / maxResults + 1;

		currPage = (firstResult + maxResults) / maxResults;
	}

	/**
	 * @return Returns the allResults.
	 */
	public int getAllResults() {
		return allResults;
	}

	/**
	 * @return Returns the firstResult.
	 */
	public int getFirstResult() {
		return firstResult;
	}

	public int getLastResult() {
		return lastResult;
	}

	public int getMaxResults() {
		return maxResults;
	}

	/**
	 * @return Returns the list.
	 */
	public List<T> getList() {
		return list;
	}

	public int getNextPage() {
		return nextPage;
	}

	public int getPrevPage() {
		return prevPage;
	}

	public int getCurrPage() {
		return currPage;
	}

	public int getLastPage() {
		return lastPage;
	}

	public int getPages() {
		return pages;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public void setLastResult(int lastResult) {
		this.lastResult = lastResult;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public void setAllResults(int allResults) {
		this.allResults = allResults;
	}

	public void setPrevPage(int prevPage) {
		this.prevPage = prevPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	@Override
	public String toString() {
		return "ListPage [list=" + list + ", firstResult=" + firstResult
				+ ", lastResult=" + lastResult + ", maxResults=" + maxResults
				+ ", allResults=" + allResults + ", prevPage=" + prevPage
				+ ", nextPage=" + nextPage + ", lastPage=" + lastPage
				+ ", currPage=" + currPage + ", pages=" + pages + "]";
	}

}
