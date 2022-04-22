package vjj.movieservice.utils;

import com.mongodb.MongoClient;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
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

//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;

@Configuration
public class Configurations {

    private final String mongoHost; // = "localhost";
    private final int mongoPort; // = 27017;
    private final String esClusterName = "jj-cluster";
    private final String esHost = "localhost";
    private final int esPort =  9300;
    private final String redisHost;

    @Bean
    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    public Configurations() throws IOException {
        Properties properties = new Properties();
        Resource resource = new ClassPathResource("application.properties");
//        properties.load(new FileInputStream(resource.getFile()));
        properties.load(resource.getInputStream());
        this.redisHost = properties.getProperty("spring.redis.host");
        this.mongoHost = properties.getProperty("mongodb.host");
        this.mongoPort = Integer.parseInt(properties.getProperty("mongodb.port"));
    }

    @Bean(name = "mongoClient")
    public MongoClient getMongoClient() throws IOException {
        MongoClient mongoClient = new MongoClient( this.mongoHost , this.mongoPort );
        return mongoClient;
    }

    @Bean(name = "esClient")
    public RestHighLevelClient getESClient() throws UnknownHostException {
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost",9200,"http"))
        );
        return esClient;
    }

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
