package com.example.springboot01.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @title:  将数据库int表示的类别转换为Enum
 * @author: Yuanbo
 * @date: 2019/8/1  10:30
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataBaseEnum {
    /**
     * 活动状态枚举
     */
    public enum ActivityState implements Ordinal {
        PENDING,                        //待审核
        REJECTED,                       //已拒绝
        PROGRESSING,                    //进行中
        COMPLETED,                      //已完成
        CANCELLED,                      //取消
//        DELETED,                        //删除
    }

    /**
     * 文件枚举
     */
    public enum DocType implements Ordinal {
        IMAGE,                          //图片
        VIDEO,                          //视频
        OTHER                           //其他
    }

    /**
     * 积分订单类型
     */
    public enum OrderType implements Ordinal {
        CHARGE,                         //充值
        WITHDRAW                        //提现
    }

    public interface Ordinal {

        int ordinal();

        default int v() {
            return ordinal();
        }
    }
}
