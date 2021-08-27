package com.example.urzadmiasta.Register;

import com.example.urzadmiasta.User.User;
import com.example.urzadmiasta.User.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Serwis rejestracji
 */
public class RegisterService {

    /**
     * Sprawdza dane rejestracji. Sprawdza czy dana nazwa użytkownika jest wolna, czy hasła sie zgadzają, czy hasło nie
     * jest zbyt łatwe. Na podstawie tych sprawdzeń ustawia odpowiednie pola zwracanego obiektu registerError.
     * Jeśli dane są poprawne tworzy na ich podstawie nowego użytkownika, którego następnie dodaje do repozytorium.
     * @param userRepository repozytorium użytkowników
     * @param register obiekt Register zawierający dane nowego użytkownika
     * @return obiekt klasy RegisterError, zawiera błędy danych rejestracji, jeżli wszystko jest poprawne pole isSuccess
     * przyjmuje wartość true
     */
    public static RegisterError RegisterCheck(UserRepository userRepository, Register register) {
        RegisterError registerError = new RegisterError();

        if (userRepository.findByUserName(register.getUserName()).isPresent()) {
            registerError.setNameError(true);
        }
        if (!register.getPassword().equals(register.getRepeatPassword())) {
            registerError.setPasswordsNotMatch(true);
        }

        Pattern pattern = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}]).{6,20})");
        Matcher matcher = pattern.matcher(register.getPassword());
        if (!matcher.matches()) {
            registerError.setPasswordTooEasy(true);
        }

        if (!registerError.isNameError() && !registerError.isPasswordsNotMatch() && !registerError.isPasswordTooEasy()) {
            registerError.setSuccess(true);
            userRepository.saveAndFlush(new User(register));
        }

        return registerError;
    }








}
