package vjj.webconsumer.FeignServices;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient("movie-service")
public interface ESService {

    @RequestMapping(value = "/es/search", method = RequestMethod.GET)
    @ResponseBody
    public String searchES2(@RequestParam String queryCollection);
}
