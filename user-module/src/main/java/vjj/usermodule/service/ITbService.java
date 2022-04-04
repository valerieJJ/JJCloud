package vjj.usermodule.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import vjj.movierec.domain.TB1;


//@Service
//@FeignClient("user-module")
public interface ITbService {
//    @GetMapping("/user")
    public TB1 queryByMid(int mid);
}
