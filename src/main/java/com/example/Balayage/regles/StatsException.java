package com.example.Balayage.regles;

public class StatsException {
    private String Type;
    private String message;
    private Integer nombre;
    private String reglesConcernees;


    public StatsException(String type, String message, Integer nombre, String reglesConcernees) {
        Type = type;
        this.message = message;
        this.nombre = nombre;
        this.reglesConcernees = reglesConcernees;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getNombre() {
        return nombre;
    }

    public void setNombre(Integer nombre) {
        this.nombre = nombre;
    }

    public String getReglesConcernees() {
        return reglesConcernees;
    }

    public void setReglesConcernees(String reglesConcernees) {
        this.reglesConcernees = reglesConcernees;
    }

    public boolean equals(Exception e){
        return (this.message.equals(e.getMessage()) && this.getType().equals( e.getClass().getCanonicalName()));
    }

    @Override
    public String toString() {
        return "StatsException{" +
                "Type='" + Type + '\'' +
                ", message='" + message + '\'' +
                ", nombre=" + nombre +
                ", reglesConcernees='" + reglesConcernees + '\'' +
                '}';
    }
}
