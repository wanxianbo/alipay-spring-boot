package com.example.alipay.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PayVo implements Serializable {
    // 支付用户
    private String userId;
    // 支付产品id
    private String courseid;
}
