package com.github.fengyaocheng.whistleblower.controller;

import com.github.fengyaocheng.whistleblower.config.WhistleblowerProperties;
import com.github.fengyaocheng.whistleblower.model.RepoReleaseInfo;
import com.github.fengyaocheng.whistleblower.service.GitHubSentinelService;
import com.github.fengyaocheng.whistleblower.service.ReportGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 吹哨人 Web 接口控制器：为前端仪表盘提供数据交互支持
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // 允许前端本地跨域调用
public class SentinelWebController {

    private final WhistleblowerProperties properties;
    private final GitHubSentinelService sentinelService;
    private final ReportGeneratorService reportGeneratorService;

    /**
     * 获取系统健康状态与基础配置
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "UP");
        map.put("appName", "Whistleblower 吹哨人");
        map.put("version", "1.0.0");
        map.put("monitoredCount", properties.getMonitoredRepos().size());
        map.put("monitoredRepos", properties.getMonitoredRepos());
        map.put("reportCount", reportGeneratorService.listAvailableReports().size());
        return ResponseEntity.ok(map);
    }

    /**
     * 获取最新一次巡检捕获的各个项目 Release 详情
     */
    @GetMapping("/latest")
    public ResponseEntity<List<RepoReleaseInfo>> getLatestReleases() {
        List<RepoReleaseInfo> list = sentinelService.getLatestInspectionResults();
        if (list.isEmpty()) {
            // 如果缓存为空，则主动执行一次全量探查
            list = sentinelService.inspectAll();
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 手动触发一次即时全量巡检并重新生成日报
     */
    @PostMapping("/scan")
    public ResponseEntity<Map<String, Object>> triggerManualScan() throws IOException {
        log.info("收到前端手动巡检请求，开始执行...");
        List<RepoReleaseInfo> results = sentinelService.inspectAll();
        java.nio.file.Path reportPath = reportGeneratorService.generateDailyReport(results);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "巡检完成，已生成最新日报");
        response.put("reportFile", reportPath.getFileName().toString());
        response.put("count", results.size());
        response.put("results", results);
        return ResponseEntity.ok(response);
    }

    /**
     * 获取所有可用的历史 Markdown 报表列表
     */
    @GetMapping("/reports")
    public ResponseEntity<List<String>> listReports() {
        return ResponseEntity.ok(reportGeneratorService.listAvailableReports());
    }

    /**
     * 获取指定 Markdown 日报的文本内容
     */
    @GetMapping(value = "/reports/{fileName}", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
    public ResponseEntity<String> getReportContent(@PathVariable String fileName) {
        try {
            String content = reportGeneratorService.readReportContent(fileName);
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("无法读取报表: " + e.getMessage());
        }
    }
}
