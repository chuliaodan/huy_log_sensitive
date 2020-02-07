package com.huy.sensitive.logext.definer;

import ch.qos.logback.core.PropertyDefinerBase;

import java.net.InetAddress;

/**
 * 获取本机ip
 *
 * @author: huyong
 * @since: 2020/2/7 11:55
 */
public class IpDefiner extends PropertyDefinerBase {

    /**
     * 本机地址
     */
    private String hostAddress;

    public IpDefiner() {
        try {
            this.hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception ignored) {
        }
    }

    @Override
    public String getPropertyValue() {
        return hostAddress;
    }
}
