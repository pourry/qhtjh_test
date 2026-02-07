# Spring Boot 项目 JDK 8 升级到 JDK 17 指南

## 已完成的更改

### 1. Maven 配置更新 (pom.xml)
- ✅ Spring Boot 版本从 2.5.7 升级到 3.2.2
- ✅ Java 版本已设置为 17
- ✅ Swagger 从 springfox 2.x 升级到 springdoc 2.3.0 (兼容 Spring Boot 3.x)
- ✅ BouncyCastle 从 jdk15on 升级到 jdk18on
- ✅ MySQL 驱动版本更新到 8.0.33
- ✅ JWT 库从单一依赖升级到模块化依赖 (0.12.3)

### 2. 应用程序类更新
- ✅ 修复了 @MapperScan 注解的包路径

## 需要手动完成的步骤

### 1. 安装 JDK 17
确保你的开发环境已安装 JDK 17：
```bash
java -version
```
应该显示类似：`openjdk version "17.0.x"`

### 2. IDE 配置
- 在 IntelliJ IDEA 中：File → Project Structure → Project → Project SDK 选择 JDK 17
- 在 Eclipse 中：项目右键 → Properties → Java Build Path → Libraries → Modulepath/Classpath → JRE System Library

### 3. 可能需要的代码更改

#### 3.1 Swagger 配置更新
如果你有 Swagger 配置类，需要更新：

**旧的 Swagger2 配置 (删除):**
```java
@EnableSwagger2
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)...
    }
}
```

**新的 SpringDoc 配置 (可选):**
```java
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API Documentation")
                .version("1.0")
                .description("Spring Boot API"));
    }
}
```

#### 3.2 JWT 代码更新
由于 JWT 库升级，可能需要更新相关代码：

**检查 TokenUtill.java 文件**，如果使用了旧的 JWT API，需要更新。

#### 3.3 javax 到 jakarta 包名更改
Spring Boot 3.x 使用 Jakarta EE，需要将所有 `javax.*` 导入更改为 `jakarta.*`：

- `javax.servlet.*` → `jakarta.servlet.*`
- `javax.persistence.*` → `jakarta.persistence.*`
- `javax.validation.*` → `jakarta.validation.*`

### 4. 测试和验证

1. **清理并重新构建项目:**
```bash
mvn clean compile
```

2. **运行应用程序:**
```bash
mvn spring-boot:run
```

3. **检查日志**确保没有兼容性错误

4. **访问 Swagger UI** (如果配置了):
   - 旧地址: `http://localhost:8001/swagger-ui.html`
   - 新地址: `http://localhost:8001/swagger-ui/index.html`

## 潜在问题和解决方案

### 1. 依赖冲突
如果遇到依赖冲突，运行：
```bash
mvn dependency:tree
```
查找冲突的依赖并排除。

### 2. 编译错误
- 检查是否有使用了已废弃的 API
- 更新所有 `javax.*` 导入为 `jakarta.*`

### 3. 运行时错误
- 检查配置文件中的属性名是否有变化
- 确保所有第三方库都兼容 Spring Boot 3.x

## 建议的测试步骤

1. 单元测试是否通过
2. 应用程序是否正常启动
3. 数据库连接是否正常
4. API 端点是否正常工作
5. WebSocket 连接是否正常
6. 文件上传功能是否正常

## 回滚计划

如果升级过程中遇到问题，可以：
1. 恢复 pom.xml 到原始版本
2. 使用 Git 回滚到升级前的提交
3. 逐步升级：先升级到 Spring Boot 2.7.x，再升级到 3.x