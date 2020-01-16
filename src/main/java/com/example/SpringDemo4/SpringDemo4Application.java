package com.example.SpringDemo4;

import com.example.SpringDemo4.documents.Product;
import com.example.SpringDemo4.models.ProductDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;

@SpringBootApplication
@Slf4j
public class SpringDemo4Application implements CommandLineRunner {

	@Autowired
	private ReactiveMongoTemplate reactiveMongoTemplate;

	@Autowired
	private ProductDao pRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringDemo4Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// Eliminamos los registros antes de insertar
		reactiveMongoTemplate.dropCollection("Products").subscribe();

		//Import products to MONGODB
		Flux.just(
				Product.builder().name("iRobot Roomba").price(247.08).build(),
				Product.builder().name("Rowenta Calentador").price(34.98).build(),
				Product.builder().name("iPencil Apple").price(87.98).build(),
				Product.builder().name("IPad 10.2").price(454.28).build(),
				Product.builder().name("iPhone XR Negro").price(704.98).build(),
				Product.builder().name("iPad Mini Black").price(214.08).build(),
				Product.builder().name("Cascos Sennheiser").price(99.00).build(),
				Product.builder().name("iPods White").price(100.58).build(),
				Product.builder().name("iWatch 3").price(450.09).build()
		)
				.flatMap(product -> {
					product.setCreateAt(new Date());
					return pRepository.save(product);
				})
				.subscribe(product -> log.info("Insert: " + product.getId() + " - " + product.getName()));
	}
}
