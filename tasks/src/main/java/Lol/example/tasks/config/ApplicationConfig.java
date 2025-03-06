package Lol.example.tasks.config;


import Lol.example.tasks.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// configuration for authentication
@Configuration
public class ApplicationConfig {
    private final UserRepository userRepository;
    public ApplicationConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username not found"));
    }

    /*
   * This method defines a Spring Bean for an AuthenticationProvider, specifically a DaoAuthenticationProvider.
   It integrates with Spring Security by fetching user details from the database and verifying passwords using a password encoder.
   * */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        // Create an instance of DaoAuthenticationProvider
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        // Set the UserDetailsService to retrieve user authentication details from the database
        authProvider.setUserDetailsService(userDetailsService());

        // Set the password encoder to securely verify user passwords
        authProvider.setPasswordEncoder(passwordEncoder());

        // Return the configured authentication provider
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
