package solr.index;

import java.util.List;

public interface SolrjIndexCreater<T> {

	/**
	 * 批量增加/更新索引
	 * @param itmes
	 */
	public void addOrUpdateBeans(List<T> itmes);
	
	
	/**
	 * 单个增加或者更新索引
	 * @param item
	 */
	public void addOrUpdateBean(T item);
	
	/**
	 * 索引优化
	 */
	public void optimize();

	
	/**
	 * 根据索引主键删除一个索引
	 * @param id
	 */
	public void deleteDocById(String id);

	/**
	 * 清空所有索引
	 */
	public void clear();

}