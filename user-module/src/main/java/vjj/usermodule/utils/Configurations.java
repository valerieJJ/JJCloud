package vjj.usermodule.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class Configurations {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Bean(name = "jedis")
    public Jedis getRedisClient(){
        Jedis jedis = new Jedis(this.redisHost);
        return jedis;
    }

//    @Bean(name = "transportClient")
//    public TransportClient getTransportClient() throws UnknownHostException {
//        Settings settings = Settings.builder().put("cluster.name",esClusterName).build();
//        TransportClient esClient = new PreBuiltTransportClient(settings);
//        esClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esHost), esPort));
//        return esClient;
//    }
}
