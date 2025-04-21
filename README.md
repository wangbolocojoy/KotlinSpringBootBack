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

# Token 自动续期机制

本项目实现了 JWT Token 自动续期功能，具体工作方式如下：

## 服务端

1. Token 默认有效期为 7 天
2. 使用 Redis 优化 token 管理：
   - token 存储在 Redis 中，键格式为 `user:token:{phone}`
   - token 在 Redis 中设置相同的过期时间
   - 验证 token 时优先从 Redis 获取，减少 JWT 解析操作
   - 支持通过登出操作使 token 立即失效

3. 当用户调用任何需要认证的接口时：
   - 系统会检查 token 的剩余有效期
   - 如果剩余有效期小于 1 天，系统会自动生成新的 token
   - 新 token 会通过响应头 `token` 字段返回给客户端
   - 同时更新 Redis 和数据库中用户的 token 记录

## 优化点

1. **减少数据库访问**：token 验证直接通过 Redis 完成，无需每次都访问数据库
2. **提高响应速度**：Redis 内存操作速度远快于数据库查询
3. **支持即时失效**：用户登出时可以立即使 token 失效，提高安全性
4. **分布式支持**：通过 Redis 集中存储 token，支持分布式系统中的一致性

## 客户端实现指南

客户端需要在每次请求后检查响应头中是否包含新的 token，如果有则更新本地存储：

### 前端实现示例（JavaScript）

```javascript
// 发送请求的函数
async function makeApiRequest(url, options = {}) {
  // 添加认证 token 到请求头
  const headers = {
    ...options.headers,
    'token': localStorage.getItem('token') // 从本地存储获取当前 token
  };
  
  const response = await fetch(url, {
    ...options,
    headers
  });
  
  // 检查响应头中是否有新的 token
  const newToken = response.headers.get('token');
  if (newToken) {
    // 更新本地存储的 token
    localStorage.setItem('token', newToken);
    console.log('Token 已自动更新');
  }
  
  return response;
}
```

### iOS 实现示例（Swift）

```swift
func makeApiRequest(url: URL, method: String = "GET", body: Data? = nil, completion: @escaping (Data?, URLResponse?, Error?) -> Void) {
    var request = URLRequest(url: url)
    request.httpMethod = method
    request.httpBody = body
    
    // 添加认证 token 到请求头
    if let token = UserDefaults.standard.string(forKey: "token") {
        request.addValue(token, forHTTPHeaderField: "token")
    }
    
    let task = URLSession.shared.dataTask(with: request) { data, response, error in
        // 检查响应头中是否有新的 token
        if let httpResponse = response as? HTTPURLResponse,
           let newToken = httpResponse.allHeaderFields["token"] as? String {
            // 更新本地存储的 token
            UserDefaults.standard.set(newToken, forKey: "token")
            print("Token 已自动更新")
        }
        
        completion(data, response, error)
    }
    
    task.resume()
}
```

### Android 实现示例（Kotlin）

```kotlin
fun makeApiRequest(url: String, method: String = "GET", body: RequestBody? = null, callback: Callback) {
    val token = sharedPreferences.getString("token", "") ?: ""
    
    val request = Request.Builder()
        .url(url)
        .method(method, body)
        .addHeader("token", token)
        .build()
    
    OkHttpClient().newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            // 检查响应头中是否有新的 token
            response.header("token")?.let { newToken ->
                // 更新本地存储的 token
                sharedPreferences.edit().putString("token", newToken).apply()
                Log.d("Token", "Token 已自动更新")
            }
            
            callback.onResponse(call, response)
        }
        
        override fun onFailure(call: Call, e: IOException) {
            callback.onFailure(call, e)
        }
    })
}
```


