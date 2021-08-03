package com.example.alipay.service.impl;
import com.alibaba.fastjson.JSONObject;
import com.example.alipay.entity.KssCourses;
import com.example.alipay.entity.KssOrderDetail;
import com.example.alipay.mapper.KssCoursesMapper;
import com.example.alipay.mapper.KssOrderDetailMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@Log4j2
public class PayCommonService {

    @Autowired
    private KssOrderDetailMapper orderDetailMapper;
    @Autowired
    private KssCoursesMapper productCourseMapper;

    /**
     * 支付课程回调封装
     * @param jsonObject
     * @param userId
     * @param orderNumber
     * @param transaction_id
     * @param paymethod
     */
    public void payproductcourse(JSONObject jsonObject, String userId, String orderNumber, String transaction_id, String paymethod) {
        // 支付的课程
        String courseId = jsonObject.getString("courseId");
        // 支付的金额
        String money = jsonObject.getString("money");
        // 根据产品查询产品信息
        KssCourses productCourse = productCourseMapper.selectById(courseId);
        if (productCourse == null) {
            log.info("【" + (paymethod.equals("2") ? "微信" : "支付宝") + "】你支付的课程被删除了：{}", courseId);
            return;
        }
        // 保存订单明细表
        KssOrderDetail orderDetail = new KssOrderDetail();
        orderDetail.setUserid(userId);
        orderDetail.setCourseid(courseId);
        orderDetail.setUsername("飞哥");
        orderDetail.setPaymethod(paymethod);
        orderDetail.setCoursetitle(productCourse.getTitle());
        orderDetail.setCourseimg(productCourse.getImg());
        orderDetail.setOrdernumber(orderNumber);
        orderDetail.setTradeno(transaction_id);
        orderDetail.setPrice(money == null ? "0.01" : money);
        orderDetailMapper.insert(orderDetail);
    }


    public void payuserSvip(JSONObject jsonObject, String userId, String orderNumber, String transaction_id, String paymethod) {

    }

    public void awardPay(JSONObject jsonObject, String userId, String orderNumber, String transaction_id, String paymethod) {

    }
}


