package com.eveb.saasops.batch.bet.dao;

import com.eveb.saasops.batch.bet.entity.OprActActivity;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.IdsMapper;


@Mapper
public interface OprActActivityMapper extends MyMapper<OprActActivity>, IdsMapper<OprActActivity> {

}
