package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.Vo.SpuInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.pms.entity.SpuInfoEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * spu信息
 *
 * @author xpf
 * @email lxf@atguigu.com
 * @date 2019-12-31 13:40:39
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageVo queryPage(QueryCondition params);

    PageVo querySpuInfo(QueryCondition queryCondition, Long catId);

    void bigSave(SpuInfoVo spuInfoVo);
}


