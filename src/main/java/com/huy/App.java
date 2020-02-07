package com.huy;

import com.alibaba.fastjson.annotation.JSONField;
import com.huy.sensitive.constants.Constant;
import com.huy.sensitive.serializer.*;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * Hello world!
 */
@Slf4j
public class App {

    public static void main(String[] args) {
//        System.setProperty(Constant.SYSTEM_ENV_SENSITIVE_KEYWORD, "false");
        //备注：身份证使用工具随机生成
        Person person = new Person().setId("110101199003072375")
                .setName("王矬锉")
                .setPhone("15012345678")
                .setEmail("10876572@qq.com")
                .setAddress("贵州省遵义市130号公路13-2-28");
        log.debug("logback 脱敏测试 {}####{}，object:{}", "110101199003072375,11010119900307897X,15023451241", "abc3456@163.com", person);
    }

    @Data
    @Accessors(chain = true)
    static class Person {

        @JSONField(serializeUsing = ChineseNameSensitiveSerializer.class)
        private String name;

        @JSONField(serializeUsing = IdSensitiveSerializer.class)
        private String id;

        @JSONField(serializeUsing = PhoneSensitiveSerializer.class)
        private String phone;

        @JSONField(serializeUsing = AddressSensitiveSerializer.class)
        private String address;

        @JSONField(serializeUsing = EmailSensitiveSerializer.class)
        private String email;

    }

}

