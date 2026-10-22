package com.github.fengyaocheng.whistleblower.runner;

import com.github.fengyaocheng.whistleblower.config.WhistleblowerProperties;
import com.github.fengyaocheng.whistleblower.model.RepoReleaseInfo;
import com.github.fengyaocheng.whistleblower.service.GitHubSentinelService;
import com.github.fengyaocheng.whistleblower.service.ReportGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 命令行执行器：支持作为独立任务执行、批处理执行后平稳退出
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
        List<String> repos = properties.getMonitoredRepos();
        log.info("待探查目标仓库总计: {} 个", repos.size());

        List<RepoReleaseInfo> results = new ArrayList<>();
        for (String repo : repos) {
            sentinelService.fetchLatestRelease(repo).ifPresent(results::add);
        }

        log.info("巡检完毕，成功获取 {} 个仓库的有效版本信息", results.size());

        if (properties.isGenerateReport()) {
            reportGeneratorService.generateDailyReport(results);
        }

        log.info("============== ✅ 吹哨人任务圆满完成，即将退出 ==============");
    }
}
