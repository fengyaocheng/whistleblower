package com.github.fengyaocheng.whistleblower.service;

import com.github.fengyaocheng.whistleblower.config.WhistleblowerProperties;
import com.github.fengyaocheng.whistleblower.model.RepoReleaseInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 报告生成器服务：将探测收集到的开源情报格式化并输出为 Markdown 日报
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReportGeneratorService {

    private final WhistleblowerProperties properties;

    /**
     * 生成日报文件
     *
     * @param releaseInfos 扫描获取到的发布列表
     * @return 生成的文件绝对路径
     */
    public Path generateDailyReport(List<RepoReleaseInfo> releaseInfos) throws IOException {
        String todayStr = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String fileName = String.format("whistleblower-report-%s.md", todayStr);

        Path outputDirPath = Paths.get(properties.getOutputDir());
        if (!Files.exists(outputDirPath)) {
            Files.createDirectories(outputDirPath);
        }

        Path targetFilePath = outputDirPath.resolve(fileName);

        StringBuilder sb = new StringBuilder();
        sb.append("# 📢 吹哨人 (Whistleblower) - 开源安全与版本动态日报\n\n");
        sb.append("> **生成日期**: `").append(todayStr).append("`  \n");
        sb.append("> **监控项目总数**: `").append(properties.getMonitoredRepos().size()).append("`  \n");
        sb.append("> **状态**: ✅ 自动化巡检完毕\n\n");
        sb.append("---\n\n");

        sb.append("## 📦 关注项目最新版本矩阵\n\n");
        sb.append("| 仓库名 | 最新版本 Tag | 发布时间 | 详情链接 |\n");
        sb.append("| :--- | :--- | :--- | :--- |\n");

        for (RepoReleaseInfo info : releaseInfos) {
            String pubTime = info.getPublishedAt() != null ?
                    info.getPublishedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "未知";
            sb.append(String.format("| **[%s](https://github.com/%s)** | `%s` | %s | [查看 Release](%s) |\n",
                    info.getRepoName(), info.getRepoName(), info.getTagName(), pubTime, info.getHtmlUrl()));
        }

        sb.append("\n---\n\n");
        sb.append("## 📝 最新版本发布详细速递\n\n");

        if (releaseInfos.isEmpty()) {
            sb.append("_本次巡检未捕获到新增版本或网络暂时不可达。_\n");
        } else {
            for (RepoReleaseInfo info : releaseInfos) {
                sb.append(String.format("### 🔔 %s `%s`\n\n", info.getRepoName(), info.getTagName()));
                sb.append(String.format("- **Release 标题**: %s\n", info.getName()));
                sb.append(String.format("- **发布地址**: [%s](%s)\n\n", info.getHtmlUrl(), info.getHtmlUrl()));
                
                String body = info.getBody();
                if (body != null && !body.isBlank()) {
                    // 截取前 300 个字符展示摘要，防止单页过长
                    String preview = body.length() > 300 ? body.substring(0, 300) + "\n\n...(省略更多更新日志，请查看原文)" : body;
                    sb.append("```markdown\n").append(preview.trim()).append("\n```\n\n");
                }
            }
        }

        sb.append("\n---\n");
        sb.append("🤖 _由 [吹哨人 (Whistleblower)](https://github.com/fengyaocheng/whistleblower) 自动化机器人于 GitHub Actions 定时执行生成。_\n");

        Files.writeString(targetFilePath, sb.toString(), StandardCharsets.UTF_8);
        log.info("🎉 吹哨人日报已成功生成至: {}", targetFilePath.toAbsolutePath());
        return targetFilePath;
    }
}
