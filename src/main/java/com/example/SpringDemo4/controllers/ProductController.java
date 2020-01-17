package com.example.SpringDemo4.controllers;

import com.example.SpringDemo4.models.documents.Product;
import com.example.SpringDemo4.models.services.ProductService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Controller
@Slf4j
@Data
public class ProductController {

    private final ProductService service;

    /**
     * List normal elements
     * @param model
     * @return
     */
    @GetMapping({"/list","/"})
    public String listProduct(Model model) {
        //Flux<Product> productFlux = productDao.findAll();
        Flux<Product> productFlux = service.findAllWithNameUpperCase();

        productFlux.subscribe(prod -> log.info(prod.getName()));

        model.addAttribute("products",productFlux);
        model.addAttribute("title","List of Products");
        return "list";
    }

    /**
     * Form1 to work backpressure DATADRIVER
     * @param model
     * @return
     */
    @GetMapping({"/listdatadriver"})
    public String listDataDriver(Model model) {
        Flux<Product> productFlux = service.findAllWithNameUpperCase().delayElements(Duration.ofSeconds(1));

        productFlux.subscribe(prod -> log.info(prod.getName()));

        model.addAttribute("products", new ReactiveDataDriverContextVariable(productFlux, 2));
        model.addAttribute("title","List of Products");
        return "list";
    }

    /**
     * Form2 to work backpressure CHUNKED (BYTES)
     * @param model
     * @return
     */
    @GetMapping({"/listfull"})
    public String listFull(Model model) {
        Flux<Product> productFlux = service.findAllWithNameUpperCaseRepeat();

        model.addAttribute("products", new ReactiveDataDriverContextVariable(productFlux, 2));
        model.addAttribute("title","List of Products");
        return "list";
    }

    /**
     * Form2 to work backpressure CHUNKED (BYTES)
     * @param model
     * @return
     */
    @GetMapping({"/listchunked"})
    public String listChunked(Model model) {
        Flux<Product> productFlux = service.findAllWithNameUpperCaseRepeat();

        model.addAttribute("products", new ReactiveDataDriverContextVariable(productFlux, 2));
        model.addAttribute("title","List of Products");
        return "listchunked";
    }
}
