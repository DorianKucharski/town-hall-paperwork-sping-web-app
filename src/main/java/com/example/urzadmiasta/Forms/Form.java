package com.example.urzadmiasta.Forms;

import com.example.urzadmiasta.ApplicationMade.ApplicationMade;
import com.example.urzadmiasta.ApplicationMade.ApplicationMadeField;
import com.example.urzadmiasta.ApplicationSaved.ApplicationSaved;
import com.example.urzadmiasta.ApplicationSaved.ApplicationSavedField;
import com.example.urzadmiasta.FormsAvailable.Field;
import com.example.urzadmiasta.FormsAvailable.FormAvailable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

/**
 * Klasa formularza. Nie jest encją, służy wyłącznie do przeprowadzania logiki biznesowej związanej z obsługą formualarzy na stronie
 * Zawiera wszystkie możliwe pola, które przyjmują klasy formularzy zapisanych i złożonych.
 * Do postaci obiektu tej klasy transformowane są wniosku złożone, wnioski zapisane, formularze tworzone przez kreator formularzy.
 * Służy głównie do konsolidacji danych z tabel wniosków i tabel pól wniosków, oraz do obsługi modelów stron.
 * Większość logiki dokonywana jest w serwisach servletów
 */
public class Form {

    /**
     * Id formularza, z repozytorium FormsAvailableRepository
     */
    private int id;
    /**
     * Nazwa formularza z repozytorium FormsAvailable
     */
    private String name;
    /**
     * Opis formularza z repozytorium FormsAvailable
     */
    private String description;
    /**
     * Lista obiektów klasy FieldInForm. Każdy obiekt przechowuje obiekt encji pola (Field) oraz jego wartość (value).
     * Obiekty klasy Field pobierane są z repozytorium FieldsRepository, zaś ich wartość z odpowiednio dla wniosku
     * z ApplicationsMadeFieldsRepository lub z ApplicationsSavedFieldsRepository
     */
    private ArrayList<FieldInForm> fieldsInForm;
    /**
     * Id wniosku, tylko w przypadku, zapisanych/złożonych wniosków, z ApplicationsMadeRepository lub ApplicationsSavedRepository
     */
    private int applicationId;
    /**
     * Data złożenia/zapisania wniosku z ApplicationsMadeRepository lub ApplicationsSavedRepository
     */
    private Timestamp timestamp;
    /**
     * Id użtykownika z ApplicationsMadeRepository lub ApplicationsSavedRepository
     */
    private int userId;
    /**
     * Id urzędnika, tylko w przypadku złożonych wniosków
     */
    private int clerkId;
    /**
     * Imię i nazwisko urzędnika sprawdzającego wniosek
     */
    private String clerkName;
    /**
     * Czy wniosek został sprawdzony
     */
    private boolean checked;
    /**
     * Czy wniosek został zatwierdzony, jeśli false - odrzucony
     */
    private boolean approved;
    /**
     * Komentarz urzędnika sprawdzającego wniosek, tylko w przypadku odrzucenia
     */
    private String comment;

    /**
     * Domyślny konstruktor
     */
    public Form(){
        timestamp = new Timestamp(System.currentTimeMillis());
        checked = false;
        this.fieldsInForm = new ArrayList<>();
    }

    /**
     * Konstruktor przyjmujący obiekt encji FormAvailable, ustawia jedynie podstawowe pola.
     * @param formAvailable obiekt klasy FormAvailable
     */
    public Form (FormAvailable formAvailable){
        this.name = formAvailable.getName();
        this.description = formAvailable.getDescription();
        this.id = formAvailable.getId();
    }

    /**
     * Konstruktor przyjmujący obiekt FormAvailable oraz listę pól formularza.
     * Ustawia podstawowe informacje formularza oraz dodaje jego pola
     * @param formAvailable encja FormAvailable
     * @param fields lista pól
     */
    public Form (FormAvailable formAvailable, ArrayList<Field> fields){
        this(formAvailable);

        this.fieldsInForm = new ArrayList<>();
        for (Field field: fields){
            this.fieldsInForm.add(new FieldInForm(field));
        }
    }

    /**
     * Metoda wypełniająca pola formularza wartościami z zapisanego formularza
     * @param savedFields pola zapisanego wniosku
     */
    public void fillFieldsFromApplicationSaved(ArrayList<ApplicationSavedField> savedFields){
        for (FieldInForm fieldInForm: fieldsInForm){
            for (ApplicationSavedField fieldSave: savedFields){
                if (fieldInForm.getField().getId() == fieldSave.getFieldId()){
                    fieldInForm.setValue(fieldSave.getValue());
                    break;
                }
            }
        }
    }

    /**
     * Metoda wypełniająca pola formularza wartościami z zapisanego formularza
     * @param savedFields pola złożonego wniosku
     */
    public void fillFieldsFromApplicationMade(ArrayList<ApplicationMadeField> savedFields){
        for (FieldInForm fieldInForm: fieldsInForm){
            for (ApplicationMadeField fieldSave: savedFields){
                if (fieldInForm.getField().getId() == fieldSave.getFieldId()){
                    fieldInForm.setValue(fieldSave.getValue());
                    break;
                }
            }
        }
    }

    /**
     * Statyczna metoda zwracająca obiekt klasy Form utworzony na podstawie zapisanego wniosku, z wypełnionymi polami
     * pobranymi z zapisanego wniosku
     * @param formAvailable formularz wniosku
     * @param fields pola formularzu
     * @param applicationSaved zapisany wniosek
     * @param savedFields pola zapisanego wniosku
     * @return
     */
    public static Form formFromApplicationSaved(FormAvailable formAvailable,
                                                ArrayList<Field> fields,
                                                ApplicationSaved applicationSaved,
                                                ArrayList<ApplicationSavedField> savedFields){
        Form form = new Form(formAvailable, fields);
        form.fillFieldsFromApplicationSaved(savedFields);
        form.setUserId(applicationSaved.getUserId());
        form.setId(applicationSaved.getFormId());
        form.setApplicationId(applicationSaved.getId());
        form.setTimestamp(applicationSaved.getTimestamp());
        return form;
    }


    /**
     * Statyczna metoda zwracająca obiekt klasy Form utworzony na podstawie złożonego wniosku, z wypełnionymi polami
     * pobranymi ze złożonego wniosku
     * @param formAvailable formularz wniosku
     * @param fields pola formularzu
     * @param applicationMade złożony wniosek
     * @param savedFields pola złożonego wniosku
     * @return
     */
    public static Form formFromApplicationMade(FormAvailable formAvailable,
                                               ArrayList<Field> fields,
                                               ApplicationMade applicationMade,
                                               ArrayList<ApplicationMadeField> savedFields){
        Form form = new Form(formAvailable, fields);
        form.fillFieldsFromApplicationMade(savedFields);
        form.setUserId(applicationMade.getUserId());
        form.setId(applicationMade.getFormId());
        form.setApplicationId(applicationMade.getId());
        form.setTimestamp(applicationMade.getTimestamp());
        form.setClerkId(applicationMade.getClerkId());
        form.setChecked(applicationMade.isChecked());
        form.setApproved(applicationMade.isApproved());
        form.setComment(applicationMade.getComment());
        return form;
    }

    /**
     * Konstruktor tworzący obiekt na podstawie listy parametrów przesłanej w postaci mapy.
     * Używany do tworzenia formularza przy pomocy kreatora formularzy.
     * Mapa zawiera informacje takie jak nazwa formularza, jego opis, oraz pola w postaci - nazwa, opis, typ
     * @param allParams mapa parametrów
     */
    public Form(Map<String, String> allParams) {
        name = allParams.get("formName");
        description = allParams.get("formDesc");
        fieldsInForm = new ArrayList<>();

        int i = 1;
        String name = null;
        String description = null;
        String type = null;
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            if (entry.getKey().contains("name")) {
                name = entry.getValue();
            }
            if (entry.getKey().contains("desc")) {
                description = entry.getValue();
            }

            if (entry.getKey().contains("type")) {
                if (entry.getValue().contains("1")) {
                    type = "character varying";
                } else if (entry.getValue().contains("2")) {
                    type = "integer";
                } else if (entry.getValue().contains("3")) {
                    type = "double precision";
                } else if (entry.getValue().contains("4")) {
                    type = "timestamp without time zone";
                } else {
                    type = "character varying";
                }
            }
            if (name != null && description != null && type != null){
                fieldsInForm.add(new FieldInForm(new Field(name, description, type)));
                name = null;
                description = null;
                type = null;
            }

        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public ArrayList<FieldInForm> getFieldsInForm() {
        return fieldsInForm;
    }

    public void setFieldsInForm(ArrayList<FieldInForm> fieldsInForm) {
        this.fieldsInForm = fieldsInForm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getClerkId() {
        return clerkId;
    }

    public void setClerkId(int clerkId) {
        this.clerkId = clerkId;
    }

    public String getClerkName() {
        return clerkName;
    }

    public void setClerkName(String clerkName) {
        this.clerkName = clerkName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
