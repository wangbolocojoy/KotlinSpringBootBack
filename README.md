# Kotlin Spring Boot 后端项目分析
通过对项目的分析，我发现这是一个使用 Kotlin 和 Spring Boot 开发的后端应用程序，主要功能包括用户管理、内容发布、小说管理和社交互动等。

## 项目技术栈
- 编程语言 ：Kotlin 1.4.30-M1
- 框架 ：Spring Boot 2.2.2.RELEASE
- 数据库访问 ：Spring Data JPA
- 数据库 ：MySQL
- 缓存 ：Redis
- API文档 ：Swagger 2.9.2
- 认证 ：JWT (Java Web Token)
- 云服务 ：阿里云OSS存储、OCR文字识别、内容安全检测
- 其他工具 ：Fastjson、Gson、HttpClient等
## 项目结构
项目采用典型的MVC架构，主要包含以下组件：

- 控制器(Controller) ：处理HTTP请求，如UserController、PostController等
- 服务层(Service) ：实现业务逻辑，如UserService接口及其实现
- 数据访问层(Repository) ：使用Spring Data JPA进行数据库操作
- 实体类(DTO) ：定义数据模型，如User、Post、Novel等
## 主要功能
1. 用户管理 ：注册、登录、个人信息管理、实名认证
2. 内容发布 ：发布帖子、上传照片
3. 社交互动 ：关注用户、点赞内容、评论
4. 小说功能 ：小说管理、分类
5. 安全检测 ：使用阿里云内容安全服务进行图片和文本检测
## 安全特性
- 使用JWT进行身份验证
- 自定义注解@UserLoginToken和@PassToken控制接口访问权限
- 密码加密存储
## 集成服务
- 阿里云OSS用于文件存储
- 阿里云OCR用于文字识别（如身份证识别）
- 短信服务用于验证码发送
该项目是一个功能完善的社交媒体后端系统，具有用户管理、内容管理和社交互动等核心功能，并集成了多种云服务增强应用能力


