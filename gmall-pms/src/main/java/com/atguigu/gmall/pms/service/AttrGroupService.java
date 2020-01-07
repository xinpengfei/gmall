package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.Vo.GroupVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;

import java.util.List;


/**
 * 属性分组
 *
 * @author xpf
 * @email lxf@atguigu.com
 * @date 2019-12-31 13:40:39
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageVo queryPage(QueryCondition params);

    PageVo queryGroupByCid(QueryCondition queryCondition, Long catId);

    GroupVo queryByGroupId(Long gid);

    List<GroupVo> queryByCid(Long catId);
}

