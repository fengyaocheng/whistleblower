package com.github.fengyaocheng.whistleblower.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 吹哨人服务核心属性配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "whistleblower")
public class WhistleblowerProperties {

    /**
     * 需要监控的开源仓库列表（例如：owner/repo）
     */
    private List<String> monitoredRepos = new ArrayList<>();

    /**
     * Markdown 日报生成的输出目录
     */
    private String outputDir = "reports";

    /**
     * 是否自动生成报告
     */
    private boolean generateReport = true;

    /**
     * GitHub Personal Access Token (可选，配置后可提高 API 调用频率上限)
     */
    private String githubToken;

    /**
     * 是否为 CLI 批处理单次模式（为 true 时执行完巡检即退出，适用于 CI/CD）
     */
    private boolean cliMode = false;
}
