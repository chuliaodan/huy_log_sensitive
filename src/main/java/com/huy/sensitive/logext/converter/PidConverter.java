package com.huy.sensitive.logext.converter;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.huy.sensitive.constants.Constant;

import java.lang.management.ManagementFactory;

/**
 * 进程号转换器
 *
 * @author: huyong
 * @since: 2020/2/7 12:12
 */
public class PidConverter extends ClassicConverter {

    /**
     * 进程id
     */
    private String pid;

    public PidConverter() {
        this.pid = ManagementFactory.getRuntimeMXBean().getName().split(Constant.SYMBOL_AT)[0];
    }

    @Override
    public String convert(ILoggingEvent event) {
        return pid;
    }

}
