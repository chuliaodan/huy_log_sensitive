package com.huy.sensitive.serializer;

/**
 * 不脱敏
 *
 * @author: huyong
 * @since: 2020/2/7 22:50
 */
public class NopSensitiveSerializer extends DefaultSensitiveSerializer {

    /**
     * 脱敏
     *
     * @param value 脱敏前的值
     * @return 脱敏后的值
     */
    @Override
    public String unsensitive(String value) {
        return value;
    }
}
