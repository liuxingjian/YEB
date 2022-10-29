package com.cdh.server.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author by cdh
 * @description:验证码配置类
 * @Date: Created in 22:02 on 2021/4/18
 */
@Configuration
public class CaptchaConfig {
    @Bean
    public DefaultKaptcha defaultKaptcha(){
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 是否有边框
        properties.setProperty("kaptcha.border", "yes");
        // 设置边框颜色
        properties.setProperty("kaptcha.border.color", "105,179,90");
        // 边框粗细度，默认为1
        properties.setProperty("kaptcha.border.thinckness", "1");
        // 验证码
        properties.setProperty("kaptcha.session.key", "code");
        // 验证码文本字符颜色，默认为黑色
        properties.setProperty("kaptcha.textproducer.font.color", "blue");
        // 设置字体样式
        properties.setProperty("kaptcha.textproducer.font.name", "宋体,楷体,微软雅黑");
        // 字体大小，默认40
        properties.setProperty("kaptcha.textproducer.font.size", "30");
        // 验证码文本字符内容范围，默认为abcde2345678fynmpwx
        properties.setProperty("kaptcha.textproducer.char.string", "");
        // 字符长度，默认为5
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        // 字符间距，默认为2
        properties.setProperty("kaptcha.textproducer.char.space", "2");
        // 验证码图片宽度，默认为200
        properties.setProperty("kaptcha.image.width","100");
        // 验证码图片高度，默认为40
        properties.setProperty("kaptcha.image.height","40");

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;

    }
}
