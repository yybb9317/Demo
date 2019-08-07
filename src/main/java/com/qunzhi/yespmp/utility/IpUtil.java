package com.qunzhi.yespmp.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class IpUtil {

  private static final String SINA_IP_URL = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=";

  public static String getIp(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (isInvalidIp(ip)) {
      ip = request.getHeader("x-real-ip");
      if (isInvalidIp(ip)) {
        ip = request.getHeader("Proxy-Client-IP");
        if (isInvalidIp(ip)) {
          ip = request.getHeader("WL-Proxy-Client-IP");
          if (isInvalidIp(ip)) {
            ip = request.getRemoteAddr();
          }
        }
      }
    }
    return ip.split(",")[0];
  }

  private static boolean isInvalidIp(String ip) {
    return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
  }

  /**
   * find china province via request
   *
   * @return empty if fail
   */
  public static String findChinaProvince(HttpServletRequest request) {
    return findChinaProvince(getIp(request));
  }

  /**
   * find china province via ip
   */
  public static String findChinaProvince(String ip) {
    if (Strings.isNullOrEmpty(ip)) {
      return "";
    }
    try {
      String result = OkHttpUtil.getString(SINA_IP_URL + ip, null);
      log.debug("ip: {}, sina response:{}", ip, result);
      JsonNode tree = JsonUtil.readTree(result);
      log.info("ip: {}, parse result:{}", ip, tree);

      if (!"中国".equals(tree.get("country").asText(null))) {
        return "";
      }
      return tree.get("province").asText(null);
    } catch (Exception e) {
      log.debug("find province by sina fail with ip:" + ip, e);
      return "";
    }
  }

}
