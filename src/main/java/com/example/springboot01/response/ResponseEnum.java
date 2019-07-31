package com.example.springboot01.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Getter;

/**
 * 无数据成功返回,异常返回 <p>状态码参考 {@link org.springframework.http.HttpStatus}
 */
@Getter
@JsonFormat(shape = Shape.OBJECT)
public enum ResponseEnum implements Response {

  // success
  SUCCESS(200, 200, "OK"),

  // client error
  BAD_REQUEST(400, 400, ResponseMsg.BAD_REQUEST), // 明显的客户端错误,传参错误
  UNAUTHORIZED(401, 401, ResponseMsg.UNAUTHORIZED), // 用户没有必要的凭据,身份无法验证
  FORBIDDEN(403, 403, ResponseMsg.FORBIDDEN), // 无权操作
  NOT_FOUND(404, 404, ResponseMsg.NOT_FOUND), // 请求资源未找到
  TOO_MANY_REQUESTS(429, 429, ResponseMsg.TOO_MANY_REQUESTS), // 给定的时间内发送了太多的请求

  // server error
  INTERNAL_SERVER_ERROR(500, 500, ResponseMsg.INTERNAL_SERVER_ERROR), // 服务器内部错误

  NULL_POINTER_EXCEPTION(500, 1, ResponseMsg.NULL_POINTER_EXCEPTION), // 服务器空指针异常
  DB_EXCEPTION(500, 2, ResponseMsg.DB_EXCEPTION), // 数据库异常,某些请求内容可能过时,检查传参是否正确
  DB_JUNK_DATA(500, 3, ResponseMsg.DB_JUNK_DATA), // 数据库脏数据!
  JSON_ERROR(500, 4, ResponseMsg.JSON_ERROR), // Json解析失败
  UPDATE_FAIL(500, 5, ResponseMsg.UPDATE_FAIL), // 数据更新失败
  UPLOAD_FAIL(500, 6, ResponseMsg.UPLOAD_FAIL), // 上传失败
  SMS_DOWN(500, 7, ResponseMsg.SMS_DOWN), // 短信服务异常
  WXPAY_EXP(500, 9, ResponseMsg.WXPAY_EXP), // 微信支付异常

  // ================= business error ===================
  // users
  INVALID_PHONE(400, 11, ResponseMsg.INVALID_PHONE), // 无效手机格式
  PHONE_EXISTED(403, 12, ResponseMsg.PHONE_EXISTED), // 登录名已经存在
  PHONE_NOT_EXIST(404, 13, ResponseMsg.PHONE_NOT_EXIST), // 手机号不存在
  SMS_LIMIT(403, 14, ResponseMsg.SMS_LIMIT), // 该手机号短信发送次数达到上限
  INVALID_SMS_CODE(403, 15, ResponseMsg.INVALID_SMS_CODE), // 无效验证码
  USER_NOT_EXIST(404, 16, ResponseMsg.USER_NOT_EXIST), // 用户不存在
  WRONG_PASSWORD(403, 17, ResponseMsg.WRONG_PASSWORD), // 密码错误
  LOGIN_FORBIDDEN(403, 18, ResponseMsg.LOGIN_FORBIDDEN), // 用户状态异常,禁止登录
  WRONG_LENGTH(403, 19, ResponseMsg.WRONG_LENGTH), // 密码长度不符合要求
  NOT_BIND_PHONE(403, 20, ResponseMsg.NOT_BIND_PHONE), // 没有绑定手机
  INVALID_EMAIL(400, 21, ResponseMsg.INVALID_EMAIL), // 邮箱格式错误
  USERNAME_EXISTED(400, 22, ResponseMsg.USERNAME_EXISTED), // 账号已存在
  NOT_FIND_VERIFY(400, 23, ResponseMsg.NOT_FIND_VERIFY), // 未找到认证信息
  WRONG_OLD_PASSWORD(403, 24, ResponseMsg.WRONG_OLD_PASSWORD), // 旧密码错误
  INVALID_LOGIN(403, 25, ResponseMsg.INVALID_LOGIN), // 用户名或密码错误
  WECHAT_NOT_EXIST(403, 26, ResponseMsg.WECHAT_NOT_EXIST), //
  WECHAT_BINDED(403, 27, ResponseMsg.WECHAT_BINDED), //
  CAPTCHA_FAIL(403, 28, ResponseMsg.CAPTCHA_FAIL), //
  Rong_Id_NOT_FIND(403, 29, ResponseMsg.Rong_Id_NOT_FIND), //
  SMS_RATE(403, 30, ResponseMsg.SMS_RATE), // 该手机号短信发送频繁

  // others
  INTEGRAL_LACK(429, 40, ResponseMsg.INTEGRAL_LACK), // 积分不足
  BANNER_NOT_EXIST(429, 41, ResponseMsg.BANNER_NOT_EXIST), // Banner不存在
  COMMENT_NOT_EXIST(403, 42, ResponseMsg.COMMENT_NOT_EXIST), // 评论不存在
  ORGANIZATION_NOT_EXIST(403, 43, ResponseMsg.ORGANIZATION_NOT_EXIST), // 机构不存在
  SMS_SENDING_FAIL(403, 44, ResponseMsg.SMS_SENDING_FAIL), // 短信发送失败
  POINT_RULE_NOT_EXIST(403, 45, ResponseMsg.RULE_NOT_EXIST), // 积分规则不存在
  ALREADY_REPLY(403, 47, ResponseMsg.ALREADY_REPLY), // 已经回复过
  NOTICE_NOT_EXIST(403, 48, ResponseMsg.NOTICE_NOT_EXIST), // 公告不存在
  RESULT_NOT_GET(404, 49, ResponseMsg.RESULT_NOT_GET),//微信请求结果失败

  //products
  CATEGORY_NOT_EXIST(404, 50, ResponseMsg.CATEGORY_NOT_EXIST), // 产品类型不存在
  PRODUCT_NOT_FOUND(404, 51, ResponseMsg.PRODUCT_NOT_FOUND), // 未找到产品/已下架
  PRODUCT_UNPASS(404, 52, ResponseMsg.PRODUCT_UNPASS), // 该产品未审核或未通过
  AGENT_ERROR(404, 53, ResponseMsg.AGENT_ERROR), // 该客户经理没有发布过该产品
  PRODUCT_AUDITED(404, 54, ResponseMsg.PRODUCT_AUDITED), // 产品已被审核过
  PRODUCT_EXIST(404, 55, ResponseMsg.PRODUCT_EXIST), // 产品名已存在

  // payments
  ORDER_PAID_CANCEL(403, 60, ResponseMsg.ORDER_PAID_CANCEL), // 订单已支付或已取消
  WXPAY_FAIL(403, 61, ResponseMsg.WXPAY_FAIL), // 微信支付失败
  WXPAY_AUTH_NEEDED(403, 62, ResponseMsg.WXPAY_AUTH_NEEDED), // 需要客户重新授权
  FILE_FORMAT_ERROR(403, 63, ResponseMsg.FILE_FORMAT_ERROR), // 文件格式错误

  //activity
  ACTIVITY_CANCELLED(403, 70, ResponseMsg.ACTIVITY_CANCELLED), // 该活动已被取消
  ACTIVITY_STARTED(403, 71, ResponseMsg.ACTIVITY_STARTED), // 该活动已经开始
  NOT_PROGRESSING(403, 73, ResponseMsg.NOT_PROGRESSING), // 该活动已经结束
  ACTIVITY_NOT_EXIST(403, 74, ResponseMsg.ACTIVITY_NOT_EXIST), // 活动不存在
  CANT_JOIN_OWN(403, 75, ResponseMsg.CANT_JOIN_OWN), // 不能参加自己发布的活动
  CAN_ONLY_ONCE(403, 76, ResponseMsg.CAN_ONLY_ONCE), // 只能参加一次
  NOT_JOIN(403, 77, ResponseMsg.NOT_JOIN), // 该用户没有参加过该活动
  AT_LEAST_ONE(403, 78, ResponseMsg.AT_LEAST_ONE), // 至少一人参加
  ACTIVITY_TYPE_ERROR(403, 79, ResponseMsg.ACTIVITY_TYPE_ERROR), // 活动状态类型错误
  ACTIVITY_AUDITED(403, 80, ResponseMsg.ACTIVITY_AUDITED), // 活动已审核过
  ACTIVITY_NOT_RELEASE(403, 81, ResponseMsg.ACTIVITY_NOT_RELEASE), // 该经理未发布过该活动
  ACTIVITY_NOT_AUDITED(403, 82, ResponseMsg.ACTIVITY_NOT_AUDITED), // 活动状态不是通过的

  //Interact
  REQUIRMENT_NOT_EXIST(403, 90, ResponseMsg.REQUIRMENT_NOT_EXIST), // 需求不存在
  QUESTION_NOT_EXIST(403, 91, ResponseMsg.QUESTION_NOT_EXIST), // 问答不存在
  REQUIRMENT_UNPASS(403, 92, ResponseMsg.REQUIRMENT_UNPASS), // 需求未审核或未通过
  REQUIRMENT_AUDITED(403, 93, ResponseMsg.REQUIRMENT_AUDITED), // 需求已审核过


  RONGCLOUD_ERROR(403, 100, ResponseMsg.RONGCLOUD_ERROR), // 融云服务错误
  ORGANIZATION_EXISTED(404, 101, ResponseMsg.ORGANIZATION_EXISTED), //机构已存在


  PRODUCT_PASSED(202, 201, ResponseMsg.PRODUCT_PASSED),
  PRODUCT_PENDING(202, 202, ResponseMsg.PRODUCT_PENDING),
  PARSE_POSITION_FAIL(202, 203, ResponseMsg.PARSE_POSITION_FAIL),
  ILLEGAL_KEYWORD(202, 204, ResponseMsg.ILLEGAL_KEYWORD), // 短信内容包含非法关键字,
  NOT_SUFFICIENT_FUNDS(202, 205, ResponseMsg.NOT_SUFFICIENT_FUNDS), // 短信余额不足,
  OVERTIME_SMS_CODE(403, 206, ResponseMsg.OVERTIME_SMS_CODE), // 验证码超时,请重新获取
  FIRST_ORGANIZATION_EXIST(403, 207, ResponseMsg.FIRST_ORGANIZATION_EXIST), // 一级机构名重复


  // 末尾分号
  ;

  /**
   * http status code
   */
  private int status;
  /**
   * response id
   */
  private int code;
  /**
   * debug message for developers
   */
  private String msg;

  ResponseEnum(int status, int code, String msg) {
    this.status = status;
    this.code = code;
    this.msg = msg;
  }
}
