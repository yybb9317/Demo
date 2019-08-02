package com.example.springboot01.security;

import com.example.springboot01.entity.UserInfo;
import com.example.springboot01.security.jwt.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * @title:
 * @author: Yuanbo
 * @date: 2019/8/1  17:30
 */
@Slf4j
public class JwtUtil {

    // JWT security config

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String CLAIM_UN = "un";
    public static final String CLAIM_PW = "pw";

    private static final String SECRET = "gbvvZeIigTktBc1QarWY5CQuxnveeT88FkRayvU14CXXItERdIRgD+012+ifnJZDl6wu/A/4ITNldut1UBNJdA==";
    //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
    private static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS512;
    //生成签名的时候使用的秘钥secret,这个方法本地封装了的，一般可以从本地配置文件中读取，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
    private static final Key KEY = new SecretKeySpec(Base64.getDecoder().decode(SECRET),
            ALGORITHM.getJcaName());

    /**
     * 用户登录成功后生成Jwt
     * 使用Hs256算法  私匙使用用户密码
     * @param user      登录成功的user对象
     * @return
     */
    public static String create(com.example.springboot01.security.jwt.JwtUser user) {
        return TOKEN_PREFIX + Jwts.builder().setSubject(user.getId())
                .claim(CLAIM_UN, user.getUsername())
                .claim(CLAIM_PW, user.getPassword())
                .setExpiration(user.getExpire()).signWith(ALGORITHM, KEY).compact();
    }

    /**
     * Token的解密
     * @param token 加密后的token
     * @return
     */
    public static com.example.springboot01.security.jwt.JwtUser parseJWT(String token) {
        if (token.startsWith(TOKEN_PREFIX)) {
            token = token.substring(TOKEN_PREFIX.length());
        } else {
            return null;
        }

        try {
            Claims claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
            String id = claims.getSubject();
            String username = (String) claims.get(CLAIM_UN);
            String password = (String) claims.get(CLAIM_PW);
            Date expire = claims.getExpiration();
            return new JwtUser(id, username, password, expire);
        } catch (JwtException ex) {
            log.debug("JwtUtil parse fail ex:{}",ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 校验token
     * 在这里可以使用官方的校验，我这里校验的是token中携带的密码于数据库一致的话就校验通过
     * @param token
     * @param user
     * @return
     */
    public static Boolean isVerify(String token, UserInfo user) {
        if (token.startsWith(TOKEN_PREFIX)) {
            token = token.substring(TOKEN_PREFIX.length());
        } else {
            return null;
        }

        //得到DefaultJwtParser
        Claims claims = Jwts.parser()
                //设置签名的秘钥
                .setSigningKey(KEY)
                //设置需要解析的jwt
                .parseClaimsJws(token).getBody();

        if (claims.get(CLAIM_PW).equals(user.getPassword())) {
            return true;
        }

        return false;
    }
}
