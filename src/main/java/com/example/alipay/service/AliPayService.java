package com.example.alipay.service;

import com.example.alipay.vo.PayVo;

/**
 * <p>
 *
 * </p>
 *
 * @author wanxinabo
 * @date 2021/8/2
 */
public interface AliPayService {
    /**
     * @return byte[]
     * @Author xuke
     * @Description 阿里支付接口
     * @Date 1:05 2020/9/9
     * @Param [payVo]
     **/
    byte[] alipay(PayVo payVo);
}
