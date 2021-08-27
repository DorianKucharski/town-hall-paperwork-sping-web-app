package com.example.urzadmiasta.ApplicationSaved;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * Repozytorium pól zapisanych wniosków
 */
@Repository
public interface ApplicationsSavedFieldsRepository extends JpaRepository<ApplicationSavedField, Integer> {

    /**
     * Zwraca wszystkie pola zapisanego wniosku
     * @param applicationSavedId id zapisanego wniosku
     * @return lista pól zapisanego wniosku
     */
    @Query(value = "SELECT applicationSavedField FROM ApplicationSavedField applicationSavedField where applicationSavedField.applicationSavedId = ?1")
    ArrayList<ApplicationSavedField> findFieldsOfApplication(Integer applicationSavedId);

    /**
     * Modyfikuje wartość zapisanego pola
     * @param value nowa wartość
     * @param applicationSavedId id zapisanego wniosku
     * @param fieldId id pola formularza
     */
    @Transactional
    @Modifying
    @Query("update ApplicationSavedField a set a.value = ?1 where a.applicationSavedId = ?2 and a.fieldId = ?3")
    void setSavedFieldValue(String value, int applicationSavedId, int fieldId);
}
