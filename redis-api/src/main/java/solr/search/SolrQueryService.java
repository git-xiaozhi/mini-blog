package solr.search;

import com.xiaozhi.blog.utils.SolrPage;



public interface SolrQueryService<T> {

    /**
     * 分页查询
     * @param keyword 查询关键字
     * @param page 第几页
     * @param pageSize 每页个数
     * @return
     * @throws Exception
     */
    public SolrPage<T> query(String keyword,int page,int pageSize,boolean isFacet) throws Exception;

    /**
     * 高亮关键字方法
     * @param keyword
     * @param page 第几页
     * @param pageSize
     * @return
     * @throws Exception
     */
    public SolrPage<T> queryHightLighting(String keyword,int page,int pageSize,boolean isFacet) throws Exception;




    /**
     * 分页查询
     * @param keyword 查询关键字
     * @param page 第几页
     * @param pageSize 每页个数
     * @return
     * @throws Exception
     */
    public SolrPage<T> query(String uid,String keyword,int page,int pageSize,boolean isFacet) throws Exception;

    /**
     * 高亮关键字方法
     * @param keyword
     * @param page 第几页
     * @param pageSize
     * @return
     * @throws Exception
     */
    public SolrPage<T> queryHightLighting(String uid,String keyword,int page,int pageSize,boolean isFacet) throws Exception;

}