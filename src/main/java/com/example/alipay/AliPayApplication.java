package com.example.alipay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 *
 * </p>
 *
 * @author wanxinabo
 * @date 2021/8/2
 */
@SpringBootApplication
@MapperScan({"com.example.alipay.**.mapper"})
public class AliPayApplication {
    public static void main(String[] args) {
        SpringApplication.run(AliPayApplication.class, args);
    }
}
