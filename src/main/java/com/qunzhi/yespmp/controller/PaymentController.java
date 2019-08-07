package com.qunzhi.yespmp.controller;

import com.qunzhi.yespmp.Service.PaymentService;
import com.qunzhi.yespmp.response.TResponse;
import com.qunzhi.yespmp.utility.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @title:   支付提现接口
 * @author: Yuanbo
 * @date: 2019/8/6  14:10
 */
@Slf4j
@RestController
@RequestMapping("/user/{uid}/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    /**
     * @description:  微信支付统一下单(预付)
     * @Author Bob
     * @date 2019/8/6 15:12
     */
    @PostMapping("/wxpay")
    public ResponseEntity createWxPayAppOrder(
            @PathVariable String uid,
            @RequestParam String orderId,
            HttpServletRequest request
    ) {
        String ip = IpUtil.getIp(request);
        return TResponse.success(paymentService.createAppOrder(uid,orderId, ip));
    }
}
