package cn.suse.entity;

import java.util.Date;

import lombok.Data;

/**
 * 销售记录
 * 类OrderRecord.java的实现描述：TODO 类实现描述 
 * @author chende 2018年9月30日 上午11:53:54
 */
@Data
public class OrderRecord {
	
	private Long id;
	private Long goodsId;
	private Long purchaseId;
	//货物名称
	private String name;
	//类型
	private Integer type;
	//材质
	private String material;
	//颜色列表:red
	private String color;
	//尺码列表:s
	private String size;
	//实际销售标价
	private Double price;
	//销售时间
	private Date sellTime;
	//毛利润
	private Double grossProfit;
	//收款方式
	private Integer paymentMethod;
	//备注
	private String remark;
}
