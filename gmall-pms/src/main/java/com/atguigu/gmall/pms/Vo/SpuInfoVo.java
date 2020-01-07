package com.atguigu.gmall.pms.Vo;

import com.atguigu.gmall.pms.entity.SpuInfoEntity;
import lombok.Data;

import java.util.List;

/**
 * @author:xpf
 * @date: 2020/1/5 20:54
 */
@Data
public class SpuInfoVo extends SpuInfoEntity {
    private List<String> spuImages;//spu的描述信息（图片）
    private List<BaseAttrVo> baseAttrs;//通用的规格参数
    private List<SkuInfoVo> skus;//

}
