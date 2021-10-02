package com.example.Balayage.client;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.*;
import java.util.List;

@Entity(name="client")
@Table(name="client")
public class Client {
    @Id
    @SequenceGenerator(
            name="client_sequence",
            sequenceName = "client_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "client_sequence"
    )
    @Column(name="id",
            updatable = false,
            nullable = false
    )
    protected Long id;

    protected String email;
    protected String Country;
    protected String City;

    public Client() {
    }

    public Client(String email, String country, String city) {
        this.email = email;
        Country = country;
        City = city;
    }
}
