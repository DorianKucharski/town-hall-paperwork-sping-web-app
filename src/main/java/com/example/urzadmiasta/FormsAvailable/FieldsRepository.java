package com.example.urzadmiasta.FormsAvailable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * Repozytorium pól formularzy
 */
@Repository
public interface FieldsRepository extends JpaRepository<Field, Integer> {

    /**
     * Zwraca wszystkie pola danego formularza
     * @param formId id formularza
     * @return lista pól
     */
    @Query(value = "SELECT field FROM Field field where field.formId = ?1")
    ArrayList<Field> findFieldsOfForm(Integer formId);

}
