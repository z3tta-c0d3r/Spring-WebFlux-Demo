package com.example.SpringDemo4.models.dao;

import com.example.SpringDemo4.models.documents.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductDao extends ReactiveMongoRepository<Product,String> {
}
