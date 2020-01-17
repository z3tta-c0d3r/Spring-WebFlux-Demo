package com.example.SpringDemo4.models.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Document(collection = "Products")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class Product {

    @Id
    private String id;
    private String name;
    private Double price;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAt;

}
