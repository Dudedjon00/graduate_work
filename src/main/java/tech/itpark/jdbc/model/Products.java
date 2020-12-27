package tech.itpark.jdbc.model;

import lombok.Value;

@Value
public class Flat {
    long id;
    long owner_id;
    String district;
    int price;
    int rooms;
}
