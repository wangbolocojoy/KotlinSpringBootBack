# 我的app后台项目
##Kotlin Spring Boot Project
###小说数据均来自爬虫存入数据库，不做商业用途
###该项目缺少阿里云OSSkey配置文件，数据库配置文件
####使用kotlin开发后台还是很不错的选择
##用户表操作接口👇
```kotlin
/**
* 用户
*/
@RestController
@RequestMapping(value = ["/swiftTemplate/User"])
class UserController  {
    
    @Autowired
    lateinit var userServiceImp :UserServiceImp
   
    @PassToken
    @RequestMapping(value = ["register"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun register(@Valid @RequestBody u: ReqBody) = userServiceImp.register(u)
    
    @PassToken
    @RequestMapping(value = ["login"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun login(@Valid @RequestBody u: ReqBody)= userServiceImp.login(u)

    @UserLoginToken
    @RequestMapping(value = ["getUseInfo"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun getUserInfo(@Valid @RequestBody u: ReqBody)= userServiceImp.getuserinfo(u)

    @UserLoginToken
    @RequestMapping(value = ["updateUser"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun updateUser(@Valid @RequestBody u: ReqBody)= userServiceImp.updateUser(u)

    @UserLoginToken
    @RequestMapping(value = ["uploadusericon"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun uploadIcon(@RequestParam  id:Int, @RequestParam uploadtype:String, @RequestPart("uploadFile")uploadFile: MultipartFile? )= userServiceImp.updateIcon(id,uploadtype,uploadFile)

    @RequestMapping(value = ["test"])
    @Throws(java.lang.Exception::class)
    private fun test()=userServiceImp.test()

    @UserLoginToken
    @RequestMapping(value = ["deleteall"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun deleteall(@RequestBody u: ReqBody) {}

    @UserLoginToken
    @RequestMapping(value = ["searchfollow"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun searchfollow(@RequestBody u: ReqBody) =userServiceImp.searchfollow(u)

}

```
##粉丝 --关注接口
```kotlin
/**
 * 关注--粉丝
 */
@RestController
@RequestMapping(value = ["/swiftTemplate/Follow"])
class FollowController {

    @Autowired
    lateinit var  followService: FollowService

    @PassToken
    @RequestMapping(value = ["followuser"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun followuser(@Valid @RequestBody u: ReqBody) = followService.followUser(u)

    @PassToken
    @RequestMapping(value = ["unfollowuser"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun unfollowuser(@Valid @RequestBody u: ReqBody) = followService.unFollowUser(u)

    @PassToken
    @RequestMapping(value = ["getfancelist"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun getfancelist(@Valid @RequestBody u: ReqBody) = followService.getFanceList(u)

    @PassToken
    @RequestMapping(value = ["getfollowlist"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun getfollowlist(@Valid @RequestBody u: ReqBody) = followService.getFollowList(u)
}


```





# 我的APP项目🍎版 使用该后台接口开发的app[传送门👉](https://github.com/wangbolocojoy/swiftTemplate)
## 该项目使用以下框架

```swift
pod 'Moya', '~> 13.0.1'
pod 'ObjectMapper', '~> 3.5.2'
pod 'AlamofireImage', '~> 3.5.2'
pod 'SwiftyBeaver', '~> 1.7.0'
pod 'IQKeyboardManagerSwift', '~> 6.4.0'
pod 'MJRefresh', '~> 3.2.0'
pod 'Gifu', '~> 3.2.0'
pod 'SwiftyRSA','~>1.5.0'
pod 'CryptoSwift' , '~> 1.0.0'
pod 'Bugly','~> 2.5.0'
```







