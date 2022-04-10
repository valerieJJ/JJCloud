package vjj.movieservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vjj.movieservice.services.MongodbService;


import java.net.UnknownHostException;

@RestController
public class MongodbController {
    @Autowired
    private MongodbService mongoservice;

    public MongodbController() throws UnknownHostException {
    }

    @RequestMapping("/showdb")
    public String getData(Model model) throws UnknownHostException {
        String data = mongoservice.getData();
        model.addAttribute("mydbdata", data);
        return data;
    }
}

