# 我的app后台项目

## Kotlin Spring Boot Project

## 使用kotlin开发后台还是很不错的选择

### 小说数据均来自爬虫存入数据库，不做商业用途

### 该项目缺少阿里云OSSkey配置文件，数据库配置文件



## 用户表操作接口👇
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
## 粉丝 --关注接口
```kotlin

/**
 * 关注--粉丝
 */
@RestController
@RequestMapping(value = ["/swiftTemplate/Follow"])
class FollowController {

    @Autowired
    lateinit var  followService: FollowService


    @UserLoginToken
    @RequestMapping(value = ["followuser"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun followuser(@Valid @RequestBody u: ReqBody) = followService.followUser(u)

    @UserLoginToken
    @RequestMapping(value = ["unfollowuser"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun unfollowuser(@Valid @RequestBody u: ReqBody) = followService.unFollowUser(u)

    @UserLoginToken
    @RequestMapping(value = ["getfancelist"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun getfancelist(@Valid @RequestBody u: ReqBody) = followService.getFanceList(u)

    @UserLoginToken
    @RequestMapping(value = ["getfollowlist"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun getfollowlist(@Valid @RequestBody u: ReqBody) = followService.getFollowList(u)
}

```
##  获取粉丝列表接口

```kotlin

    /**
     * 获取粉丝列表
     * 返回该粉丝是否关注自己的  状态
     */
    override fun getFanceList(body: ReqBody): BaseResult {
        val fancelist = body.userid?.let { followRespository.findByFollowid(it) }
        return if (fancelist.isNullOrEmpty()) {
            BaseResult.FAIL("粉丝列表为空")
        } else {
            val list = ArrayList<UserVo>()
            fancelist.forEach {
                val user = userRespository.findById(it.userid ?: 0)
                val followlist = body.userid?.let { followRespository.findByUserid(body.userid ?: 0) }
                //该粉丝状态是否关注了用户
                user?.isfollow = followlist?.any { it1 ->
                    it1.followid == user?.id
                }
                if (user != null) {
                    //重新包装user
                    val s = CopierUtil.copyProperties(user, UserVo::class.java)
                    s?.let { it1 -> list.add(it1) }
                }
            }
            BaseResult.SECUESS(list)
        }
    }

```
## 获取推荐人列表 测试完成
### 过滤掉已经关注的用户
```json
{
    "status":200,
    "msg":"success",
    "data":[
        {
            "id":30,
            "nickname":null,
            "account":"13550247642",
            "icon":null,
            "address":null,
            "usersex":false,
            "seayinfo":null,
            "isfollow":false
        },
        {
            "id":32,
            "nickname":null,
            "account":"13550247643",
            "icon":null,
            "address":null,
            "usersex":false,
            "seayinfo":null,
            "isfollow":false
        },
        {
            "id":33,
            "nickname":null,
            "account":"13550247644",
            "icon":null,
            "address":null,
            "usersex":false,
            "seayinfo":null,
            "isfollow":false
        },
        {
            "id":34,
            "nickname":null,
            "account":"13550247645",
            "icon":null,
            "address":null,
            "usersex":false,
            "seayinfo":null,
            "isfollow":false
        },
        {
            "id":35,
            "nickname":null,
            "account":"13550247646",
            "icon":null,
            "address":null,
            "usersex":false,
            "seayinfo":null,
            "isfollow":false
        },
        {
            "id":36,
            "nickname":null,
            "account":"13550247647",
            "icon":null,
            "address":null,
            "usersex":false,
            "seayinfo":null,
            "isfollow":false
        },
        {
            "id":37,
            "nickname":null,
            "account":"13550247648",
            "icon":null,
            "address":null,
            "usersex":false,
            "seayinfo":null,
            "isfollow":false
        },
        {
            "id":38,
            "nickname":null,
            "account":"13550247649",
            "icon":null,
            "address":null,
            "usersex":false,
            "seayinfo":null,
            "isfollow":false
        },
        {
            "id":39,
            "nickname":null,
            "account":"13550247650",
            "icon":null,
            "address":null,
            "usersex":false,
            "seayinfo":null,
            "isfollow":false
        }
    ]
}
```
### 批量上传图片
```json
{
    "status": 200,
    "msg": "上传2张图片成功",
    "data": [
        {
            "id": 86,
            "userid": 30,
            "postid": 2,
            "filetype": "image/jpeg",
            "originalFilename": "IMG_2512.JPG",
            "fileurl": "https://myiosandroidkotlinapplication.oss-cn-chengdu.aliyuncs.com/home/picture/30/IMG_2512.JPG",
            "fileLikes": 0,
            "fileseenum": 0
        },
        {
            "id": 87,
            "userid": 30,
            "postid": 2,
            "filetype": "image/jpeg",
            "originalFilename": "IMG_2513.JPG",
            "fileurl": "https://myiosandroidkotlinapplication.oss-cn-chengdu.aliyuncs.com/home/picture/30/IMG_2513.JPG",
            "fileLikes": 0,
            "fileseenum": 0
        }
    ]
}
```


# 我的APP项目版 使用该后台接口开发的app👉[传送门](https://github.com/wangbolocojoy/swiftTemplate)
## 该项目使用以下框架

```podfile
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







