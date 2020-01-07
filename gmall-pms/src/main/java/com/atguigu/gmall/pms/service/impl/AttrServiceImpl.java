package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.Vo.AttrVo;
import com.atguigu.gmall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gmall.pms.entity.AttrAttrgroupRelationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.pms.dao.AttrDao;
import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.gmall.pms.service.AttrService;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Autowired
    private AttrAttrgroupRelationDao relationDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo queryAttrByCidOrType(Integer cid, Long type, QueryCondition queryCondition) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        if (type != null) {
            queryWrapper.eq("attr_type", type);
        }
        queryWrapper.eq("catelog_id", cid);

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(queryCondition), queryWrapper);


        return new PageVo(page);
    }

    @Override
    public void saveAttrVo(AttrVo attrVo) {
        //新增规格参数
        this.save(attrVo);
        Long attrId = attrVo.getAttrId();
        //新增中间表
        AttrAttrgroupRelationEntity relation = new AttrAttrgroupRelationEntity();
        relation.setAttrId(attrVo.getAttrId());
        relation.setAttrGroupId(attrVo.getAttrGroupId());
        this.relationDao.insert(relation);
    }

}