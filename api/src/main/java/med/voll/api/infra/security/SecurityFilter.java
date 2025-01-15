package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    //Injecao de dependencias
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;


    // Filtro personalizado que encaminha a requisição e resposta para o próximo filtro
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //Recuperação do Token JWT
        var tokenJWT = recuperarToken(request);

        //Pega o login do usuario se vier token na requisição vai buscar o login no repository, criar um dto do userLogin e autenticar
        if (tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT);
            var usuario = repository.findByLogin(subject);

            //Criação da Autenticação
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
//            System.out.println("LOGADO");
        }

        // Continua o processamento da cadeia de filtros, passando a requisição e resposta adiante
        filterChain.doFilter(request, response);
    }

    // Metodo para recuperar token
    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "").trim();
        }
        return null;
    }

}
