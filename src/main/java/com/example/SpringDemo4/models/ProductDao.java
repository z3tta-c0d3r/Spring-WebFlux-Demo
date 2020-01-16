package com.example.SpringDemo4.models;

import com.example.SpringDemo4.documents.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductDao extends ReactiveMongoRepository<Product,String> {
}
