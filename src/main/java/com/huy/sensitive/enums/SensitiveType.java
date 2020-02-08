package com.huy.sensitive.enums;

import com.huy.sensitive.serializer.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 脱敏类型
 *
 * @author: huyong
 * @since: 2020/2/5 18:39
 */
public enum SensitiveType {

    ID(
            "身份证号",
            IdSensitiveSerializer.class,
            Arrays.asList(
                    "[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])(\\d{4}|\\d{3}X)",//18位身份证
                    "[1-9]\\d{5}\\d{2}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}")//15位身份证
    ),
    TEL(
            "电话",
            PhoneSensitiveSerializer.class,
            Arrays.asList(
                    "1\\d{10}",//手机号
                    "(\\+86-)?(\\d{7,8})|(0\\d{2}-?\\d{8})|(0\\d{3}-?\\d{7,8})")//座机号
    ),
    EMAIL(
            "电子邮件",
            EmailSensitiveSerializer.class,
            Collections.singletonList("\\w+@[a-z0-9]+\\.[a-z]+")
    ),
    CHINESE_NAME(
            "中文名字",
            ChineseNameSensitiveSerializer.class,
            Collections.emptyList()
    ),
    ADDRESS(
            "地址",
            AddressSensitiveSerializer.class,
            Collections.emptyList()
    ),
    BANK_CARD(
            "银行卡",
            NopSensitiveSerializer.class,
            Collections.emptyList()
    ),
    NOP(
            "空操作",
            NopSensitiveSerializer.class,
            Collections.emptyList()
    ),
    DEFAULT_TYPE(
            "默认脱敏类型（数据全部脱敏）",
            DefaultSensitiveSerializer.class,
            Collections.emptyList()
    );

    /**
     * 脱敏描述
     */
    public final String sensitiveDesc;

    /**
     * 该脱敏类型对应的正则表达式
     */
    public final List<String> regexes;

    /**
     * 该类型默认的脱敏器
     */
    public final Class<? extends AbstractSensitiveSerializer> serializer;

    SensitiveType(String sensitiveDesc, Class<? extends AbstractSensitiveSerializer> serializer, List<String> regexes) {
        this.sensitiveDesc = sensitiveDesc;
        this.serializer = serializer;
        this.regexes = regexes;
    }
}
