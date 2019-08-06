package com.example.springboot01.utility.thirdpart.wxpay.model;

import com.example.springboot01.utility.thirdpart.ApiHashMap;

/**
 * @title:   weixin pay model
 * @author: Yuanbo
 * @date: 2019/8/6  14:32
 */
public interface WxModel {

//  /**
//   * 微信开放平台审核通过的应用APPID, Util内置
//   */
//  private String appid;
//  /**
//   * 微信支付分配的商户号, Util内置
//   */
//  private String mchId;
//  /**
//   * 随机字符串，不长于32位, Util内置
//   */
//  private String nonceStr;
//  /**
//   * 签名, Util内置
//   */
//  private String sign;

    ApiHashMap toMap();

}
