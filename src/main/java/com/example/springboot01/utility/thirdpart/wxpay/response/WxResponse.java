package com.example.springboot01.utility.thirdpart.wxpay.response;

import lombok.Getter;

import java.util.Map;

/**
 * @description:  微信支付基础返回参数模板
 * @Author Bob
 * @date 2019/8/6 15:54
 */
@Getter
public abstract class WxResponse {

  public static final String SUCCESS = "SUCCESS";
  public static final String FAIL = "FAIL";

  /**
   * SUCCESS/FAIL
   *
   * 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
   */
  private String returnCode;

  /**
   * 返回信息，如非空，为错误原因
   */
  private String returnMsg;

  /**
   * 调用接口提交的应用ID e.g.wx8888888888888888
   */
  private String appid;
  /**
   * 调用接口提交的商户号 e.g.1900000109
   */
  private String mchId;
  /**
   * 调用接口提交的终端设备号 e.g.013467007045764
   */
  private String deviceInfo;
  /**
   * 微信返回的随机字符串
   */
  private String nonceStr;
  /**
   * 微信返回的签名
   */
  private String sign;
  /**
   * SUCCESS/FAIL
   */
  private String resultCode;
  /**
   * 详细参见第6节错误列表
   */
  private String errCode;
  /**
   * 错误返回的信息描述 e.g.系统错误
   */
  private String errCodeDes;

  /**
   * check response map
   *
   * @return true if return success
   */
  protected boolean check(Map<String, String> map) {
    returnCode = map.get("return_code");
    returnMsg = map.get("return_msg");
    if (!SUCCESS.equals(returnCode)) {
      return false;
    }
    appid = map.get("appid");
    mchId = map.get("mch_id");
    deviceInfo = map.get("device_info");
    nonceStr = map.get("nonce_str");
    sign = map.get("sign");
    resultCode = map.get("result_code");
    errCode = map.get("err_code");
    errCodeDes = map.get("err_code_des");
    return SUCCESS.equals(resultCode);
  }

  /**
   * check if request or business failed
   */
  public String fail() {
    if (!SUCCESS.equals(returnCode)) {
      return returnMsg == null ? FAIL : returnMsg;
    }
    if (!SUCCESS.equals(getResultCode())) {
      return errCode + " - " + errCodeDes;
    }
    return null;
  }

}
