package com.example.springbootwebflux.app;

import com.example.springbootwebflux.app.models.dao.ProductoDAO;
import com.example.springbootwebflux.app.models.documents.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;

@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner {

	@Autowired
	private ProductoDAO dao;

	@Autowired
	private ReactiveMongoTemplate template;

	private static final Logger LOG = LoggerFactory.getLogger(SpringBootWebfluxApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		template.dropCollection("productos").subscribe();

		Flux.just(
				new Producto("Smart Tv 55\" 4K Ultra HD NU7100 SAMSUNG", 54999.00),
				new Producto("Celular Libre 1 5033AL 5\" 16 GB Negro ALCATEL", 5599.00),
				new Producto("Celular Libre A20 SM 6,4\" 32 GB Negro SAMSUNG", 18999.00),
				new Producto("Smart Tv 32\" HD L32S6500 Con Android TCL", 14599.00),
				new Producto("Heladera Con Freezer 277 Lts HPK135B01 Blanco PATRICK", 28999.00),
				new Producto("Notebook S130-14IGM 14\" Celeron N4000 2GB 32 GB LENOVO", 24999.00),
				new Producto("Lavarropas Carga Frontal 6,5 Kg 1000 Rpm SAMSUNG", 30999.00),
				new Producto("BATIDORA SL-SB0037R 1000W 6V C/B RO SMARTLIFE", 7899.00)
		)
				.flatMap(producto -> {
					producto.setCreatedAt(new Date());
					return dao.save(producto);
				})
				.subscribe(producto -> LOG.info("producto insertado -> " +producto.getId()+ " - " +producto.getNombre()));
	}
}
