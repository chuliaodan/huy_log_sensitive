package com.huy.sensitive.constants;

/**
 * 常量
 *
 * @author: huyong
 * @since: 2020/2/6 18:01
 */
public class Constant {

    private Constant() {

    }

    /**
     * 脱敏默认掩码
     */
    public final static String MASK_DEFAULT = "*";


    /**
     * 脱敏字符串长度上限
     */
    public final static int SENSITIVE_LENGTH_LIMIT = 1000;

    /**
     * at符号
     */
    public final static String SYMBOL_AT = "@";

    /**
     * 系统环境变量脱敏关键字
     */
    public final static String SYSTEM_ENV_SENSITIVE_KEYWORD = "LOG_SENSITIVE";

}
