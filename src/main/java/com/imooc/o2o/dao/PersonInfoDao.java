package com.imooc.o2o.dao;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.PersonInfo;

public interface PersonInfoDao {
	/**
	 * 通过用户Id查询用户
	 * 
	 * @param userId
	 * @return
	 */
	PersonInfo queryPersonInfoById(@Param("userId") long userId);

	/**
	 * 添加用户信息
	 * 
	 * @param personInfo
	 * @return
	 */
	int insertPersonInfo(PersonInfo personInfo);
}
