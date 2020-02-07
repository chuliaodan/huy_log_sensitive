package com.huy.sensitive.logext.converter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.DynamicConverter;
import com.alibaba.fastjson.JSON;
import com.huy.sensitive.constants.Constant;
import com.huy.sensitive.enums.SensitiveType;
import com.huy.sensitive.serializer.AbstractSensitiveSerializer;
import com.huy.sensitive.serializer.DefaultSensitiveSerializer;
import com.huy.sensitive.utils.Util;
import lombok.Data;
import lombok.experimental.Accessors;
import org.slf4j.helpers.MessageFormatter;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 自定义日志脱敏转化器，建议打印日志的数据为对象，非基本类型(因为基本类型是通过正则表达式匹配的，效率低，很容易错误)
 *
 * @author: huyong
 * @since: 2020/2/5 15:20
 */
public class LogSensitiveConverter extends DynamicConverter<ILoggingEvent> {

    /**
     * 脱敏正则
     */
    private List<SensitivePattern> sensitivePatterns;

    /**
     * 脱敏类型与脱敏器映射
     */
    private Map<SensitiveType, AbstractSensitiveSerializer> serializerMap;

    /**
     * 默认脱敏器
     */
    private AbstractSensitiveSerializer defaultSensitiveSerializer = new DefaultSensitiveSerializer();

    public LogSensitiveConverter() {
        Map<SensitiveType, List<SensitivePattern>> sensitiveTypeMap = Arrays.stream(SensitiveType.values())
                .filter(sensitiveType -> Util.isNotEmpty(sensitiveType.regexes))
                .collect(Collectors.toMap(
                        Function.identity(),
                        sensitiveType -> sensitiveType.regexes.stream()
                                .map(regex -> new SensitivePattern().setSensitiveType(sensitiveType).setPattern(Pattern.compile(regex)))
                                .collect(Collectors.toList()))
                );
        this.sensitivePatterns = sensitiveTypeMap.values().stream()
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(sp -> sp.getSensitiveType().ordinal()))
                .collect(Collectors.toList());
        this.serializerMap = sensitiveTypeMap.keySet().stream()
                .collect(Collectors.toMap(Function.identity(), sensitiveType -> instance(sensitiveType.serializer)));
    }

    /**
     * 实例化
     *
     * @param serializer 脱敏器类型
     * @return 脱敏器对象
     */
    private AbstractSensitiveSerializer instance(Class<? extends AbstractSensitiveSerializer> serializer) {
        try {
            return serializer.newInstance();
        } catch (Exception e) {
            addWarn("实例化脱敏器异常!!!");
        }
        return new DefaultSensitiveSerializer();
    }

    @Override
    public String convert(ILoggingEvent event) {
        Object[] params = event.getArgumentArray();
        Optional<Object[]> optional = Optional.ofNullable(params);
        if (optional.isPresent() && isSensitive()) {
            params = Arrays.stream(optional.get()).map(this::sensitive).toArray();
        }
        return MessageFormatter.arrayFormat(event.getMessage(), params).getMessage();
    }

    /**
     * 脱敏的总开关
     * <p>
     * 从系统变量中获取是否脱敏，默认脱敏
     *
     * @return true-脱敏
     */
    private boolean isSensitive() {
        return Boolean.parseBoolean(System.getProperty(Constant.SYSTEM_ENV_SENSITIVE_KEYWORD, "true"));
    }

    /**
     * 脱敏
     *
     * @param param 待脱敏参数
     * @return 脱敏后的数据
     */
    private Object sensitive(Object param) {

        if (param == null) {
            return param;
        }

        //对象类型，使用 @JSONField(serializeUsing = xxx.class) 来进行脱敏
        if (!Util.isPrimitive(param)) {
            return JSON.toJSONString(param);
        }

        return matchAndReplace(param.toString());
    }

    /**
     * 使用正则匹配并进行脱敏
     *
     * @param param 待脱敏参数
     * @return 脱敏后的数据
     */
    private String matchAndReplace(String param) {

        if (param.length() > Constant.SENSITIVE_LENGTH_LIMIT) {
            return param;
        }

        for (SensitivePattern sp : sensitivePatterns) {
            Matcher matcher = sp.pattern.matcher(param);
            while (matcher.find()) {
                AbstractSensitiveSerializer serializer = serializerMap.getOrDefault(sp.getSensitiveType(), defaultSensitiveSerializer);
                String group = matcher.group();
                param = param.replaceAll(group, serializer.unsensitive(group));
            }
        }

        return param;
    }

    /**
     * 脱敏正则表达式
     */
    @Data
    @Accessors(chain = true)
    private static class SensitivePattern {
        SensitiveType sensitiveType;
        Pattern pattern;
    }

}
