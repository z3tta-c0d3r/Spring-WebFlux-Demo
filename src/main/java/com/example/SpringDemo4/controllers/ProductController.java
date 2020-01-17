package com.example.SpringDemo4.controllers;

import com.example.SpringDemo4.models.documents.Product;
import com.example.SpringDemo4.models.services.ProductService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Controller
@SessionAttributes("product")
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
    public Mono<String> listProduct(Model model) {
        //Flux<Product> productFlux = productDao.findAll();
        Flux<Product> productFlux = service.findAllWithNameUpperCase();

        productFlux.subscribe(prod -> log.info(prod.getName()));

        model.addAttribute("products",productFlux);
        model.addAttribute("title","List of Products");
        //return "list";
        return Mono.just("list");
    }

    @GetMapping("/form")
    public Mono<String> createProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("title", "Form of product");
        return Mono.just("form");
    }

    @PostMapping("/form")
    public Mono<String> saveProduct(Product product, SessionStatus status) {
        status.setComplete();
        return service.save(product).doOnNext(p -> {
            log.info("Product save: " + product .getName() + " Id: " + product.getId());
        }).thenReturn("redirect:/list");
        //.then(Mono.just("redirect:/list"));
    }

    @GetMapping("/form/{id}")
    public Mono<String> editProduct(@PathVariable String id, Model model){
        Mono<Product> productMono = service.findById(id).doOnNext(product -> {
            log.info("Product: " + product.getName());
        }).defaultIfEmpty(new Product());

        model.addAttribute("title", "Edit Product");
        model.addAttribute("product", productMono);

        return Mono.just("/form");
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
