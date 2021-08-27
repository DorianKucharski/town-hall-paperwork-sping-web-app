package com.example.urzadmiasta.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repozytorium użytkowników
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Znajduje użytkownika za pomocą jego nazwy
     * @param userName nazwa użytkownika
     * @return Optional użytkownik
     */
    Optional<User> findByUserName(String userName);

    /**
     * Zmienia dane personalne użytkownika
     * @param firstName imię
     * @param lastName nazwisko
     * @param pesel pesel
     * @param state województwo
     * @param city miasto
     * @param street ulica
     * @param email emial
     * @param phone telefon
     * @param userName nazwa użytkownika
     * @param userId id użytkownika
     */
    @Transactional
    @Modifying
    @Query("update User u set u.firstName = ?1, u.lastName = ?2, u.pesel = ?3, u.state = ?4, u.city = ?5, u.street = ?6, u.email = ?7, u.phone = ?8, u.userName = ?9 where u.id = ?10")
    void updateUserDetails(String firstName, String lastName, String pesel, String state, String city, String street, String email, String phone, String userName, Integer userId);

    /**
     * Zmienia hasło użytkownika
     * @param password nowe hasło
     * @param userId id użytkownika
     */
    @Transactional
    @Modifying
    @Query("update User u set u.password = ?1 where u.id = ?2")
    void changeUserPassword(String password, Integer userId);


}
