Note: Latest updates are available on `dev` branch. 

# Intro

This is an project that is currently under development.

It extends my previous backend project **["JJSpring"](https://github.com/valerieJJ/JJSpring)** to micro-services using springcloud and zookeeper. 

The spring-boot modules are registered on different zk nodes(on virtual machine). 

1. Functions
- Welcome page: movie display + Login registration entrance (jump to login page)
- after login -> mainpage, menu bar includes: movie search + popular movies(number of ratings) + high rating movie list(zset) + user home page
- Movie search: (drop-down list) select search method (name, actor, language, year), input search information, then jump to page to show the result list
- Click movie picture => enter **movie detail page**, operation: **score** the movie, **add to my favourite** folder
- User home page: user information modification, logout, my favorites, user browsing history (redis-list)
2. Techniques
- springcloud, zookeeper, feign+hystrix
- redis, mongodb, mysql，elasticsearch
- thymleaf
- maven, git, docker
3. Services
- api-common
- movie-service: zk1(mongodb,elasticsearch), CompletableFuture
- user-module: zk2(mysql)
- web-consumer：zk3(redis), Feign, Hystrix, Intercepter, AOP(user identity & authentication)
- order-module is currently under development

<img width="1402" alt="image-20220426111822894" src="https://user-images.githubusercontent.com/43733497/165213803-398d0086-df97-4ecf-99e1-18d35451036e.png">


<img width="1407" alt="image" src="https://user-images.githubusercontent.com/43733497/164595514-ad0e1941-da58-4267-946b-331cab6a041b.png">

<img width="1486" alt="image" src="https://user-images.githubusercontent.com/43733497/164651015-e8dbcfb1-0372-458d-9a35-54c389400a93.png">


<img width="949" alt="image" src="https://user-images.githubusercontent.com/43733497/161598285-4eb467d8-189e-43cc-ada3-0d856ca53ca5.png">

<img width="1105" alt="image" src="https://user-images.githubusercontent.com/43733497/161597948-b302ebde-a4d0-421d-9694-8570b4f0229f.png">


### shell commands for zookeeper cluster
 ```shell
 case $1 in
"start"){
	for i in master hadoop0 hadoop2
	do
        if [ "$i" == "master" ];then
            echo "starting master..."
            ssh $i "/Users/jj/zookeepers/zk1/bin/zkServer.sh start"
        else
            echo "starting hadoop $i zookeeper..."
            ssh $i "docker start zk-container"
        fi
		echo "started!"
	done
};;
"status"){
	for i in master hadoop0 hadoop2
	do
        echo "status of $i :"
        if [ "$i" == "master" ];then
            ssh $i "/Users/jj/zookeepers/zk1/bin/zkServer.sh status"
        elif [ "$i" == "hadoop0" ];then
            ssh $i "~/Documents/zk2/bin/zkServer.sh status"
        elif [ "$i" == "hadoop2" ];then
            ssh $i "~/Documents/zk3/bin/zkServer.sh status"
        fi
    done
};;
"stop"){
	for i in master hadoop0 hadoop2
	do
        echo "stopping $i :"
        if [ "$i" == "master" ];then
            ssh $i "/Users/jj/zookeepers/zk1/bin/zkServer.sh stop"
        else
            ssh $i "docker stop zk-container"
        fi
	done
};;
esac

```



### mongoDB
Useful Tool: MongoDB - Robo

start server:

`jj@master ~ % mongod --dbpath /usr/local/Cellar/data/db`

### redis

start server:

`jj@master ~ % redis-server`
<img width="1623" alt="image" src="https://user-images.githubusercontent.com/43733497/158499459-46ce4489-72af-4133-98f7-9459892ee47d.png">

### elasticsearch 
`jj@master ~ % elasticsearch`（It has already been added to my system environment variables）
Here it is applied to improve the speed of searching movies in various ways.

It will return the movie ID retrieved based on the content, based on which you can further check the MongoDB table to get more information.

### zookeeper utils
I am considering extending this project to spring-cloud based on zookeeper

<img width="1603" alt="image" src="https://user-images.githubusercontent.com/43733497/159327103-3947eda0-b6d3-4471-8de1-4dca4ce826fb.png">


Here are some system showcases：
Note: To save storage space, I didn't download all the poster images for each movie. Instead, I used the same images as general movie posters.

![image-20220316094252015](/Users/jj/Library/Application Support/typora-user-images/image-20220316094252015.png)

<img width="1280" alt="image" src="https://user-images.githubusercontent.com/43733497/158501934-0de13ccb-5eec-46e1-a3b2-e58f42b6f2e6.png">
![piccc](/Users/jj/Pictures/piccc.png)

<img width="1285" alt="image" src="https://user-images.githubusercontent.com/43733497/158501345-8cfe6ef0-97a5-4115-8dd0-7129cc249c9a.png">

<img width="1296" alt="image" src="https://user-images.githubusercontent.com/43733497/158501396-3c576e5c-5032-44c6-a5ab-6d8e1ea842f5.png">


<img width="1277" alt="image" src="https://user-images.githubusercontent.com/43733497/158501832-fb7a6d82-bbe8-4b15-b5fd-3feebe5ff946.png">



