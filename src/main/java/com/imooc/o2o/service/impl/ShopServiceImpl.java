package com.imooc.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ShopAuthMapDao;
import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService {
	@Autowired
	private ShopDao shopDao;
	@Autowired
	private ShopAuthMapDao shopAuthMapDao;
	private final static Logger LOG = LoggerFactory.getLogger(ShopServiceImpl.class);

	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		int count = shopDao.queryShopCount(shopCondition);
		ShopExecution se = new ShopExecution();
		if (shopList != null) {
			se.setShopList(shopList);
			se.setCount(count);
		} else {
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}

	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, ImageHolder thumbnail) {
		if (shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		// 插入店铺数据
		try {
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			int affectNum = shopDao.insertShop(shop);
			if (affectNum < 1) {
				throw new ShopOperationException("店铺创建失败");
			} else {
				if (thumbnail.getImage() != null) {
					// 插入店铺图片
					try {
						insertImg(shop, thumbnail);
					} catch (Exception e) {
						throw new ShopOperationException("addShopImg error: " + e.getMessage());
					}
					// 更新店铺图片地址
					affectNum = shopDao.updateShop(shop);
					if (affectNum < 1) {
						throw new ShopOperationException("更新图片地址失败");
					}
					// 执行增加shopAuthMap操作
					ShopAuthMap shopAuthMap = new ShopAuthMap();
					shopAuthMap.setEmployee(shop.getOwner());
					shopAuthMap.setShop(shop);
					shopAuthMap.setTitle("店家");
					shopAuthMap.setTitleFlag(0);
					shopAuthMap.setCreateTime(new Date());
					shopAuthMap.setLastEditTime(new Date());
					shopAuthMap.setEnableStatus(1);
					try {
						affectNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
						if (affectNum <= 0) {
							LOG.error("addShop:授权创建失败");
							throw new ShopOperationException("授权创建失败");
						}
					} catch (Exception e) {
						LOG.error("insertShopAuthMap error: " + e.getMessage());
						throw new ShopOperationException("授权创建失败");
					}
				}
			}
		} catch (Exception e) {
			throw new ShopOperationException("Add shop error" + e.getMessage());
		}
		return new ShopExecution(ShopStateEnum.CHECK, shop);
	}

	private void insertImg(Shop shop, ImageHolder thumbnail) {
		// 获取图片相对路径
		String relativePath = PathUtil.getShopImagePath(shop.getShopId());
		// 存入图片
		String destPath = ImageUtil.generateThumbnail(thumbnail, relativePath);
		// 更新shop里的图片地址(绝对值)
		shop.setShopImg(destPath);
	}

	@Override
	public Shop getByShopId(long shopId) {
		return shopDao.queryByShopId(shopId);
	}

	@Override
	public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException {
		if (shop == null || shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		try {
			// 是否修改过图片
			if (thumbnail.getImage() != null) {
				Shop tempShop = shopDao.queryByShopId(shop.getShopId());
				if (tempShop.getShopImg() != null && thumbnail.getImageName() != null) {
					ImageUtil.deleteFileOrPath(tempShop.getShopImg());
				}
				insertImg(shop, thumbnail);
			}
			// 更新店铺信息
			shop.setLastEditTime(new Date());
			int effectedNum = shopDao.updateShop(shop);
			if (effectedNum < 0) {
				return new ShopExecution(ShopStateEnum.INNER_ERROR);
			} else {
				shop = shopDao.queryByShopId(shop.getShopId());
				return new ShopExecution(ShopStateEnum.SUCCESS, shop);
			}
		} catch (Exception e) {
			throw new ShopOperationException("modifyShop error: " + e.getMessage());
		}

	}

}
