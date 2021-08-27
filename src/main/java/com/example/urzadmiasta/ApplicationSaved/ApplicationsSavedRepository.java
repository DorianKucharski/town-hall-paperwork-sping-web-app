package com.example.urzadmiasta.ApplicationSaved;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ApplicationsSavedRepository extends JpaRepository<ApplicationSaved, Integer> {

    /**
     * Pobiera listę wszystkich zapisanych wniosków użytkownika o danym id
     * @param userId id użytkownika
     * @return lista zapisanych wniosków
     */
    @Query(value = "SELECT applicationSaved FROM ApplicationSaved applicationSaved where applicationSaved.userId = ?1")
    ArrayList<ApplicationSaved> findAllByUserId(Integer userId);
}
