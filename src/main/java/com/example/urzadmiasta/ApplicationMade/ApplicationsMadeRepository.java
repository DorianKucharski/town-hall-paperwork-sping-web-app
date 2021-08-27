package com.example.urzadmiasta.ApplicationMade;

import com.example.urzadmiasta.ApplicationSaved.ApplicationSaved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * Repozytorium złożonych wniosków.
 */
@Repository
public interface ApplicationsMadeRepository extends JpaRepository<ApplicationMade, Integer> {

    /**
     * Pobiera listę wszystkich złożonych wniosków użytkownika o danym id
     * @param userId id użytkownika
     * @return lista złożonych wniosków użytkownika
     */
    @Query(value = "SELECT applicationMade FROM ApplicationMade applicationMade where applicationMade.userId = ?1")
    ArrayList<ApplicationMade> findAllByUserId(Integer userId);

    /**
     * Zmienia status złożonego wniosku
     * @param checked
     * @param approved
     * @param comment
     * @param clerkId
     * @param applicationId
     */
    @Transactional
    @Modifying
    @Query("update ApplicationMade a set a.checked = ?1, a.approved = ?2, a.comment = ?3, a.clerkId = ?4 where a.id = ?5")
    void updateApplicationStatus(boolean checked, boolean approved, String comment, int clerkId, int applicationId);
}