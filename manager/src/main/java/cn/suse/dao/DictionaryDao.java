package cn.suse.dao;

import java.util.List;
import java.util.Map;

public interface DictionaryDao{
	
	public List<Map<String,String>> getList(String tableName);
}
