package cn.suse.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import cn.suse.service.impl.DictionaryService;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
	
	@Autowired
	private DictionaryService dictionaryService;
	
	private LoadingCache<Integer,List<Map<String,String>>> dictionaryCache
			//CacheBuilder的构造函数是私有的，只能通过其静态方法newBuilder()来获得CacheBuilder的实例
			= CacheBuilder.newBuilder()
			//设置并发级别为8，并发级别是指可以同时写缓存的线程数
			.concurrencyLevel(8)
			//设置写缓存后8秒钟过期
			.expireAfterWrite(1, TimeUnit.HOURS)
			//设置缓存容器的初始容量为4
			.initialCapacity(4)
			//设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
			.maximumSize(100)
			//使用弱引用存储键。当没有(强或软)引用到该键时，相应的缓存项将可以被垃圾回收。由于垃圾回收是依赖==进行判断，因此这样会导致整个缓存也会使用==来比较键的相等性，而不是使用equals()；
			.weakKeys()
			//使用弱引用存储缓存值。当没有(强或软)引用到该缓存项时，将可以被垃圾回收。由于垃圾回收是依赖==进行判断，因此这样会导致整个缓存也会使用==来比较缓存值的相等性，而不是使用equals()；
			.weakValues()
			//设置要统计缓存的命中率
			.recordStats()
			//build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
			.build(
					new CacheLoader<Integer,List<Map<String,String>>>() {
						@Override
						public List<Map<String,String>> load(Integer key) throws Exception {
							System.out.println("load dictionary " + key);
							List<Map<String,String>> list = dictionaryService.getList(key);
							return list;
						}
					}
			);
	@RequestMapping("dictionary/{id}")
	public String dictionary(@PathVariable Integer id){
		
		List<Map<String, String>> list = null;
		try {
			list = dictionaryCache.get(id);
		} catch (ExecutionException e) {
			e.printStackTrace();
			log.warn("find dictionary data fail by id:{}", id, e);
		}
		return JSON.toJSONString(list);
	}
}
