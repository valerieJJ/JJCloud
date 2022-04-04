package vjj.purchaseserivce.myrule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//随机
@Configuration
public class MyselfRule {
    @Bean
    public IRule MyRule(){
        return new RandomRule();
    }
}