package cn.suse.service;

import java.util.List;

import cn.suse.common.Condition;
import cn.suse.common.Page;

public interface CommonsService<T> {
	
	public int insert(T entity);
	
	public int delete(Long id);
	
	public int update(T entity);
	
	public T selectById(Long id);
	
	public List<T> selectListByCondition(Condition condition);
	
	public Page<T> selectPageByCondition(Page<T> condition);
}
