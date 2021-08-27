package com.example.urzadmiasta.User;

import com.example.urzadmiasta.Register.Register;

import javax.persistence.*;

/**
 * Użytkownik
 */
@Entity
@Table(name = "user", schema = "public")
public class User {
    /**
     * Id użytkownika
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    /**
     * Nazwa użytkownika
     */
    private String userName;
    /**
     * Hasło
     */
    private String password;
    /**
     * Czy jest aktywny
     */
    private boolean active;
    /**
     * Rola użytkownika
     */
    private String roles;
    /**
     * Imię
     */
    private String firstName;
    /**
     * Nazwisko
     */
    private String lastName;
    /**
     * Pesel
     */
    private String pesel;
    /**
     * Województwo
     */
    private String state;
    /**
     * Miasto
     */
    private String city;
    /**
     * Ulica
     */
    private String street;
    /**
     * Email
     */
    private String email;
    /**
     * Telofon
     */
    private String phone;

    /**
     * Konstruktor. Tworzy nowego, aktywnego użytkownika z rolą użytkownik
     */
    public User(){
        active = true;
        roles = "ROLE_USER";
    }

    /**
     * Tworzy aktywnego użytkownik o roli użytkownik na podstawie obiektu rejestracji.
     * @param register obiekt klasy Register powstały w wyniku rejestracji
     */
    public User(Register register){
        userName = register.getUserName();
        password = register.getPassword();
        active = true;
        roles = "ROLE_USER";

        firstName = register.getFirstName();
        lastName = register.getLastName();
        pesel = register.getPesel();
        state = register.getState();
        city = register.getCity();
        street = register.getStreet();
        email = register.getEmail();
        phone = register.getPhone();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
