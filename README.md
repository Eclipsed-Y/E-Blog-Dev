# 搭建一个微服务架构下的社区Blog项目
## 数据库
### MySQL
通过冗余字段实现不同库联表查询

同数据库则使用view代替联表查询

### Redis
继承RedisCacheManager、RedisCache类，重写两个类的部分方法，实现Redis基于SpringCache的随机过期时间缓存，应对缓存雪崩

使用旁路缓存模式，针对分页缓存自定义RedisUtil类删除缓存
## 注册配置中心
Nacos作为注册和配置中心

## 服务架构
### 1. 网关 gateway
解析jwt令牌，并在redis中验证是否存在，通过请求头字段传递用户id

后续所有服务通过interceptor解析用户id
### 2. 上传服务 upload-service
利用阿里云OSS上传文件
### 3. 用户服务 user-service
登录后创建jwt令牌，并以相同过期时间存储在redis中

退出时删除redis对应jwt令牌

密码加盐后md5编码存储

修改密码后调用退出方法
### 4. 文章服务 article-service
利用mybatis-plus interceptor实现分页

利用TransactionTemplate和PlatformTransactionManager细化事务粒度
### 5. 活动服务 activity-service
二级评论机制，每篇文章有一级评论，一级评论包含二级评论，可追踪根评论和父评论

利用消息队列rabbitmq实现对评论、点赞、关注等功能的异步、解耦、削峰

消息队列重传6次保证可靠性，仍失败则加入死信队列

redis中点赞数据和是否点赞均设置随机过期时间，如果过期，则无需修改redis，下次刷新页面将从数据库读取到缓存

使用redis的lua脚本、rabbitmq实现点赞功能实时更新redis（未过期时）并异步写入数据库

额外启动一个RedisCacheManager修改序列化规则，防止redis无法获取到数字

## 分布式
### 使用Feign内部调用
调用时添加请求头字段，gateway去除对应的请求头字段

内部接口利用AOP检测是否有对应请求头字段，进行权限验证

防止内部接口暴露
### 分布式事务
利用Seata进行分布式事务管理，采用AT模式

将全局异常处理器的响应状态码设置4XX、5XX来解决其导致的Feign请求正常返回，造成Seata无法回滚的问题
## 接口
### upload-service
- 上传图片
### user-service
- 用户注册
- 用户登录
- 用户退出
- 修改密码
- 根据id获取用户信息
- 更新用户信息
### article-service
- 获取分类列表
- 获取标签列表
- 根据分类分页查询文章
- 根据文章id查看文章详情
- 新增文章（暂存/发布）
- 修改文章
- 删除文章
- 判断文章是否存在（内部接口）
### activity-service
- 新增评论
- 修改评论
- 删除评论
- 分页查询根评论（一级）
- 分页查询子评论（二级）
- 点赞
- 查看文章点赞数
- 查看是否已点赞
- 关注
- 查看用户关注数
- 查看是否已关注
## RoadMap
- [X] 网关服务
- [X] 上传服务
- [X] 用户服务
- [X] 文章服务
- [X] 评论点赞关注(活动)服务
- [ ] 数据统计服务
- [ ] 搜索服务
- [ ] 推送服务
- [ ] 大模型调用服务