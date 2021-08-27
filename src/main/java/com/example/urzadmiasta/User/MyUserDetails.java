package com.example.urzadmiasta.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Informacje uzytkownika
 */
public class MyUserDetails implements UserDetails {

    /**
     * Nazwa użytkownika
     */
    private String userName;
    /**
     * Hasło użytkownika
     */
    private String password;
    /**
     * Czy jest aktywny
     */
    private boolean active;
    /**
     * Lista przyznanych uprawnień
     */
    private List<GrantedAuthority> authorities;

    /**
     * Konstruktor przyjmujący użytkownika klasy User, encji bazy danych. Kopiuje dane użytkownika i tworzy listę
     * jego uprawnień
     * @param user użytkownik
     */
    public MyUserDetails(User user) {
        this.userName = user.getUserName();
        this.password = user.getPassword();
        this.active = user.isActive();
        this.authorities = Arrays.stream(user.getRoles().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
    }

    /**
     * Zwraca listę przyznanych uprawnień
     * @return lista uprawnień
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Getter hasła
     * @return hasło
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Getter nazwy użytkownika
     * @return nazwa użytkownika
     */
    @Override
    public String getUsername() {
        return userName;
    }

    /**
     * Getter przedawnienia konta. Metoda wymagana przez UserDetailService.
     * W tym przypadku zwraca zawsze true, gdyż użytkownicy się nie przedawniają.
     * @return zwraca zawsze true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Getter blokady konta. Zawsze zwraca true, gdyż blokowanie nie zostało zaimplementowane
     * @return true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Getter przedawnienia uprawnień konta. Zwraca zawsze true.
     * @return true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Getter aktywacji konta. Zwraca pole o skopiowanej wartości z klasy User, jednakże, funkcjonalność aktywacji konta
     * nie została zaimplementowana, toteż zawsze metoda zwraca true.
     * @return true jeśli konto aktywne.
     */
    @Override
    public boolean isEnabled() {
        return active;
    }
}
