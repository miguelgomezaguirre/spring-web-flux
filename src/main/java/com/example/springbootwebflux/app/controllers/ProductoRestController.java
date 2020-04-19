package com.example.springbootwebflux.app.controllers;

import com.example.springbootwebflux.app.models.dao.ProductoDAO;
import com.example.springbootwebflux.app.models.documents.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {

    @Autowired
    private ProductoDAO dao;

    private static final Logger LOG = LoggerFactory.getLogger(ProductoController.class);

    @GetMapping
    public Flux<Producto> index() {
        return dao.findAll().map(producto -> {
            producto.setNombre(producto.getNombre().toUpperCase());
            return producto;
        }).doOnNext(p -> LOG.info(p.getNombre()));
    }

    @GetMapping("/{id}")
    public Mono<Producto> show(@PathVariable String id) {
        return  dao.findAll()
                .filter(p -> p.getId().equals(id))
                .next()
                .doOnNext(p -> LOG.info(p.getNombre()));
    }
}
