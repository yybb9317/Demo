package com.example.springboot01.utility.thirdpart.wxpay;

import com.example.springboot01.exception.TException;
import com.example.springboot01.response.ResponseEnum;
import com.example.springboot01.utility.thirdpart.wxpay.model.UnifiedOrderModel;
import com.example.springboot01.utility.thirdpart.wxpay.response.UnifiedOrderResponse;
import com.example.springboot01.utility.thirdpart.wxpay.response.WxPayAsyncNotify;
import com.example.springboot01.utility.thirdpart.wxpay.response.WxResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

/**
 * weixin pay helper for any kind of payment request
 */
@Slf4j
@UtilityClass
public final class WxHelper {

  /**
   * call weixin pay unified order
   *
   * @return prepay id
   */
  public static String unifiedorder(@Nonnull UnifiedOrderModel model) {
    log.info("WX PAY: try send unified order:\n {}", model);

    UnifiedOrderResponse response;
    try {
      response = new UnifiedOrderResponse(WxPay.init().unifiedOrder(model.toMap()));
    } catch (Exception e) {
      throw TException.of(ResponseEnum.WXPAY_EXP).with(e);
    }
    checkFail(response);

    log.info("WX PAY: get response:\n {}", response);
    return response.getPrepayId();
  }

  // TODO
//  public static String orderQuery(@Nonnull OrderQueryModel model) {
//    log.info("WX PAY: order query:\n{}", model);
//
//    OrderQueryResponse response;
//    try {
//      response = new OrderQueryResponse(WxPay.init().orderQuery(model.toMap()));
//    } catch (Exception e) {
//      throw TException.of(ResponseEnum.WXPAY_EXP).with(e);
//    }
//    checkFail(response);
//
//    log.info("WX PAY: response:\n{}", response);
//    String state = response.getTradeState();
//    return "SUCCESS".equals(state) ? response.getTransactionId() : null;
//  }

  public static void close() {

  }

  /**
   * @description:  微信支付回调
   * @Author Bob
   * @date 2019/8/6 15:56
   */
  public static WxPayAsyncNotify notifyUrl(HttpServletRequest request) {
    log.info("微信支付回调--------------------");

    WxPayAsyncNotify notify;
    try {
      Map<String, String> map = parseRequest(request);
      // check sign valid
      boolean valid = WxPay.init().isPayResultNotifySignatureValid(map);
      if (!valid) {
        log.info("微信支付回调签名校验失败!");
        return null;
      }
      notify = new WxPayAsyncNotify(map);
    } catch (Exception e) {
      log.info("微信支付回调异常", e);
      return null;
    }

    checkFail(notify);

    log.info("微信支付回调内容:\n{}", notify);
    return notify;
  }

//  public static String refund(@Nonnull RefundModel model) {
//    log.info("WX PAY: order refund:\n{}", model);
//
//    RefundResponse response;
//    try {
//      response = new RefundResponse(WxPay.init()
//          .refund(model.toMap()));
//    } catch (Exception e) {
//      throw TException.of(ResponseEnum.WXPAY_EXP).with(e);
//    }
//    checkFail(response);
//
//    log.info("WX PAY: response:\n{}", response);
//
//    return response.getRefundId();
//  }

//  public static boolean refundQuery(@Nonnull RefundQueryModel model) {
//    log.info("微信退款查询---------------------");
//
//    RefundQueryResponse response;
//    try {
//      response = new RefundQueryResponse(WxPay.init().refundQuery(model.toMap()));
//    } catch (Exception e) {
//      throw TException.of(ResponseEnum.WXPAY_EXP).with(e);
//    }
//    checkFail(response);
//
//    log.info("WX PAY: response:\n{}", response);
//
//    // TODO ???
//    for (Entry<String, String> entry : response.getResultMap().entrySet()) {
//      if (entry.getKey().startsWith("refund_status_") && "SUCCESS".equals(entry.getValue())) {
//        return true;
//      }
//    }
//    return false;
//  }

//  public static WxRefundAsyncNotify refundNotify(HttpServletRequest request) {
//    log.info("微信退款回调--------------------");
//
//    WxRefundAsyncNotify notify;
//    try {
//      Map<String, String> map = parseRequest(request);
//      // check sign valid
//      boolean valid = WxPay.init().isPayResultNotifySignatureValid(map);
//      if (!valid) {
//        log.info("微信退款回调签名校验失败!");
//        return null;
//      }
//      notify = new WxRefundAsyncNotify(map);
//    } catch (Exception e) {
//      log.info("微信退款回调异常", e);
//      return null;
//    }
//
//    checkFail(notify);
//
//    log.info("微信退款回调内容:\n{}", notify);
//    return notify;
//  }

  private static Map<String, String> parseRequest(HttpServletRequest request) throws Exception {
    try (InputStream in = request.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      // read request
      byte[] bytes = new byte[1024];
      int read;
      while ((read = in.read(bytes)) != -1) {
        out.write(bytes, 0, read);
      }
      // transfer to map
      Map<String, String> map = WxUtil.xmlToMap(out.toString("UTF-8"));
      log.info("微信回调参数:\n{}", map);
      return map;
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * check if weixin failed
   */
  private static void checkFail(WxResponse response) {
    String error = response.fail();
    if (error != null) {
      throw TException.of(ResponseEnum.WXPAY_FAIL).with(error);
    }
  }

}
