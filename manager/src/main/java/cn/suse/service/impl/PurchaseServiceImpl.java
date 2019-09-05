package cn.suse.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.suse.common.Condition;
import cn.suse.common.Page;
import cn.suse.dao.PurchaseDao;
import cn.suse.entity.Purchase;
import cn.suse.service.PurchaseService;
@Service
public class PurchaseServiceImpl implements PurchaseService {

	
	@Autowired
	private PurchaseDao purchaseDao;
	
	@Override
	public int insert(Purchase entity) {
		return purchaseDao.insert(entity);
	}

	@Override
	public int delete(Long id) {
		return purchaseDao.delete(id);
	}

	@Override
	public int update(Purchase entity) {
		return purchaseDao.update(entity);
	}

	@Override
	public Purchase selectById(Long id) {
		return purchaseDao.selectById(id);
	}

	@Override
	public List<Purchase> selectListByCondition(Condition condition) {
		return purchaseDao.selectByCondition(condition);
	}

	@Override
	public Page<Purchase> selectPageByCondition(Page<Purchase> page) {
		
		PageHelper.startPage(page.getPage(), page.getLimit());
        List<Purchase> list = purchaseDao.selectByCondition(page.getCondition());
        PageInfo<Purchase> pageInfo = new PageInfo<>(list);
        page.setData(list);
        page.setTotals(pageInfo.getTotal());
        page.setPages(pageInfo.getPages());
        return page;
	}
	
}
