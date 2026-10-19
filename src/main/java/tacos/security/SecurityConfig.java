package tacos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // W oryginalnym kodzie był StandardPasswordEncoder,
        // ale jest on obecnie uznawany za niebezpieczny.
        // BCryptPasswordEncoder to współczesny standard.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Reguły autoryzacji ścieżek
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/design", "/orders").hasRole("USER")
                        .requestMatchers("/", "/**").permitAll()
                )

                // 2. Własna strona logowania
                .formLogin(form -> form
                        .loginPage("/login")
                )

                // 3. Wylogowywanie i przekierowanie
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                )

                // 4. Wyłączenie ochrony CSRF tylko dla konsoli H2 (w celach debugowania)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )

                // 5. Zezwolenie na ładowanie ramek (frame) z tego samego źródła
                // (niezbędne, by konsola bazy danych H2 wyświetlała się poprawnie w przeglądarce)
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                );

        return http.build();
    }

    /*
     * UWAGA DOTYCZĄCA ZAKOMENTOWANYCH SEKCJI Z GITHUBA AUTORA:
     *
     * Autor w oryginalnym pliku zostawił w komentarzach przykłady dla inMemory,
     * jdbcAuthentication oraz ldapAuthentication.
     *
     * W nowym podejściu nie musisz używać metody configure(AuthenticationManagerBuilder auth),
     * aby powiązać swój UserDetailsService z systemem zabezpieczeń.
     * Ponieważ Twoja docelowa usługa wczytywania użytkowników z bazy (JPA)
     * będzie oznaczona adnotacją @Service, Spring sam ją znajdzie, dopasuje
     * do zdefiniowanego wyżej PasswordEncodera i automatycznie obsłuży logowanie.
     */
}