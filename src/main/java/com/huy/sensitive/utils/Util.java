package com.huy.sensitive.utils;

import com.huy.sensitive.constants.Constant;
import com.huy.sensitive.exception.SensitiveException;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * 工具类
 *
 * @author: huyong
 * @since: 2020/2/5 23:26
 */
public class Util {

    private Util() {
    }

    /**
     * 默认掩码
     */
    public final static String DEFAULT_MASK = Constant.MASK_DEFAULT;

    /**
     * 按位置进行字符串替换
     *
     * @param data         待操作数据
     * @param replaceIndex 替换位置索引，从1开始
     * @param replaceSize  替换长度
     * @param mask         掩码
     * @return 替换为掩码后的值
     */
    public static String replace(String data, int replaceIndex, int replaceSize, String mask) {

        if (replaceIndex < 0 || replaceSize < 0) {
            throw new SensitiveException("对数据【%s】脱敏异常，替换位置索引或替换长度都要求大于0", data);
        }

        if (data.length() <= replaceIndex) {
            return data;
        }

        //在数据长度范围内脱敏
        if (data.length() > replaceIndex + replaceSize) {
            return innerReplace(data, replaceIndex, replaceIndex + replaceSize, replaceSize, mask);
        }

        //超过数据本身长度的脱敏
        return innerReplace(data, replaceIndex, null, data.length() - replaceIndex + 1, mask);

    }

    private static String innerReplace(String originData, Integer startIndex, Integer endIndex, int maskSize, String mask) {
        return new StringBuilder()
                .append(originData, 0, startIndex - 1)
                .append(StringUtils.repeat(mask, maskSize))
                .append(endIndex == null ? "" : originData.substring(endIndex - 1))
                .toString();
    }

    /**
     * 地址脱敏 (脱敏规则：把所有数字脱敏。i.e  贵州省遵义市湄潭县***号)
     *
     * @param address 地址
     * @param mask    掩码
     * @return 脱敏后的地址
     */
    public static String addressUnsensitive(String address, String mask) {
        return address.replaceAll("\\d", mask);
    }

    /**
     * 地址脱敏 (脱敏规则：把所有数字脱敏。i.e  贵州省遵义市湄潭县***号)
     *
     * @param address 地址
     * @return 脱敏后的地址
     */
    public static String addressUnsensitive(String address) {
        return addressUnsensitive(address, DEFAULT_MASK);
    }

    /**
     * 姓名脱敏(脱敏规则：第一个字符脱敏。i.e *勇)
     *
     * @param chineseName 姓名
     * @param mask        掩码
     * @return 脱敏后的姓名
     */
    public static String chineseNameUnsensitive(String chineseName, String mask) {
        return replace(chineseName, 1, 1, mask);
    }

    /**
     * 姓名脱敏(脱敏规则：第一个字符脱敏。i.e *勇)
     *
     * @param chineseName 姓名
     * @return 脱敏后的姓名
     */
    public static String chineseNameUnsensitive(String chineseName) {
        return chineseNameUnsensitive(chineseName, DEFAULT_MASK);
    }

    /**
     * 电话脱敏(脱敏规则：长度大于等于7位，从倒数第7位开始脱敏，掩码3位。i.e. 1517***8634)
     *
     * @param phone 电话
     * @param mask  掩码
     * @return 脱敏后的电话
     */
    public static String phoneUnsensitive(String phone, String mask) {
        String val = phone.replace("-", "").replace("\\s", "");
        return val.length() < 7
                ? val
                : replace(val, val.length() - 7 + 1, 3, mask);
    }

    /**
     * 电话脱敏(脱敏规则：长度大于等于7位，从倒数第7位开始脱敏，掩码3位。i.e. 1517***8634)
     *
     * @param phone 电话
     * @return 脱敏后的电话
     */
    public static String phoneUnsensitive(String phone) {
        return phoneUnsensitive(phone, DEFAULT_MASK);
    }

    /**
     * 邮箱脱敏(脱敏规则：邮箱长度(不含邮箱后缀，i.e. @qq.com) 大于等于6，把倒数4位脱敏。i.e. 106224****@qq.com)
     *
     * @param email 邮箱
     * @param mask  掩码
     * @return 脱敏后的邮箱
     */
    public static String emailUnsensitive(String email, String mask) {
        int atIndex = email.indexOf(Constant.SYMBOL_AT);
        if (atIndex == -1) {
            return email;
        }
        String emailPrefix = email.substring(0, atIndex);
        return emailPrefix.length() < 6
                ? email
                : replace(emailPrefix, emailPrefix.length() - 4, 4, mask) + email.substring(atIndex);
    }

    /**
     * 邮箱脱敏(脱敏规则：邮箱长度(不含邮箱后缀，i.e. @qq.com) 大于等于6，把倒数4位脱敏。i.e. 106224****@qq.com)
     *
     * @param email 邮箱
     * @return 脱敏后的邮箱
     */
    public static String emailUnsensitive(String email) {
        return emailUnsensitive(email, DEFAULT_MASK);
    }

    /**
     * 身份证脱敏（脱敏规则：把8位生日进行脱敏，i.e. 522121********7654）
     *
     * @param id   身份证
     * @param mask 掩码
     * @return 脱敏后的身份证
     */
    public static String idUnsensitive(String id, String mask) {
        return id.length() == 15
                ? replace(id, 7, 6, mask)
                : replace(id, 7, 8, mask);
    }

    /**
     * 身份证脱敏（脱敏规则：把8位生日进行脱敏，i.e. 522121********7654）
     *
     * @param id 身份证
     * @return 脱敏后的身份证
     */
    public static String idUnsensitive(String id) {
        return idUnsensitive(id, DEFAULT_MASK);
    }

    /**
     * 判断是否为数字类型或字符串类型
     *
     * @param value 待判断值
     * @return true-数字类型或字符串类型
     */
    public static boolean isPrimitive(Object value) {
        return value instanceof Number
                || value instanceof CharSequence
                || value instanceof Character;
    }

    /**
     * 判断集合是否为空
     *
     * @param collection 待判断集合
     * @return true-空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合是否不为空
     *
     * @param collection 待判断集合
     * @return true-不为空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

}
