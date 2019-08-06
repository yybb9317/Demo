package com.example.springboot01.constant;

import com.example.springboot01.utility.JsonUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * 群智项目常量
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TConst {

  // project default date time format
  public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
  public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  public static final String MEDIA_TYPE = "application/json; charset=utf-8";

  // token expire time
  public static final long APP_EXPIRE = 10 * 24 * 3600 * 1000L; // 10 days
  public static final long WEB_EXPIRE = 8 * 3600 * 1000L; // 8 hours

  public static final int VERIFY_CODE_DAILY_LIMIT = 20; // 20 times a day
  public static final long VERIFY_CODE_EXPIRE = 5L; // 5 minutes
  public static final int SMS_CODE_SIZE = 6;

  public static final long TRACKING_EXPIRE = 600L;  //600 minutes

  public static final int REDEEM_CODE_SIZE = 15;

  // TODO 修改回调地址
  public static final String SERVER_URL = "http://127.0.0.1/qunzhi";
  public static final String THIRD_PARTY_CALLBACK = SERVER_URL + "/callback";

  /**
   * any operation with this ID is forbidden!
   */
  public static final String ROOT_ID = "364423032dde4870bbd95666117a5fd3";
  public static final String ROOT_NAME = "root";

  /**
   *  default userinfo
   */
  public static final String AVATAR_URL = "http://keyike.com.cn/api/files/download/";
  public static final String DEFAULT_NAME = "新用户";
  public static final String DEFAULT_AVATAR = "8a78da3f58b642708b4711cb89837e7b";

//  public static final Rights RIGHTS;
//
//  static {
//    try {
//      RIGHTS = JsonUtil.YML_MAPPER.readValue(
//          TConst.class.getResourceAsStream("/rights.yml"), Rights.class);
//      Assert.notEmpty(RIGHTS.getGroups(), "Empty Rights!");
//    } catch (IOException e) {
//      throw new RuntimeException(e);
//    }
//  }

}
