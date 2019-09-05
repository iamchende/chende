package cn.suse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import cn.suse.common.BaseResult;
import cn.suse.common.Page;
import cn.suse.entity.Purchase;
import cn.suse.service.PurchaseService;

@RestController
@RequestMapping("purchase")
public class PurchaseController {
	
	@Autowired
	private PurchaseService purchaseService;
	
	@RequestMapping(value="purchase",method=RequestMethod.GET)
	public String list(Page<Purchase> page){
		BaseResult<Purchase> result = new BaseResult<>();
		page.setPage(page.getPage() == null?0:(page.getPage()-1));
		Page<Purchase> selectPageByCondition = purchaseService.selectPageByCondition(page);
		result.setCode(BaseResult.SUCCESS);
		result.setData(selectPageByCondition.getData());
		result.setCount(selectPageByCondition.getTotals());
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value="/purchase",method=RequestMethod.PUT)
	public String add(){
		return null;
	}
	
	@RequestMapping(value="/purchase",method=RequestMethod.DELETE)
	public String delete(){
		return null;
	}
}
