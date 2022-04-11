package vjj.usermodule.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import models.*;

@Configuration
public class DiscoverServer {
    @Autowired
    private MovieRecService service;

}
