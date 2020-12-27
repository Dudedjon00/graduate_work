package tech.itpark.jdbc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.itpark.jdbc.manager.FlatManager;
import tech.itpark.jdbc.model.Flat;

import java.util.List;

@RestController
@RequestMapping("/flats")
@RequiredArgsConstructor
public class FlatController {
    private final FlatManager manager;

    @RequestMapping
    public List<Flat> getall() {
        return manager.getAll();
    }

    @RequestMapping("/{id}")
    public Flat getById(@PathVariable long id) {
        return manager.getById(id);
    }

    @RequestMapping("/owners/{ownerId}")
    public List<Flat> getByOwnerId(@PathVariable long ownerId) {
        return manager.getByOwnerId(ownerId);
    }

    @RequestMapping("/{id}/save")
    public Flat save(
            @PathVariable long id,
            @RequestParam long ownerId,
            @RequestParam String district,
            @RequestParam int price,
            @RequestParam int rooms
            ) {
        return manager.save(new Flat(id, ownerId, district, price, rooms));
    }

    @RequestMapping("/{id}/remove")
    public Flat removeById(@PathVariable long id) {
        return manager.removeById(id);
    }
}