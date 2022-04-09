# mall-backend
商城后台系统 基于springboot + mybatis + mybatisplus + redis 

添加了redis实现对热门商品的缓存以及用户登录token存储，同时增加了校验登录和刷新token拦截器

添加了定时任务，每天定时刷新热门商品
