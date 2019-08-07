package com.qunzhi.yespmp.utility.thirdpart.wxpay.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @description:  微信支付回调模型
 * @Author Bob
 * @date 2019/8/6 15:52
 */
@Data
@NoArgsConstructor
public class WxPayAsyncNotify extends WxResponse {

  /**
   * 用户标识
   */
  private String openid;
  /**
   * 是否关注公众账号, Y
   */
  private String isSubscribe;
  /**
   * 交易类型, APP
   */
  private String tradeType;
  /**
   * 付款银行
   */
  private String bankType;
  /**
   * 总金额,分,Int
   */
  private String totalFee;
  /**
   * 货币种类
   */
  private String feeType;
  /**
   * 现金支付金额,Int
   */
  private String cashFee;
  /**
   * 现金支付货币类型
   */
  private String cashFeeType;
  /**
   * 微信支付订单号
   */
  private String transactionId;
  /**
   * 商户订单号
   */
  private String outTradeNo;
  /**
   * 商家数据包
   */
  private String attach;
  /**
   * 支付完成时间,支付完成时间，格式为yyyyMMddHHmmss
   */
  private String timeEnd;

  public WxPayAsyncNotify(Map<String, String> map) {
    if (super.check(map)) {
      openid = map.get("openid");
      isSubscribe = map.get("is_subscribe");
      tradeType = map.get("trade_type");
      bankType = map.get("bank_type");
      totalFee = map.get("total_fee");
      feeType = map.get("fee_type");
      cashFee = map.get("cash_fee");
      cashFeeType = map.get("cash_fee_type");
      transactionId = map.get("transaction_id");
      outTradeNo = map.get("out_trade_no");
      attach = map.get("attach");
      timeEnd = map.get("time_end");
    }
  }

}
