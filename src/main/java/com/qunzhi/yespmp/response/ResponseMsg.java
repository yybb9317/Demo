package com.qunzhi.yespmp.response;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ResponseMsg {

    // ================= system ===================

  public static final String BAD_REQUEST = "明显的客户端错误,传参错误";
  public static final String UNAUTHORIZED = "用户没有必要的凭据,身份无法验证";
  public static final String TOKEN_EXPIR = "凭证过期，请重新登录";
  public static final String FORBIDDEN = "无权操作";
  public static final String NOT_FOUND = "请求资源未找到";
  public static final String TOO_MANY_REQUESTS = "给定的时间内发送了太多的请求";
  public static final String INTERNAL_SERVER_ERROR = "服务器内部错误";
  public static final String NULL_POINTER_EXCEPTION = "服务器空指针异常";
  public static final String DB_EXCEPTION = "数据库异常,某些请求内容可能过时,检查传参是否正确";
  public static final String DB_JUNK_DATA = "数据库脏数据!";
  public static final String JSON_ERROR = "Json解析失败";
  public static final String UPDATE_FAIL = "数据更新失败";
  public static final String UPLOAD_FAIL = "上传失败";
  public static final String SMS_DOWN = "短信服务异常";
  public static final String ALIPAY_EXP = "支付宝异常";
  public static final String WXPAY_EXP = "微信支付异常";

// ================= old ===================

  public static final String INVALID_EMAIL = "邮箱格式错误";
  public static final String WRONG_OLD_PASSWORD = "旧密码错误";
  public static final String INVALID_LOGIN = "用户名或密码错误";
  public static final String SINGLE_SIGN_ON = "账号在别处登录";
  public static final String CAPTCHA_FAIL = "验证码错误";
  public static final String ORDER_PAID_CANCEL = "订单已支付或已取消";
  public static final String WXPAY_FAIL = "微信支付失败";
  public static final String WXPAY_AUTH_NEEDED = "需要客户重新授权";
  public static final String FILE_FORMAT_ERROR = "文件格式错误";
  public static final String RESULT_NOT_GET = "微信请求结果失败";
  public static final String WECHAT_NOT_EXIST = "微信不存在";
  public static final String WECHAT_BINDED = "微信已被绑定";

// ================= now ===================
  public static final String INVALID_SMS_CODE = "无效验证码";
  public static final String CATEGORY_NOT_EXIST = "类型不存在";
  public static final String PRODUCT_NOT_FOUND = "未找到商品/产品已下架";
  public static final String INVALID_PHONE = "无效手机格式";
  public static final String PHONE_EXISTED = "登录名已经存在";
  public static final String PHONE_NOT_EXIST = "手机号不存在";
  public static final String SMS_LIMIT = "该手机号短信发送次数达到上限";
  public static final String SMS_RATE = "该手机号短信发送过于频繁";
  public static final String USER_NOT_EXIST = "用户不存在";
  public static final String WRONG_PASSWORD = "密码错误";
  public static final String LOGIN_FORBIDDEN = "用户状态异常,禁止登录";
  public static final String PRODUCT_UNPASS = "该产品未审核或未通过";
  public static final String CANT_JOIN_OWN = "不能参加自己的活动";
  public static final String CAN_ONLY_ONCE = "只能参加一次";
  public static final String ACTIVITY_NOT_EXIST = "活动不存在";
  public static final String ACTIVITY_CANCELLED = "该活动已被取消";
  public static final String ACTIVITY_STARTED = "该活动已经开始";
  public static final String AT_LEAST_ONE = "至少一人参加";
  public static final String NOT_PROGRESSING = "活动已经取消或者结束";
  public static final String REQUIRMENT_NOT_EXIST = "需求不存在";
  public static final String QUESTION_NOT_EXIST = "问答不存在";
  public static final String REQUIRMENT_UNPASS = "需求未审核或未通过";
  public static final String NOT_JOIN = "该用户没有参加过该活动";
  public static final String INTEGRAL_LACK = "积分不足";
  public static final String WRONG_LENGTH = "密码长度不符合要求";
  public static final String NOT_BIND_PHONE = "没有绑定手机";
  public static final String AGENT_ERROR = "该客户经理没有发布过该产品";
  public static final String BANNER_NOT_EXIST = "Banner不存在";
  public static final String COMMENT_NOT_EXIST = "评论不存在";
  public static final String REQUIRMENT_AUDITED = "需求已审核过";
  public static final String ACTIVITY_TYPE_ERROR = "活动状态类型错误";
  public static final String USERNAME_EXISTED = "账号已存在";
  public static final String ACTIVITY_AUDITED = "活动已审核过";
  public static final String PRODUCT_AUDITED = "产品已审核过";
  public static final String PRODUCT_PASSED = "产品已上线";
  public static final String PRODUCT_PENDING = "产品审核中";
  public static final String PRODUCT_EXIST = "产品名已存在";
  public static final String ORGANIZATION_NOT_EXIST = "机构不存在";
  public static final String ORGANIZATION_EXISTED = "机构已存在";
  public static final String NOT_FIND_VERIFY = "未找到认证信息";
  public static final String SMS_SENDING_FAIL = "短信发送失败";
  public static final String ACTIVITY_NOT_RELEASE = "该经理未发布过该活动";
  public static final String ACTIVITY_NOT_AUDITED = "活动状态不是通过的";
  public static final String RULE_NOT_EXIST = "积分规则不存在";
  public static final String ALREADY_REPLY = "已经回复过";
  public static final String NOTICE_NOT_EXIST = "公告不存在";
  public static final String RONGCLOUD_ERROR = "融云服务错误";
  public static final String Rong_Id_NOT_FIND = "融云用户id没找到";
  public static final String PARSE_POSITION_FAIL = "错误的地理格式";
  public static final String ILLEGAL_KEYWORD = "短信内容包含非法关键字";
  public static final String NOT_SUFFICIENT_FUNDS = "短信余额不足";
  public static final String OVERTIME_SMS_CODE = "验证码超时,请重新获取";
  public static final String FIRST_ORGANIZATION_EXIST = "一级机构名重复";




}
