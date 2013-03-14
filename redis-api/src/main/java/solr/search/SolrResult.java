package solr.search;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.response.FacetField.Count;

/**
 * @author xiaozhi
 * @version 2010-7-20 下午01:00:55
 */

public class SolrResult <T>  implements Serializable{


    private int qtime;//查询时间
    private List<Count> counts;//分组列表
    private List<T> docs;//查询结果
    private Map<String,Map<String,List<String>>> highlightMap;//高亮字段
    private long hits;//总命中数

    public SolrResult(int qtime, List<Count> counts, List<T> docs,
            Map<String, Map<String, List<String>>> highlightMap, long hits) {
        super();
        this.qtime = qtime;
        this.counts = counts;
        this.docs = docs;
        this.highlightMap = highlightMap;
        this.hits = hits;
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

    public List<T> getDocs() {
        return docs;
    }

    public void setDocs(List<T> docs) {
        this.docs = docs;
    }

    public Map<String, Map<String, List<String>>> getHighlightMap() {
        return highlightMap;
    }

    public void setHighlightMap(Map<String, Map<String, List<String>>> highlightMap) {
        this.highlightMap = highlightMap;
    }

    public long getHits() {
        return hits;
    }

    public void setHits(long hits) {
        this.hits = hits;
    }


    @Override
    public String toString() {
        return "SolrResult [qtime=" + qtime + ", counts=" + counts + ", docs="
                + docs + ", highlightMap=" + highlightMap + ", hits=" + hits
                + "]";
    }

}