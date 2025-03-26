# Redis连接问题解决方案

## 问题描述

应用程序启动时出现以下错误：

```
code: 500  msg: Could not get a resource from the pool; nested exception is io.lettuce.core.RedisConnectionException: Unable to connect to 127.0.0.1:6379
```

这表明应用程序无法连接到Redis服务器。

## 可能的原因

1. Redis服务未启动或未正确安装
2. Redis配置不正确（地址、端口、密码等）
3. 防火墙阻止了Redis连接
4. Redis绑定地址限制了连接

## 解决方案

### 1. 检查Redis服务状态

```bash
# CentOS
sudo systemctl status redis

# Ubuntu
sudo systemctl status redis-server
```

如果服务未运行，启动服务：

```bash
# CentOS
sudo systemctl start redis

# Ubuntu
sudo systemctl start redis-server
```

### 2. 验证Redis连接

使用Redis命令行工具测试连接：

```bash
redis-cli ping
```

如果返回`PONG`，表示Redis服务正常运行。

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
- 如果设置了密码，确保`requirepass`配置正确

### 4. 修改应用程序配置

我们已经更新了应用程序的Redis配置，将`host`从`localhost`改为`127.0.0.1`，这在某些系统中可能会解决DNS解析问题。

如果Redis服务器不在本地运行，请修改以下配置文件中的Redis连接信息：

- `/src/main/resources/application.yml`
- `/src/main/resources/application-prod.properties`

确保以下配置与您的Redis服务器匹配：

```yaml
redis:
  host: 127.0.0.1  # Redis服务器地址
  port: 6379       # Redis端口
  password:        # Redis密码，如果有的话
  database: 0      # 使用的数据库索引
```

### 5. 检查防火墙设置

如果Redis服务器在远程主机上，确保防火墙允许6379端口的连接：

```bash
# CentOS
sudo firewall-cmd --permanent --add-port=6379/tcp
sudo firewall-cmd --reload

# Ubuntu
sudo ufw allow 6379/tcp
```

### 6. 重启应用程序

在完成上述更改后，重启应用程序以应用新的配置。

## 其他建议

1. 在生产环境中，建议为Redis设置密码以增强安全性
2. 考虑使用Redis连接池来优化性能
3. 如果不需要使用Redis，可以考虑在配置中禁用Redis自动配置

## 参考资源

- [Spring Boot Redis文档](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.nosql.redis)
- [Redis官方文档](https://redis.io/documentation)
- [Lettuce客户端文档](https://lettuce.io/core/release/reference/)