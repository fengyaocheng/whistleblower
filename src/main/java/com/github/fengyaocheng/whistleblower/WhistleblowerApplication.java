package com.github.fengyaocheng.whistleblower;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 吹哨人 (Whistleblower) 启动主类
 * 
 * 专为开源软件版本动态、安全漏洞情报提供自动化定时预警与监控
 */
@SpringBootApplication
public class WhistleblowerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhistleblowerApplication.class, args);
    }
}
