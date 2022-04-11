package vjj.orderservice.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("vjj.orderservice.mapper")
public class MyBatisConfig {
}
