# Redis连接错误解决方案

## 问题描述

应用程序启动时出现以下错误：

```
ERROR c.b.b.u.ControllerAdviceProcessor[46]: code: 500  msg: Could not get a resource from the pool; nested exception is io.lettuce.core.RedisConnectionException: Unable to connect to 127.0.0.1:6379
```

这表明应用程序无法连接到本地Redis服务器。

## 解决步骤

### 1. 检查Redis服务是否运行

```bash
# 在服务器上执行以下命令检查Redis服务状态

# CentOS
sudo systemctl status redis

# Ubuntu
sudo systemctl status redis-server
```

如果服务未运行，启动服务：

```bash
# CentOS
sudo systemctl start redis
sudo systemctl enable redis

# Ubuntu
sudo systemctl start redis-server
sudo systemctl enable redis-server
```

### 2. 验证Redis连接

使用Redis命令行工具测试连接：

```bash
redis-cli ping
```

如果返回`PONG`，表示Redis服务正常运行。

如果设置了密码，使用以下命令：

```bash
redis-cli -a 你的密码 ping
```

### 3. 检查Redis配置

编辑Redis配置文件：

```bash
# CentOS
sudo nano /etc/redis.conf

# Ubuntu
sudo nano /etc/redis/redis.conf
```

确保以下设置正确：

- `bind 127.0.0.1`或`bind 0.0.0.0`（允许所有IP访问）
- `port 6379`（默认端口）
- 如果需要密码认证，确保`requirepass`配置正确

修改后重启Redis服务：

```bash
# CentOS
sudo systemctl restart redis

# Ubuntu
sudo systemctl restart redis-server
```

### 4. 检查应用程序配置

查看`application-prod.properties`文件中的Redis配置：

```properties
spring.redis.host=127.0.0.1
spring.redis.port=6379
spring.redis.password=
spring.redis.database=0
```

确保这些配置与您的Redis服务器设置匹配。如果Redis服务器不在本地运行，请修改`host`为正确的服务器地址。

### 5. 检查防火墙设置

如果Redis服务器在远程主机上，确保防火墙允许6379端口的连接：

```bash
# CentOS
sudo firewall-cmd --permanent --add-port=6379/tcp
sudo firewall-cmd --reload

# Ubuntu
sudo ufw allow 6379/tcp
```

### 6. 临时禁用Redis（如果不需要）

如果您的应用程序不依赖Redis，可以临时禁用Redis连接：

1. 在`application-prod.properties`文件中添加以下配置：

```properties
spring.data.redis.repositories.enabled=false
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
```

2. 重启应用程序

### 7. 检查Redis依赖

确保项目的`pom.xml`文件中包含正确的Redis依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

## 常见问题

1. **连接被拒绝**：确保Redis服务正在运行，并且端口未被其他程序占用
2. **认证失败**：检查密码配置是否正确
3. **超时**：检查网络连接和防火墙设置
4. **内存不足**：Redis可能因内存不足而无法启动，检查服务器内存使用情况

## 参考资源

- [Spring Boot Redis文档](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.nosql.redis)
- [Redis官方文档](https://redis.io/documentation)
- [Lettuce客户端文档](https://lettuce.io/core/release/reference/)