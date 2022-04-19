# Latest updates can be seen on `dev` branch. 

### functions

- Welcome page: movie display + Login registration entrance (jump to login page)
- after login -> mainpage, menu bar includes: movie search + popular movies(number of ratings) + high rating movie list(zset) + user home page
- Movie search: (drop-down list) select search method (name, actor, language, year), input search information, then jump to page to show the result list
- Click movie picture => enter **movie detail page**, operation: **score** the movie, **add to my favourite** folder
- User home page: user information modification, logout, my favorites, user browsing history (redis-list)


### techniques
- springcloud, zookeeper, feign+hystrix
- redis、mongo、mysql，elasticsearch
- thymleaf
- maven，git

### services
- api-common
- movie-service: zk1(mongodb,elasticsearch)，CompletableFuture异步查表+分数计算，
- user-module: zk2(mysql) CRUD
- web-consumer：zk3(redis)，Feign调用服务，hystrix服务熔断，Intercepter登陆拦截，aop(身份、权限验证)
- order-module is currently under development

This is an project that is currently under development.

It extends my previous backend project ["JJSpring"](https://github.com/valerieJJ/JJSpring) to springcloud. 

The spring-boot modules are registered on different zk nodes(on virtual machine). 


<img width="949" alt="image" src="https://user-images.githubusercontent.com/43733497/161598285-4eb467d8-189e-43cc-ada3-0d856ca53ca5.png">

<img width="1105" alt="image" src="https://user-images.githubusercontent.com/43733497/161597948-b302ebde-a4d0-421d-9694-8570b4f0229f.png">
