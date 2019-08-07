package com.qunzhi.yespmp.utility.thirdpart.wxpay.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @description:  微信统一下单回复参数模板
 * @Author Bob
 * @date 2019/8/6 15:54
 */
@Data
@NoArgsConstructor
public class UnifiedOrderResponse extends WxResponse {

  /**
   * 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP
   */
  private String tradeType;
  /**
   * 微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时 e.g.wx201410272009395522657a690389285100
   */
  private String prepayId;

  public UnifiedOrderResponse(Map<String, String> map) {
    if (super.check(map)) {
      tradeType = map.get("trade_type");
      prepayId = map.get("prepay_id");
    }
  }


}
