package com.imooc.o2o.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.imooc.o2o.entity.HeadLine;

public interface HeadLineService {

	public static final String HLLISTKEY = "headLineList";

	List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException;

}
