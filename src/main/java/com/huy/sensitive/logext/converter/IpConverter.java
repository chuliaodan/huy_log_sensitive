package com.huy.sensitive.logext.converter;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.net.InetAddress;

/**
 * ip转换器
 *
 * @author: huyong
 * @since: 2020/2/7 11:32
 */
public class IpConverter extends ClassicConverter {

    /**
     * 本机地址
     */
    private String hostAddress;

    public IpConverter() {
        try {
            this.hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception ignored) {
        }
    }

    @Override
    public String convert(ILoggingEvent event) {
        return hostAddress;
    }

}
