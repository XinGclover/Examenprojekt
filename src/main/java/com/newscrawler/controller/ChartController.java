package com.newscrawler.controller;

import com.newscrawler.entity.Keyword;
import com.newscrawler.entity.Product;
import com.newscrawler.models.ProductModel;
import com.newscrawler.service.KeywordService;
import com.newscrawler.util.Analysis.KeywordGenerator;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping(value = { "", "chart" })
public class ChartController {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeywordGenerator.class);

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "chart/index";
    }

//    @RequestMapping(value = "data", method = RequestMethod.GET)
//    @ResponseBody
//    public List<Product> data() {
//        ProductModel productModel = new ProductModel();
//        return productModel.findAll();
//    }



}
