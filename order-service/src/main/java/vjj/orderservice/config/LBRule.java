package vjj.orderservice.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LBRule {
    @Bean
    public IRule getRule(){
        return new RandomRule();
    }
}
