# NANA robot
基于Simbot v2、SpringBoot的QQ机器人程序

## 目前的功能
*以下命令均不带中括号*
* 关键词触发
* `nana学习` 学习功能
  * 第一次发送的Key必须是字符，最好不要过短。第二次发送的value可以是图片
* `nana删除` 删除学习过的内容
* `nana查询关键词` 通过value查询触发的key
* `nana图片` 随机发送二次元图片
* `nana天气 [city]` 查询指定城市的天气
* `nana模块管理` \*管理员使用，管理机器人功能
* `nana每日新闻`
* `nana微博热搜`
* `nana历史上的今天`
* `nana翻译 [keyword]`
  * 后跟需要翻译的单词或句子
* `nana百度 [keyword]`
  * 后跟需要查询的关键词
* ~~`nana查询`~~
* `nana摸鱼日历`
* `nana找番`
  * 需要提供动漫截图
* `nana聊天`
  * nana将监听你发送的信息并且做出回复 退出请输入`nana退出聊天`
* `nana今天吃什么`
  * `学习今天吃什么` 需提供：菜品名 、菜品图片
  * `删除今天吃什么 [id]`输入指定菜品id将其删除
  * `查询今天吃什么 [keyword]`通过关键词查询菜品id
* `nana动漫资讯`
* `nana小鸡词典 [keyword] 小鸡词典项目目前被强制下架 服务器已关闭`
  * 示例：nana小鸡词典 盐系
* `nana刷新`
* 戳一戳nana发送信息 *nana爱你哟*
* 定时发送固定信息
* 权限管理


## 快速开始
* clone本项目，导入IDEA，使用Maven下载相关依赖
* 在`src/main/`目录下创建`resources`文件夹
* 在`src/main/resources/`目录下创建`application.yml`
  * 需要配置的内容：
    * [数据库相关配置](https://blog.csdn.net/weixin_45750972/article/details/119608168)
    * [minio相关配置](https://www.jianshu.com/p/403eaf7d401c)
    * [simbot相关配置](https://www.yuque.com/simpler-robot/simpler-robot-doc/fk6o3e)
    * [redis相关配置](https://blog.csdn.net/qq_47431361/article/details/123240363?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522167558647916800222889736%2522%252C%2522scm%2522%253A%252220140713.130102334.pc%255Fblog.%2522%257D&request_id=167558647916800222889736&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~blog~first_rank_ecpm_v1~rank_v31_ecpm-1-123240363-null-null.blog_rank_default&utm_term=redis&spm=1018.2226.3001.4450)
* 在`src/main/resources/`目录下创建`simbot-bots`目录，存放simbot账号配置，参考：simbot文档 [账号配置部分](https://www.yuque.com/simpler-robot/simpler-robot-doc/fk6o3e)
  
## 可能遇到的问题
* 登录失败、滑块验证、当前账号或网络异常  参考：simbot文档 [常见问题](https://www.yuque.com/simpler-robot/simpler-robot-doc/ul3m12)
* 如果显示版本过低的话可尝试使用Simbot v3进行登录之后拷贝缓存进行替换

## 相关链接
本项目基于Simbot，请善用其官方文档  
* [Simbot文档](https://www.yuque.com/simpler-robot/simpler-robot-doc)  
* [Simbot Github](https://github.com/simple-robot/simple-robot-v2)  
* [Simbot示例内容](https://github.com/simple-robot/simbot-examples)  
* [Mirai](https://mirai.mamoe.net/)


