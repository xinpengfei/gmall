package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.Vo.BaseAttrVo;
import com.atguigu.gmall.pms.Vo.SkuInfoVo;
import com.atguigu.gmall.pms.Vo.SpuInfoVo;
import com.atguigu.gmall.pms.dao.AttrDao;
import com.atguigu.gmall.pms.dao.SkuInfoDao;
import com.atguigu.gmall.pms.dao.SpuInfoDescDao;

import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.feign.SkuSaleFeign;
import com.atguigu.gmall.pms.service.ProductAttrValueService;
import com.atguigu.gmall.pms.service.SkuImagesService;
import com.atguigu.gmall.pms.service.SkuSaleAttrValueService;
import com.atguigu.gmall.sms.vo.SkuSaleVo;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.pms.dao.SpuInfoDao;

import com.atguigu.gmall.pms.service.SpuInfoService;
import org.springframework.util.CollectionUtils;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Autowired
    private AttrDao attrDao;
    @Autowired
    private SkuImagesService skuImagesService;
    @Autowired
    private SpuInfoDescDao spuInfoDescDao;
    @Autowired
    private SkuInfoDao skuInfoDao;
    @Autowired
    private ProductAttrValueService productAttrValueService;
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    private SkuSaleFeign skuSaleFeign;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo querySpuInfo(QueryCondition queryCondition, Long catId) {
        IPage<SpuInfoEntity> page = new Query<SpuInfoEntity>().getPage(queryCondition);
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        if (catId != 0l) {
            wrapper.eq("catalog_id", wrapper);
        }
        String key = queryCondition.getKey();

        if (StringUtils.isNotBlank(key)) {
            wrapper.and(t -> t.like("spu_name", key).or().eq("id", key));

        }
        return new PageVo(page(page, wrapper));
    }

    @GlobalTransactional
    @Override
    public void bigSave(SpuInfoVo spuInfoVo) {

        //1.保存spu相关信息
        //1.1spuInfo
        spuInfo(spuInfoVo);

        Long spuId = spuInfoVo.getId();
        //1.2spuInfoDase
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuId);
        spuInfoDescEntity.setDecript(StringUtils.join(spuInfoVo.getSpuImages(), ","));
        this.spuInfoDescDao.insert(spuInfoDescEntity);

        //1.3基础属性相关信息
        List<BaseAttrVo> baseAttrs = spuInfoVo.getBaseAttrs();
        if (!CollectionUtils.isEmpty(baseAttrs)) {
            List<ProductAttrValueEntity> productAttrValueEntities = baseAttrs.stream().map(productAttrValueVO -> {
                productAttrValueVO.setSpuId(spuId);
                productAttrValueVO.setAttrSort(0);
                productAttrValueVO.setQuickShow(0);
                return productAttrValueVO;
            }).collect(Collectors.toList());
            this.productAttrValueService.saveBatch(productAttrValueEntities);

            //2.Sku相关信息
            List<SkuInfoVo> skuInfoVos = spuInfoVo.getSkus();
            if (CollectionUtils.isEmpty(skuInfoVos)) {
                return;
            }
            skuInfoVos.forEach(skuInfoVo -> {
                //2.1skuInfo基本信息
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(skuInfoVo, skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoVo.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoVo.getCatalogId());
                skuInfoEntity.setSkuCode(UUID.randomUUID().toString().substring(0, 10).toUpperCase());
                List<String> images = skuInfoVo.getImages();
                if (!CollectionUtils.isEmpty(images)) {
                    skuInfoEntity.setSkuDefaultImg(skuInfoEntity.getSkuDefaultImg() == null ?
                            images.get(0) : skuInfoEntity.getSkuDefaultImg());
                }
                skuInfoEntity.setSpuId(spuId);
                this.skuInfoDao.insert(skuInfoEntity);
                Long skuId = skuInfoEntity.getSkuId();
                //2.2skuInfoImages保存图片的基本信息
                if (!CollectionUtils.isEmpty(images)) {
                    String defaultImage = images.get(0);
                    List<SkuImagesEntity> skuImageses = images.stream().map(image -> {
                        SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                        skuImagesEntity.setDefaultImg(StringUtils.equals(defaultImage, image) ? 1 : 0);
                        skuImagesEntity.setSkuId(skuId);
                        skuImagesEntity.setImgSort(0);
                        skuImagesEntity.setImgUrl(image);
                        return skuImagesEntity;
                    }).collect(Collectors.toList());
                    this.skuImagesService.saveBatch(skuImageses);
                }

                //2.3skuSaleAttrValue销售规格参数
                List<SkuSaleAttrValueEntity> saleAttrs = skuInfoVo.getSaleAttrs();
                saleAttrs.forEach(saleAttr -> {
                    // 设置属性名，需要根据id查询AttrEntity
                    // saleAttr.setAttrName(this.attrDao.selectById(saleAttr.getAttrId()).getAttrName());
                    saleAttr.setAttrSort(0);
                    saleAttr.setSkuId(skuId);
                });
                this.skuSaleAttrValueService.saveBatch(saleAttrs);

                //3.营销相关信息
                SkuSaleVo skuSaleDto = new SkuSaleVo();
                BeanUtils.copyProperties(skuInfoVo, skuSaleDto);
                skuSaleDto.setSkuId(skuId);
                this.skuSaleFeign.saveSkuSaleInfo(skuSaleDto);


                //3.1skuBounds积分

                //3.2skuLadder打折

                //3.3FullRedution满减

            });

        }
    }

    private void spuInfo(SpuInfoVo spuInfoVo) {
        spuInfoVo.setPublishStatus(1);
        spuInfoVo.setUodateTime(new Date());
        spuInfoVo.setPublishStatus(spuInfoVo.getPublishStatus());
        this.save(spuInfoVo);
    }

}