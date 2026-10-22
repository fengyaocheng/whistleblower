package com.github.fengyaocheng.whistleblower.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * 仓库最新 Release 版本信息模型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepoReleaseInfo {

    /**
     * 仓库全名，如 spring-projects/spring-boot
     */
    private String repoName;

    /**
     * Release 标签名，如 v3.3.4
     */
    private String tagName;

    /**
     * Release 标题名称
     */
    private String name;

    /**
     * Release 发布时间
     */
    private OffsetDateTime publishedAt;

    /**
     * 访问链接
     */
    private String htmlUrl;

    /**
     * 版本发布简要说明
     */
    private String body;

    /**
     * 是否为预发布版本 (Pre-release)
     */
    private boolean prerelease;
}
