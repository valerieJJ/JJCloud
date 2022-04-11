package vjj.orderservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import vjj.orderservice.model.MovieOrder;
import vjj.orderservice.services.OrderService;

import java.util.List;

@RequestMapping("/order")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public List<MovieOrder> getOrderList(){
        List<MovieOrder> orders = orderService.getAllOrders();
//        orders.forEach(x->System.out.println(x.getMid()));
//        System.out.println(orders);
        return orders;
    }
}
