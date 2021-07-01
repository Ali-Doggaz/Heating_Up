package com.example.Balayage.client;
import static javax.persistence.GenerationType.SEQUENCE;
import org.springframework.stereotype.Component;

import javax.persistence.*;

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
    private Long id;

    private String nationalite;
    private int age;
    private double revenus;
    private boolean suspect;

    public Client() {
    }

    public Client(String nationalite, int age, double revenus, boolean suspect) {
        this.nationalite = nationalite;
        this.age = age;
        this.revenus = revenus;
        this.suspect = suspect;
    }

    public Client(String nationalite, int age, double revenus) {
        this.nationalite = nationalite;
        this.age = age;
        this.revenus = revenus;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getRevenus() {
        return revenus;
    }

    public void setRevenus(double revenus) {
        this.revenus = revenus;
    }

    public boolean isSuspect() {
        return suspect;
    }

    public void setSuspect(boolean suspect) {
        this.suspect = suspect;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nationalite='" + nationalite + '\'' +
                ", age=" + age +
                ", revenus=" + revenus +
                ", suspect=" + suspect +
                '}';
    }
}
