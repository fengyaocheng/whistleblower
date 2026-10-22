# ==========================================
# 阶段 1：构建环境（在容器内自动编译打包）
# ==========================================
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# 复制 pom.xml 和源码
COPY pom.xml .
COPY src ./src

# 执行打包构建（跳过测试加速）
RUN mvn clean package -DskipTests

# ==========================================
# 阶段 2：生产运行环境（极轻量 Alpine JRE 21）
# ==========================================
FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="fengyaocheng"

WORKDIR /app

# 创建持久化报表目录
RUN mkdir -p /app/reports

# 从构建阶段拷贝编译好的 Jar 包
COPY --from=builder /build/target/whistleblower-1.0.0-SNAPSHOT.jar /app/whistleblower.jar

# 暴露 Web 端口
EXPOSE 8080

# 优化 JVM 内存参数（适应云服务器小内存：初始 64M，最大 256M，极大减少资源占用）
ENTRYPOINT ["java", "-XX:+UseG1GC", "-Xms64m", "-Xmx256m", "-Dfile.encoding=UTF-8", "-jar", "/app/whistleblower.jar"]
