package com.imooc.o2o.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.service.PersonInfoService;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {

	@Autowired
	private PersonInfoService personInfoService;

	@Override
	public PersonInfo getPersonInfoById(Long userId) {
		return personInfoService.getPersonInfoById(userId);
	}

}
