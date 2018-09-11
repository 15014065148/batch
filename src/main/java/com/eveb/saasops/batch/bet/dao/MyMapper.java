package com.eveb.saasops.batch.bet.dao;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface MyMapper<T> extends  MySqlMapper<T>,Mapper<T> {

}