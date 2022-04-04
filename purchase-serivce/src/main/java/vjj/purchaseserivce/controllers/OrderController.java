package vjj.purchaseserivce.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import vjj.purchaseserivce.commons.CommonResult;
import vjj.purchaseserivce.commons.Payment;

import javax.annotation.Resource;

@RestController
@Slf4j
public class OrderController {
    //    public static final String PAYMENT_URL = "http://localhost:8001";
    public static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";//服务不能写死
    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/consumer/Payment/Creat")
    public CommonResult<Payment> Creat(Payment payment){
        return restTemplate.postForObject(PAYMENT_URL + "/Payment/Creat",payment, CommonResult.class);
    }

    @GetMapping("/consumer/Payment/getPaymentById/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
        return restTemplate.getForObject(PAYMENT_URL+"/Payment/getPaymentById/"+id,CommonResult.class);
    }

    @GetMapping("/consumer/Payment/getForEntity/{id}")
    public CommonResult<Payment> getPaymentById2(@PathVariable("id") Long id){
        ResponseEntity<CommonResult> forEntity = restTemplate.getForEntity(PAYMENT_URL + "/Payment/getPaymentById/" + id, CommonResult.class);
        if (forEntity.getStatusCode().is2xxSuccessful()){
            return forEntity.getBody();
        }else
            return new CommonResult(201,"操作失败");
    }
    @GetMapping("/consumer/payment/zipkin")
    public String paymentZipkin() {
        String result = restTemplate.getForObject(PAYMENT_URL+"/payment/zipkin/", String.class);
        return result;
    }

}
