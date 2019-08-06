package com.example.springboot01.Service;

import com.example.springboot01.constant.RegexConst;
import com.example.springboot01.constant.TConst;
import com.example.springboot01.dao.UserInfoMapper;
import com.example.springboot01.entity.UserInfo;
import com.example.springboot01.exception.TException;
import com.example.springboot01.pojo.UserInfoDTO;
import com.example.springboot01.pojo.UserLoginDTO;
import com.example.springboot01.response.ResponseEnum;
import com.example.springboot01.security.jwt.JwtUser;
import com.example.springboot01.security.jwt.JwtUtil;
import com.example.springboot01.utility.BeanUtil;
import com.example.springboot01.utility.TUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @title:
 * @author: Yuanbo
 * @date: 2019/8/1  14:32
 */
@Service
public class AuthService {
    @Resource
    private UserInfoMapper userMapper;

    /**
     * @description: 用户注册
     * @Author Bob
     * @date 2019/8/2 10:54
     */
    public UserLoginDTO signup(String phone, String password) {
        // check phone and password(add salt)
        password = checkPhonePassword(phone, password);

        //check db
        UserInfo userInfo = userMapper.selectByPhone(phone);
        if (userInfo != null) {
            throw TException.of(ResponseEnum.PHONE_EXISTED);
        }
        //初始化用户
        String id = TUtil.uuid();
        String nickname = TConst.DEFAULT_NAME + RandomStringUtils.randomAlphanumeric(4);

        //insert userinfo
        UserInfo user = new UserInfo(id, phone, password, nickname, TConst.DEFAULT_AVATAR,
                null, null, null, null, null, null, LocalDateTime.now());
        userMapper.insert(user);

        //generator token
        String token = JwtUtil.create(new JwtUser(id, phone, password,
                new Date(System.currentTimeMillis() + TConst.APP_EXPIRE)));

        return new UserLoginDTO(BeanUtil.copy(user, new UserInfoDTO()), token);
    }

    /**
     * @description: 用户登录
     * @Author Bob
     * @date 2019/8/2 10:38
     */
    public UserLoginDTO login(String phone, String password) {
        password = checkPhonePassword(phone, password);

        UserInfo user = userMapper.selectByPhone(phone);
        if (user == null) {
            throw TException.of(ResponseEnum.USER_NOT_EXIST);
        }
        if (!password.equals(user.getPassword())) {
            throw TException.of(ResponseEnum.WRONG_PASSWORD);
        }

        // copy user info
        UserInfoDTO info = new UserInfoDTO();
        BeanUtil.copy(user, info);

        // generate token
        Date expire = new Date(System.currentTimeMillis() + TConst.APP_EXPIRE);
        String token = JwtUtil.create(new JwtUser(user.getId(), user.getPhone(), user.getPassword(), expire));

        return new UserLoginDTO(info, token);
    }

    /**
     * @description: 校验手机号并给密码进行SHA256加密
     * @Author Bob
     * @date 2019/8/2 10:38
     */
    private String checkPhonePassword(String phone, String password) {
        // password must be sha256 hashed, phone must be better formatted
//        if (password.length() != 64) {
//            throw TException.of(ResponseEnum.WRONG_LENGTH);
//        }

        if (!RegexConst.CHINA_PHONE.matcher(phone).matches()) {
            throw TException.of(ResponseEnum.INVALID_PHONE);
        }
        return TUtil.saltHash(password);
    }
}
