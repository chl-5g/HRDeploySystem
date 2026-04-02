# HRDeploySystem

## 项目缘起

这是我人生里认真做完的第一个完整项目。  
前后大概投入了两个月：后端约一周，前端一个多月，剩下时间都在补框架、补算法和反复调试。  
项目虽然有很多不成熟的地方，但它是我从“会写一点代码”走向“能交付一个系统”的起点。

这是一个基于 `Spring + Spring MVC + Hibernate` 的传统 Java Web 人力资源管理系统（WAR 项目）。

项目核心目标是两部分：
- 基础人事管理：员工、学历、岗位等信息维护。
- 智能匹配模块：对新员工进行岗位/级别推荐，对老员工进行评估辅助。

## 当前功能概览

### 1) 基础人事模块
- 员工基础信息管理（新增、查询、更新、离职）。
- 学历信息管理（含学历变更历史）。
- 岗位信息管理与相关展示页面。

### 2) 智能匹配模块
- 新员工智能分配：基于学历类别、学历、专业、毕业院校进行部门与级别推荐。
- 老员工评估：基于能力/业绩等评分辅助岗位调整（现有规则逻辑）。

## 关键更新（已落地）

### 新员工匹配已接入 ID3 决策树
- 位置：`src/main/Java/org/caihaolun/service/CalculateNewStaff.java`
- 新增：`src/main/Java/org/caihaolun/service/ID3DecisionTree.java`
- 方式：离散特征 ID3 训练 + 预测，输出 `部门|级别`。
- 回退策略：预测异常时自动走兜底等级逻辑，保证服务可用。

### 修复输入映射错误
- 位置：`src/main/Java/org/caihaolun/controller/StaffPatternController.java`
- 修复点：`graduate` 由错误的 `getMajor()` 改为 `getGraduate()`，避免院校特征失真。

### Maven 构建兼容性调整
- 在 `pom.xml` 中新增 `javax.annotation-api`（兼容较新 JDK 编译）。
- 排除不可解析的传递依赖 `com.intellify:intellify-api`。
- 升级 `maven-war-plugin` 到 `3.4.0`，避免旧插件与新 JDK 不兼容。

### 前后端解耦第一步（已开始）
- 新增 API：`POST /api/staff/match/new`
- 新增 API：`POST /api/staff/match/old`
- 位置：`src/main/Java/org/caihaolun/controller/StaffMatchApiController.java`
- 说明：在保留原 JSP 流程的前提下，先将“新员工智能匹配/老员工评估”能力 API 化，后续可直接接 Vue/React 等新前端。
- 请求 JSON 示例：
```json
{
  "category": "理工类",
  "qual": "本科",
  "major": "计算机科学与技术",
  "graduate": "北京大学"
}
```
- 响应 JSON 示例：
```json
{
  "deptName": "研发部",
  "level": 3,
  "raw": {
    "部门名称": "研发部",
    "级别": "3"
  }
}
```

- 老员工评估请求 JSON 示例（建议直接传岗位与职级，当前接口按请求体计算）：
```json
{
  "score1": "8",
  "score2": "7",
  "score3": "9",
  "post": "研发部",
  "rank": "P5"
}
```

## 技术栈

- Java 8 语法目标（`maven.compiler.source/target=1.8`）
- Spring 4.1.3
- Spring MVC
- Hibernate 4.3.5
- JSP + JSTL
- MySQL + c3p0
- Maven（打包类型：`war`）

## 项目结构（核心）

- `src/main/Java/org/caihaolun/controller`：Web 控制层
- `src/main/Java/org/caihaolun/dao`：数据访问层
- `src/main/Java/org/caihaolun/model`：实体模型
- `src/main/Java/org/caihaolun/service`：业务逻辑（含智能匹配）
- `src/main/resources/applicationContext.xml`：Spring/Hibernate 配置
- `src/main/resources/dispatcher.xml`：Spring MVC 配置
- `src/main/resources/jdbc.properties`：数据库连接配置
- `src/main/webapp`：JSP 页面与静态资源

## 本地运行

### 1) 环境准备
- JDK 14（当前机器已验证可编译通过）
- Maven 3.x
- MySQL 5.x/8.x（需提前创建数据库）

### 2) 配置数据库
编辑 `src/main/resources/jdbc.properties`：
- `jdbc.url`
- `jdbc.username`
- `jdbc.password`

默认示例：
- 库名：`hrdeploysystem`
- 用户：`root`
- 密码：`1234`（建议改为本地实际密码）

### 2.1) 数据库当前状态（本仓库已按此验证）
- MySQL：`8.0.x`，本地监听 `127.0.0.1:3306`。
- JDBC 驱动：`com.mysql.cj.jdbc.Driver`（`mysql-connector-j 8.0.33`）。
- 连接参数（默认）：`serverTimezone=Asia/Shanghai`、`useSSL=false`、`allowPublicKeyRetrieval=true`。
- 已使用库：`hrdeploysystem`。
- 登录依赖表：`user_`（字段：`email` 主键、`username`、`password`）。

可用以下命令快速自检：
```bash
nc -zv 127.0.0.1 3306
mysql -h127.0.0.1 -P3306 -uroot -p -e "USE hrdeploysystem; SHOW TABLES LIKE 'user_';"
```

若 `user_` 不存在，可先初始化：
```sql
CREATE TABLE IF NOT EXISTS user_ (
  email VARCHAR(128) NOT NULL,
  username VARCHAR(128),
  password VARCHAR(128),
  PRIMARY KEY (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

### 3) 编译打包
```bash
mvn -DskipTests package
```

成功后产物：
- `target/hrdeploysystem2.war`

### 4) 部署
将 `war` 部署到 Tomcat 等 Servlet 容器，启动后访问登录页（`login.jsp`）。

## 已知问题

- `pom.xml` 中存在重复的 `c3p0` 依赖声明，当前不影响打包，但建议后续清理。
- 暂无自动化测试用例，算法和业务回归主要依赖手工验证。
- 页面为传统 JSP 结构，前端可维护性一般，适合后续逐步模块化改造。

## 后续建议

- 为 `CalculateNewStaff` 增加最小单测，锁定 ID3 输出稳定性。
- 将 ID3 训练样本从硬编码迁移到配置或数据库。
- 清理重复依赖并补充标准 `.gitignore`（如 `target/`）。
