package com.example.springboot01.utility.thirdpart.wxpay.model;

import com.example.springboot01.utility.thirdpart.ApiHashMap;
import com.example.springboot01.utility.thirdpart.wxpay.WxUtil;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UnifiedOrderModel implements WxModel {

  /**
   * required,128, APP——需传入应用市场上的APP名字-实际商品名称，e.g.天天爱消除-游戏充值
   */
  private String body;
  /**
   * <pre>
   * required,256,接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数
   * e.g. http://www.weixin.qq.com/wxpay/pay.php
   */
  private String notifyUrl;
  /**
   * required,32, 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一
   */
  private String outTradeNo;
  /**
   * required,16, 用户端实际ip
   */
  private String spbillCreateIp;
  /**
   * required, 订单总金额，单位为分
   */
  private Integer totalFee;
  /**
   * required,16, 支付类型 e.g.APP
   */
  private String tradeType;
  /**
   * 8192,商品详细描述
   */
  private String detail;
  /**
   * 32,附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据, e.g.深圳分店
   */
  private String attach;
  /**
   * 14,订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
   */
  private LocalDateTime timeStart;
  /**
   * <pre>
   * 14,订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。
   * 订单失效时间是针对订单号而言的，由于在请求支付的时候有一个必传参数prepay_id只有两小时的有效期，
   * 所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id
   */
  private LocalDateTime timeExpire;
  /**
   * 32,指定支付方式 e.g.no_credit--指定不能使用信用卡支付
   */
  private String limitPay;

  /**
   * trade_type=JSAPI时（即公众号支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识。
   */
  private String openid;

  public UnifiedOrderModel(String body, String notifyUrl, String outTradeNo,
                           String spbillCreateIp, Integer totalFee, String tradeType, String openid) {
    this.body = body;
    this.notifyUrl = notifyUrl;
    this.outTradeNo = outTradeNo;
    this.spbillCreateIp = spbillCreateIp;
    this.totalFee = totalFee;
    this.tradeType = tradeType;
    this.openid = openid;
  }
  public UnifiedOrderModel(String body, String notifyUrl, String outTradeNo,
                           String spbillCreateIp, Integer totalFee, String tradeType) {
    this.body = body;
    this.notifyUrl = notifyUrl;
    this.outTradeNo = outTradeNo;
    this.spbillCreateIp = spbillCreateIp;
    this.totalFee = totalFee;
    this.tradeType = tradeType;
  }

  public UnifiedOrderModel(String body, String notifyUrl, String outTradeNo,
                           String spbillCreateIp, Integer totalFee) {
    this.body = body;
    this.notifyUrl = notifyUrl;
    this.outTradeNo = outTradeNo;
    this.spbillCreateIp = spbillCreateIp;
    this.totalFee = totalFee;
    this.tradeType = "APP";
  }

  public ApiHashMap toMap() {
    ApiHashMap map = new ApiHashMap();
    map.put("body", body);
    map.put("detail", detail);
    map.put("attach", attach);
    map.put("out_trade_no", outTradeNo);
    map.put("total_fee", totalFee);
    map.put("spbill_create_ip", spbillCreateIp);
    if (timeStart != null) {
      map.put("time_start", WxUtil.DTFORMAT.format(timeStart));
    }
    if (timeExpire != null) {
      map.put("time_expire", WxUtil.DTFORMAT.format(timeExpire));
    }
    if (openid != null) {
      map.put("openid", openid);
    }
    map.put("notify_url", notifyUrl);
    map.put("trade_type", tradeType);
    map.put("limit_pay", limitPay);
    return map;
  }

}
