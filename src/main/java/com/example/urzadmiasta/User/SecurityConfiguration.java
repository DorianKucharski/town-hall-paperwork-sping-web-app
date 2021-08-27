package com.example.urzadmiasta.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Adapter konfiguracji security
 */
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * Service uzytkowników
     */
    @Autowired
    UserDetailsService userDetailsService;

    /**
     * Konfiguracja adaptera. Ustawia service autoryzacji użytkowników.
     * @param auth Builder autoryzacji
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    /**
     * Konfiguracja autoryzacji requestów. Ustawia autoryzacje dostępów do poszególnych adresów, dla poszczególnych ról
     * użytkowników, oraz stronę logowania w przypadku próby uzyskania dostępu do ustawionych adresów,
     * oraz stronę wylogowania.
     * @param http HttpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/official/**").hasRole("ADMIN")
                .antMatchers("/user").hasAnyRole("ADMIN", "USER")
                .antMatchers("/profile").hasAnyRole("ADMIN", "USER")
                .antMatchers("/password").hasAnyRole("ADMIN", "USER")
                .antMatchers("/madeApplications").hasAnyRole("ADMIN", "USER")
                .antMatchers("/savedApplications").hasAnyRole("ADMIN", "USER")
                .antMatchers("/forms").hasAnyRole("ADMIN", "USER")
                .antMatchers("/").permitAll()
                .and().formLogin().loginPage("/login").permitAll()
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/");
        http.cors().and().csrf().disable();
    }


    /**
     * Zwraca instancje kodera haseł. W tym przypadku jest to instancja klasy NoOpPasswordEncoder.
     * @return instancja kodera
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
