package com.huy.sensitive.serializer;

import com.huy.sensitive.enums.SensitiveType;
import com.huy.sensitive.utils.Util;

/**
 * 电子邮箱脱敏
 *
 * @author: huyong
 * @since: 2020/2/6 0:46
 */
public class EmailSensitiveSerializer extends DefaultSensitiveSerializer {

    /**
     * 脱敏类型
     *
     * @return 脱敏类型
     */
    @Override
    public SensitiveType sensitiveType() {
        return SensitiveType.EMAIL;
    }

    /**
     * 脱敏
     *
     * @param value 脱敏前的值
     * @return 脱敏后的值
     */
    @Override
    public String unsensitive(String value) {
        return Util.emailUnsensitive(value, mask());
    }
}
