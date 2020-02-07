package com.huy.sensitive.serializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.huy.sensitive.constants.Constant;
import com.huy.sensitive.exception.SensitiveException;
import com.huy.sensitive.utils.Util;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author: huyong
 * @since: 2020/2/5 20:37
 */
public abstract class AbstractSensitiveSerializer implements ObjectSerializer {

    /**
     * 脱敏的掩码
     *
     * @return 掩码
     */
    public String mask() {
        return Constant.MASK_DEFAULT;
    }

    /**
     * 是否脱敏
     *
     * @return true-脱敏
     */
    public boolean isSensitive() {
        return true;
    }

    /**
     * 脱敏
     *
     * @param value 脱敏前的值
     * @return 脱敏后的值
     */
    public abstract String unsensitive(String value);

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        serializer.write(innerUnsensitive(object));
    }

    /**
     * 脱敏
     *
     * @param value 脱敏前的值
     * @return 脱敏后的值
     */
    private Object innerUnsensitive(Object value) {

        if (value == null || !isSensitive()) {
            return value;
        }

        if (Util.isPrimitive(value)) {
            return unsensitive(value.toString());
        }

        throw new SensitiveException("序列化类只支持基本类型(包括包装类型)");

    }

}
