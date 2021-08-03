//package com.example.alipay.util;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.alipay.api.AlipayApiException;
//import com.alipay.api.AlipayClient;
//import com.alipay.api.domain.*;
//import com.alipay.api.request.AlipayFundTransUniTransferRequest;
//import com.alipay.api.request.AlipayTradeAppPayRequest;
//import com.alipay.api.request.AlipayTradeQueryRequest;
//import com.alipay.api.response.AlipayFundTransUniTransferResponse;
//import com.alipay.api.response.AlipayTradeAppPayResponse;
//import com.alipay.api.response.AlipayTradeQueryResponse;
//import com.example.alipay.entity.BizContentForUniTransfer;
//import com.example.alipay.entity.TransferParams;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//
//@Slf4j
//@Service
//public class AliPayUtils {
//
//    @Autowired
//    @Qualifier("alipayClient")
//    private AlipayClient alipayClient;
//
//    private static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";
//
//    /**
//     * 交易查询接口
//     * @param model
//     * @return
//     * @throws Exception
//     */
//    public boolean isTradeQuery(AlipayTradeQueryModel model) throws AlipayApiException {
//        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
//        request.setBizModel(model);
//        AlipayTradeQueryResponse alipayTradeQueryResponse = alipayClient.certificateExecute(request);
//        if(alipayTradeQueryResponse.isSuccess()){
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    /**
//     * app支付
//     * @param model
//     * @param notifyUrl
//     * @return
//     * @throws AlipayApiException
//     */
//    public String startAppPay(AlipayTradeAppPayModel model, String notifyUrl)  throws AlipayApiException {
//        AlipayTradeAppPayRequest aliPayRequest = new AlipayTradeAppPayRequest();
//        model.setProductCode("QUICK_MSECURITY_PAY");
//        aliPayRequest.setNotifyUrl(notifyUrl);
//        aliPayRequest.setBizModel(model);
//        // 这里和普通的接口调用不同，使用的是sdkExecute
//        AlipayTradeAppPayResponse aliResponse = alipayClient.sdkExecute(aliPayRequest);
//        return aliResponse.getBody();
//    }
//    /**
//     * 转账接口
//     *
//     * @param transferParams
//     * @return AlipayFundTransToaccountTransferResponse
//     */
//     public AlipayFundTransUniTransferResponse doTransferNew(TransferParams transferParams) throws Exception {
//
//        String title = (StringUtils.isNotBlank(transferParams.getRemark()) ? transferParams
//                .getRemark() : "转账");
//        //转账请求入参
//        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
//        //转账参数
//        BizContentForUniTransfer bizContent = new BizContentForUniTransfer();
//        bizContent.setOut_biz_no(transferParams.getOutBizNo());
//        bizContent.setTrans_amount(changeF2Y(Math.abs(Integer.parseInt(transferParams.getAmount()))));
//        bizContent.setProduct_code("TRANS_ACCOUNT_NO_PWD");
//        bizContent.setBiz_scene("DIRECT_TRANSFER");
//        bizContent.setOrder_title(title);
//        Participant participant = new Participant();
//        participant.setIdentity(transferParams.getPayeeAccount());
//        participant.setIdentityType(transferParams.getPayeeType());
//        participant.setName((StringUtils.isNotBlank(transferParams.getPayeeRealName()) ? transferParams
//                .getPayeeRealName() : StringUtils.EMPTY));
//        bizContent.setPayee_info(participant);
//        bizContent.setRemark(title);
//
//        request.setBizContent(JSON.toJSONString(bizContent));
//
//        //转账请求返回
//        AlipayFundTransUniTransferResponse response = null;
//        try {
//            response = alipayClient.certificateExecute(request);
//        } catch (Exception e) {
//
//            log.info("doTransfer exception，异常信息：{}", e.toString());
//
//            log.info("doTransfer exception，支付宝返回信息：{}", JSONObject.toJSONString(response));
//
//        }
//
//        log.info("doTransfer,AlipayFundTransUniTransferResponse:{}", JSONObject.toJSONString(response));
//
//        return response;
//    }
//
//    private BigDecimal changeF2Y(Integer amount) throws Exception{
//        if(!amount.toString().matches(CURRENCY_FEN_REGEX)) {
//            throw new Exception("金额格式有误");
//        }
//        return BigDecimal.valueOf(amount).divide(new BigDecimal(100));
//    }
//}