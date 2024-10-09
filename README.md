
## 后端技术栈
   
- 基于 SpringBoot + Mybatis Plus+ Shiro + mysql + redis构建的智慧课堂云笔记系统 
- 实现基于Shiro框架下 RBAC 权限模型设置角色权限
- 支持服务集群(系统已集成分布式session与Jwt token 机制)
- 集成sharding-jdbc， 支持mysql 数据库读写分离
- 集成阿里巴巴数据同步中间件Canal，可用于mysql 与其他类型数据库进行数据实时同步
## 前端技术

- Vue
- Vuex
- Vxe-Table
- Element-UI
- vue-router
- axios 
### 学生系统功能
模块 | 介绍
---|---
登录 |  用户名、密码  
试题 | 题干支持文本、图片、数学公式、表格等 
考试 | 主观题支持答题板作答之后保存文件上传到服务器
视频学习 | 支持在线学习录播视频
个人信息 | 显示用户最近的个人动态
### 管理员系统功能
模块 | 介绍
---|---
登录 |  用户名、密码  
主页 |  试卷总数、题目总数、用户活跃度、题目月数量
学生列表 |  显示系统所有的学生，新增、修改、删除、禁用
视频学习 |  支持在线学习录播视频
个人信息 |  显示用户最近的个人动态
管理员列表 |  显示系统所有的管理员，新增、修改、删除、禁用
科目列表 |  学科查询、修改、删除
试卷列表 |  试卷查询、修改、删除、设置
课程管理 |  支持课程视频上传
考试管理 |  考试列表、考试分析
题目创建 |  题目支持单选题、多选题、判断题、填空题、简答题，题干支持文本、图片、表格、数学公式
题目列表 |  题目查询、修改、删除
### 系统特色

- 支持填空题、综合题、选择题等多种试题类型的录入
- 使用Websocket并调用腾讯地图API限制多账号登录
- 集成了百度富文本编辑器支持数学公式的插入同时也支持通过excel 导入试题
- 使用Redis热点数据缓存和持久化配置，并限制了表单重复提交
- 使用Freemarker引擎技术可以将试卷试题导出word或者html,并且支持试题图片导出word


### 模块说明
#### education
- ├── education-api -- 系统api模块
- └── education-common -- 系统公共模块 
- └── education-business -- 系统业务模块
- └── education-canal-- canal数据同步模块
- └── education-common-api -- 系统公共api模块
- ├── education-model -- 实体类模块	


	
### 核心依赖 
依赖 | 版本
---|---
Spring Boot |  2.2.5.RELEASE  
Mybatis Plus | 3.4.0  
Mysql | 5.7
Element-UI | 2.13.0
Shiro | 1.4.0
Canal | 1.1.4
shardingsphere| 3.1.0.M1

#### 开发环境
   
- 开发环境支持：JDK1.8、mysql5.7、Rabbitmq、Redis
- 启动education-api模块下面的EducationApiApplication即可启动服务(本项目属于在学校实验室开发,所以将学生端接口与后台管理接口集成到一个服务器中，节省服务器成本)





