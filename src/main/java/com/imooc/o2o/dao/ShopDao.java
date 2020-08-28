package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.Shop;

public interface ShopDao {
	/**
	 * 分页查询店铺，可输入条件有：店铺名（模糊），店铺状态，店铺类别，区域id，owner
	 * 
	 * @param shopCondition
	 * @param rowIndex      从第几行开始取数据
	 * @param pageSize      返回的条数
	 * @return
	 */
	List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);

	int queryShopCount(@Param("shopCondition") Shop shopCondition);

	Shop queryByShopId(long shopId);

	int insertShop(Shop shop);

	int updateShop(Shop shop);
}
