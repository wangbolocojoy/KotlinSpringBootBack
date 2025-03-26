# KotlinSpringBootBack 项目阿里云部署指南

本文档提供了将 KotlinSpringBootBack 项目部署到阿里云服务器的详细步骤和配置说明。

## 1. 准备工作

### 1.1 阿里云服务器准备

1. 购买阿里云 ECS 服务器（推荐配置）：
   - 操作系统：CentOS 7.x 或 Ubuntu 18.04/20.04
   - 内存：至少 2GB
   - CPU：至少 1核
   - 带宽：根据访问量选择，建议至少 1Mbps
   - 磁盘：至少 40GB

2. 安全组配置：
   - 开放端口：22(SSH)、80(HTTP)、443(HTTPS)、8080(应用端口)
   - 如果使用自定义端口，请相应开放

### 1.2 域名与证书（可选）

1. 购买域名并完成备案（国内服务器必须备案）
2. 申请 SSL 证书（如需 HTTPS）

## 2. 服务器环境配置

### 2.1 安装 JDK 8

```bash
# CentOS
sudo yum install -y java-1.8.0-openjdk java-1.8.0-openjdk-devel

# Ubuntu
sudo apt update
sudo apt install -y openjdk-8-jdk
```

验证安装：
```bash
java -version
```

### 2.2 安装 MySQL

```bash
# CentOS
sudo yum install -y mysql-server
sudo systemctl start mysqld
sudo systemctl enable mysqld

# Ubuntu
sudo apt update
sudo apt install -y mysql-server
sudo systemctl start mysql
sudo systemctl enable mysql
```

配置 MySQL：
```bash
sudo mysql_secure_installation
```

创建数据库和用户：
```sql
CREATE DATABASE aliyun CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'root'@'localhost' IDENTIFIED BY 'Gw09dianxin';
GRANT ALL PRIVILEGES ON aliyun.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

### 2.3 安装 Redis（如需使用）

```bash
# CentOS
sudo yum install -y redis
sudo systemctl start redis
sudo systemctl enable redis

# Ubuntu
sudo apt update
sudo apt install -y redis-server
sudo systemctl start redis-server
sudo systemctl enable redis-server
```

配置 Redis 密码（可选）：
```bash
sudo nano /etc/redis/redis.conf
```

找到 `# requirepass foobared` 行，取消注释并修改为：
```
requirepass your_redis_password
```

重启 Redis：
```bash
sudo systemctl restart redis
```

## 3. 项目配置与部署

### 3.1 创建应用配置文件

在项目中创建生产环境配置文件 `application-prod.properties` 或 `application-prod.yml`，放在 `src/main/resources` 目录下：

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Redis配置（如需使用）
spring.redis.host=127.0.0.1
spring.redis.port=6379
spring.redis.password=your_redis_password
spring.redis.database=0

# 服务器配置
server.port=8080
server.servlet.context-path=/

# 日志配置
logging.file.path=/var/log/springboot
logging.level.root=WARN
logging.level.com.btm.back=INFO

# 文件上传配置
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB


```

### 3.2 打包项目

在本地开发环境中执行：

```bash
./mvnw clean package -Pprod -DskipTests
```

这将在 `target` 目录下生成 WAR 文件。

### 3.3 上传项目到服务器

使用 SCP 或 SFTP 将 WAR 文件上传到服务器：

```bash
scp target/back-1.war username@your_server_ip:/path/to/deployment/
```

### 3.4 创建启动脚本

在服务器上创建启动脚本 `start.sh`：

```bash
#!/bin/bash

APP_NAME=back-1.war
JAVA_OPTS="-Xms512m -Xmx1024m -Dspring.profiles.active=prod"
LOG_PATH=/var/log/springboot

# 创建日志目录
mkdir -p $LOG_PATH

# 检查应用是否已运行
pid=$(ps -ef | grep $APP_NAME | grep -v grep | awk '{print $2}')
if [ -n "$pid" ]; then
    echo "$APP_NAME is already running with pid $pid"
    exit 1
fi

# 启动应用
nohup java $JAVA_OPTS -jar /path/to/deployment/$APP_NAME > $LOG_PATH/startup.log 2>&1 &

# 检查启动状态
sleep 5
pid=$(ps -ef | grep $APP_NAME | grep -v grep | awk '{print $2}')
if [ -n "$pid" ]; then
    echo "$APP_NAME started successfully with pid $pid"
else
    echo "$APP_NAME failed to start. Check logs at $LOG_PATH/startup.log"
    exit 1
fi
```

赋予执行权限：
```bash
chmod +x start.sh
```

### 3.5 创建停止脚本

创建停止脚本 `stop.sh`：

```bash
#!/bin/bash

APP_NAME=back-1.war

# 查找进程
pid=$(ps -ef | grep $APP_NAME | grep -v grep | awk '{print $2}')

if [ -z "$pid" ]; then
    echo "$APP_NAME is not running"
    exit 0
fi

# 停止进程
echo "Stopping $APP_NAME with pid $pid"
kill $pid

# 检查是否成功停止
sleep 5
pid=$(ps -ef | grep $APP_NAME | grep -v grep | awk '{print $2}')
if [ -z "$pid" ]; then
    echo "$APP_NAME stopped successfully"
else
    echo "$APP_NAME failed to stop, force killing..."
    kill -9 $pid
    echo "$APP_NAME force stopped"
fi
```

赋予执行权限：
```bash
chmod +x stop.sh
```

## 4. 配置系统服务（可选）

为了让应用开机自启动，可以配置系统服务：

```bash
sudo nano /etc/systemd/system/springboot.service
```

添加以下内容：

```
[Unit]
Description=Spring Boot Application
After=syslog.target network.target

[Service]
User=your_username
WorkingDirectory=/path/to/deployment
ExecStart=/usr/bin/java -Xms512m -Xmx1024m -Dspring.profiles.active=prod -jar /path/to/deployment/back-1.war
SuccessExitStatus=143
TimeoutStopSec=10
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
```

启用服务：

```bash
sudo systemctl daemon-reload
sudo systemctl enable springboot.service
sudo systemctl start springboot.service
```

## 5. 配置 Nginx（可选）

如果需要使用 Nginx 作为反向代理：

```bash
sudo apt install -y nginx   # Ubuntu
# 或
sudo yum install -y nginx   # CentOS
```

配置 Nginx：

```bash
sudo nano /etc/nginx/sites-available/springboot
```

添加以下内容：

```
server {
    listen 80;
    server_name your_domain.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

创建符号链接并重启 Nginx：

```bash
# Ubuntu
sudo ln -s /etc/nginx/sites-available/springboot /etc/nginx/sites-enabled/
sudo systemctl restart nginx

# CentOS
sudo ln -s /etc/nginx/sites-available/springboot /etc/nginx/conf.d/
sudo systemctl restart nginx
```

## 6. 监控与维护

### 6.1 查看日志

```bash
tail -f /var/log/springboot/startup.log
```

### 6.2 监控应用状态

```bash
sudo systemctl status springboot.service
```

### 6.3 定期备份数据库

创建备份脚本 `backup.sh`：

```bash
#!/bin/bash

BACKUP_DIR=/var/backups/mysql
DATABASE=your_database_name
USERNAME=your_username
PASSWORD=your_password
DATE=$(date +%Y%m%d_%H%M%S)

# 创建备份目录
mkdir -p $BACKUP_DIR

# 备份数据库
mysqldump -u$USERNAME -p$PASSWORD $DATABASE | gzip > $BACKUP_DIR/$DATABASE-$DATE.sql.gz

# 删除7天前的备份
find $BACKUP_DIR -name "$DATABASE-*.sql.gz" -mtime +7 -delete
```

赋予执行权限并添加到 crontab：

```bash
chmod +x backup.sh
crontab -e
```

添加以下内容（每天凌晨2点执行备份）：

```
0 2 * * * /path/to/backup.sh
```

## 7. 故障排除

### 7.1 应用无法启动

1. 检查日志文件：`/var/log/springboot/startup.log`
2. 确认 JDK 版本正确：`java -version`
3. 检查数据库连接配置是否正确
4. 检查端口是否被占用：`netstat -tulpn | grep 8080`

### 7.2 数据库连接问题

1. 确认 MySQL 服务运行状态：`systemctl status mysql`
2. 检查数据库用户权限
3. 确认防火墙设置允许连接

### 7.3 内存不足

如果应用因内存不足而崩溃，可以调整 JVM 参数：

```bash
# 修改启动脚本中的 JAVA_OPTS
JAVA_OPTS="-Xms256m -Xmx512m -Dspring.profiles.active=prod"
```

## 8. 安全建议

1. 定期更新系统和软件包
2. 使用强密码并定期更换
3. 配置防火墙，只开放必要端口
4. 使用 HTTPS 加密传输
5. 定期备份数据
6. 配置 SSH 密钥认证，禁用密码登录

## 9. 性能优化

1. 配置合适的 JVM 参数
2. 优化数据库查询和索引
3. 使用连接池管理数据库连接
4. 配置适当的缓存策略
5. 使用 CDN 加速静态资源加载

## 10. 参考资源

- [Spring Boot 官方文档](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [阿里云 ECS 文档](https://help.aliyun.com/product/25365.html)
- [MySQL 官方文档](https://dev.mysql.com/doc/)
- [Nginx 官方文档](https://nginx.org/en/docs/)