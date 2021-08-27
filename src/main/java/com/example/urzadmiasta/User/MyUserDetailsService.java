package com.example.urzadmiasta.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Serwis informacji użytkownika.
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    /**
     * Repozytorium użytkowników
     */
    @Autowired
    UserRepository userRepository;

    /**
     * Szuka użytkownika na podstawie jego nazwy, zwraca go w postaci zmapowanej do Klasy MyUserDetails. W
     * przypadku nie znalezienia danego użytkownika rzuca wyjątek UsernameNotFoundException
     * @param userName nazwa szukanego użytkownika
     * @return zmapowany użytkownik
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(userName);
        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + userName));
        return user.map(MyUserDetails::new).get();
    }
}
