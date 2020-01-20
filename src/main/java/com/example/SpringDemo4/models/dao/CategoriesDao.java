package com.example.SpringDemo4.models.dao;

import com.example.SpringDemo4.models.documents.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoriesDao extends ReactiveMongoRepository<Category,String> {
}
