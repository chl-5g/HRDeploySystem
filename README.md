# HRDeploySystem — 人力资源智能部署系统

## 项目简介

基于 **Spring Boot 2.7** + **Spring Data JPA** + **Vue 3** 的人力资源管理系统，核心功能是通过 **ID3 决策树算法** 实现新员工岗位智能匹配，以及基于绩效评分的老员工岗位评估。

> 这是我第一个完整项目的现代化重构版本。原版基于 Spring 4 + Hibernate 4 + JSP（2017），现已全面升级为前后端分离的现代架构。

## 技术栈

| 层面 | 技术 |
|------|------|
| **后端** | Spring Boot 2.7、Spring Data JPA、HikariCP |
| **数据库** | MySQL 8.0 |
| **前端** | Vue 3 + Vite + Vue Router + Axios |
| **算法** | ID3 决策树（纯 Java 实现，训练数据存储在数据库中） |
| **API 文档** | SpringDoc OpenAPI（Swagger UI） |
| **测试** | JUnit 5 + Mockito |
| **部署** | Docker + docker-compose |

## 核心功能

### 1. 基础人事管理
- 员工信息 CRUD（基本信息、学历、岗位）
- 部门管理

### 2. 新员工智能匹配
- 输入：学历类别、学历、专业、毕业院校
- 算法：ID3 决策树（基于信息增益的特征分裂）
- 输出：推荐部门 + 推荐职级
- 训练样本存储在数据库 `training_sample` 表中，支持运行时增删和决策树重建

### 3. 老员工评估
- 输入：能力/业绩/态度三项评分（1-10）
- 算法：加权综合评分（0.4/0.3/0.3）
- 输出：建议职级调整

## 项目结构

```
HRDeploySystem/
├── pom.xml                          # Spring Boot 2.7 + JPA + MySQL
├── src/main/java/org/caihaolun/
│   ├── HrDeployApplication.java     # 启动类
│   ├── config/WebConfig.java        # CORS 配置
│   ├── model/                       # JPA 实体（11个）
│   ├── repository/                  # Spring Data JPA 接口（6个）
│   ├── service/
│   │   ├── ID3DecisionTree.java     # ID3 算法实现
│   │   └── MatchService.java        # 智能匹配服务
│   └── controller/                  # REST API（4个）
├── src/main/resources/
│   ├── application.yml              # 配置
│   └── data.sql                     # 训练样本初始数据
├── src/test/java/                   # JUnit 5 测试
├── frontend/                        # Vue 3 + Vite
│   ├── src/views/                   # 页面组件
│   └── src/api/http.js              # Axios 封装
├── Dockerfile
├── docker-compose.yml
└── .gitignore
```

## API 接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/auth/login` | 登录 |
| POST | `/api/auth/register` | 注册 |
| GET | `/api/staff` | 员工列表 |
| GET | `/api/staff/{id}` | 员工详情（含学历/岗位） |
| POST | `/api/staff` | 新增员工 |
| PUT | `/api/staff/{id}` | 更新员工 |
| DELETE | `/api/staff/{id}` | 删除员工 |
| GET/POST | `/api/staff/edu` | 学历管理 |
| GET/POST | `/api/staff/post` | 岗位管理 |
| GET | `/api/dept` | 部门列表 |
| POST | `/api/match/new` | 新员工智能匹配 |
| POST | `/api/match/old` | 老员工评估 |
| GET | `/api/match/samples` | 查看训练样本 |
| POST | `/api/match/samples` | 添加训练样本并重建决策树 |
| POST | `/api/match/rebuild` | 手动重建决策树 |

启动后访问 `http://localhost:8080/swagger-ui.html` 查看完整 API 文档。

## 快速开始

### 环境要求
- JDK 14+
- Maven 3.x
- MySQL 8.x
- Node.js 18+（前端开发）

### 1. 数据库准备

```bash
mysql -uroot -p -e "CREATE DATABASE IF NOT EXISTS hrdeploysystem CHARACTER SET utf8mb4;"
```

### 2. 启动后端

```bash
mvn spring-boot:run
```

首次启动会自动建表并导入训练样本数据。

### 3. 启动前端（开发模式）

```bash
cd frontend
npm install
npm run dev
```

访问 `http://localhost:5173`。

### 4. Docker 一键启动

```bash
mvn package -DskipTests
docker-compose up --build
```

## 测试

```bash
mvn test
```

当前通过 9 个测试用例：
- `ID3DecisionTreeTest`（4）：训练、预测、边界条件
- `MatchServiceTest`（5）：新员工匹配、老员工评估、特征归一化

## 与 v1.0 的主要变化

| 维度 | v1.0（2017） | v2.0（2026） |
|------|-------------|-------------|
| 框架 | Spring 4.1 + Hibernate 4.3 + XML 配置 | Spring Boot 2.7 + Spring Data JPA + YAML |
| 前端 | JSP + jQuery | Vue 3 + Vite |
| API | JSP 表单提交 + 页面跳转 | RESTful JSON API |
| 连接池 | c3p0（3个重复依赖） | HikariCP（零配置） |
| 数据层 | HibernateDaoSupport + 手动 CRUD | JpaRepository 接口（零代码） |
| DI | Controller 里 `new ClassPathXmlApplicationContext()` | 构造器注入 |
| 线程安全 | `static privateStaff` 跨请求共享 | 无状态 REST |
| 训练数据 | Java 代码硬编码 | 数据库存储，运行时可更新 |
| 测试 | 无 | JUnit 5 + Mockito |
| 部署 | WAR + Tomcat | JAR + Docker |
| API 文档 | 无 | Swagger UI |
