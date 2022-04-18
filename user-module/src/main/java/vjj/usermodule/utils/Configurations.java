package vjj.usermodule.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import redis.clients.jedis.Jedis;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;

@Configuration
public class Configurations {

    private final String mongoHost = "localhost";
    private final int mongoPort = 27017;
    private final String esClusterName = "jj-cluster";
    private final String esHost = "localhost";
    private final int esPort =  9300;
    private final String redisHost;


    public Configurations() throws IOException {
        Properties properties = new Properties();
        Resource resource = new ClassPathResource("application.properties");
//        properties.load(new FileInputStream(resource.getFile()));
        properties.load(resource.getInputStream());
        this.redisHost = properties.getProperty("spring.redis.host");
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
