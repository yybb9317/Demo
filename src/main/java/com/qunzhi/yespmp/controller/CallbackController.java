package com.qunzhi.yespmp.controller;

import com.qunzhi.yespmp.Service.PaymentService;
import com.qunzhi.yespmp.constant.thirdpart.WxPayConst;
import com.qunzhi.yespmp.utility.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @title:    回调接口
 * @author: Yuanbo
 * @date: 2019/8/6  15:31
 */
@Slf4j
@RestController
@RequestMapping("/callback")
public class CallbackController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/wxpay/{prePayId}")
    public String wxPayAppOrder(
            @PathVariable String prePayId,    //  预付订单表的id
            HttpServletRequest request) {
        log.info("【微信回调】:{}, ip:{}", prePayId, IpUtil.getIp(request));

        if (prePayId.length() != 32) {
            return String.format(WxPayConst.NOTIFY_FAIL, "Invalid url");
        }
        try {
//            return paymentService.notifyAppOrder(prePayId, request);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return String.format(WxPayConst.NOTIFY_FAIL, "Exception");
        }
    }
}
