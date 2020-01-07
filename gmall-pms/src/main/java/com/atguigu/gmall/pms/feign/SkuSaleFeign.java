package com.atguigu.gmall.pms.feign;

import com.atguigu.gmall.sms.feign.GmallSmsApi;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * @author:xpf
 * @date: 2020/1/6 18:17
 */
@FeignClient("sms-service")
public interface SkuSaleFeign extends GmallSmsApi {

}
