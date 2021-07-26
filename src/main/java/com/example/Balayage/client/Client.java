package com.example.Balayage.client;
import com.example.Balayage.regles.clientsTestResults.ClientTestResult;

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

    protected String nationalite;
    protected int age;
    protected double revenus;
    protected boolean suspect;

    @OneToMany(mappedBy = "client")
    private List<ClientTestResult> clientTestResults;

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

    public Client(Long id, String nationalite, int age, double revenus) {
        this.id = id;
        this.nationalite = nationalite;
        this.age = age;
        this.revenus = revenus;
        this.suspect = suspect;
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
