package vjj.webconsumer.FeignServices;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "order-service")
public interface FeignOrderService {

}
