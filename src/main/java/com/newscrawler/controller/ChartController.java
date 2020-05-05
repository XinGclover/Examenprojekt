package com.newscrawler.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * ChartController to handle web requests of drawing chart
 */
@Controller
@RequestMapping(value = { "", "chart" })
public class ChartController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChartController.class);

    /**
     * Offer endpoint where Spring application will respond with server-side renderer template.
     * @return the name of your template to transfer to the browser
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "chart/index";
    }


}
