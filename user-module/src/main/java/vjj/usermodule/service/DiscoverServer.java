package vjj.usermodule.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
public class DiscoverServer {
    @Autowired
    private ITbService service;

    public String getTB(){
        return service.queryByMid();
    }
}
