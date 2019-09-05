package cn.suse.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

import cn.suse.common.Condition;
import cn.suse.common.Page;
import cn.suse.dao.GoodsDao;
import cn.suse.entity.Goods;
import cn.suse.service.GoodsService;
@Service
public class GoodsServiceImpl implements GoodsService {

	
	@Autowired
	private GoodsDao goodsDao;
	
	@Override
	public int insert(Goods entity) {
		return goodsDao.insert(entity);
	}

	@Override
	public int delete(Long id) {
		return goodsDao.delete(id);
	}

	@Override
	public int update(Goods entity) {
		return goodsDao.update(entity);
	}

	@Override
	public Goods selectById(Long id) {
		return goodsDao.selectById(id);
	}

	@Override
	public List<Goods> selectListByCondition(Condition condition) {
		return goodsDao.selectByCondition(condition);
	}

	@Override
	public Page<Goods> selectPageByCondition(Page<Goods> page) {
		
		PageHelper.startPage(page.getPage(), page.getLimit());
        List<Goods> list = goodsDao.selectByCondition(page.getCondition());
        page.setData(list);
        com.github.pagehelper.Page<Object> localPage = PageHelper.getLocalPage();
        page.setTotals(localPage.getTotal());
        page.setPages(localPage.getPages());
        return page;
	}
	
}
