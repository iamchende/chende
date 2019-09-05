package cn.suse.entity;

import java.util.Date;

import lombok.Data;
/**
 * 描述库存
 * 类Goods.java的实现描述：TODO 类实现描述 
 * @author chende 2018年9月30日 上午11:50:10
 */
@Data
public class Goods {
	
	private Long id;
	//货物名称
	private String name;
	//进货id
	private Long purchaseId;
	//颜色
	private String color;
	//尺码
	private String size;
	//类型
	private Integer type;
	//材质
	private String material;
	//销售标价
	private Double sellingPrice;
	//具体品类库存数:14（如红色，s号剩余14）
	private Integer simpleSurplus;
	//具体品类残次品数:1
	private Integer imperfections;
	//库存数:99
	private Integer surplus;
	//最近一次的销售时间
	private Date lastSellTime;
	//备注
	private String remark;
}
