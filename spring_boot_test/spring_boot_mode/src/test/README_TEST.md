# 测试文档

## 测试结构

```
src/test/java/com/example/spring_boot_mode/
├── SpringBootModeApplicationTests.java          # 应用启动测试
├── IntegrationTestSuite.java                    # 集成测试套件
├── config/
│   └── DataSourceConfigTest.java               # 数据源配置测试
├── model/
│   ├── service/
│   │   └── DatabaseBackupServiceTest.java      # 服务层测试
│   └── web/
│       └── DatabaseBackupControllerTest.java   # 控制器测试
├── utils/
│   └── ResponseUtilTest.java                   # 工具类测试
└── performance/
    └── DatabaseBackupPerformanceTest.java      # 性能测试
```

## 测试类说明

### 1. SpringBootModeApplicationTests
**功能：** 应用启动和基础配置测试
- 测试应用上下文加载
- 测试主要Bean是否正确加载
- 测试应用配置属性

### 2. DataSourceConfigTest
**功能：** 多数据源配置测试
- 测试主数据源连接
- 测试Flowable数据源连接
- 测试两个数据源的独立性

### 3. DatabaseBackupServiceTest
**功能：** 数据库备份服务测试
- 测试数据库备份功能
- 测试备份文件生成
- 测试备份文件内容格式
- 测试获取备份记录列表

### 4. DatabaseBackupControllerTest
**功能：** 控制器接口测试
- 测试执行备份接口
- 测试获取备份文件列表接口
- 测试获取备份记录接口

### 5. ResponseUtilTest
**功能：** 响应工具类测试
- 测试成功响应
- 测试错误响应
- 测试Token过期响应

### 6. DatabaseBackupPerformanceTest
**功能：** 性能测试（默认禁用）
- 测试备份执行时间
- 测试连续备份性能
- 测试内存使用情况

## 运行测试

### 运行所有测试
```bash
mvn test
```

### 运行单个测试类
```bash
# 运行应用启动测试
mvn test -Dtest=SpringBootModeApplicationTests

# 运行数据源配置测试
mvn test -Dtest=DataSourceConfigTest

# 运行数据库备份服务测试
mvn test -Dtest=DatabaseBackupServiceTest

# 运行控制器测试
mvn test -Dtest=DatabaseBackupControllerTest
```

### 运行测试套件
```bash
mvn test -Dtest=IntegrationTestSuite
```

### 运行性能测试（需要手动启用）
```bash
# 编辑测试类，移除 @Disabled 注解后运行
mvn test -Dtest=DatabaseBackupPerformanceTest
```

### 跳过测试
```bash
mvn clean install -DskipTests
```

## IDE 中运行测试

### IntelliJ IDEA
1. 右键点击测试类或测试方法
2. 选择 "Run 'TestClassName'" 或 "Run 'testMethodName()'"
3. 查看测试结果面板

### Eclipse
1. 右键点击测试类
2. 选择 "Run As" -> "JUnit Test"
3. 查看 JUnit 视图中的测试结果

### VS Code
1. 安装 "Java Test Runner" 插件
2. 点击测试方法旁边的运行按钮
3. 查看测试输出

## 测试覆盖率

### 生成测试覆盖率报告
```bash
mvn clean test jacoco:report
```

报告位置：`target/site/jacoco/index.html`

### 配置 JaCoCo（在 pom.xml 中添加）
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.8</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## 测试最佳实践

### 1. 测试命名规范
- 测试类名：`XxxTest` 或 `XxxTests`
- 测试方法名：`testXxx` 或使用 `@DisplayName` 注解

### 2. 测试组织
- 单元测试：测试单个方法或类
- 集成测试：测试多个组件的交互
- 性能测试：测试系统性能指标

### 3. 断言使用
```java
// 基本断言
assertEquals(expected, actual);
assertNotNull(object);
assertTrue(condition);
assertFalse(condition);

// 异常断言
assertThrows(Exception.class, () -> {
    // 会抛出异常的代码
});

// 超时断言
assertTimeout(Duration.ofSeconds(5), () -> {
    // 需要在5秒内完成的代码
});
```

### 4. 测试数据准备
```java
@BeforeEach
void setUp() {
    // 每个测试方法执行前运行
}

@AfterEach
void tearDown() {
    // 每个测试方法执行后运行
}

@BeforeAll
static void setUpAll() {
    // 所有测试方法执行前运行一次
}

@AfterAll
static void tearDownAll() {
    // 所有测试方法执行后运行一次
}
```

## 测试输出示例

```
✅ Spring Boot 应用上下文加载成功
   Bean总数: 245

✅ 主数据源连接成功
   数据库名称: test_all
   数据库产品: MySQL
   数据库版本: 8.0.19

✅ 备份成功！文件路径: E:\project\src\main\resources\sql\primary_db_20260207.sql
✅ 文件大小: 1024567 bytes

✅ 备份接口响应: {"code":200,"success":true,"data":{"message":"数据库备份成功","filePath":"..."}}
```

## 常见问题

### 问题1：数据库连接失败
**解决方案：**
- 检查数据库是否启动
- 检查配置文件中的数据库连接信息
- 确认数据库用户权限

### 问题2：测试超时
**解决方案：**
- 增加测试超时时间
- 检查数据库性能
- 优化测试代码

### 问题3：测试文件冲突
**解决方案：**
- 使用唯一的测试文件名
- 在测试后清理生成的文件
- 使用临时目录

## 持续集成

### GitHub Actions 配置示例
```yaml
name: Java CI

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v2
    - name: Set up JDK 8
      uses: actions/setup-java@v2
      with:
        java-version: '8'
        distribution: 'adopt'
    - name: Build with Maven
      run: mvn clean test
    - name: Generate test report
      run: mvn surefire-report:report
```

## 测试报告

测试完成后，可以在以下位置查看报告：
- Surefire 报告：`target/surefire-reports/`
- JaCoCo 覆盖率报告：`target/site/jacoco/index.html`

## 联系支持

如有测试相关问题，请查看：
1. 测试日志输出
2. 错误堆栈信息
3. 数据库连接状态
4. 应用配置文件
