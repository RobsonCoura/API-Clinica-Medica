package med.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import med.voll.api.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // Atributo
    // Anotacao spring para indicar a variavel que esta no app.properties
    @Value("{api.security.token.secret}")
    private String secret;

   //Metodo responsavel pela geracao do token
    public String gerarToken(Usuario usuario) {
        try {
            var algoritimo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API Voll.med")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritimo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    //Metodo que valida o token
    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
           return JWT.require(algoritmo)
                   .withIssuer("API Voll.med")
                   .build()
                   .verify(tokenJWT)
                   .getSubject();
        } catch (JWTVerificationException exception) {
            throw  new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    //Metodo responsavel pela expiracao do token
    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
