package vjj.webconsumer.FeignServices;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("movie-service")
public interface ESService {
}
