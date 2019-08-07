package com.qunzhi.yespmp.constant.thirdpart;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThirdPartyConst {
  //wx配置信息
  public static final String GETUserInfoURLWX = "https://api.weixin.qq.com/sns/userinfo?access_token={0}&openid={1}";
  public static final String AccessTokenURLWX = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";

  public static final String QUERY_JS_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token={0}";
  public static final String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid={0}&grant_type=refresh_token&refresh_token={1}";
  public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";

  public static final String SEND_MODEL_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={0}";
}
