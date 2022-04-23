package vjj.movieservice.utils;

import com.mongodb.MongoClient;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;


@Configuration
public class Configurations {

    @Value("${spring.data.mongodb.host}")
    private String mongoHost;
    @Value("${spring.data.mongodb.port}")
    private int mongoPort;

    @Value("${es.clustername}")
    private String esClusterName;
    @Value("${es.host}")
    private final String esHost = "localhost";
    @Value("${es.port}")
    private final int esPort =  9300;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Bean
    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
    }


    @Bean(name = "mongoClient")
    public MongoClient getMongoClient() throws IOException {
        MongoClient mongoClient = new MongoClient( this.mongoHost , this.mongoPort );
        return mongoClient;
    }

    @Bean(name = "esClient")
    public RestHighLevelClient getESClient() throws UnknownHostException {
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost(this.esHost,this.esPort,"http"))
        );
        return esClient;
    }

    @Bean(name = "jedis")
    public Jedis getRedisClient(){
        Jedis jedis = new Jedis(this.redisHost);
        return jedis;
    }
}
