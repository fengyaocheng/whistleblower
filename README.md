# 📢 吹哨人 (Whistleblower)

<p align="center">
  <strong>开源软件版本动态、安全漏洞情报智能哨兵 & 自动化定时监控助手</strong>
</p>

<p align="center">
  🌐 <strong>在线仪表盘 Demo:</strong> <a href="https://wb.rimutuyuan.com" target="_blank">https://wb.rimutuyuan.com</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-orange.svg?style=flat-square&logo=openjdk" alt="Java 21" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg?style=flat-square&logo=springboot" alt="Spring Boot 3" />
  <img src="https://img.shields.io/badge/License-MIT-blue.svg?style=flat-square" alt="License MIT" />
  <img src="https://img.shields.io/badge/Automation-GitHub%20Actions-blueviolet.svg?style=flat-square&logo=githubactions" alt="GitHub Actions" />
  <img src="https://img.shields.io/badge/Dependabot-Active-success.svg?style=flat-square&logo=dependabot" alt="Dependabot" />
</p>

---

## 📖 项目简介 (Introduction)

> **“吹哨人” (Whistleblower)**：旨在成为开发者与运维工程师身边的安全与技术动态“哨兵”。

在日常开发与架构设计中，团队往往依赖数十个核心开源组件（如 Spring Boot、Nacos、Dubbo、Kubernetes、Redis 等）。一旦核心依赖发布了重大功能版本或紧急修复了**高危安全漏洞 (CVE)**，团队若无法及时感知，将面临巨大的安全隐患与维护滞后。

**吹哨人 (Whistleblower)** 专为解决此痛点而生：
- 🔍 **全天候巡检**：定时探测关注的开源项目最新 Release 变更与安全公告。
- 📊 **自动生成情报快报**：格式化生成清晰的 Markdown 日报并归档。
- ⚡ **原生无服务器运行**：直接托管于 GitHub Actions，**无需购买服务器，无需本地安装 Java 21**，全自动定时产出并提交代码。

---

## ✨ 核心特性 (Features)

* **极简优雅**：基于最新的 **Java 21** 与 **Spring Boot 3**，采用现代 HTTP 客户端，启动飞快，资源占用极小。
* **双模式运行**：
  * **CI/CD 任务模式**：与 GitHub Actions 深度融合，定时巡检并在完成之后自动将报告提交回仓库。
  * **独立应用模式**：支持打包为可执行 Jar 或 Docker 容器常驻运行。
* **零成本白嫖**：利用 GitHub 官方基础设施作为算力引擎与存储介质，开箱即用。
* **高度可扩展**：支持灵活配置关注的仓库列表、自定义输出格式，未来将支持企微/钉钉/飞书等即时通讯报警推送。

---

## 🛠️ 项目结构 (Architecture)

```text
whistleblower/
├── .github/
│   ├── workflows/
│   │   └── daily-pulse.yml      # GitHub Actions 自动化定时巡检与推送工作流
│   └── dependabot.yml           # 依赖安全与版本自动升级机器人配置
├── reports/                     # 每日自动生成的开源情报快报归档目录
├── src/
│   ├── main/
│   │   ├── java/com/github/fengyaocheng/whistleblower/
│   │   │   ├── config/          # 核心参数配置绑定
│   │   │   ├── model/           # 数据模型 (ReleaseInfo, SecurityAdvisory)
│   │   │   ├── service/         # GitHub API 探查与 Markdown 日报生成服务
│   │   │   ├── runner/          # 命令行任务执行器
│   │   │   └── WhistleblowerApplication.java # 启动入口
│   │   └── resources/
│   │       └── application.yml  # 基础配置文件（监控仓库列表等）
│   └── test/                    # 单元测试
├── pom.xml                      # Maven 构建配置
├── LICENSE                      # MIT 开源协议
└── README.md                    # 项目说明文档
```

---

## 🚀 快速上手 (Quick Start)

### 方式 1：云端自动化（推荐，完全无需本地环境）

1. **Fork 本仓库** 或将本项目推送至你自己的 GitHub 仓库。
2. 仓库自带的 GitHub Actions 工作流会在**每天定时自动运行**。
3. 你也可以随时在仓库页面点击 **Actions** -> **Daily Whistle & Auto-Commit** -> **Run workflow** 手动触发一次巡检。
4. 巡检完成后，生成的报告将自动提交到 `reports/` 目录下！

---

### 方式 2：本地开发与调试

#### 1. 环境要求
* **JDK 21+**（推荐在 IntelliJ IDEA 中直接一键下载安装）
* **Maven 3.8+**

#### 2. 自定义监控仓库
在 `src/main/resources/application.yml` 中配置你感兴趣的开源仓库：

```yaml
whistleblower:
  monitored-repos:
    - spring-projects/spring-boot
    - alibaba/nacos
    - apache/dubbo
    - kubernetes/kubernetes
    - redis/redis
```

#### 3. 运行项目
```bash
# 编译并运行
mvn compile exec:java -Dexec.mainClass="com.github.fengyaocheng.whistleblower.WhistleblowerApplication"
```

运行完成后，在 `reports/` 目录下即可查看到最新生成的 Markdown 日报！

---

## 📄 开源许可证 (License)

本项目基于 [MIT 许可证](LICENSE) 开源，欢迎自由使用、修改与贡献！
