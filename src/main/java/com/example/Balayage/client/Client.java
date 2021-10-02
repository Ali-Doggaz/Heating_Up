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
    protected String country;
    protected String city;

    public Client() {
    }

    public Client(String email, String country, String city) {
        this.email = email;
        this.country = country;
        this.city = city;
    }

    public Long getId() {
        return id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
