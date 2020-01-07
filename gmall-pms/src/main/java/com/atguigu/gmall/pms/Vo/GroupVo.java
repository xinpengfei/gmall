package com.atguigu.gmall.pms.Vo;

import com.atguigu.gmall.pms.controller.AttrGroupController;
import com.atguigu.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

/**
 * @author:xpf
 * @date: 2020/1/3 23:11
 */
@Data
public class GroupVo extends AttrGroupEntity {
    private List<AttrEntity> attrEntities;
    private List<AttrAttrgroupRelationEntity> relations;
}
