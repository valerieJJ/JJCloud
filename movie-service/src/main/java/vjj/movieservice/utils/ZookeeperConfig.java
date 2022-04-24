package vjj.movieservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;

@Slf4j
//@Configuration
public class ZookeeperConfig {

    @Value("${zookeeper.address}")
    private String host;

    @Value("${zookeeper.timeout}")
    private int timeout;

    public void setZookeeperConfig(String host, int timeout){
        this.host = host;
        this.timeout = timeout;
    }

    @Bean(name = "zkClient")
    public ZooKeeper zkClient(){
        ZooKeeper zooKeeper=null;
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);// 异步连接,成功后回调watcher监听
            zooKeeper = new ZooKeeper(host, timeout, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if(Event.KeeperState.SyncConnected==event.getState()){
                        //如果收到了服务端的响应事件,连接成功
                        log.info("zkClient connected successfully");
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
            log.info("init ZooKeeper connection: zk.state={}",zooKeeper.getState());

        }catch (Exception e){
            log.error("init ZooKeeper exception: {}",e);
        }
        return  zooKeeper;
    }


}


