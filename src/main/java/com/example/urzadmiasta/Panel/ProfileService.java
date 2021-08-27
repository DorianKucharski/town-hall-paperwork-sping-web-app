package com.example.urzadmiasta.Panel;

import com.example.urzadmiasta.User.User;
import com.example.urzadmiasta.User.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Serwis profilu użytkownika
 */
public class ProfileService {

    /**
     * Sprawdza czy podane hasło zgadza się z hasłem użytkownika zapisanym w bazie danych
     * @param userRepository repozytorium użytkowników
     * @param userId id użytkownika
     * @param password podane hasło
     * @return true jeśli hasła się zgadzają, false - jeśli nie
     */
    static boolean checkOldPassword(UserRepository userRepository, int userId, String password){
        User user = userRepository.findById(userId).orElse(new User());
        return user.getPassword().equals(password);
    }

    /**
     * Sprawsza czy dwa podane hasła są równe
     * @param password hasło
     * @param repeatPassword powtórzone hasło
     * @return true jeśli hasła się zgadzają
     */
    static boolean checkIfPasswordsMatch(String password, String repeatPassword){
        return password.equals(repeatPassword);
    }

    /**
     * Sprawdza czy podane hasło jest hasłem za łatwym, sprawdza czy zawiera znaki specjalne, duże i małe litery i cyfry,
     * oraz czy hasło ma od 6 do 20 znaków.
     * @param password hasło
     * @return true jeśli hasło jest za łatwe
     */
    static boolean checkIfPasswordTooEasy(String password){
        Pattern pattern = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}]).{6,20})");
        Matcher matcher = pattern.matcher(password);
        return !matcher.matches();
    }
}
