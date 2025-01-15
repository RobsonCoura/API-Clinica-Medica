package med.voll.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    //Para devolvermos um objeto para o Spring, usamos a anotação @Bean
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        //liberar endopoints swagger
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "swagger-ui/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(withDefaults())
                // Configura a API para não usar sessões (stateless).
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(withDefaults())
                // Desativa a proteção CSRF, não necessária para APIs REST.
                .csrf(csrf -> csrf.disable())
                //estabele a ordem dos filtros - 1 do projeto, 2 do spring
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                // Conclui e retorna o filtro de segurança.
                .build();

    }

    // Cria o AuthenticationManager para gerenciar a autenticação
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // Cria o PasswordEncoder para criptografar e comparar senhas de forma segura
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
