package com.imooc.o2o.service;

import java.io.InputStream;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exceptions.ShopOperationException;

public interface ShopService {

	ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);

	ShopExecution addShop(Shop shop, ImageHolder thumbnail);

	Shop getByShopId(long shopId);

	ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException;
}
