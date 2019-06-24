package com.apps.apene.bioclock_1.model;


/**
 * Clase User modela los datos de usuario que son tratados en la aplicación principalmente para
 * crear, loguear y modificar datos del mismo
 * */
public class User {

    // Atributos de User
    private String email;
    private String pass;
    private String name;
    private String birth_year;
    private String uid;

    // Sobrecarga de Constructores. Diferentes según los usos dentro de la aplicación
    public User(){
    }

    public User(String email){
        this.email = email;
    }

    public User(String email, String pass) {
        this.email = email;
        this.pass= pass;
    }

    public User(String email, String pass, String name, String birth_year) {
        this.email = email;
        this.pass= pass;
        this.name = name;
        this.birth_year = birth_year;

    }

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(String birth_year) {
        this.birth_year = birth_year;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    // Método toString
    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", birth_year='" + birth_year + '\'' +
                '}';
    }
}
