package com.github.fengyaocheng.whistleblower.runner;

import com.github.fengyaocheng.whistleblower.config.WhistleblowerProperties;
import com.github.fengyaocheng.whistleblower.model.RepoReleaseInfo;
import com.github.fengyaocheng.whistleblower.service.GitHubSentinelService;
import com.github.fengyaocheng.whistleblower.service.ReportGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 命令行执行器：支持 Web 模式启动预热巡检，或在 CI/CD 下批处理执行后退出
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SentinelCommandLineRunner implements CommandLineRunner {

    private final WhistleblowerProperties properties;
    private final GitHubSentinelService sentinelService;
    private final ReportGeneratorService reportGeneratorService;

    @Override
    public void run(String... args) throws Exception {
        log.info("============== 📢 吹哨人 (Whistleblower) 启动巡检 ==============");
        List<RepoReleaseInfo> results = sentinelService.inspectAll();

        if (properties.isGenerateReport()) {
            reportGeneratorService.generateDailyReport(results);
        }

        if (properties.isCliMode()) {
            log.info("============== ✅ 批处理模式：任务执行完毕，正常退出进程 ==============");
            System.exit(0);
        } else {
            log.info("============== 🌐 Web 模式：服务已常驻运行，访问 http://localhost:8080 查看仪表盘 ==============");
        }
    }
}
