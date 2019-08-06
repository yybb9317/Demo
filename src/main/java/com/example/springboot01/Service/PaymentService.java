package com.example.springboot01.Service;

import com.example.springboot01.constant.AccountConst;
import com.example.springboot01.constant.thirdpart.WxPayConst;
import com.example.springboot01.exception.TException;
import com.example.springboot01.response.ResponseEnum;
import com.example.springboot01.utility.thirdpart.ApiHashMap;
import com.example.springboot01.utility.thirdpart.wxpay.WxHelper;
import com.example.springboot01.utility.thirdpart.wxpay.WxUtil;
import com.example.springboot01.utility.thirdpart.wxpay.model.UnifiedOrderModel;
import com.example.springboot01.utility.thirdpart.wxpay.response.WxPayAsyncNotify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * @title:  支付、提现
 * @author: Yuanbo
 * @date: 2019/8/6  14:17
 */
@Service
@Slf4j
public class PaymentService {

    /**
     * @description:  微信支付统一下单
     * @Author Bob
     * @date 2019/8/6 14:18
     */
    @Transactional
    public Object createAppOrder(String uid,String orderId,String ip) {
        log.info("UID:{} try to create prepay for system order ID:{},from IP:{}",uid,orderId,ip);

        // TODO
//        PointOrder order = pointOrderMapper.selectById(orderId);
//        if (order == null)
//            throw TException.badRequest("订单不存在！");
//        if (order.getState().compareTo(PointOrderState.UNPAID) != 0) {
//            throw TException.of(ResponseEnum.ORDER_PAID_CANCEL);
//        }
//        String openId = stringRedis.get(RedisConst.openid(uid));
//        if (Strings.isNullOrEmpty(openId))
//            throw TException.of(ResponseEnum.WXPAY_AUTH_NEEDED);
//        // create new pre pay
//        Prepay prepay = new Prepay(TUtil.uuid(), uid, TUtil.uuid(), null,
//                orderId, ip, order.getMoney(), "积分充值",
//                LocalDateTime.now(), null,null);
//        log.info("Prepay Info:{}",prepay);
//        UnifiedOrderModel model = new UnifiedOrderModel(prepay.getSubject(),
//                String.format(WxPayConst.NOTIFY_URL,prepay.getId()),prepay.getOrderNo(),prepay.getIp(),
//                prepay.getTotal().intValue(),"JSAPI",openId);
//        String prepayId = WxHelper.unifiedorder(model);
//        log.info("WX returned prepayId:{}",prepayId);
        ApiHashMap map = new ApiHashMap();
//        map.put("appId", AccountConst.CONFIG.wxAppId);
//        map.put("signType", WxPayConst.SignType.MD5);
//        map.put("package", "prepay_id=" + prepayId);
//        map.put("nonceStr", WxUtil.generateNonceStr());
//        map.put("timeStamp", System.currentTimeMillis() / 1000);
//        try {
//            map.put("paySign", WxUtil.generateSignature(map, AccountConst.CONFIG.wxSecretKey, WxPayConst.SignType.MD5));
//        } catch (Exception e) {
//            throw TException.of(ResponseEnum.WXPAY_EXP).with(e);
//        }
//        // create new pre pay
//        prepayMapper.insertSelective(prepay);
//
//        log.info("wx pay app response:\n{}", map);
        return map;
    }

    /**
     * @description:  微信支付回调
     * @Author Bob
     * @date 2019/8/6 15:40
     */
//    @Transactional
//    public String notifyAppOrder(String prePayId, HttpServletRequest request) {
//        log.info("开始回调 ID 为 {} 的预支付", prePayId);
//
//        // check if successed
//        Prepay prepay = prepayMapper.selectById(prePayId);
//        if (prepay == null) {
//            // prepay table would not clear, any invalid id is impossible
//            return "FAIL";
//        }
//        if (prepay.getFinishedOn() != null) {
//            return WxPayConst.NOTIFY_SUCCESS;
//        }
//        // request sign check & trade success check
//
//        WxPayAsyncNotify notify = WxHelper.notifyUrl(request);
//        if (notify == null || !prepay.getOrderNo().equals(notify.getOutTradeNo())) {
//            return String.format(WxPayConst.NOTIFY_FAIL, "Request parse fail or payment fail");
//        } else {
//            // weixin would return
//            prepay.setTradeNo(notify.getTransactionId());
//        }
//
//        //mark success to db.
//        prepaySuccess(prepay);
//
//        return  WxPayConst.NOTIFY_SUCCESS;
//    }

    /**
     * @description:  回调支付成功后对数据处理
     * @Author Bob
     * @date 2019/8/6 16:05
     */
//    @Transactional
//    public void prepaySuccess(Prepay prepay) {
//        // find order, order would never be null!
//        PointOrder order = pointOrderMapper.selectById(prepay.getOrderId());
////         check price, state
//        if (order.getMoney().equals(prepay.getTotal())
//                && order.getState() == PointOrderState.UNPAID) {
//            PointOrder pointOrder = PointOrder.builder().id(order.getId())
//                    .state(PointOrderState.COMPLETED).completeOn(LocalDateTime.now()).build();
//            pointOrderMapper.updateByIdSelective(pointOrder);
//            //plus integral
//            pointService.insertPoint(order.getUserId(), PointType.CHARGE,order.getPoint());
//        }
//
//        // update prepay
//        prepay.setFinishedOn(LocalDateTime.now());
//        //TODO need to set no used prepay to closed or del?
//        prepayMapper.updateByIdSelective(prepay);
//    }
}
