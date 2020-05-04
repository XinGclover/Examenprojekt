package com.newscrawler.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = { "", "chart" })
public class ChartController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChartController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "chart/index";
    }


}
