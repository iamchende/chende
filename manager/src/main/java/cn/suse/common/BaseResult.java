package cn.suse.common;

import java.util.List;

import lombok.Data;
@Data
public class BaseResult<T> {
	
	public final static Integer SUCCESS = 0;
	
	private Integer code;
	
	private String msg;
	
	private Long count;
	
	private List<T> data;
}
