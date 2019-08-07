package com.qunzhi.yespmp.security.jwt;

import com.qunzhi.yespmp.exception.TException;
import com.qunzhi.yespmp.response.ResponseEnum;
import com.qunzhi.yespmp.security.SingleSignOn;
import com.qunzhi.yespmp.security.anno.CheckToken;
import com.qunzhi.yespmp.security.anno.LoginToken;
import com.qunzhi.yespmp.utility.userlimit.BlackHouse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @title:
 * @author: Yuanbo
 * @date: 2019/8/2  9:54
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {

        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有LoginToken注释，有则跳过认证
        if (method.isAnnotationPresent(LoginToken.class)) {
            LoginToken loginToken = method.getAnnotation(LoginToken.class);
            if (loginToken.required()) {
                return true;
            }
        }

        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(CheckToken.class)) {
            CheckToken checkToken = method.getAnnotation(CheckToken.class);
            if (checkToken.required()) {
                // 执行认证
                if (token != null) {
                    // 校验是否是正确的token 并且没有在小黑屋中
                    JwtUser detail = JwtUtil.parseJWT(token);
                    if (detail == null || BlackHouse.exist(detail.getUsername())) {
                        throw TException.of(ResponseEnum.UNAUTHORIZED);
                    }
                    //校验是否过期
                    if (new Date().after(detail.getExpire())) {
                        throw TException.of(ResponseEnum.TOKEN_EXPIR);
                    }
                    // 校验是否被挤掉线
                    if (!SingleSignOn.check(detail.getId(), detail.getExpire())) {
                        throw TException.of(ResponseEnum.SINGLE_SIGN_ON);
                    }
                } else {
                    throw TException.of(ResponseEnum.UNAUTHORIZED);
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }


}