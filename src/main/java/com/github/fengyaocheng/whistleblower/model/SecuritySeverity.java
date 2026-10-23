package com.github.fengyaocheng.whistleblower.model;

import lombok.Getter;

/**
 * 安全漏洞威胁等级枚举
 * 符合 CVSS 3.1 评分标准规范
 */
@Getter
public enum SecuritySeverity {

    CRITICAL("严重", 4, 9.0, 10.0),
    HIGH("高危", 3, 7.0, 8.9),
    MEDIUM("中危", 2, 4.0, 6.9),
    LOW("低危", 1, 0.1, 3.9),
    NONE("无风险", 0, 0.0, 0.0);

    private final String displayName;
    private final int level;
    private final double minCvss;
    private final double maxCvss;

    SecuritySeverity(String displayName, int level, double minCvss, double maxCvss) {
        this.displayName = displayName;
        this.level = level;
        this.minCvss = minCvss;
        this.maxCvss = maxCvss;
    }

    /**
     * 根据 CVSS 评分转换为对应的安全等级
     *
     * @param score CVSS 分值 (0.0 - 10.0)
     * @return 对应的安全严重等级
     */
    public static SecuritySeverity fromCvssScore(double score) {
        if (score >= 9.0) {
            return CRITICAL;
        } else if (score >= 7.0) {
            return HIGH;
        } else if (score >= 4.0) {
            return MEDIUM;
        } else if (score > 0.0) {
            return LOW;
        } else {
            return NONE;
        }
    }

    /**
     * 判断是否属于高危及以上危害
     */
    public boolean isHighOrCritical() {
        return this.level >= HIGH.level;
    }
}
