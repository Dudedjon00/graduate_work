package tech.itpark.jdbc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.itpark.jdbc.manager.ProductManager;
import tech.itpark.jdbc.model.Products;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductManager manager;

    @RequestMapping
    public List<Products> getall() {
        return manager.getAll();
    }

    @RequestMapping("/{id}")
    public Products getById(@PathVariable long id) {
        return manager.getById(id);
    }

    @RequestMapping("/Workers/{WorkerId}")
    public List<Products> getByWorkerId(@PathVariable long WorkerId) {
        return manager.getByworkerId(WorkerId);
    }

    @RequestMapping("/{id}/save")
    public Products save(
            @PathVariable long id,
            @RequestParam long WorkerId,
            @RequestParam String district,
            @RequestParam int price,
            @RequestParam int rooms
            ) {
        return manager.save(new Products(id, WorkerId, district, price, rooms));
    }

    @RequestMapping("/{id}/remove")
    public Products removeById(@PathVariable long id) {
        return manager.removeById(id);
    }
}