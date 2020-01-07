package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.Vo.AttrVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 商品属性
 *
 * @author xpf
 * @email lxf@atguigu.com
 * @date 2019-12-31 13:40:39
 */
public interface AttrService extends IService<AttrEntity> {

    PageVo queryPage(QueryCondition params);


    PageVo queryAttrByCidOrType(Integer cid, Long type, QueryCondition queryCondition);

    void saveAttrVo(AttrVo attrVo);
}

