package com.example.alipay.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.example.alipay.config.AliPayBean;
import com.example.alipay.service.AliPayService;
import com.example.alipay.service.impl.PayCommonService;
import com.example.alipay.util.AlipayCore;
import com.example.alipay.vo.PayVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;


@Controller
@Log4j2
public class AlipayController {

    @Autowired
    private AliPayService alipayService;
    @Autowired
    private PayCommonService payCommonService;
    @Autowired
    private AliPayBean aliPayBean;

    @GetMapping("/alipay/pay")
    @ResponseBody
    public byte[] alipay(String courseid){
        PayVo payVo = new PayVo();
        payVo.setCourseid(courseid);
        payVo.setUserId("1");
        return alipayService.alipay(payVo);
    }


    /**
     * 定义支付回调的地址
     *
     * 1：第一步：打成一个jar 发布正式服务器
     * 2：第二步：购买一个域名：https:/www.kuangstudy.com/
     * 3：第三步：部署的项目到服务器上。java -jar ksd-alipay.jar >>1.txt &
     * 4：第四步：获取真实的回调地址  https:/www.kuangstudy.com/alipay/notifyUrl?body=&param&
     */
    @ResponseBody
    @RequestMapping("/alipay/notifyUrl")
    public String notify_url(HttpServletRequest request) throws Exception {
        // 1: 调用支付回调
        boolean result = alipayCallback(request);
        if (result) {
            return "success"; // 请不要修改或删除
        } else {
            // 验证失败
            return "fail";
        }
    }


    private boolean alipayCallback(HttpServletRequest request) throws UnsupportedEncodingException {
        // 1:获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Map.Entry<String, String[]> entity : requestParams.entrySet()) {
            String[] values = entity.getValue();
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            // 乱码使用
//            valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            params.put(entity.getKey(), valueStr);
        }

        // 进行回调参数验签
        try {
            String serverPath = Objects.requireNonNull(this.getClass().getResource("/")).getPath();
            AlipaySignature.certVerifyV1(params, serverPath + aliPayBean.getAlipayCertPath(), "UTF-8", aliPayBean.getSignType());
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return false;
        }
        //2：计算得出通知验证结果
        log.info("1：---->获取支付宝回传的参数---------------------------------->" + params);
        // 返回公共参数
        String tradeno = params.get("trade_no");
        //支付返回的请求参数body
        String bodyJson = params.get("body");
        log.info("3---->:【支付宝】交易的参数信息是：{}，流水号是：{}", bodyJson, tradeno);
        try {
            JSONObject bodyJsonObject = JSONObject.parseObject(bodyJson);
            String userId = bodyJsonObject.getString("userId");
            String ptype = bodyJsonObject.getString("ptype");
            String orderNumber = bodyJsonObject.getString("orderNumber");
            log.info("4---->:【支付宝】交易的参数信息是：ptype:{}，orderNumber是：{}",  ptype,orderNumber);
            // 课程支付成功，保存的订单交易明细
            if(ptype!=null && ptype.equalsIgnoreCase("productcourse")){
                payCommonService.payproductcourse(bodyJsonObject,"1",orderNumber,tradeno,"1");
            }

            if(ptype!=null && ptype.equalsIgnoreCase("svip")){
                payCommonService.payuserSvip(bodyJsonObject,"1",orderNumber,tradeno,"1");
            }

            if(ptype!=null && ptype.equalsIgnoreCase("award")){
                payCommonService.awardPay(bodyJsonObject,"1",orderNumber,tradeno,"1");
            }

        } catch (Exception ex) {
            log.info("支付宝支付出现了异常.....");
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}
