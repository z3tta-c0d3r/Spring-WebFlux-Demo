package com.example.SpringDemo4.models.services;

import com.example.SpringDemo4.models.documents.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    public Flux<Product> findAll();
    public Flux<Product> findAllWithNameUpperCase();
    public Flux<Product> findAllWithNameUpperCaseRepeat();
    public Mono<Product> findById(String id);
    public Mono<Product> save(Product product);
    public Mono<Void> delete(Product product);
}
