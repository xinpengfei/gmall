package com.atguigu.gmall.pms.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.pms.Vo.GroupVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import com.atguigu.gmall.pms.service.AttrGroupService;




/**
 * 属性分组
 *
 * @author xpf
 * @email lxf@atguigu.com
 * @date 2019-12-31 13:40:39
 */
@Api(tags = "属性分组 管理")
@RestController
@RequestMapping("pms/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @GetMapping("/withattrs/cat/{catId}")
    public Resp<List<GroupVo>> queryByCid(@PathVariable("catId")Long catId){

        List<GroupVo> attrGroupVOs = this.attrGroupService.queryByCid(catId);
        return Resp.ok(attrGroupVOs);
    }
    /**
     * 根据groupId查询关联关系
     */
    @GetMapping("/withattr/{gid}")
    public Resp<GroupVo> queryByGroupId(@PathVariable(value = "gid")Long gid){
        GroupVo groupVo=this.attrGroupService.queryByGroupId(gid);
        return Resp.ok(groupVo);
    }
    /**
     * 属性维护之属性分组
     */
    @GetMapping("{catId}")
    public Resp<PageVo> queryGroupByCid(QueryCondition queryCondition,@PathVariable("catId")Long catId){
        PageVo pageVo = this.attrGroupService.queryGroupByCid(queryCondition,catId);
        return Resp.ok(pageVo);
    }

    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('pms:attrgroup:list')")
    public Resp<PageVo> list(QueryCondition queryCondition) {
        PageVo page = attrGroupService.queryPage(queryCondition);

        return Resp.ok(page);
    }


    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{attrGroupId}")
    @PreAuthorize("hasAuthority('pms:attrgroup:info')")
    public Resp<AttrGroupEntity> info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        return Resp.ok(attrGroup);
    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('pms:attrgroup:save')")
    public Resp<Object> save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return Resp.ok(null);
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('pms:attrgroup:update')")
    public Resp<Object> update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return Resp.ok(null);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('pms:attrgroup:delete')")
    public Resp<Object> delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return Resp.ok(null);
    }

}
