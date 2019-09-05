package cn.suse.dao;

import java.util.List;

import cn.suse.common.Condition;
public interface CommonsDao<T> {
	
	public int insert(T entity);
	
	public int delete(Long id);
	
	public int update(T entity);
	
	public T selectById(Long id);
	
	public List<T> selectByCondition(Condition condition);
}
