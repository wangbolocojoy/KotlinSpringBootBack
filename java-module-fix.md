# Java模块系统访问限制问题解决方案

## 问题描述

在使用Java 9或更高版本编译KotlinSpringBootBack项目时，出现以下错误：

```
java.lang.reflect.InaccessibleObjectException: Unable to make protected void java.util.ResourceBundle.setParent(java.util.ResourceBundle) accessible: module java.base does not "opens java.util" to unnamed module @1f7e52d1
```

这是因为Java 9引入的模块系统增加了访问限制，Kotlin编译器（版本1.4.30-M1）无法访问`java.util`包中的`ResourceBundle.setParent`方法。

## 解决方案

### 方案1：设置MAVEN_OPTS环境变量

在终端中执行以下命令，设置MAVEN_OPTS环境变量，允许访问受限制的Java模块：

```bash
export MAVEN_OPTS="--add-opens java.base/java.util=ALL-UNNAMED"
```

然后再执行Maven命令：

```bash
./mvnw clean package -Pprod
```

### 方案2：直接在Maven命令中添加JVM参数

```bash
./mvnw clean package -Pprod -Djvm.args="--add-opens java.base/java.util=ALL-UNNAMED"
```

### 方案3：修改.mvn/jvm.config文件

在项目根目录下创建或编辑`.mvn/jvm.config`文件，添加以下内容：

```
--add-opens java.base/java.util=ALL-UNNAMED
```

这样每次执行Maven命令时都会自动应用这些JVM参数。

### 方案4：降级Java版本

如果可能，使用Java 8来编译项目，因为Java 8没有模块系统的访问限制。

```bash
export JAVA_HOME=/path/to/java8
./mvnw clean package -Pprod
```

## 长期解决方案

1. 升级Kotlin版本到更新的版本（如1.5+），这些版本已经解决了与Java 9+模块系统的兼容性问题。

2. 在`pom.xml`中的Kotlin Maven插件配置中添加JVM参数：

```xml
<plugin>
    <groupId>org.jetbrains.kotlin</groupId>
    <artifactId>kotlin-maven-plugin</artifactId>
    <version>${kotlin.version}</version>
    <configuration>
        <!-- 现有配置 -->
        <jvmTarget>1.8</jvmTarget>
        <!-- 添加以下配置 -->
        <jvmArgs>
            <jvmArg>--add-opens</jvmArg>
            <jvmArg>java.base/java.util=ALL-UNNAMED</jvmArg>
        </jvmArgs>
    </configuration>
    <!-- 其他配置 -->
</plugin>
```

## 注意事项

- 这些解决方案是临时的，最好的方法是升级Kotlin版本。
- 使用`--add-opens`参数可能会导致安全风险，因为它绕过了Java模块系统的访问控制。
- 在生产环境中，建议使用方案3或方案5，以确保构建过程的一致性。