package com.huy.sensitive.serializer;

import com.huy.sensitive.enums.SensitiveType;
import org.apache.commons.lang3.StringUtils;

/**
 * 默认脱敏序列化器(fastjson)
 *
 * @author: huyong
 * @since: 2020/2/5 20:55
 */
public class DefaultSensitiveSerializer extends AbstractSensitiveSerializer {

    /**
     * 脱敏类型
     *
     * @return 脱敏类型
     */
    public SensitiveType sensitiveType() {
        return SensitiveType.DEFAULT_TYPE;
    }

    /**
     * 脱敏
     *
     * @param value 脱敏前的值
     * @return 脱敏后的值
     */
    @Override
    public String unsensitive(String value) {
        return StringUtils.repeat(mask(), value.length());
    }

}
