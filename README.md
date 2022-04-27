# Latest updates can be seen on `dev` branch. 

This is an project that is currently under development.

It extends my previous backend project ["JJSpring"](https://github.com/valerieJJ/JJSpring) to springcloud. 

The spring-boot modules are registered on different zk nodes(on virtual machine). 

<img width="1402" alt="image-20220426111822894" src="https://user-images.githubusercontent.com/43733497/165213803-398d0086-df97-4ecf-99e1-18d35451036e.png">


<img width="1407" alt="image" src="https://user-images.githubusercontent.com/43733497/164595514-ad0e1941-da58-4267-946b-331cab6a041b.png">

<img width="1486" alt="image" src="https://user-images.githubusercontent.com/43733497/164651015-e8dbcfb1-0372-458d-9a35-54c389400a93.png">


<img width="949" alt="image" src="https://user-images.githubusercontent.com/43733497/161598285-4eb467d8-189e-43cc-ada3-0d856ca53ca5.png">

<img width="1105" alt="image" src="https://user-images.githubusercontent.com/43733497/161597948-b302ebde-a4d0-421d-9694-8570b4f0229f.png">




### zk起停脚本
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
