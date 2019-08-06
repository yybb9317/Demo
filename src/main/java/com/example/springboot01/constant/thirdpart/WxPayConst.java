package com.example.springboot01.constant.thirdpart;

import com.example.springboot01.constant.AccountConst;
import com.example.springboot01.constant.TConst;
import lombok.Data;
import lombok.experimental.UtilityClass;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@UtilityClass
public final class WxPayConst {

  public static final int CONNECT_TIMEOUT = 3000;
  public static final int READ_TIMEOUT = 15000;

  /**
   * 商户外网可以访问的异步地址
   */
  public static final String NOTIFY_URL = TConst.THIRD_PARTY_CALLBACK + "/wxpay/app/order/%s";
  public static final String NOTIFY_URL_REFUND =
      TConst.THIRD_PARTY_CALLBACK + "/wxpay/app/refund/%s";

  public static final String NOTIFY_SUCCESS = "<xml><return_code>"
      + "<![CDATA[SUCCESS]]></return_code><return_msg>"
      + "<![CDATA[OK]]></return_msg></xml>";
  /**
   * weixin pay norify fail, MUST call with String.format()
   */
  public static final String NOTIFY_FAIL = "<xml><return_code>"
      + "<![CDATA[FAIL]]></return_code><return_msg>"
      + "<![CDATA[%s]]></return_msg></xml>";

  public static final String FAIL = "FAIL";
  public static final String SUCCESS = "SUCCESS";
  public static final String HMACSHA256 = "HMAC-SHA256";
  public static final String MD5 = "MD5";
  public static final String FIELD_SIGN = "sign";
  public static final String FIELD_SIGN_TYPE = "sign_type";
  public static final String MICROPAY_URL = "https://api.mch.weixin.qq.com/pay/micropay";
  public static final String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
  public static final String ORDERQUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
  public static final String REVERSE_URL = "https://api.mch.weixin.qq.com/secapi/pay/reverse";
  public static final String CLOSEORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
  public static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
  public static final String REFUNDQUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
  public static final String DOWNLOADBILL_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
  public static final String REPORT_URL = "https://api.mch.weixin.qq.com/payitil/report";
  public static final String SHORTURL_URL = "https://api.mch.weixin.qq.com/tools/shorturl";
  public static final String AUTHCODETOOPENID_URL = "https://api.mch.weixin.qq.com/tools/authcodetoopenid";
  public static final String ENTERPRISE_PAYMENT_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
  public static final String QUERY_ENTERPRISE_PAYMENT_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo";



  // sandbox
  public static final String SANDBOX_MICROPAY_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/micropay";
  public static final String SANDBOX_UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder";
  public static final String SANDBOX_ORDERQUERY_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/orderquery";
  public static final String SANDBOX_REVERSE_URL = "https://api.mch.weixin.qq.com/sandboxnew/secapi/pay/reverse";
  public static final String SANDBOX_CLOSEORDER_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/closeorder";
  public static final String SANDBOX_REFUND_URL = "https://api.mch.weixin.qq.com/sandboxnew/secapi/pay/refund";
  public static final String SANDBOX_REFUNDQUERY_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/refundquery";
  public static final String SANDBOX_DOWNLOADBILL_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/downloadbill";
  public static final String SANDBOX_REPORT_URL = "https://api.mch.weixin.qq.com/sandboxnew/payitil/report";
  public static final String SANDBOX_SHORTURL_URL = "https://api.mch.weixin.qq.com/sandboxnew/tools/shorturl";
  public static final String SANDBOX_AUTHCODETOOPENID_URL = "https://api.mch.weixin.qq.com/sandboxnew/tools/authcodetoopenid";

  public enum SignType {
    MD5, HMACSHA256
  }

  @Data
  public static class WXPayConfig {

    /**
     * 获取 App ID
     *
     * @return App ID
     */
    public String getAppID() {
      return AccountConst.CONFIG.wxAppId;
    }


    /**
     * 获取 Mch ID
     *
     * @return Mch ID
     */
    public String getMchID() {
      return AccountConst.CONFIG.wxMchId;
    }


    /**
     * 获取 API 密钥
     *
     * @return API密钥
     */
    public String getKey() {
      return AccountConst.CONFIG.wxSecretKey;
    }


    /**
     * 获取商户证书内容
     *
     * @return 商户证书内容
     */
    public InputStream getCertStream() throws FileNotFoundException {
      return new FileInputStream(AccountConst.CONFIG.wxCertPath);
    }

    /**
     * HTTP(S) 连接超时时间，单位毫秒
     */
    public int getHttpConnectTimeoutMs() {
      return CONNECT_TIMEOUT;
    }

    /**
     * HTTP(S) 读数据超时时间，单位毫秒
     */
    public int getHttpReadTimeoutMs() {
      return READ_TIMEOUT;
    }
  }

}
