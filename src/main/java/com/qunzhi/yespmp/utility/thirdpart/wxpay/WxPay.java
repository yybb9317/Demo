package com.qunzhi.yespmp.utility.thirdpart.wxpay;

import com.qunzhi.yespmp.constant.thirdpart.WxPayConst;
import com.qunzhi.yespmp.constant.thirdpart.WxPayConst.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * weixin pay requestor
 */
public class WxPay {

  private WXPayConfig config = new WXPayConfig();
  private SignType signType;
  private boolean useSandbox;

  public WxPay() {
    this(SignType.MD5, false);
  }

  public WxPay(final SignType signType) {
    this(signType, false);
  }

  public WxPay(final SignType signType, final boolean useSandbox) {
    this.signType = signType;
    this.useSandbox = useSandbox;
  }

  public static WxPay init() {
    return new WxPay();
  }

  public static WxPay init(boolean useSandbox) {
    return new WxPay(SignType.MD5, useSandbox);
  }

  /**
   * 向 Map 中添加 appid、mch_id、nonce_str、sign_type、sign <br> 该函数适用于商户适用于统一下单等接口，不适用于红包、代金券接口
   */
  public Map<String, String> fillRequestData(Map<String, String> reqData) throws Exception {
    reqData.put("appid", config.getAppID());
    reqData.put("mch_id", config.getMchID());
    reqData.put("nonce_str", WxUtil.generateNonceStr());
    if (SignType.MD5.equals(this.signType)) {
      reqData.put("sign_type", WxPayConst.MD5);
    } else if (SignType.HMACSHA256.equals(this.signType)) {
      reqData.put("sign_type", WxPayConst.HMACSHA256);
    }
    System.out.println("===============\n" + reqData);
    reqData.put("sign", WxUtil.generateSignature(reqData, config.getKey(), this.signType));
    System.out.println("=================\n" + reqData.get("sign"));
    return reqData;
  }

  /**
   * 判断xml数据的sign是否有效，必须包含sign字段，否则返回false。
   *
   * @param reqData 向wxpay post的请求数据
   * @return 签名是否有效
   */
  public boolean isResponseSignatureValid(Map<String, String> reqData) throws Exception {
    // 返回数据的签名方式和请求中给定的签名方式是一致的
    return WxUtil.isSignatureValid(reqData, this.config.getKey(), this.signType);
  }

  /**
   * 判断支付结果通知中的sign是否有效
   *
   * @param reqData 向wxpay post的请求数据
   * @return 签名是否有效
   */
  public boolean isPayResultNotifySignatureValid(Map<String, String> reqData) throws Exception {
    String signTypeInData = reqData.get(WxPayConst.FIELD_SIGN_TYPE);
    SignType signType;
    if (signTypeInData == null) {
      signType = SignType.MD5;
    } else {
      signTypeInData = signTypeInData.trim();
      if (signTypeInData.length() == 0) {
        signType = SignType.MD5;
      } else if (WxPayConst.MD5.equals(signTypeInData)) {
        signType = SignType.MD5;
      } else if (WxPayConst.HMACSHA256.equals(signTypeInData)) {
        signType = SignType.HMACSHA256;
      } else {
        throw new Exception(String.format("Unsupported sign_type: %s", signTypeInData));
      }
    }
    return WxUtil.isSignatureValid(reqData, this.config.getKey(), signType);
  }



  /**
   * 不需要证书的请求
   *
   * @param strUrl String
   * @param reqData 向wxpay post的请求数据
   * @param connectTimeoutMs 超时时间，单位是毫秒
   * @param readTimeoutMs 超时时间，单位是毫秒
   * @return API返回数据
   */
  public String requestWithoutCert(String strUrl, Map<String, String> reqData,
      int connectTimeoutMs, int readTimeoutMs) throws Exception {
    String UTF8 = "UTF-8";
    String reqBody = WxUtil.mapToXml(reqData);
    URL httpUrl = new URL(strUrl);
    HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
    httpURLConnection.setDoOutput(true);
    httpURLConnection.setRequestMethod("POST");
    httpURLConnection.setConnectTimeout(connectTimeoutMs);
    httpURLConnection.setReadTimeout(readTimeoutMs);
    httpURLConnection.connect();
    OutputStream outputStream = httpURLConnection.getOutputStream();
    outputStream.write(reqBody.getBytes(UTF8));

    // if (httpURLConnection.getResponseCode()!= 200) {
    //     throw new Exception(String.format("HTTP response code is %d, not 200", httpURLConnection.getResponseCode()));
    // }

    //获取内容
    InputStream inputStream = httpURLConnection.getInputStream();
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UTF8));
    final StringBuffer stringBuffer = new StringBuffer();
    String line = null;
    while ((line = bufferedReader.readLine()) != null) {
      stringBuffer.append(line).append("\n");
    }
    String resp = stringBuffer.toString();
    if (stringBuffer != null) {
      try {
        bufferedReader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    if (inputStream != null) {
      try {
        inputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    if (outputStream != null) {
      try {
        outputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    // if (httpURLConnection!=null) {
    //     httpURLConnection.disconnect();
    // }

    return resp;
  }


  /**
   * 需要证书的请求
   *
   * @param strUrl String
   * @param reqData 向wxpay post的请求数据  Map
   * @param connectTimeoutMs 超时时间，单位是毫秒
   * @param readTimeoutMs 超时时间，单位是毫秒
   * @return API返回数据
   */
  public String requestWithCert(String strUrl, Map<String, String> reqData,
      int connectTimeoutMs, int readTimeoutMs) throws Exception {
    String UTF8 = "UTF-8";
    String reqBody = WxUtil.mapToXml(reqData);
    URL httpUrl = new URL(strUrl);
    char[] password = config.getMchID().toCharArray();
    InputStream certStream = config.getCertStream();
    KeyStore ks = KeyStore.getInstance("PKCS12");
    ks.load(certStream, password);

    // 实例化密钥库 & 初始化密钥工厂
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(ks, password);

    // 创建SSLContext
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

    HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();

    httpURLConnection.setDoOutput(true);
    httpURLConnection.setRequestMethod("POST");
    httpURLConnection.setConnectTimeout(connectTimeoutMs);
    httpURLConnection.setReadTimeout(readTimeoutMs);
    httpURLConnection.connect();
    OutputStream outputStream = httpURLConnection.getOutputStream();
    outputStream.write(reqBody.getBytes(UTF8));

    // if (httpURLConnection.getResponseCode()!= 200) {
    //     throw new Exception(String.format("HTTP response code is %d, not 200", httpURLConnection.getResponseCode()));
    // }

    //获取内容
    InputStream inputStream = httpURLConnection.getInputStream();
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UTF8));
    final StringBuffer stringBuffer = new StringBuffer();
    String line = null;
    while ((line = bufferedReader.readLine()) != null) {
      stringBuffer.append(line);
    }
    String resp = stringBuffer.toString();
    if (stringBuffer != null) {
      try {
        bufferedReader.close();
      } catch (IOException e) {
        // e.printStackTrace();
      }
    }
    if (inputStream != null) {
      try {
        inputStream.close();
      } catch (IOException e) {
        // e.printStackTrace();
      }
    }
    if (outputStream != null) {
      try {
        outputStream.close();
      } catch (IOException e) {
        // e.printStackTrace();
      }
    }
    if (certStream != null) {
      try {
        certStream.close();
      } catch (IOException e) {
        // e.printStackTrace();
      }
    }
    // if (httpURLConnection!=null) {
    //     httpURLConnection.disconnect();
    // }

    return resp;
  }

  /**
   * 处理 HTTPS API返回数据，转换成Map对象。return_code为SUCCESS时，验证签名。
   *
   * @param xmlStr API返回的XML格式数据
   * @return Map类型数据
   */
  public Map<String, String> processResponseXml(String xmlStr) throws Exception {
    String RETURN_CODE = "return_code";
    String return_code;
    Map<String, String> respData = WxUtil.xmlToMap(xmlStr);
    if (respData.containsKey(RETURN_CODE)) {
      return_code = respData.get(RETURN_CODE);
    } else {
      throw new Exception(String.format("No `return_code` in XML: %s", xmlStr));
    }

    if (return_code.equals(WxPayConst.FAIL)) {
      return respData;
    } else if (return_code.equals(WxPayConst.SUCCESS)) {
      if (this.isResponseSignatureValid(respData)) {
        return respData;
      } else {
        throw new Exception(String.format("Invalid sign value in XML: %s", xmlStr));
      }
    } else {
      throw new Exception(
          String.format("return_code value %s is invalid in XML: %s", return_code, xmlStr));
    }
  }

  /**
   * 作用：提交刷卡支付<br> 场景：刷卡支付
   *
   * @param reqData 向wxpay post的请求数据
   * @return API返回数据
   */
  public Map<String, String> microPay(Map<String, String> reqData) throws Exception {
    return this.microPay(reqData, this.config.getHttpConnectTimeoutMs(),
        this.config.getHttpReadTimeoutMs());
  }


  /**
   * 作用：提交刷卡支付<br> 场景：刷卡支付
   *
   * @param reqData 向wxpay post的请求数据
   * @param connectTimeoutMs 连接超时时间，单位是毫秒
   * @param readTimeoutMs 读超时时间，单位是毫秒
   * @return API返回数据
   */
  public Map<String, String> microPay(Map<String, String> reqData, int connectTimeoutMs,
      int readTimeoutMs) throws Exception {
    String url;
    if (this.useSandbox) {
      url = WxPayConst.SANDBOX_MICROPAY_URL;
    } else {
      url = WxPayConst.MICROPAY_URL;
    }
    String respXml = this
        .requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
    return this.processResponseXml(respXml);
  }


  /**
   * 作用：统一下单<br> 场景：公共号支付、扫码支付、APP支付
   *
   * @param reqData 向wxpay post的请求数据
   * @return API返回数据
   */
  public Map<String, String> unifiedOrder(Map<String, String> reqData) throws Exception {
    return this.unifiedOrder(reqData, config.getHttpConnectTimeoutMs(),
        this.config.getHttpReadTimeoutMs());
  }


  /**
   * 作用：统一下单<br> 场景：公共号支付、扫码支付、APP支付
   *
   * @param reqData 向wxpay post的请求数据
   * @param connectTimeoutMs 连接超时时间，单位是毫秒
   * @param readTimeoutMs 读超时时间，单位是毫秒
   * @return API返回数据
   */
  public Map<String, String> unifiedOrder(Map<String, String> reqData, int connectTimeoutMs,
      int readTimeoutMs) throws Exception {
    String url;
    if (this.useSandbox) {
      url = WxPayConst.SANDBOX_UNIFIEDORDER_URL;
    } else {
      url = WxPayConst.UNIFIEDORDER_URL;
    }
    String respXml = this
        .requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
    return this.processResponseXml(respXml);
  }


  /**
   * 企业付款下单接口
   *
   * @param reqData 向wxpay post的请求数据
   * @return API返回数据
   */
  public Map<String, String> paymentOrder(Map<String, String> reqData) throws Exception {
    return this.paymentOrder(reqData, config.getHttpConnectTimeoutMs(),
            this.config.getHttpReadTimeoutMs());
  }


  /**
   * 企业付款下单接口
   * @param reqData 向wxpay post的请求数据
   * @param connectTimeoutMs 连接超时时间，单位是毫秒
   * @param readTimeoutMs 读超时时间，单位是毫秒
   * @return API返回数据
   * @throws Exception
   */
  public Map<String,String> paymentOrder(Map<String,String> reqData, int connectTimeoutMs,
                                         int readTimeoutMs) throws Exception{
    String url;
    if (this.useSandbox) {
      url = WxPayConst.ENTERPRISE_PAYMENT_URL;//not do
    } else {
      url = WxPayConst.ENTERPRISE_PAYMENT_URL;
    }
    String respXml = this
            .requestWithCert(url, reqData, connectTimeoutMs, readTimeoutMs);
    return this.processResponseXml(respXml);
  }

  /**
   * 查询企业付款接口
   *
   * @param reqData 向wxpay post的请求数据
   * @return API返回数据
   */
  public Map<String, String> queryPaymentOrder(Map<String, String> reqData) throws Exception {
    return this.queryPaymentOrder(reqData, config.getHttpConnectTimeoutMs(),
            this.config.getHttpReadTimeoutMs());
  }

  /**
   * 查询企业付款接口
   * @param reqData 向wxpay post的请求数据
   * @param connectTimeoutMs 连接超时时间，单位是毫秒
   * @param readTimeoutMs 读超时时间，单位是毫秒
   * @return API返回数据
   * @throws Exception
   */
  public Map<String,String> queryPaymentOrder(Map<String,String> reqData, int connectTimeoutMs,
                                         int readTimeoutMs) throws Exception{
    String url;
    if (this.useSandbox) {
      url = WxPayConst.QUERY_ENTERPRISE_PAYMENT_URL;//not do
    } else {
      url = WxPayConst.QUERY_ENTERPRISE_PAYMENT_URL;
    }
    String respXml = this
            .requestWithCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
    return this.processResponseXml(respXml);
  }

  /**
   * 作用：查询订单<br> 场景：刷卡支付、公共号支付、扫码支付、APP支付
   *
   * @param reqData 向wxpay post的请求数据
   * @return API返回数据
   */
  public Map<String, String> orderQuery(Map<String, String> reqData) throws Exception {
    return this
        .orderQuery(reqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
  }


  /**
   * 作用：查询订单<br> 场景：刷卡支付、公共号支付、扫码支付、APP支付
   *
   * @param reqData 向wxpay post的请求数据 int
   * @param connectTimeoutMs 连接超时时间，单位是毫秒
   * @param readTimeoutMs 读超时时间，单位是毫秒
   * @return API返回数据
   */
  public Map<String, String> orderQuery(Map<String, String> reqData, int connectTimeoutMs,
      int readTimeoutMs) throws Exception {
    String url;
    if (this.useSandbox) {
      url = WxPayConst.SANDBOX_ORDERQUERY_URL;
    } else {
      url = WxPayConst.ORDERQUERY_URL;
    }
    String respXml = this
        .requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
    return this.processResponseXml(respXml);
  }


  /**
   * 作用：撤销订单<br> 场景：刷卡支付
   *
   * @param reqData 向wxpay post的请求数据
   * @return API返回数据
   */
  public Map<String, String> reverse(Map<String, String> reqData) throws Exception {
    return this
        .reverse(reqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
  }


  /**
   * 作用：撤销订单<br> 场景：刷卡支付<br> 其他：需要证书
   *
   * @param reqData 向wxpay post的请求数据
   * @param connectTimeoutMs 连接超时时间，单位是毫秒
   * @param readTimeoutMs 读超时时间，单位是毫秒
   * @return API返回数据
   */
  public Map<String, String> reverse(Map<String, String> reqData, int connectTimeoutMs,
      int readTimeoutMs) throws Exception {
    String url;
    if (this.useSandbox) {
      url = WxPayConst.SANDBOX_REVERSE_URL;
    } else {
      url = WxPayConst.REVERSE_URL;
    }
    String respXml = this
        .requestWithCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
    return this.processResponseXml(respXml);
  }


  /**
   * 作用：关闭订单<br> 场景：公共号支付、扫码支付、APP支付
   *
   * @param reqData 向wxpay post的请求数据
   * @return API返回数据
   */
  public Map<String, String> closeOrder(Map<String, String> reqData) throws Exception {
    return this
        .closeOrder(reqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
  }


  /**
   * 作用：关闭订单<br> 场景：公共号支付、扫码支付、APP支付
   *
   * @param reqData 向wxpay post的请求数据
   * @param connectTimeoutMs 连接超时时间，单位是毫秒
   * @param readTimeoutMs 读超时时间，单位是毫秒
   * @return API返回数据
   */
  public Map<String, String> closeOrder(Map<String, String> reqData, int connectTimeoutMs,
      int readTimeoutMs) throws Exception {
    String url;
    if (this.useSandbox) {
      url = WxPayConst.SANDBOX_CLOSEORDER_URL;
    } else {
      url = WxPayConst.CLOSEORDER_URL;
    }
    String respXml = this
        .requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
    return this.processResponseXml(respXml);
  }


  /**
   * 作用：申请退款<br> 场景：刷卡支付、公共号支付、扫码支付、APP支付
   *
   * @param reqData 向wxpay post的请求数据
   * @return API返回数据
   */
  public Map<String, String> refund(Map<String, String> reqData) throws Exception {
    return this
        .refund(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
  }


  /**
   * 作用：申请退款<br> 场景：刷卡支付、公共号支付、扫码支付、APP支付<br> 其他：需要证书
   *
   * @param reqData 向wxpay post的请求数据
   * @param connectTimeoutMs 连接超时时间，单位是毫秒
   * @param readTimeoutMs 读超时时间，单位是毫秒
   * @return API返回数据
   */
  public Map<String, String> refund(Map<String, String> reqData, int connectTimeoutMs,
      int readTimeoutMs) throws Exception {
    String url;
    if (this.useSandbox) {
      url = WxPayConst.SANDBOX_REFUND_URL;
    } else {
      url = WxPayConst.REFUND_URL;
    }
    String respXml = this
        .requestWithCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
    return this.processResponseXml(respXml);
  }


  /**
   * 作用：退款查询<br> 场景：刷卡支付、公共号支付、扫码支付、APP支付
   *
   * @param reqData 向wxpay post的请求数据
   * @return API返回数据
   */
  public Map<String, String> refundQuery(Map<String, String> reqData) throws Exception {
    return this.refundQuery(reqData, this.config.getHttpConnectTimeoutMs(),
        this.config.getHttpReadTimeoutMs());
  }


  /**
   * 作用：退款查询<br> 场景：刷卡支付、公共号支付、扫码支付、APP支付
   *
   * @param reqData 向wxpay post的请求数据
   * @param connectTimeoutMs 连接超时时间，单位是毫秒
   * @param readTimeoutMs 读超时时间，单位是毫秒
   * @return API返回数据
   */
  public Map<String, String> refundQuery(Map<String, String> reqData, int connectTimeoutMs,
      int readTimeoutMs) throws Exception {
    String url;
    if (this.useSandbox) {
      url = WxPayConst.SANDBOX_REFUNDQUERY_URL;
    } else {
      url = WxPayConst.REFUNDQUERY_URL;
    }
    String respXml = this
        .requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
    return this.processResponseXml(respXml);
  }


  /**
   * 作用：对账单下载（成功时返回对账单数据，失败时返回XML格式数据）<br> 场景：刷卡支付、公共号支付、扫码支付、APP支付
   *
   * @param reqData 向wxpay post的请求数据
   * @return API返回数据
   */
  public Map<String, String> downloadBill(Map<String, String> reqData) throws Exception {
    return this.downloadBill(reqData, this.config.getHttpConnectTimeoutMs(),
        this.config.getHttpReadTimeoutMs());
  }


  /**
   * 作用：对账单下载<br> 场景：刷卡支付、公共号支付、扫码支付、APP支付<br> 其他：无论是否成功都返回Map。若成功，返回的Map中含有return_code、return_msg、data，
   * 其中return_code为`SUCCESS`，data为对账单数据。
   *
   * @param reqData 向wxpay post的请求数据
   * @param connectTimeoutMs 连接超时时间，单位是毫秒
   * @param readTimeoutMs 读超时时间，单位是毫秒
   * @return 经过封装的API返回数据
   */
  public Map<String, String> downloadBill(Map<String, String> reqData, int connectTimeoutMs,
      int readTimeoutMs) throws Exception {
    String url;
    if (this.useSandbox) {
      url = WxPayConst.SANDBOX_DOWNLOADBILL_URL;
    } else {
      url = WxPayConst.DOWNLOADBILL_URL;
    }
    String respStr = this
        .requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs)
        .trim();
    Map<String, String> ret;
    // 出现错误，返回XML数据
    if (respStr.indexOf("<") == 0) {
      ret = WxUtil.xmlToMap(respStr);
    } else {
      // 正常返回csv数据
      ret = new HashMap<String, String>();
      ret.put("return_code", WxPayConst.SUCCESS);
      ret.put("return_msg", "ok");
      ret.put("data", respStr);
    }
    return ret;
  }


  /**
   * 作用：交易保障<br> 场景：刷卡支付、公共号支付、扫码支付、APP支付
   *
   * @param reqData 向wxpay post的请求数据
   * @return API返回数据
   */
  public Map<String, String> report(Map<String, String> reqData) throws Exception {
    return this
        .report(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
  }


  /**
   * 作用：交易保障<br> 场景：刷卡支付、公共号支付、扫码支付、APP支付
   *
   * @param reqData 向wxpay post的请求数据
   * @param connectTimeoutMs 连接超时时间，单位是毫秒
   * @param readTimeoutMs 读超时时间，单位是毫秒
   * @return API返回数据
   */
  public Map<String, String> report(Map<String, String> reqData, int connectTimeoutMs,
      int readTimeoutMs) throws Exception {
    String url;
    if (this.useSandbox) {
      url = WxPayConst.SANDBOX_REPORT_URL;
    } else {
      url = WxPayConst.REPORT_URL;
    }
    String respXml = this
        .requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
    return WxUtil.xmlToMap(respXml);
  }


  /**
   * 作用：转换短链接<br> 场景：刷卡支付、扫码支付
   *
   * @param reqData 向wxpay post的请求数据
   * @return API返回数据
   */
  public Map<String, String> shortUrl(Map<String, String> reqData) throws Exception {
    return this.shortUrl(reqData, this.config.getHttpConnectTimeoutMs(),
        this.config.getHttpReadTimeoutMs());
  }


  /**
   * 作用：转换短链接<br> 场景：刷卡支付、扫码支付
   *
   * @param reqData 向wxpay post的请求数据
   * @return API返回数据
   */
  public Map<String, String> shortUrl(Map<String, String> reqData, int connectTimeoutMs,
      int readTimeoutMs) throws Exception {
    String url;
    if (this.useSandbox) {
      url = WxPayConst.SANDBOX_SHORTURL_URL;
    } else {
      url = WxPayConst.SHORTURL_URL;
    }
    String respXml = this
        .requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
    return this.processResponseXml(respXml);
  }


  /**
   * 作用：授权码查询OPENID接口<br> 场景：刷卡支付
   *
   * @param reqData 向wxpay post的请求数据
   * @return API返回数据
   */
  public Map<String, String> authCodeToOpenid(Map<String, String> reqData) throws Exception {
    return this.authCodeToOpenid(reqData, this.config.getHttpConnectTimeoutMs(),
        this.config.getHttpReadTimeoutMs());
  }


  /**
   * 作用：授权码查询OPENID接口<br> 场景：刷卡支付
   *
   * @param reqData 向wxpay post的请求数据
   * @param connectTimeoutMs 连接超时时间，单位是毫秒
   * @param readTimeoutMs 读超时时间，单位是毫秒
   * @return API返回数据
   */
  public Map<String, String> authCodeToOpenid(Map<String, String> reqData, int connectTimeoutMs,
      int readTimeoutMs) throws Exception {
    String url;
    if (this.useSandbox) {
      url = WxPayConst.SANDBOX_AUTHCODETOOPENID_URL;
    } else {
      url = WxPayConst.AUTHCODETOOPENID_URL;
    }
    String respXml = this
        .requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
    return this.processResponseXml(respXml);
  }

}
