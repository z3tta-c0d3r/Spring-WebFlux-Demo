package com.example.SpringDemo4.controllers;

import com.example.SpringDemo4.models.documents.Product;
import com.example.SpringDemo4.models.services.ProductService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.Duration;
import java.util.Date;

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
        model.addAttribute("button","Crear");
        model.addAttribute("buttondel","Borrar");
        return Mono.just("form");
    }

    @PostMapping("/form")
    public Mono<String> saveProduct(@Valid @ModelAttribute("product") Product product, BindingResult result, SessionStatus status, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("title", "Errors of product");
            model.addAttribute("button","Save");
            return Mono.just("form");
        } else {

            if(product.getCreateAt() == null) {
                product.setCreateAt(new Date());
            }

            status.setComplete();
            return service.save(product).doOnNext(p -> {
                log.info("Product save: " + product.getName() + " Id: " + product.getId());
            }).thenReturn("redirect:/list?success=save+product+with+success");
            //.then(Mono.just("redirect:/list"));
        }
    }

    @GetMapping("/form/{id}")
    public Mono<String> editProduct(@PathVariable String id, Model model){
        Mono<Product> productMono = service.findById(id).doOnNext(product -> {
            log.info("Product: " + product.getName());
        }).defaultIfEmpty(new Product());

        model.addAttribute("title", "Edit Product");
        model.addAttribute("product", productMono);
        model.addAttribute("button","edit");
        model.addAttribute("buttondel","Borrar");

        return Mono.just("/form");
    }

    @GetMapping("/form-v2/{id}")
    public Mono<String> editProductv2(@PathVariable String id, Model model){
        return service.findById(id).doOnNext(product -> {
            log.info("Product: " + product.getName());

            model.addAttribute("title", "Edit Product");
            model.addAttribute("product", product);
            model.addAttribute("button","edit");
        }).defaultIfEmpty(new Product()).flatMap(p -> {
            if (p.getId() == null) {
                return Mono.error(new InterruptedException("Don´t exist the product"));
            }
            return Mono.just(p);
        }).then(Mono.just("/form"))
                .onErrorResume(ex -> Mono.just("redirect:/list?error=not+exist+product"));
    }

    @GetMapping("/delete/{id}")
    public Mono<String> deleteProduct(@PathVariable String id, Model model) {
        return service.findById(id).doOnNext(p -> {
            log.info("Product: " + p.getName());
        }).defaultIfEmpty(new Product()).flatMap(p -> {
            if (p.getId() == null) {
                return Mono.error(new InterruptedException("Don´t exist the product for delete"));
            }
            return Mono.just(p);
        }).flatMap(p -> service.delete(p)).then(Mono.just("/form")).onErrorResume(ex -> Mono.just("redirect:/list?error=not+exist+product+delete"))
                .thenReturn("redirect:/list?success=delete+product+with+success");
        //}).flatMap(service::delete).then(Mono.just("/form")).onErrorResume(ex -> Mono.just("redirect:/list?error=not+exist+product+delete"))
        //    .thenReturn("redirect:/list?success=delete+product+with+success");

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
