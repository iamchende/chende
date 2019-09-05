package cn.suse.common;

import java.util.List;

import lombok.Data;

@Data
public class Page<T> {
	
	private Integer page;
	
	private Integer start;
	
	private Integer limit;
	
	private Long totals;
	
	private Integer pages;
	
	private List<T> data;
	
	private Condition condition;
}
