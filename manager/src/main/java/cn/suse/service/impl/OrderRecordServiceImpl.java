package cn.suse.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

import cn.suse.common.Condition;
import cn.suse.common.Page;
import cn.suse.dao.OrderRecordDao;
import cn.suse.entity.OrderRecord;
import cn.suse.service.OrderRecordService;
@Service
public class OrderRecordServiceImpl implements OrderRecordService {

	
	@Autowired
	private OrderRecordDao orderRecordDao;
	
	@Override
	public int insert(OrderRecord entity) {
		return orderRecordDao.insert(entity);
	}

	@Override
	public int delete(Long id) {
		return orderRecordDao.delete(id);
	}

	@Override
	public int update(OrderRecord entity) {
		return orderRecordDao.update(entity);
	}

	@Override
	public OrderRecord selectById(Long id) {
		return orderRecordDao.selectById(id);
	}

	@Override
	public List<OrderRecord> selectListByCondition(Condition condition) {
		return orderRecordDao.selectByCondition(condition);
	}

	@Override
	public Page<OrderRecord> selectPageByCondition(Page<OrderRecord> page) {
		
		PageHelper.startPage(page.getPage(), page.getLimit());
        List<OrderRecord> list = orderRecordDao.selectByCondition(page.getCondition());
        page.setData(list);
        com.github.pagehelper.Page<Object> localPage = PageHelper.getLocalPage();
        page.setTotals(localPage.getTotal());
        page.setPages(localPage.getPages());
        return page;
	}
	
}
