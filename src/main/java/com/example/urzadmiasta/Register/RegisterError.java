package com.example.urzadmiasta.Register;

/**
 * Klasa błędów danych rejestracyjnych
 */
public class RegisterError {

    /**
     * Błąd nazwy użytkownika
     */
    private boolean nameError;
    /**
     * Bład - hasła się nie zgadzają
     */
    private boolean passwordsNotMatch;
    /**
     * Błąd - hasło zbyt łatwe
     */
    private boolean passwordTooEasy;
    /**
     * Dane poprawne
     */
    private boolean success;

    public boolean isNameError() {
        return nameError;
    }

    public void setNameError(boolean nameError) {
        this.nameError = nameError;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isPasswordsNotMatch() {
        return passwordsNotMatch;
    }

    public void setPasswordsNotMatch(boolean passwordsNotMatch) {
        this.passwordsNotMatch = passwordsNotMatch;
    }

    public boolean isPasswordTooEasy() {
        return passwordTooEasy;
    }

    public void setPasswordTooEasy(boolean passwordTooEasy) {
        this.passwordTooEasy = passwordTooEasy;
    }
}
