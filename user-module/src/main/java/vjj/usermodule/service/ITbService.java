package vjj.usermodule.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import vjj.movierec.domain.TB1;


@FeignClient(name="movie-recommend-service")
public interface ITbService {

    @RequestMapping(path = "/zkk", method = RequestMethod.GET)
    @ResponseBody
    public String queryByMid();
}
