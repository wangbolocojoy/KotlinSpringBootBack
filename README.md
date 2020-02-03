# KotlinSpringBootBack
Kotlin Spring Boot Project
小说数据均来自爬虫存入数据库，不做商业用途
测试需要改变这两个接口为get 浏览器直接访问  其他接口均为post请求浏览器不能直接访问
测试接口：获取小说列表 get 参数可不传body{page:1,size:10}
http://47.112.107.122:8080/back-1/myApplication/cas/getPageNovelList

搜索说接口改为get 参数novelName=都市
http://47.112.107.122:8080/back-1/myApplication/cas/searchNovel?novelName=都市

