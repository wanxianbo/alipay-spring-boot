package com.example.alipay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <p>
 *
 * </p>
 *
 * @author wanxinabo
 * @date 2021/8/2
 */
@Controller
public class IndexController {

    @GetMapping("/index")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/main")
    public String getMain() {
        return "main";
    }
}
