package com.github.fengyaocheng.whistleblower.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SecuritySeverity 安全等级枚举单元测试")
class SecuritySeverityTest {

    @Test
    @DisplayName("验证 CVSS 评分映射逻辑")
    void testFromCvssScore() {
        assertEquals(SecuritySeverity.CRITICAL, SecuritySeverity.fromCvssScore(9.8));
        assertEquals(SecuritySeverity.CRITICAL, SecuritySeverity.fromCvssScore(9.0));
        assertEquals(SecuritySeverity.HIGH, SecuritySeverity.fromCvssScore(8.5));
        assertEquals(SecuritySeverity.HIGH, SecuritySeverity.fromCvssScore(7.0));
        assertEquals(SecuritySeverity.MEDIUM, SecuritySeverity.fromCvssScore(5.3));
        assertEquals(SecuritySeverity.LOW, SecuritySeverity.fromCvssScore(2.1));
        assertEquals(SecuritySeverity.NONE, SecuritySeverity.fromCvssScore(0.0));
    }

    @Test
    @DisplayName("验证高危及以上判定方法")
    void testIsHighOrCritical() {
        assertTrue(SecuritySeverity.CRITICAL.isHighOrCritical());
        assertTrue(SecuritySeverity.HIGH.isHighOrCritical());
        assertFalse(SecuritySeverity.MEDIUM.isHighOrCritical());
        assertFalse(SecuritySeverity.LOW.isHighOrCritical());
        assertFalse(SecuritySeverity.NONE.isHighOrCritical());
    }
}
