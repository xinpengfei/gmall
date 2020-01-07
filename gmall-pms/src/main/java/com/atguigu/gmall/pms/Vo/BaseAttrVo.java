package com.atguigu.gmall.pms.Vo;

import com.alibaba.nacos.client.naming.utils.StringUtils;
import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author:xpf
 * @date: 2020/1/5 21:04
 */
@Data
public class BaseAttrVo extends ProductAttrValueEntity {
    private List<String> valueSelected;
    public void setValueSelected(List<String> valueSelected){
        // 如果接受的集合为空，则不设置
        if (CollectionUtils.isEmpty(valueSelected)){
            return;
        }
        this.setAttrValue(StringUtils.join(valueSelected, ","));

    }

}
