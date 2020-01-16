package com.example.SpringDemo4.controllers;

import com.example.SpringDemo4.documents.Product;
import com.example.SpringDemo4.models.ProductDao;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
@Slf4j
public class ProductRestController {

    @Autowired
    private ProductDao productDao;

    @GetMapping()
    public Flux<Product> index() {
        Flux<Product> products = productDao.findAll().map(product -> {
            product.setName(product.getName().toUpperCase());
            return product;
        }).doOnNext(prod -> log.info(prod.getName()));

        return products;
    }

    @GetMapping("/{id}")
    public Mono<Product> show(@PathVariable String id) {
        // One form
        //return productDao.findById(id);

        // Second form
        Flux<Product> products = productDao.findAll();
        Mono<Product> monop = products.filter(p -> p.getId().equalsIgnoreCase(id))
                .next()
                .doOnNext(prod -> log.info(prod.getName()));

        return monop;

    }
}
