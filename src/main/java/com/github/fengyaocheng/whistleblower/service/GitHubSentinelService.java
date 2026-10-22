package com.github.fengyaocheng.whistleblower.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fengyaocheng.whistleblower.config.WhistleblowerProperties;
import com.github.fengyaocheng.whistleblower.model.RepoReleaseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * GitHub 哨兵服务：负责请求 GitHub REST API 获取版本与公告情报
 */
@Slf4j
@Service
public class GitHubSentinelService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public GitHubSentinelService(WhistleblowerProperties properties, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        // 构建带有标准 GitHub API 请求头的客户端
        RestClient.Builder builder = RestClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .defaultHeader(HttpHeaders.USER_AGENT, "Whistleblower-Sentinel-App");

        // 如果配置了 Token 或环境变量中有 GITHUB_TOKEN，则添加 Authorization 鉴权
        String token = properties.getGithubToken();
        if (token == null || token.isBlank()) {
            token = System.getenv("GITHUB_TOKEN");
        }

        if (token != null && !token.isBlank()) {
            log.info("检测到有效 GitHub 凭据，已启用身份认证请求模式");
            builder.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        } else {
            log.warn("未配置 GitHub Token，将以匿名模式调用 API（每小时限流 60 次）");
        }

        this.restClient = builder.build();
    }

    /**
     * 获取指定仓库最新的 Release 版本信息
     *
     * @param repoFullName 仓库全路径，如 spring-projects/spring-boot
     * @return Release 详细数据
     */
    public Optional<RepoReleaseInfo> fetchLatestRelease(String repoFullName) {
        try {
            log.info("正在探查仓库最新动态: {}", repoFullName);
            String response = restClient.get()
                    .uri("/repos/{owner}/{repo}/releases/latest", (Object[]) repoFullName.split("/"))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(String.class);

            if (response == null || response.isBlank()) {
                return Optional.empty();
            }

            JsonNode node = objectMapper.readTree(response);
            OffsetDateTime publishedAt = null;
            if (node.hasNonNull("published_at")) {
                publishedAt = OffsetDateTime.parse(node.get("published_at").asText());
            }

            RepoReleaseInfo info = RepoReleaseInfo.builder()
                    .repoName(repoFullName)
                    .tagName(node.path("tag_name").asText("unknown"))
                    .name(node.path("name").asText(node.path("tag_name").asText()))
                    .htmlUrl(node.path("html_url").asText())
                    .body(node.path("body").asText(""))
                    .prerelease(node.path("prerelease").asBoolean(false))
                    .publishedAt(publishedAt)
                    .build();

            return Optional.of(info);
        } catch (Exception e) {
            log.warn("获取仓库 [{}] 最新 Release 异常或该仓库暂无正式 Release: {}", repoFullName, e.getMessage());
            return Optional.empty();
        }
    }
}
