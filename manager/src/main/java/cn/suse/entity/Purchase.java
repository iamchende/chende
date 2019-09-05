package cn.suse.entity;

import java.util.Date;

import lombok.Data;

/**
 * 描述进货
 * 类Purchase.java的实现描述：TODO 类实现描述 
 * @author chende 2018年9月26日 上午10:39:19
 */
@Data
public class Purchase {
	
	private Long id;
	//货物名称
	private String name;
	//材质
	private String material;
	//进货总数:1000
	private Integer totals;
	//进货单价:58.5
	private Double buyingPrice;
	//进货总价:5850
	private Double totalBuyingPrice;
	//颜色列表:red,blue,black
	private String color;
	//尺码列表:s,m,l
	private String size;
	//类型
	private Integer type;
	//进货日期
	private Date importTime;
	//备注
	private String remark;
}
