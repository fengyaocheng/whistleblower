package com.github.fengyaocheng.whistleblower.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * 仓库安全漏洞公告与情报模型 (CVE / Security Advisory)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityAdvisory {

    /**
     * 漏洞编号 (如 GHSA-xxxx-xxxx 或 CVE-2024-xxxx)
     */
    private String ghsaId;

    /**
     * CVE 编号
     */
    private String cveId;

    /**
     * 漏洞摘要简述
     */
    private String summary;

    /**
     * 严重程度级别 (CRITICAL, HIGH, MEDIUM, LOW)
     */
    private String severity;

    /**
     * 公告发布时间
     */
    private OffsetDateTime publishedAt;

    /**
     * 公告详情链接
     */
    private String htmlUrl;
}
