# mall-backend
## 前端
前端部分我是直接拉的代码，同时对他的axios请求和表单数据进行了部分修改，修改为restful风格

前端传送门：[vue-store-masters](https://github.com/Snailsirs/vue-store-masters)

## 商城后台系统 基于springboot + mybatis + mybatisplus + redis 
### 2022-4-5
添加了redis实现对热门商品的缓存以及用户登录token存储，同时增加了校验登录和刷新token拦截器

添加了定时任务，每天定时刷新热门商品
### 2022-4-9
添加了自定义异常和错误码枚举类，便于与前端和用户信息交互，同时记录日志

添加了全局异常处理器
### 2022-4-11
添加了乐观锁，解决超卖问题

添加了库存缓存，缓解数据库请求压力

### 2022-5-2
为购物车添加缓存，优化部分业务逻辑

### 2022-5-4
添加RabbitMQ消息队列，实现秒杀功能



