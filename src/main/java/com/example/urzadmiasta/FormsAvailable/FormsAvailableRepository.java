package com.example.urzadmiasta.FormsAvailable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * Repozytorium dostępnych wniosków
 */
@Repository
public interface FormsAvailableRepository extends JpaRepository<FormAvailable, Integer> {

    /**
     * Usuwa formularz, poprzez ustawienie jego pola available na false, co uniemowżliwia wypełenienie formualrza
     * @param id id formularza
     */
    @Transactional
    @Modifying
    @Query("update FormAvailable f set f.available = false where f.id = ?1")
    void deleteForm(int id);

    /**
     * Zwraca listę wszystkich dostępnych obecnie formularzy
     * @return lista formularzy
     */
    @Query(value = "SELECT formAvailable FROM FormAvailable formAvailable where formAvailable.available = true")
    ArrayList<FormAvailable> getAllAvailable();
}
