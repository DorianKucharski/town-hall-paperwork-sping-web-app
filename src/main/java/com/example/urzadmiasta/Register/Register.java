package com.example.urzadmiasta.Register;

/**
 * Klasa zawierające informacje nowego użytkownika
 */
public class Register {

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
     * Numer telefonu
     */
    private String phone;
    /**
     * Nazwa użytkownika
     */
    private String userName;
    /**
     * Hasło
     */
    private String password;
    /**
     * Powtórzone hasło
     */
    private String repeatPassword;
    /**
     * Stare hasło
     */
    private String oldPassword;

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

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
