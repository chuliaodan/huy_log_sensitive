# 日志脱敏

    huyong/2020.02.07
    ============================================
    1.提供功能：优先推荐使用封装方式进行日志打印
        a.当待进行日志打印的数据为封装数据类型，自动json输出，如果某个字段进行脱敏输出时，使用@JSONField(serializeUsing = xx.class),
            序列化的类型需要继承AbstractSensitiveSerializer.class，默认提供的类型：
                a.1: 身份证号--com.huy.sensitive.serializer.IdSensitiveSerializer。脱敏规则：把8位生日进行脱敏，i.e. 522121********7654
                a.2: 电话------com.huy.sensitive.serializer.PhoneSensitiveSerializer。脱敏规则：长度大于等于7位，从倒数第7位开始脱敏，掩码3位。i.e. 1517***8634
                a.3: 电子邮件--com.huy.sensitive.serializer.EmailSensitiveSerializer。脱敏规则：邮箱长度(不含邮箱后缀，i.e. @qq.com) 大于等于6，把倒数4位脱敏。i.e. 106224****@qq.com
                a.4: 中文名字--com.huy.sensitive.serializer.ChineseNameSensitiveSerializer。脱敏规则：第一个字符脱敏。i.e *勇
                a.5: 地址------com.huy.sensitive.serializer.AddressSensitiveSerializer。脱敏规则：把所有数字脱敏。i.e  贵州省遵义市湄潭县***号
                a.6: 银行卡----com.huy.sensitive.serializer.DefaultSensitiveSerializer。脱敏规则：使用默认脱敏
                a.7: 默认------com.huy.sensitive.serializer.DefaultSensitiveSerializer。脱敏规则：全部脱敏
                
                需个性化脱敏，请继承：com.huy.sensitive.serializer.AbstractSensitiveSerializer 进行扩展
                
        b.当非封装类型直接使用log进行输出时，i.e. log.debug("身份证号:{}","110101199003070871")，目前字符串长度小于1000(为了控制正则效率),会自动识别进行脱敏，否者原样输出，
            则利用正则表达式，按下面进行顺序匹配(原则：位数长的优先匹配)，自动脱敏，有：
                b.1: 身份证号, 15位/18位
                b.2: 电话，手机/座机 (7-12位)
                b.3: 电子邮件
                
            此种方式，不建议直接对非封装对象进行数据输出，根据值在去判断使用哪种脱敏，敏感数据的识别完全依赖于正则表达式，很有可能识别错误，效率也较低。
         
        c.自动脱敏，需要在日志文件配置转换规则LogSensitiveConverter
            <!-- 脱敏转换器 -->
            <conversionRule conversionWord="sensitivemsg" converterClass="com.huy.sensitive.logext.converter.LogSensitiveConverter"/>
            需要在pattern中配置 %sensitivemsg 来获取脱敏后的信息。比如：
            <!-- 脱敏日志模板 -->
            <property name="LOG_PATTERN"
                      value="%d{yyyy-MM-dd HH:mm:ss.SSS} | %-5level | %pid | %thread | %X{tid:-N/A} | %class{40}#%method - %sensitivemsg %n"/>
            
            不需要脱敏，有两种方式
            c.1 直接使用原生的 %msg
                <!-- 正常日志模板 -->
                <property name="LOG_PATTERN"
                          value="%d{yyyy-MM-dd HH:mm:ss.SSS} | %-5level | %pid | %thread | %X{tid:-N/A} | %class{40}#%method - %msg %n"/>
            c.2  如果脱敏与非脱敏使用同一个模板，可以设置系统环境变量：System.setProperty("LOG_SENSITIVE", "false");
                <!-- 脱敏日志模板 -->
                <property name="LOG_PATTERN"
                          value="%d{yyyy-MM-dd HH:mm:ss.SSS} | %-5level | %pid | %thread | %X{tid:-N/A} | %class{40}#%method - %sensitivemsg %n"/>

        d.该工程扩展了两个功能：
            d.1 <!-- 进程号转换器 -->
                <conversionRule conversionWord="pid" converterClass="com.huy.sensitive.logext.converter.PidConverter"/>
                 在日志模板中可以通过 %pid 获得当前进程号，示例：
                 %d{yyyy-MM-dd HH:mm:ss.SSS} | %-5level | %pid | %thread | %X{tid:-N/A} | %class{40}#%method - %sensitivemsg %n
            
            d.2  <!-- 自定义ip属性 -->
                <define name="ip" class="com.huy.sensitive.logext.definer.IpDefiner"/>
                可以通过${ip} 获取当前机器的ip，示例
                <property name="LOG_FILE" value="${LOG_HOME}/${PROJECT_NAME}/${PROJECT_NAME}-${ip}.log"/>
            
        e.logback.xml 示例
            <?xml version="1.0" encoding="UTF-8"?>
            <configuration debug="true">
            
                <contextName>huy-log-test</contextName>
            
                <!-- 项目名字(也是日志文件名)，使用只需修改该名字即可 -->
                <property name="PROJECT_NAME" value="huy-log-test"/>
            
                <!-- 日志路径 -->
                <property name="LOG_HOME" value="d:/data/log/"/>
            
                <!-- 脱敏转换器 -->
                <conversionRule conversionWord="sensitivemsg"
                                converterClass="com.huy.sensitive.logext.converter.LogSensitiveConverter"/>
            
                <!-- 进程号转换器 -->
                <conversionRule conversionWord="pid" converterClass="com.huy.sensitive.logext.converter.PidConverter"/>
            
                <!-- 自定义ip属性 -->
                <define name="ip" class="com.huy.sensitive.logext.definer.IpDefiner"/>
            
                <!-- 日志名字 -->
                <property name="LOG_FILE" value="${LOG_HOME}/${PROJECT_NAME}/${PROJECT_NAME}-${ip}.log"/>
            
                <!-- 日志模板 -->
                <property name="LOG_PATTERN"
                          value="%d{yyyy-MM-dd HH:mm:ss.SSS} | %-5level | %pid | %thread | %X{tid:-N/A} | %class{40}#%method - %sensitivemsg %n"/>
            
                <!-- 控制台输出 -->
                <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
                    <encoder>
                        <pattern>${LOG_PATTERN}</pattern>
                    </encoder>
                </appender>
            
                <!-- 文件输出 -->
                <appender name="file" class="ch.qos.logback.core.rolling.RollingFileAppender">
                    <file>${LOG_FILE}</file>
                    <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
                        <fileNamePattern>${LOG_FILE}.%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
                        <maxFileSize>100MB</maxFileSize>
                        <maxHistory>120</maxHistory>
                        <totalSizeCap>10GB</totalSizeCap>
                    </rollingPolicy>
                    <encoder>
                        <pattern>${LOG_PATTERN}</pattern>
                    </encoder>
                </appender>
            
                <root level="debug">
                    <appender-ref ref="console"/>
                    <appender-ref ref="file"/>
                </root>
            
            </configuration>
            
        f. java 代码运行结果
            f.1 pom.xml
                <properties>
                    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                    <maven.compiler.source>1.8</maven.compiler.source>
                    <maven.compiler.target>1.8</maven.compiler.target>
                    <junit.version>4.11</junit.version>
                    <logback.version>1.2.3</logback.version>
                    <lombok.version>1.18.8</lombok.version>
                    <common.lang.version>3.9</common.lang.version>
                    <fastjson.version>1.2.62</fastjson.version>
                </properties>
            
                <dependencies>
                    <dependency>
                        <groupId>junit</groupId>
                        <artifactId>junit</artifactId>
                        <version>${junit.version}</version>
                        <scope>test</scope>
                    </dependency>
                    <dependency>
                        <groupId>ch.qos.logback</groupId>
                        <artifactId>logback-classic</artifactId>
                        <version>${logback.version}</version>
                    </dependency>
                    <dependency>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <version>${lombok.version}</version>
                        <scope>compile</scope>
                    </dependency>
                    <dependency>
                        <groupId>org.apache.commons</groupId>
                        <artifactId>commons-lang3</artifactId>
                        <version>${common.lang.version}</version>
                    </dependency>
                    <dependency>
                        <groupId>com.alibaba</groupId>
                        <artifactId>fastjson</artifactId>
                        <version>${fastjson.version}</version>
                    </dependency>
                </dependencies>
        
            f.2 java代码
                @Slf4j
                public class App {
                    public static void main(String[] args) {
                        //System.setProperty(Constant.SYSTEM_ENV_SENSITIVE_KEYWORD, "false");
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
                运行结果：
                2020-02-07 19:15:54.423 | DEBUG | 7788 | main | N/A | com.huy.App#main - logback 脱敏测试 110101********2375,110101********897X,1502***1241####ab*****@163.com，object:{"address":"贵州省遵义市***号公路**-*-**","email":"108*****@qq.com","id":"110101********2375","name":"*矬锉","phone":"1501***5678"} 
