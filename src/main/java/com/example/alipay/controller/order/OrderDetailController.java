package com.example.alipay.controller.order;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.alipay.entity.KssOrderDetail;
import com.example.alipay.service.KssOrderDetailService;
import com.example.alipay.vo.PayVo;
import com.example.alipay.vo.R;
import lombok.var;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author wanxinabo
 * @date 2021/8/2
 */
@RestController
@RequestMapping(value = "/api/order")
public class OrderDetailController {

    private final KssOrderDetailService kssOrderDetailService;

    public OrderDetailController(KssOrderDetailService kssOrderDetailService) {
        this.kssOrderDetailService = kssOrderDetailService;
    }

    @PostMapping(value = "/detail/callback", consumes = MediaType.APPLICATION_JSON_VALUE)
    public R listenerCallback(@RequestBody PayVo payVo) {
        var queryWrapper = Wrappers.<KssOrderDetail>lambdaQuery().eq(KssOrderDetail::getCourseid, payVo.getCourseid())
                .eq(KssOrderDetail::getUserid, payVo.getUserId());
        int count = kssOrderDetailService.count(queryWrapper);
        return count > 0 ?R.ok():R.error();
    }

    @GetMapping(value = "/listOrders/transaction")
    public R listOrdersTransaction(String userId, Long courseId) {
        var queryWrapper = Wrappers.<KssOrderDetail>lambdaQuery().eq(KssOrderDetail::getCourseid, courseId)
                .eq(KssOrderDetail::getUserid, userId);
        List<KssOrderDetail> list = kssOrderDetailService.list(queryWrapper);
        if (!CollectionUtils.isEmpty(list)) {
            Map<String, Object> dataMap = list.stream().collect(Collectors.toMap(KssOrderDetail::getOrdernumber, Function.identity()));
            return R.ok().data(dataMap);
        }
        return R.ok();
    }
}
