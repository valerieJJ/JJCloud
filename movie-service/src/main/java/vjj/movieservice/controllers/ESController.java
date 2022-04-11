package vjj.movieservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vjj.movieservice.services.ESService;


import java.io.IOException;

@Controller
public class ESController {
    @Autowired
    private ESService esService;

    public ESController() {}

    @RequestMapping(value = "/essearch", method = RequestMethod.GET)
    public String searchES(Model model) throws IOException {
        String queryCollection = "movietags";
        String data = this.esService.search(queryCollection);
        model.addAttribute("ESdata", data);
        return "/searchES";
    }

    @RequestMapping(value = "/es/search", method = RequestMethod.GET)
    @ResponseBody
    public String searchES2(@RequestParam("queryCollection") String queryCollection) throws IOException {
        String data = this.esService.search(queryCollection);
        return data;
    }
}
