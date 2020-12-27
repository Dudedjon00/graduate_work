package tech.itpark.jdbc.model;

import lombok.Value;

@Value
public class Products {
    long id;
    long workers_id;
    String items;
    int price;
    int quantity;
}
