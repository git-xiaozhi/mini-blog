package com.xiaozhi.blog.utils;

import java.util.List;

import org.apache.solr.client.solrj.response.FacetField.Count;

import solr.search.SolrResult;



public class SolrPage<T> extends ListPage<T> {

    private static final long serialVersionUID = 7910598014919697934L;

    private int qtime;//查询时间
    private List<Count> counts;//分组列表

    public SolrPage() {

    }

    public SolrPage(int startIndex, int lastResult,SolrResult<T> items) {
        super(items.getDocs(), startIndex, lastResult, Integer.parseInt(items.getHits()+""));
        this.qtime = items.getQtime();
        this.counts = items.getCounts();
        // TODO Auto-generated constructor stub
    }


    public int getQtime() {
        return qtime;
    }
    public void setQtime(int qtime) {
        this.qtime = qtime;
    }
    public List<Count> getCounts() {
        return counts;
    }
    public void setCounts(List<Count> counts) {
        this.counts = counts;
    }

    @Override
	public String toString() {
		return "SolrPage [qtime=" + qtime + ", counts=" + counts
				+ ", getAllResults()="
				+ getAllResults() + ", getFirstResult()=" + getFirstResult()
				+ ", getLastResult()=" + getLastResult() + ", getMaxResults()="
				+ getMaxResults() + ", getList()=" + getList()
				+ ", getNextPage()=" + getNextPage() + ", getPrevPage()="
				+ getPrevPage() + ", getCurrPage()=" + getCurrPage()
				+ ", getLastPage()=" + getLastPage() + ", getPages()="
				+ getPages() + ", toString()=" + super.toString()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ "]";
	}


}
