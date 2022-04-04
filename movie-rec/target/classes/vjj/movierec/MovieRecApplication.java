package vjj.movierec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@SpringBootApplication//(exclude = SecurityAutoConfiguration.class)
public class MovieRecApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieRecApplication.class, args);
    }

}
