package com.example.urzadmiasta.ApplicationMade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * Reposytorium złożonych wniosków
 */
@Repository
public interface ApplicationsMadeFieldsRepository extends JpaRepository<ApplicationMadeField, Integer> {

    /**
     * Zwraca listę wszystkich pól danego wniosku
     * @param applicationMadeId id złożonego wniosku
     * @return lista pól
     */
    @Query(value = "SELECT applicationMadeField FROM ApplicationMadeField applicationMadeField where applicationMadeField.applicationMadeId = ?1")
    ArrayList<ApplicationMadeField> findFieldsOfApplication(Integer applicationMadeId);


}
