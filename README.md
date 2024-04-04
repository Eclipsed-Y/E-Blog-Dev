# 搭建一个微服务架构下的blog项目

## 数据库
mysql

redis

## 配置中心
nacos

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

使用view代替联表查询

利用TransactionTemplate细化事务粒度
## 通信
### 使用Feign内部调用
调用时添加请求头字段，gateway去除对应的请求头字段

内部接口利用AOP检测是否有对应请求头字段，进行权限验证

防止内部接口暴露
## 接口
### upload-service
上传图片
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
## RoadMap
- [X] 网关服务
- [X] 上传服务
- [X] 用户服务
- [X] 文章服务
- [ ] 评论点赞关注(活动)服务
- [ ] 数据统计服务
- [ ] 搜索服务
- [ ] 推送服务
- [ ] 大模型调用服务