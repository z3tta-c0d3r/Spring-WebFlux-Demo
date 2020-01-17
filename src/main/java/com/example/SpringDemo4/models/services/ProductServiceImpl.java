package com.example.SpringDemo4.models.services;

import com.example.SpringDemo4.models.documents.Product;
import com.example.SpringDemo4.models.dao.ProductDao;
import lombok.Data;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Data
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;

    @Override
    public Flux<Product> findAll() {
        return productDao.findAll();
    }

    @Override
    public Flux<Product> findAllWithNameUpperCase() {
        return productDao.findAll().map(product -> {
            product.setName(product.getName().toUpperCase());
            return product;
        });
    }

    @Override
    public Flux<Product> findAllWithNameUpperCaseRepeat() {
        return productDao.findAll().map(product -> {
            product.setName(product.getName().toUpperCase());
            return product;
        }).repeat(5000);
    }

    @Override
    public Mono<Product> findById(String id) {
        return productDao.findById(id);
    }

    @Override
    public Mono<Product> save(Product product) {
        return productDao.save(product);
    }

    @Override
    public Mono<Void> delete(Product product) {
        return productDao.delete(product);
    }
}
