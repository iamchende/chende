package cn.suse.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.suse.dao.DictionaryDao;

/**
 * 字典查询通用接口
 * 1	color_type
 * 2	size_type
 * 3	clothing_type
 * 4	material_type
 * @author Administrator
 *
 */
@Service
public class DictionaryService {
	
	@Autowired
	private DictionaryDao dictionaryDao;
	
	public List<Map<String,String>> getList(Integer tableNum){
		String tableName = "";
		switch(tableNum){
			case 1:
				tableName="color_type";
				break;
			case 2:
				tableName="size_type";
				break;
			case 3:
				tableName="clothing_type";
				break;
			case 4:
				tableName="material_type";
				break;
			default:
				return new ArrayList<Map<String,String>>();
		}
		return dictionaryDao.getList(tableName);
	}
	
}
