package com.atguigu.gmall.oms.dao;

import com.atguigu.gmall.oms.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author xpf
 * @email lxf@atguigu.com
 * @date 2020-01-01 14:57:48
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
