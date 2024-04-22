package com.yeyue.yyzbot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yeyue.yyzbot.event.dao")
public class YyzBotApplication {

    public static void main(String[] args) {
//        System.setProperty("socksProxyHost","localhost");//如果不能访问chatGPT的话自行配置代理
//        System.setProperty("socksProxyPort","10808");//如果不能访问chatGPT的话自行配置代理
        SpringApplication.run(YyzBotApplication.class, args);
    }

}
