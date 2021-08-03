package com.example.alipay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.example.alipay.config.AliPayBean;
import com.example.alipay.entity.KssCourses;
import com.example.alipay.qrcode.QRCodeUtil;
import com.example.alipay.qrcode.QrCodeResponse;
import com.example.alipay.qrcode.QrResponse;
import com.example.alipay.service.AliPayService;
import com.example.alipay.service.KssCoursesService;
import com.example.alipay.util.GenerateNum;
import com.example.alipay.vo.PayVo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Objects;

/**
 * <p>
 *
 * </p>
 *
 * @author wanxinabo
 * @date 2021/8/2
 */
@Service
public class AliPayServiceImpl implements AliPayService {

    private final KssCoursesService kssCoursesService;

    private final AlipayClient alipayClient;
    private final AliPayBean aliPayBean;

    public AliPayServiceImpl(KssCoursesService kssCoursesService, @Qualifier("alipayClient") AlipayClient alipayClient, AliPayBean aliPayBean) {
        this.kssCoursesService = kssCoursesService;
        this.alipayClient = alipayClient;
        this.aliPayBean = aliPayBean;
    }

    @Override
    public byte[] alipay(PayVo payVo) {
        // 业务数据
        KssCourses course = kssCoursesService.getById(payVo.getCourseid());
        if (Objects.isNull(course)) return null;
        // 获取金额
        String price = course.getPrice().toString();
        String title = course.getTitle();

        // 4: 支付的订单编号
        String orderNumber = GenerateNum.generateOrder();

        // 5：支付宝携带的参数在回调中可以通过request获取 回调带的参数
        JSONObject json = new JSONObject();
        json.put("userId", payVo.getUserId());
        json.put("orderNumber", orderNumber);
        json.put("money", price);
        json.put("ptype","productcourse");// 支付类型
        json.put("courseId",payVo.getCourseid());
        String params = json.toString();

        // 支付信息的参数
        // 6：设置支付相关的信息
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setOutTradeNo(orderNumber);
        model.setTotalAmount(price);
        model.setSubject(title);
        model.setBody(params);
        model.setTimeoutExpress("30m");
        model.setQrCodeTimeoutExpress("20m");
        model.setStoreId(payVo.getUserId());
        QrCodeResponse qrCodeResponse = qrcodePay(model);

        try {
            //7 二维码合成
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            String logopath = "";
            try {
                logopath = ResourceUtils.getFile("classpath:favicon.png").getAbsolutePath();
            } catch (Exception ex) {
                logopath = new java.io.File("/www/web/favicon.png").getAbsolutePath();
            }

            //获取二维码
            BufferedImage buffImg = QRCodeUtil.encode(qrCodeResponse.getQr_code(), logopath, false);
            ImageOutputStream imageOut = ImageIO.createImageOutputStream(output);
            ImageIO.write(buffImg, "JPEG", imageOut);
            imageOut.close();
            ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
            return FileCopyUtils.copyToByteArray(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private QrCodeResponse qrcodePay(AlipayTradePrecreateModel model) {
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setBizModel(model);
        request.setNotifyUrl(aliPayBean.getNotifyUrl());
//        request.setReturnUrl(aliPayBean.getReturnUrl());

        AlipayTradePrecreateResponse response = null;

        try {
            response = alipayClient.certificateExecute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        QrResponse qrResponse = JSONObject.parseObject(response.getBody(), QrResponse.class);

        return qrResponse.getAlipay_trade_precreate_response();
    }
}
